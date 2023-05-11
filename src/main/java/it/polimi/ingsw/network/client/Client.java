package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Player;
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
import java.util.Objects;
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

    private static View virtualView;



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
     * @param socket true if socket connection, false if RMI connection.
     */
    public static void sendMessage(String message, boolean socket) throws RemoteException {
        if (message.equals("/help")) {
            virtualView.help();
        } else {
            GeneralMessage clientMessage;
            //Controlla che il formato del comando sia giusto
            clientMessage = controller.checkMessageShape(message, controller);
            Action curr_action = clientMessage.getAction();
            String toSend = clientMessage.toString();
            //Se il formato è sbagliato checkmessage restituisce un messaggio di tipo error e non viene inviato
            if (curr_action.equals(Action.ERROR)) {
                virtualView.displayMessage(toSend);
            } else if (curr_action.equals(Action.SHOWPERSONAL)) {
                virtualView.displayMessage("\n  Your personal goal");
                virtualView.showBoard(controller.getSimpleGoal(), Action.UPDATESHELF);
            } else if (curr_action.equals(Action.SHOWCOMMONS)) {
                virtualView.displayMessage("Common goals: ");
                virtualView.showCommons(controller.cc);
            } else if(curr_action.equals(Action.SHOWOTHERS)){
                virtualView.showOthers(controller.getOthers());
            }else {
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
    public static void listenSocket() throws IOException, ParseException, InvalidKeyException {
        while(true) {
            String message = in.readLine();
            elaborate(message);
        }
    }
    public static void elaborate(String message) throws ParseException, InvalidKeyException {
        ReplyMessage reply;
        Action replyAction = ReplyMessage.identify(message);
        switch (replyAction) {
            //Crate a lobby
            case CREATELOBBY -> {
                reply = CreateLobbyReplyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                virtualView.displayMessage(reply.getMessage());
            }
            //Join a lobby given its id
            case JOINLOBBY -> {
                reply = JoinLobbyReplyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                virtualView.displayMessage(reply.getMessage());
            }
            //List of available lobbies
            case SHOWLOBBY -> {
                reply = ShowLobbyReplyMessage.decrypt(message);
                virtualView.showLobby(((ShowLobbyReplyMessage)reply).getLobbies());
            }
            //Start the game
            case STARTGAME -> {
                controller.setIdLobby(0);
                reply = StartGameReplyMessage.decrypt(message);
                controller.setIdGame(reply.getIdGame());
                controller.setFirstTurn(true);
                virtualView.displayMessage(reply.getMessage());
            }
            case UPDATEBOARD,UPDATESHELF -> {
                reply = UpdateBoardMessage.decrypt(message);
                virtualView.showBoard(reply.getSimpleBoard(),replyAction);
                if(controller.isFirstTurn()){
                    controller.setFirstTurn(false);
                    virtualView.displayMessage("\n  Your personal goal");
                    virtualView.showBoard(controller.getSimpleGoal(), Action.UPDATESHELF);
                    virtualView.displayMessage("Common goals: ");
                    virtualView.showCommons(controller.cc);
                }
            }
            case INGAMEEVENT -> {
                reply = ReplyMessage.decrypt(message);
                virtualView.displayMessage(reply.getMessage());
            }
            case CHOSENTILES ->{
                reply = ChosenTilesMessage.decrypt(message);
                List<Tile> tile=new ArrayList<>();
                reply.getTiles(tile);
                virtualView.showChosenTiles(tile);
            }
            case SHOWPERSONAL -> {
                reply = UpdateBoardMessage.decrypt(message);
                controller.setSimpleGoal(reply.getSimpleBoard());
            }
            case SHOWCOMMONS -> {
                reply = SendCommonCards.decrypt(message);
                reply.getCC(controller.cc);
            }
            case SHOWOTHERS -> {
                reply = OtherPlayersMessage.decrypt(message);
                String nick=((OtherPlayersMessage) reply).getP().getNickname();
                Player p=((OtherPlayersMessage) reply).getP();
                controller.getOthers().put(nick,p);
            }
            case C -> {
                reply = ChatMessage.decrypt(message);
                virtualView.displayMessage(reply.getUsername() + ": " + ((ChatMessage)reply).getPhrase());
            }
            case CA -> {
                reply=BroadcastMessage.decrypt(message);
                virtualView.displayMessage(reply.getUsername() + ": " + ((BroadcastMessage)reply).getPhrase());
            }
            //TODO Decidire cosa fare una volta finito il game
            //Per gli altri comandi si aspetta errore perchè se non è in una lobby non li può chiamare
            //altrimenti non è questa sezione che li controlla(e invece ha senso):D
            case ENDGAME -> {
                controller.setIdGame(0);
            }
            default -> {
                reply = ReplyMessage.decrypt(message);
            }
        }
        //distinzione cli gui
    }

    /** It starts the connection based on the client's decision. */
    public static void main(String[] args) throws IOException {
        //TODO DISCONNESSIONI
        String connectionType;
        do {
            System.out.println("""
                Choose connection type:\s
                [1]: for Socket
                [2]: for RMI""");
            Scanner input = new Scanner(System.in);
            connectionType = input.next();
        }while(!(connectionType.equals("1") || connectionType.equals("2")));
        if (connectionType.equals("1")) {
            System.out.println("Socket connection chosen");
            socket();
        } else {
            System.out.println("RMI connection chosen");
            RMI();
        }
    }

    public static void socket() throws IOException {
        Client Client = new Client();
        //Client.connection("192.168.1.234", 2345);
        Client.connection("127.0.0.1", 23450);
        Scanner input = new Scanner(System.in);
        String username;
        SetNameMessage nick;
        virtualView = new CLI();
        //Richiesta nickname unico
        System.out.println(in.readLine());
        //Controllo unicità nome
        do {
            username = virtualView.askUsername();
            nick = new SetNameMessage(username,true);
            out.println(nick);
            try{
                nick = SetNameMessage.decrypt(in.readLine());
            }catch(Exception ignored){}

            virtualView.printUsername(nick.getUsername(), nick.isAvailable());
        } while (!nick.isAvailable());
        //Ogni player ha il suo clientController
        controller = new clientController(nick.getUsername());
        //inizio connessione
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //thread che rimane in ascolto di messaggi
        executor.submit(()-> {
            try {
                listenSocket();
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        virtualView.displayMessage("Write /help for command list.");
        //Client.sendMessage("createlobby 2",controller,in,out,cli);//per velocizzare, sarà da rimuovere
        //Ciclo per invio messaggi
        while(true) { //Condizione da rivedere
            sendMessage(input.nextLine(),true);
        }

        //executor.shutdownNow();//uccisione thread
    }

    /** It starts the RMI connections. */
    public static void RMI(){
        Scanner in = new Scanner(System.in);
        Client c = new Client();
        controller = new clientController();
        virtualView = new CLI();//TODO differenziazione tra  cli e gui in futuro

        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23451);
            stub = (RMIconnection) Naming.lookup("rmi://localhost:" + 23451 + "/RMIServer");
            RMIclient = new RMIclientImpl(controller); //per ricevere risposta
            Naming.rebind("rmi://localhost:" + 23451 + "/RMIServer", RMIclient);
            //TODO MANCA IL PING PER SOCKET E CLIENT
            System.out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
            setName();
            while(true){
                sendMessage(in.nextLine(), false);
            }
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
    //TODO da fare
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
        nick = new SetNameMessage(input, true);
        stub.RMIsendName(nick.toString(), RMIclient);
    }

    //FORTEST
    public static PrintWriter getOut() {
        return out;
    }

    public static void setOut(PrintWriter out) {
        Client.out = out;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public static void setIn(BufferedReader in) {
        Client.in = in;
    }

    public static clientController getController() {
        return controller;
    }

    public static void setController(clientController controller) {
        Client.controller = controller;
    }
    public static View getVirtualView() {
        return virtualView;
    }

    public static void setVirtualView(View virtualView) {
        Client.virtualView = virtualView;
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
