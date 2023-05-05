package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client2 {
    private static PrintWriter out;
    private static BufferedReader in;
    private static clientController controller;
    public static void main(String[] args) throws IOException, InterruptedException {

        socketClient Client = new socketClient();
        //Client.connection("192.168.1.234", 2345); //Forse dovrei censurarlo il mio ip tbh
        Socket clientSocket = new Socket("127.0.0.1", 2345);

        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Scanner input = new Scanner(System.in);
        SetNameMessage nick;
        System.out.println(in.readLine());
        //Controllo unicità nome
        do {
            out.println(new SetNameMessage("Gynephobia",true));
            //out.println(new SetNameMessage(input.nextLine(),false));
            nick = SetNameMessage.decrypt(in.readLine());
            nick.print();
        } while (!nick.isAvailable());
        //Ogni player ha il suo clientController
        controller = new clientController(nick.getUsername());
        //inizio connessione
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(()-> {
            try {
                Client.listenMessages(controller,in);
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        Client.sendMessage("joinlobby 1",controller,in,out);//Codice temporaneo per velocizzare
        TimeUnit.SECONDS.sleep(1);//toccava aspettare la risposta per farlo automatico
        Client.sendMessage("startgame",controller,in,out);
        while (true) {//Condizione da rivedere
            Client.sendMessage(input.nextLine(),controller,in,out);
        }
    }
}
