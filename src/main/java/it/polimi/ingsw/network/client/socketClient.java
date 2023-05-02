package main.java.it.polimi.ingsw.network.client;

import main.java.it.polimi.ingsw.controller.clientController;
import main.java.it.polimi.ingsw.exceptions.InvalidCommandException;
import main.java.it.polimi.ingsw.network.messages.GeneralMessage;
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
    public String sendMessage(String message) throws IOException, InvalidCommandException {
        GeneralMessage clientMessage;
        /*
        Controllo argomenti
         */
        clientMessage = controller.checkMessageShape(message);
        //formattazione
        String toSend = clientMessage.toString();
        //invio
        out.println(toSend);
        // A OGNI MESSAGGIO DEVE CORRISPONDERE UNA RISPOSTA DEL SERVER
        return in.readLine();//non so a cosa serva/come gestire questa riga, ho solo modificato il metodo che ho trovato
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
        //scegli nickname
        System.out.println(in.readLine());
        do {
            out.println(input.nextLine());
            nick = in.readLine();
        } while (!Objects.equals(nick, "NotValid"));
        //creazione client controller associato al giocatore dopo aver ricevuto conferma di avere un nickname unico
        controller = new clientController(nick);
        //invio messaggi
        while (true) {
            String x = input.nextLine();
            //controllo correttezza messaggio e formattazione in json
            Client.sendMessage(x);

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
