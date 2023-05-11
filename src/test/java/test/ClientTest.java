package test;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.SetNameMessage;
import it.polimi.ingsw.view.CLI;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import test.Message.SetNameTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void socket1() throws IOException, InterruptedException {
        Client c=new Client();
        CLI virtualView=new CLI();
        c.connection("127.0.0.1",23450);
        System.out.println(Client.getIn().readLine());
        Client.getOut().println(new SetNameMessage("Mayhem",true));
        SetNameMessage reply = SetNameMessage.decrypt(Client.getIn().readLine());
        assertEquals("Mayhem",reply.getUsername());
        assertTrue(reply.isAvailable());
        Client.setController(new clientController(reply.getUsername()));
        Client.setVirtualView(new CLI());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //thread che rimane in ascolto di messaggi
        executor.submit(()-> {
            try {
                Client.listenSocket();
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        //Client.sendMessage("createlobby 2",controller,in,out,cli);//per velocizzare, sarà da rimuovere
        //Ciclo per invio messaggi
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("createlobby 2",true);
        TimeUnit.SECONDS.sleep(10);
        TimeUnit.MILLISECONDS.sleep(250);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("pt 1 4 1 5",true);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("so 2 1",true);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("sc 3",true);
        TimeUnit.SECONDS.sleep(20);
    }

    @Test
    void socket2() throws IOException, InterruptedException {
        Client c=new Client();
        c.connection("127.0.0.1",23450);
        System.out.println(Client.getIn().readLine());
        Client.getOut().println(new SetNameMessage("Mayhem",true));
        SetNameMessage reply = SetNameMessage.decrypt(Client.getIn().readLine());
        assertFalse(reply.isAvailable());
        Client.getOut().println(new SetNameMessage("Gyne",true));
        reply = SetNameMessage.decrypt(Client.getIn().readLine());
        assertEquals("Gyne",reply.getUsername());
        assertTrue(reply.isAvailable());
        Client.setController(new clientController(reply.getUsername()));
        Client.setVirtualView(new CLI());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //thread che rimane in ascolto di messaggi
        executor.submit(()-> {
            try {
                Client.listenSocket();
            } catch (IOException | ParseException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        });
        //Client.sendMessage("createlobby 2",controller,in,out,cli);//per velocizzare, sarà da rimuovere
        //Ciclo per invio messaggi
        TimeUnit.MILLISECONDS.sleep(250);

        Client.sendMessage("joinlobby 1",true);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("startgame",true);
        TimeUnit.SECONDS.sleep(1);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("pt 3 1 4 1",true);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("so 2 1",true);
        TimeUnit.MILLISECONDS.sleep(250);
        Client.sendMessage("sc 3",true);
        TimeUnit.SECONDS.sleep(20);

    }

    @Test
    void RMI() {
    }
}