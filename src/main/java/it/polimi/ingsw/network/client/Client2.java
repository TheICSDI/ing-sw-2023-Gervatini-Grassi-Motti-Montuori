package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidCommandException;
import it.polimi.ingsw.network.messages.ReplyMessage;

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
        while (!reply.getGameStart()) {
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
