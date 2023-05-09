package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.RMIclientImpl;
import it.polimi.ingsw.network.server.RMIconnection;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.View;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client {
    private Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;

    private static RMIconnection stub;
    private static RMIclientImpl RMIclient;

    /*
    bisogna usare un id per il client handler, potremmo avere un id per il client e un id per i giocatore assegnato solo
    per il game per esempio (questione aperta, almeno per me)
     */
    private static clientController controller;
    public void connection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /**
     * Function that checks if the message has the right format and sends them to server.
     * @param message Command to send.
     * @param view type of view.
     * @param socket true if socket connection, false if RMI connection.
     */
    public void sendMessage(String message, View view, boolean socket) throws RemoteException {
        if (message.equals("/help")) {
            view.help();
        } else {
            GeneralMessage clientMessage;
            //Controlla che il formato del comando sia giusto
            clientMessage = controller.checkMessageShape(message, controller);
            Action curr_action = clientMessage.getAction();
            String toSend = clientMessage.toString();
            //Se il formato è sbagliato checkmessage restituisce un messaggio di tipo error e non viene inviato
            if (curr_action.equals(Action.ERROR)) {
                new ReplyMessage(toSend, Action.ERROR).print();
            } else if (curr_action.equals(Action.SHOWPERSONAL)) {
                view.displayMessage("\n  Your personal goal");
                view.showBoard(controller.getSimpleGoal(), Action.UPDATESHELF);
            } else if (curr_action.equals(Action.SHOWCOMMONS)) {
                view.displayMessage("Common goals: ");
                view.showCommons(controller.cc);
            } else {
                if(socket) {
                    out.println(toSend);
                } else {
                    //RMI
                    stub.RMIsend(toSend);
                }
            }
        }
    }

    public void endConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    /**
     * Function that receives json messages ,identifies them and acts differently upon the action they have.
     */
    public void listenSocket() throws IOException, ParseException, InvalidKeyException {
        while(true) {
            String message = in.readLine();
            elaborate(message);
        }
    }
    public void listenRMI() throws ParseException, InvalidKeyException {
        while(true){
            String message="";//Ricevi messaggi in RMI
            elaborate(message);
        }
    }
    public static void elaborate( String message) throws ParseException, InvalidKeyException {
        CLI cli = new CLI();
        ReplyMessage reply;
        boolean isLobby=false;
        Action replyAction = ReplyMessage.identify(message);
        switch (replyAction) {
            case CREATELOBBY -> {
                reply = CreateLobbyReplyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                isLobby=true;
            }
            //Semplicemente una stringa di successo/errore
            case JOINLOBBY -> {
                reply = JoinLobbyReplyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                isLobby=true;
            }
            //Lista di tutte le lobby disponibili
            case SHOWLOBBY -> {
                reply = ShowLobbyReplyMessage.decrypt(message);
                isLobby=true;
            }
            //Conferma che il game può iniziare
            case STARTGAME -> {
                controller.setIdLobby(0);
                reply = StartGameReplyMessage.decrypt(message);
                controller.setIdGame(reply.getIdGame());
                controller.setFirstTurn(true);
                isLobby=true;
            }
            case UPDATEBOARD,UPDATESHELF -> {
                reply = UpdateBoardMessage.decrypt(message);
            }
            case INGAMEEVENT -> {
                reply = ReplyMessage.decrypt(message);
            }
            case CHOSENTILES -> {
                reply = ChosenTilesMessage.decrypt(message);
            }
            case SHOWPERSONAL -> {
                reply = UpdateBoardMessage.decrypt(message);
                controller.setSimpleGoal(reply.getSimpleBoard());
            }
            case SHOWCOMMONS -> {
                reply = SendCommonCards.decrypt(message);
                reply.getCC(controller.cc);
            }

            //TODO Decidire cosa fare una volta finito il game
            //Per gli altri comandi si aspetta errore perchè se non è in una lobby non li può chiamare
            //altrimenti non è questa sezione che li controlla(e invece ha senso):D
            default -> {
                reply = ReplyMessage.decrypt(message);
                isLobby=true;
            }
        }
        //distinzione cli gui
        if(true/*isCLI*/){
            if(isLobby){
                reply.print();
            }else{
                switch (replyAction){
                    case UPDATEBOARD, UPDATESHELF -> {
                        cli.showBoard(reply.getSimpleBoard(),replyAction);
                        if(controller.isFirstTurn()){
                            controller.setFirstTurn(false);
                            cli.displayMessage("\n  Your personal goal");
                            cli.showBoard(controller.getSimpleGoal(), Action.UPDATESHELF);
                            cli.displayMessage("Common goals: ");
                            cli.showCommons(controller.cc);
                        }
                    }
                    case INGAMEEVENT -> reply.print(); //da implementare in cli?
                    case CHOSENTILES -> {
                        List<Tile> tile=new ArrayList<>();
                        reply.getTiles(tile);
                        cli.showChosenTiles(tile);
                    }
                }
            }
        }else{//GUI
            /*

             */
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("""
                Choose connection type:\s
                [1]: for Socket
                [2]: for RMI""");
        Scanner input = new Scanner(System.in);
        String connectionType=input.next();
        if(connectionType.equals("1")){
            socket();
        }else if(connectionType.equals("2")){
            RMI();
        }else{
            System.out.println("Wrong input");
        }
    }

    public static void socket() throws IOException {
        Client Client = new Client();
        //Client.connection("192.168.1.234", 2345);
        Client.connection("127.0.0.1", 23450);
        Scanner input = new Scanner(System.in);
        String username;
        SetNameMessage nick;
        CLI cli = new CLI();
        //Richiesta nickname unico
        System.out.println(in.readLine());
        //Controllo unicità nome
        do {
            username = cli.askUsername();
            nick = new SetNameMessage(username,true);
            //nick = new SetNameMessage("Mayhem",true);
            out.println(nick);//Avevo messo toString() all invio di ogni messaggio che lo traduce in json, non so perchè me lo dava ridondante e funziona anche senza no idea
            try{
                //nick = new SetNameMessage(in.readLine());
                nick = SetNameMessage.decrypt(in.readLine());
            }catch(Exception ignored){}
            //
            cli.printUsername(nick.getUsername(), nick.isAvailable());
        } while (!nick.isAvailable());
        //Ogni player ha il suo clientController
        controller = new clientController(nick.getUsername());
        //inizio connessione
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //thread che rimane in ascolto di messaggi
        executor.submit(()-> {
            try {
                Client.listenSocket();
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        cli.displayMessage("Write /help for command list.");
        //Client.sendMessage("createlobby 2",controller,in,out,cli);//per velocizzare, sarà da rimuovere
        //Ciclo per invio messaggi
        while(true) { //Condizione da rivedere
            Client.sendMessage(input.nextLine(),cli,true);
        }

        //executor.shutdownNow();//uccisione thread
    }

    public static void RMI(){
        Scanner in = new Scanner(System.in);
        Client c = new Client();
        controller = new clientController();
        CLI cli=new CLI();

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23451);
            stub = (RMIconnection) Naming.lookup("rmi://localhost:" + 23451 + "/RMIServer");
            RMIclient = new RMIclientImpl(controller); //per ricevere risposta
            Naming.rebind("rmi://localhost:" + 23451 + "/RMIServer", RMIclient);
            //TODO MANCA IL PING E IL CICLO DEL CONTROLLO IN CASO DI NOME GIA' PRESO
            System.out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
            setName();
            while(true){
                c.sendMessage(in.nextLine(), cli, false);
            }
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void ping() throws InterruptedException {
        while(true) {
            TimeUnit.SECONDS.sleep(30);
            try {
                Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23451);
                RMIconnection stub = (RMIconnection) Naming.lookup("rmi://localhost:" + 23451 + "/RMIServer");
                //stub.RMIsendName("ping");
            } catch (RemoteException | NotBoundException | MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setName() throws RemoteException {
        String input;
        SetNameMessage nick;
        Scanner in = new Scanner(System.in);
        System.out.println("Enter your nickname: ");
        input = in.nextLine();
        nick= new SetNameMessage(input, true);
        stub.RMIsendName(nick.toString(), RMIclient);
    }
}






/*
-questione serializable
-dove aggiungere i messaggi di tipo server (farei un'altra classe)
-definire count dei turni lato client, e anche se siamo in game avviato e anche se siamo in lobby, possiamo chiedere a server
-questione degli id in game e in lobby dato che virtualmente un player esiste quando comincia una partita,
ma un client esiste da quando avvia l'applicazione e comunque fa anche le operazioni riguardo alla lobby che non sono gestibili in partita
-problema dello static
 */
