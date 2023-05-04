package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client2 {
    private static PrintWriter out;
    private static BufferedReader in;
    private static clientController controller;
    public static void main(String[] args) throws IOException {

        socketClient Client = new socketClient();
        //Client.connection("192.168.1.234", 2345); //Forse dovrei censurarlo il mio ip tbh
        Socket clientSocket = new Socket("127.0.0.1", 2345);

        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Scanner input = new Scanner(System.in);
        String nick;
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
    }
}
