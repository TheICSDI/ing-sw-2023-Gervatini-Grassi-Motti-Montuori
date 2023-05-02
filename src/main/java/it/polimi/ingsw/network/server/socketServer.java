package main.java.it.polimi.ingsw.network.server;

import main.java.it.polimi.ingsw.controller.gameController;
import main.java.it.polimi.ingsw.controller.serverController;
import main.java.it.polimi.ingsw.exceptions.InvalidActionException;
import main.java.it.polimi.ingsw.exceptions.InvalidKeyException;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.network.messages.CreateLobbyMessage;
import main.java.it.polimi.ingsw.network.messages.GeneralMessage;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class socketServer {
    private ServerSocket serverSocket;

    private static final serverController SC=new serverController();


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
                out.println("Set your nickname: ");
                String nickname;
                nickname=in.readLine();
                while(gameController.allPlayers.containsKey(nickname)){
                    System.out.println("Mex ricevuto: " + nickname);
                    out.println("NotValid");
                    nickname=in.readLine();
                }
                gameController.allPlayers.put(nickname,new Player(nickname));
                out.println(nickname);

                GeneralMessage mex = null;

                while((input = in.readLine()) != null){
                    switch (GeneralMessage.identify(input)){
                        case CREATELOBBY -> {
                            mex=new CreateLobbyMessage(input);
                        }
                        //TODO Mettere tutti i casi nella ricezione messaggi
                    }
                    if(!(mex ==null)){
                        SC.executeMessage(mex);
                    }
                }

                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException | ParseException | InvalidKeyException | InvalidActionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        socketServer server = new socketServer();
        server.start(2345);
    }
}



