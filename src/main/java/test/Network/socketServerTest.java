package test.Network;

import it.polimi.ingsw.exceptions.InvalidCommandException;
import it.polimi.ingsw.network.client.Client;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class socketServerTest {

    @Test
    void client1() throws IOException, InvalidCommandException {
        Client client1 = new Client();
        /*client1.connection("127.0.0.1", 2345);
        String m1 = client1.sendMessage("hello");
        String m2 = client1.sendMessage("world");
        String m3 = client1.sendMessage("end");

        assertEquals("hello", m1);
        assertEquals("world", m2);
        assertEquals("end of connection", m3);*/
    }

    @Test
    void client2() throws IOException, InvalidCommandException {
        Client client2 = new Client();
        client2.connection("127.0.0.1", 2345);
        /*String m1 = client2.sendMessage("hello");
        String m2 = client2.sendMessage("world");
        String m3 = client2.sendMessage("end");

        assertEquals("hello", m1);
        assertEquals("world", m2);
        assertEquals("end of connection", m3);*/
    }
}