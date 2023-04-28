package main.java.it.polimi.ingsw.network.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class socketServer {
    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while(true){
            new clientHandler(serverSocket.accept()).start();
        }
    }

    public void end() throws IOException {
        serverSocket.close();
    }

    private static class clientHandler extends Thread{
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public clientHandler(Socket socket){
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String input;
                while((input = in.readLine()) != null){
                    if("end".equals(input)){
                        out.println("end of connection");
                        break;
                    }
                    out.println(input);
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        socketServer server = new socketServer();
        server.start(2345);
    }
}



