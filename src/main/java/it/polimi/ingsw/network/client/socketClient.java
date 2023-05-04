package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidCommandException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.messages.*;
import org.json.simple.parser.ParseException;

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
    public void sendMessage(String message,clientController control,BufferedReader In,PrintWriter Out) throws IOException {
        GeneralMessage clientMessage;
        //Controlla che il formato del comando sia giusto
        clientMessage = control.checkMessageShape(message,control);
        Action curr_action=clientMessage.getAction();
        String toSend = clientMessage.toString();
        //Se il formato è sbagliato checkmessage restituisce un messaggio di tipo error e non viene inviato
        if(curr_action.equals(Action.ERROR)) {
            new ReplyMessage(toSend,Action.ERROR).print();
        }else{
            Out.println(toSend);
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

        //inizio connessione
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(()-> {
            try {
                Client.listenMessages(controller,in);
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });

        while (true) { //TODO (rivedere)probabilmente serve un thread che riceve un messaggio quando un altro giocatore della stessa lobby inizia il game
            Client.sendMessage(input.nextLine(),controller,in,out);
        }

        //A regola esce dal ciclo dopo STARTGAME
        //creazione thread che sta in ascolto dal server
        //executor.shutdownNow();//uccisione thread
        //implementare ciclo per tornare a lobby ecc
    }
    public void listenMessages(clientController controller, BufferedReader In) throws IOException, ParseException, InvalidKeyException {
        ReplyMessage reply;
        while(true) {
            String message = In.readLine();
            Action replyAction = ReplyMessage.identify(message);
            switch (replyAction) {
                case CREATELOBBY -> {
                    reply = CreateLobbyReplyMessage.decrypt(message);
                    controller.setIdLobby(reply.getIdLobby());
                }
                //Semplicemente una stringa di successo/errore
                case JOINLOBBY -> {
                    reply = JoinLobbyReplyMessage.decrypt(message);
                    controller.setIdLobby(reply.getIdLobby());
                }
                //Lista di tutte le lobby disponibili
                case SHOWLOBBY -> {
                    reply = ShowLobbyReplyMessage.decrypt(message);
                }
                //Conferma che il game può iniziare
                case STARTGAME -> {
                    controller.setIdLobby(0);
                    reply = StartGameReplyMessage.decrypt(message);
                }
                //TODO Entrano in game ora dobbiamo fare il gioco(Il gioco inizia ora)
                //TODO AGGIUNGERE IN GAME LE RICHIESTE PER IL CLIENT DI INVIARE I COMANDI
                //TODO AGGIUNGERE I METODI E LE STRUTTURE DATI PER LA CLI
                //TODO MATRICE SHELF, MATRICE BOARD, CARTE PERSONAL GOAL ETC;
                //TODO COLLEGARE MESSAGGIO CHIUSURA GAME
                //Per gli altri comandi si aspetta errore perchè se non è in una lobby non li può chiamare
                //altrimenti non è questa sezione che li controlla(e invece ha senso):D
                default -> {
                    reply = ReplyMessage.decrypt(message);
                }
            }
            reply.print();
        }
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
