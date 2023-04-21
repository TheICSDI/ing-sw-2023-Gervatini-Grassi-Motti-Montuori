package main.java.it.polimi.ingsw.client;

import main.java.it.polimi.ingsw.messages.ClientMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class socketClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    /*
    bisogna usare un id per il client handler, potremmo avere un id per il client e un id per i giocatore assegnato solo
    per il game per esempio (questione aperta, almeno per me)
     */
    private final clientController controller = new clientController(1);
    public void connection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    //sendMessage chiama un filtro applicato dal controller che gli blocca l'invio di messaggi formattati male oppure
    //che non puo fare in quel momento
    public String sendMessage(String message) throws IOException {
        ClientMessage clientMessage;
        /*
        non so dove l'errore dello static context quindi non tocco nulla finche' non abbiamo le idee piu chiare
         */
        clientMessage = clientController.checkMessageShape(message);
        out.println(clientMessage);
        return in.readLine();//non so a cosa serva/come gestire questa riga, ho solo modificato il metodo che ho trovato
    }

    public void endConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
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
