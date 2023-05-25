/** It represents a client that is able to establish connection with a sever, both via socket and RMI (exclusive).
 * @author Caterina Motti, Andrea Grassi, Marco Gervatini. */
package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.RMIconnection;
import it.polimi.ingsw.view.CLI;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Client extends Application {
    private Socket clientSocket;
    private static clientController controller;
    private static PrintWriter out;
    private static BufferedReader in;
    private static RMIconnection stub;
    private static RMIclientImpl RMIclient;
    private static View virtualView;
    private static int ping = 0;
    private static final int pingTime = 30;
    public static boolean connected = true;


    /*
        // Load root layout from fxml file.
        FXMLLoader loader = new FXMLLid per il client e un id per i giocatore assegnato solo
        per il game per esempio (questione aperta, almeno per me)
         */
    /** It starts the socket connection on the given ip and port. */
    public void connection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /** It closes the socket connection. */
    public void endConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    /** It listens to the messages received by via socket, and it calls the elaborate method. */
    public static void listenSocket() throws IOException, ParseException, InvalidKeyException, InterruptedException {
        while(true) {
            String message = in.readLine();
            elaborate(message);
        }
    }

    /** It elaborates the given message and creates the equivalent message type based on the action.
     * It interacts with the instance of clientController and with the virtualView.
     * @param message to be elaborated. */
    public static void elaborate(String message) throws ParseException, InvalidKeyException, RemoteException, InterruptedException {
        ReplyMessage reply;
        Action replyAction = ReplyMessage.identify(message);
        switch (replyAction) {
            case CREATELOBBY -> {
                reply = CreateLobbyReplyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                virtualView.displayMessage(reply.getMessage());
            }
            case JOINLOBBY -> {
                reply = JoinLobbyReplyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                virtualView.displayMessage(reply.getMessage());
            }
            case SHOWLOBBY -> {
                reply = ShowLobbyReplyMessage.decrypt(message);
                virtualView.showLobby(((ShowLobbyReplyMessage)reply).getLobbies());
            }
            case STARTGAME -> {
                controller.setIdLobby(0);
                reply = StartGameReplyMessage.decrypt(message);
                controller.setIdGame(reply.getIdGame());
                controller.setFirstTurn(true);
                virtualView.startGame(reply.getMessage());
            }
            case UPDATEBOARD -> {
                reply = UpdateBoardMessage.decrypt(message);
                virtualView.showBoard(reply.getSimpleBoard());
                if(controller.isFirstTurn()){
                    controller.setFirstTurn(false);
                    virtualView.showPersonal(controller.getSimpleGoal());
                    virtualView.showCommons(controller.cc);
                }
            }
            case UPDATESHELF -> {
                reply = UpdateBoardMessage.decrypt(message);
                virtualView.showShelf(reply.getSimpleBoard());
            }
            case INGAMEEVENT -> {
                reply = ReplyMessage.decrypt(message);
                virtualView.displayMessage(reply.getMessage());
            }
            case TURN -> {
                reply = ReplyMessage.decrypt(message);
                virtualView.playersTurn(reply.getMessage());
            }
            case CHOSENTILES ->{
                reply = ChosenTilesMessage.decrypt(message);
                List<Tile> tile = new ArrayList<>();
                reply.getTiles(tile);
                virtualView.showChosenTiles(tile,((ChosenTilesMessage) reply).isToOrder());
            }
            case SHOWPERSONAL -> {
                reply = UpdateBoardMessage.decrypt(message);
                controller.setSimpleGoal(Integer.parseInt(reply.getMessage()));
            }
            case SHOWCOMMONS -> {
                reply = SendCommonCards.decrypt(message);
                reply.getCC(controller.cc);
            }
            case SHOWOTHERS -> {
                reply = OtherPlayersMessage.decrypt(message);
                String nick=((OtherPlayersMessage) reply).getP().getNickname();
                Player p=((OtherPlayersMessage) reply).getP();
                controller.getOthers().put(nick,p);
                virtualView.updateOthers(controller.getOthers());
            }
            case C -> {
                reply = ChatMessage.decrypt(message);
                virtualView.showChat(reply.getUsername() + " to you: " + ((ChatMessage)reply).getPhrase());
            }
            case CA -> {
                reply=BroadcastMessage.decrypt(message);
                virtualView.showChat(reply.getUsername() + " to all: " + ((BroadcastMessage)reply).getPhrase());
            }
            case ENDGAME -> {
                controller.setIdGame(0);
                virtualView.endGame();
            }
            case PING -> {
                //RMI
                ping++;
                TimeUnit.SECONDS.sleep(pingTime);
                stub.RMIsend(new PingMessage(controller.getNickname()).toString());
            }
            case ERROR -> {
                reply = ReplyMessage.decrypt(message);
                virtualView.displayError(reply.getMessage());
            }
            default -> reply = ReplyMessage.decrypt(message);
        }
    }

    /**
     * It checks if the message has the right format and sends it to server.
     * @param message to be sent.
     * @param socket true if socket connection, false if RMI connection.
     */
    public static void sendMessage(String message, boolean socket) throws RemoteException {
        if (message.equals("/help")) {
            virtualView.help();
        } else {
            GeneralMessage clientMessage;
            //Check the format
            clientMessage = controller.checkMessageShape(message, controller);
            Action curr_action = clientMessage.getAction();
            String toSend = clientMessage.toString();
            //If the format is wrong the function checkMessageShape return an error message
            if (curr_action.equals(Action.ERROR)) {
                virtualView.displayMessage(toSend);
            } else if (curr_action.equals(Action.SHOWPERSONAL)) {
                virtualView.displayMessage("\n  Your personal goal");
                virtualView.showPersonal(controller.getSimpleGoal());
            } else if (curr_action.equals(Action.SHOWCOMMONS)) {
                virtualView.displayMessage("Common goals: ");
                virtualView.showCommons(controller.cc);
            } else if(curr_action.equals(Action.SHOWOTHERS)){
                virtualView.showOthers(controller.getOthers());
            } else {
                //The message is sent to the right output
                if(socket) {
                    out.println(toSend);
                } else {
                    stub.RMIsend(toSend);
                }
            }
        }
    }

    /** It starts the game based on the arguments (regarding the type of view, CLI or GUI) and
     * on client's decision regarding the type of connection. */
    public static void main(String[] args) throws IOException {
        boolean isCli = false;
        virtualView = null;
        for (String arg : args) {
            if (arg.equals("--cli") || arg.equals("-c")) {
                isCli = true;
                break;
            }
        }
        if(isCli){
            virtualView = new CLI();
        } else{
            virtualView = new GUI();
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(()-> {
                launch(args);
            });
        }
        String connectionType = virtualView.chooseConnection();
        if (connectionType.equals("1")) {
            socket();
        } else {
            RMI();
        }
    }

    /** It starts a socket connection with the server. */
    public static void socket() throws IOException {
        Client Client = new Client();
        //Client.connection("192.168.1.234", 2345);
        Client.connection("127.0.0.1", 23450);
        String username;
        SetNameMessage nick;

        //Request unique nickname for the client
        System.out.println(in.readLine());
        do {
            username = virtualView.askUsername();
            nick = new SetNameMessage(username,true);
            out.println(nick);
            try{
                nick = SetNameMessage.decrypt(in.readLine());
            } catch(Exception ignored){}
            virtualView.printUsername(nick.getUsername(), nick.isAvailable());
        } while (!nick.isAvailable());
        //If the nickname is available the clientController is created
        controller = new clientController(nick.getUsername());

        //Connection starts with a pool of thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()-> {
            try {
                listenSocket();
            } catch (IOException | ParseException | InvalidKeyException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        virtualView.displayMessage("Write /help for command list.");

        //TODO: condizione valida sse il client è connesso, da rivedere
        while(true) {
            sendMessage(virtualView.getInput(),true);
        }
        //executor.shutdownNow();
    }

    /** It starts the RMI connection with the server. */
    public static void RMI(){
        controller = new clientController();
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23451);
            stub = (RMIconnection) Naming.lookup("rmi://localhost:" + 23451 + "/RMIServer");
            RMIclient = new RMIclientImpl(controller);
            System.out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
            setName();

            //Pool of thread for the ping messages
            ExecutorService executor1 = Executors.newSingleThreadExecutor();
            executor1.submit(()-> {
                try {
                    stub.RMIsend(new PingMessage(controller.getNickname()).toString());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            ExecutorService executor2 = Executors.newSingleThreadExecutor();
            executor2.submit(()-> {
                try {
                    ping();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            //While the client is connected it sends the messages to the server
            while(connected){
                sendMessage(virtualView.getInput(), false);
            }
            //Otherwise it closes the threads
            executor1.shutdownNow();
            executor2.shutdownNow();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ping() throws InterruptedException {
        int x=-1;
        while(x<ping) {
            x=ping;
            //System.out.println("Ping n^" + ping);
            TimeUnit.SECONDS.sleep(pingTime);
        }
        System.out.println("Disconnected"); //TODO dovrebbe essere in view
        connected=false;
    }

    public static void setName() throws RemoteException {
        String input;
        SetNameMessage nick;
        input = virtualView.askUsername();
        nick = new SetNameMessage(input, true);
        stub.RMIsendName(nick.toString(), RMIclient);
    }

    @Override
    public void start(Stage stage) {
        ((GUI)virtualView).startGuiConnection(stage);
    }

    //GETTER/SETTER
    public static void setVirtualView(View virtualView) {
        Client.virtualView = virtualView;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public static clientController getController() {
        return controller;
    }

    public static void setController(clientController controller) {
        Client.controller = controller;
    }

    public static PrintWriter getOut() {
        return out;
    }

    public static void setOut(PrintWriter out) {
        Client.out = out;
    }

    public static BufferedReader getIn() {
        return in;
    }

    public static void setIn(BufferedReader in) {
        Client.in = in;
    }

    public static View getVirtualView() {
        return virtualView;
    }
}