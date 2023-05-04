package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidCommandException;
import it.polimi.ingsw.network.messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class socketClient {
    private Socket clientSocket;
    private static PrintWriter out;
    private static BufferedReader in;
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
    //sendMessage chiama un filtro applicato dal controller che gli blocca l'invio di messaggi formattati male oppure
    //che non puo fare in quel momento//Ho aggiunto qualche parametro per renderla generale così possiamo fare più client
    public ReplyMessage sendMessage(String message,clientController control,BufferedReader In,PrintWriter Out) throws IOException {
        GeneralMessage clientMessage;
        //Controlla che il formato del comando sia giusto
        clientMessage = control.checkMessageShape(message,control);
        Action curr_action=clientMessage.getAction();
        String toSend = clientMessage.toString();
        //Se il formato è sbagliato checkmessage restituisce un messaggio di tipo error e non viene inviato
        if(curr_action.equals(Action.ERROR)) return new ReplyMessage(toSend);
        //altrimenti viene inviato
        Out.println(toSend);
        //in base al tipo di comando inviato il client aspetta un determinato tipo di risposta
        switch (curr_action){
            case CREATELOBBY -> {
                CreateLobbyReplyMessage x = CreateLobbyReplyMessage.decrypt(In.readLine());
                control.setIdLobby(x.getIdLobby());
                return x;
            }
            //Semplicemente una stringa di successo/errore
            case JOINLOBBY -> {
                JoinLobbyReplyMessage x= JoinLobbyReplyMessage.decrypt(In.readLine());
                control.setIdLobby(x.getIdLobby());
                return x;
            }
            //Lista di tutte le lobby disponibili
            case SHOWLOBBY -> {
                return ShowLobbyReplyMessage.decrypt(In.readLine());
            }
            //Conferma che il game può iniziare
            case STARTGAME -> {
                //TODO I client coinvolti nel game devono avere un nuovo tipo di invio/ricezione messaggi con un
                //thread che riceve sempre e stampa (board shelf e goal ecc.) appena riceve e uno che invia messaggi
                //messaggio con formattazione shelf e board in gson, turnazione ecc...
                control.setIdLobby(0);
                return StartGameReplyMessage.decrypt(In.readLine());
            }
            //Per gli altri comandi si aspetta errore perchè se non è in una lobby non li può chiamare
            //altrimenti non è questa sezione che li controlla(e invece ha senso):D
            default -> {
                return ReplyMessage.decrypt(In.readLine());
            }
        }
    }

    public void inGameSend(PrintWriter out,socketClient Client) throws IOException,InvalidCommandException{
        Scanner input = new Scanner(System.in);
        //Invio messaggi al server durante il game, funzione simile alla precedente ma qua non aspetto una risposta
        //se mando un comando da lobby riceve errore e non invia
        while(true){
            GeneralMessage clientMessage;
            clientMessage = controller.checkMessageShape(input.nextLine(),controller);
            Action curr_action=clientMessage.getAction();
            String toSend = clientMessage.toString();
            if(toSend.equals("Error") ) System.out.println("Errore");
            if(curr_action.equals(Action.CREATELOBBY)||
                    curr_action.equals(Action.SHOWLOBBY) ||
                    curr_action.equals(Action.JOINLOBBY)){
                System.out.println("Already in a game");
            }else {
                out.println(toSend);
            }
        }
    }
    public void endConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException {
        socketClient Client = new socketClient();
        //Client.connection("192.168.1.234", 2345);
        Client.connection("127.0.0.1", 2345);
        Scanner input = new Scanner(System.in);
        String nick;

        //Richiesta nickname unico
        System.out.println(in.readLine());
        do {
            out.println(input.nextLine());
            nick = in.readLine();
        } while (nick.equals("NotValid"));
        //Ogni player ha il suo clientController
        controller = new clientController(nick);
        System.out.println("Nickname set: "+nick);
        ReplyMessage reply=new ReplyMessage("");
        //inizio connessione
        while (!reply.getGameStart()) { //TODO (rivedere)probabilmente serve un thread che riceve un messaggio quando un altro giocatore della stessa lobby inizia il game
            String x = input.nextLine();
            reply = Client.sendMessage(x,controller,in,out);
            reply.print();
        }
        //A regola esce dal ciclo dopo STARTGAME
        //creazione thread che sta in ascolto dal server
        ExecutorService executor = Executors.newFixedThreadPool(10);

        executor.submit(()-> {
            try {
                Client.inGameSend(out,Client);
            } catch (IOException | InvalidCommandException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println("sgroda");
        String msg=in.readLine();//non sarà string ma un formato di un messaggio
        while(msg!=null/*Placeholder per la vera condizione che sarà un messaggio di fine game*/){
            /*
             Lettura messaggio e stampa di board/shelf/ecc.
            */
        }
        executor.shutdownNow();//uccisione thread
        //implementare ciclo per tornare a lobby ecc
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
