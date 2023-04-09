package main.java.it.polimi.ingsw.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class socketClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void connection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);

        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String m) throws IOException {
        out.println(m);
        return in.readLine();
    }

    public void endConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
