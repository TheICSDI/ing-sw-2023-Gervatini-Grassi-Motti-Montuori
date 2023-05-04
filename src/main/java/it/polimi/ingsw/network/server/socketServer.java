package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.*;
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
                //Prima cosa dopo la connessione chiede un nickname finchè non ne riceve uno unico
                out.println("Set your nickname: ");
                String nickname;
                nickname=in.readLine();
                System.out.println("Mex ricevuto: " + nickname);
                while(gameController.allPlayers.containsKey(nickname)){
                    out.println("NotValid");
                    nickname=in.readLine();
                }
                //che poi viene aggiungo alla hashmap statica allPlayers e crea l'oggetto player associato
                gameController.allPlayers.put(nickname,new Player(nickname,out));
                out.println(nickname);

                GeneralMessage mex = null;
                //loop infinito che riceve i messaggi
                while((input = in.readLine()) != null){
                    //identify legge la action del messaggio e re istanzia mex come la corrispondente sottoclasse di General Message
                    switch (GeneralMessage.identify(input)){
                        case CREATELOBBY -> mex=new CreateLobbyMessage(input);
                        case SHOWLOBBY -> mex=new ShowLobbyMessage(input);
                        case JOINLOBBY -> mex=new JoinLobbyMessage(input);
                        case STARTGAME -> mex=new StartGameMessage(input);
                        case PICKTILES -> mex=new PickTilesMessage(input);
                        case SELECTORDER -> mex = new SelectOrderMessage(input);
                        case SELECTCOLUMN -> mex = new SelectColumnMessage(input);
                        //TODO Mettere tutti i casi nella ricezione messaggi
                    }
                    //il comando viene eseguito
                    if(!(mex ==null)){
                        out.println(SC.executeMessage(mex));
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



