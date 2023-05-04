package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidCommandException;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.ReplyMessage;
import it.polimi.ingsw.network.messages.ShowLobbyReplyMessage;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

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
    //che non puo fare in quel momento
    public ReplyMessage sendMessage(String message) throws IOException, InvalidCommandException {
        GeneralMessage clientMessage;
        /*
        non so dove l'errore dello static context quindi non tocco nulla finche' non abbiamo le idee piu chiare
         */
        clientMessage = controller.checkMessageShape(message);
        Action curr_action=clientMessage.getAction();
        String toSend = clientMessage.toString();
        if(toSend.equals("Error") ) return new ReplyMessage("Errore");
        out.println(toSend);
        switch (curr_action){
            case CREATELOBBY, JOINLOBBY -> {
                return ReplyMessage.decrypt(in.readLine());
            }
            case SHOWLOBBY -> {
                return ShowLobbyReplyMessage.decrypt(in.readLine());
            }
            case STARTGAME -> {
                //TODO I client coinvolti nel game devono avere un nuovo tipo di invio/ricezione messaggi con un
                //thread che riceve sempre e stampa (board shelf e goal ecc.) appena riceve e uno che invia messaggi
                //messaggio con formattazione shelf e board in gson, turnazione ecc...
            }

        }
    }

    public void endConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException, InvalidCommandException {
        socketClient Client = new socketClient();
        Client.connection("127.0.0.1", 2345);
        Scanner input = new Scanner(System.in);
        String nick;
        System.out.println(in.readLine());
        do {
            out.println(input.nextLine());
            nick = in.readLine();
        } while (nick.equals("NotValid"));
        controller = new clientController(nick);
        System.out.println("Nickname set: "+nick);

        while (true) {
            String x = input.nextLine();
            ReplyMessage reply = Client.sendMessage(x);
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
