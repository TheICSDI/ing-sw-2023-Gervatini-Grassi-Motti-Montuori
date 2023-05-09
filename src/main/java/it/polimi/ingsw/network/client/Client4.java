package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.messages.SetNameMessage;
import it.polimi.ingsw.view.CLI;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client4 {
    private static PrintWriter out;
    private static BufferedReader in;
    private static clientController controller;
    public static void main(String[] args) throws IOException, InterruptedException {

        Client Client = new Client();
        //Client.connection("192.168.1.234", 2345); //Forse dovrei censurarlo il mio ip tbh
        Socket clientSocket = new Socket("127.0.0.1", 23450);

        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        Scanner input = new Scanner(System.in);
        SetNameMessage nick;
        CLI cli = new CLI();
        //Richiesta nickname unico
        System.out.println(in.readLine());
        //Controllo unicità nome
        do {

            //username = cli.askUsername();
            //nick = new SetNameMessage(username,true); linee effettive
            nick = new SetNameMessage("iFap2Kat",true); //provv
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
        ExecutorService executor = Executors.newFixedThreadPool(1);
        //thread che rimane in ascolto di messaggi
        executor.submit(()-> {
            try {
                Client.listenSocket(controller,in);
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        Client.sendMessage("createlobby 2",controller,in,out,cli,true,null);//per velocizzare, sarà da rimuovere
        //Ciclio per invio messaggi
        while (true) { //Condizione da rivedere

            Client.sendMessage(input.nextLine(),controller,in,out,cli,true,null);
            out.flush();
        }
    }
}
