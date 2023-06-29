package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
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
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** It represents a client that is able to establish connection with a sever, both via socket and RMI (exclusive).
 * @author Caterina Motti, Andrea Grassi, Marco Gervatini. */
public class Client extends Application {
    private Socket clientSocket;
    private static clientController controller;
    private static PrintWriter out;
    private static BufferedReader in;
    private static RMIconnection stub;
    private static RMIclientImpl RMIclient;
    private static View virtualView;
    private static int ping = 0;
    private static final int pingTime = 5;
    public static boolean connected = true;
    private static boolean socket = false;
    private static final Object SocketLock = new Object();
    private static boolean firstTurn;
    private static String IPv4;
    private final static String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    
    /** It starts the socket connection on the given ip and port.
     * @param ip address of the connection.
     * @param port chosen port for the connection. */
    public void connection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        //Directed communication between client and server
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    /** It listens to the messages received by via socket, and it calls the elaborate method. */
    public static void listenSocket() throws IOException, ParseException, InterruptedException {
        while(true) {
            String message = in.readLine();
            elaborate(message);
        }
    }

    /** It elaborates the given message and creates the equivalent message type based on the action.
     * It interacts with the instance of clientController and with the virtualView.
     * @param message to be elaborated. */
    public static void elaborate(String message) throws ParseException, RemoteException, InterruptedException {
        GeneralMessage reply;
        Action replyAction = GeneralMessage.identify(message);
        switch (replyAction) {
            case CREATELOBBY -> {
                reply = CreateLobbyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                virtualView.displayMessage(reply.getMessage());
            }
            case JOINLOBBY -> {
                reply = JoinLobbyMessage.decrypt(message);
                controller.setIdLobby(reply.getIdLobby());
                virtualView.displayMessage(reply.getMessage());
            }
            case SHOWLOBBY -> {
                reply = ShowLobbyMessage.decrypt(message);
                virtualView.showLobby(((ShowLobbyMessage)reply).getLobbies());
            }
            case STARTGAME -> {
                controller.setIdLobby(0);
                reply = StartGameMessage.decrypt(message);
                controller.setNPlayers(((StartGameMessage) reply).getNPlayers());
                controller.setIdGame(reply.getGameId());
                controller.setFirstTurn(true);
                virtualView.startGame(reply.getMessage());
                firstTurn = true;
                //set the local shelf to empty
                controller.emptyShelf();
            }
            case UPDATEBOARD, SHOWBOARD -> {
                reply = UpdateBoardMessage.decrypt(message);
                controller.setBoard(reply.getSimpleBoard());
                virtualView.showBoard(reply.getSimpleBoard());
                if(controller.isFirstTurn()){
                    controller.setFirstTurn(false);
                    virtualView.showPersonal(controller.getSimpleGoal());
                    virtualView.showCommons(controller.getCc());
                }
            }
            case UPDATESHELF, SHOWSHELF -> {
                reply = UpdateBoardMessage.decrypt(message);
                controller.setShelf(reply.getSimpleBoard());
                virtualView.showShelf(reply.getSimpleBoard());
            }
            case INGAMEEVENT, ERROR -> {
                reply = SimpleReply.decrypt(message);
                virtualView.displayMessage(reply.getMessage());
            }
            case TURN -> {
                reply = SimpleReply.decrypt(message);
                virtualView.playersTurn(reply.getMessage(), firstTurn);
                firstTurn = false;
            }
            case CHOSENTILES ->{
                reply = ChosenTilesMessage.decrypt(message);
                List<Tile> tile = new ArrayList<>();
                reply.getTiles(tile);
                virtualView.showChosenTiles(tile,((ChosenTilesMessage) reply).getToOrder());
            }
            case SHOWPERSONAL -> {
                reply = ShowPersonalCardMessage.decrypt(message);
                controller.setSimpleGoal(Integer.parseInt(((ShowPersonalCardMessage) reply).getPersonalId()));
            }
            case SHOWCOMMONS -> {
                reply = ShowCommonCards.decrypt(message);
                reply.getCC(controller.getCc());
            }
            case COMMONCOMPLETED -> {
                reply = CommonCompletedMessage.decrypt(message);
                virtualView.commonCompleted(reply.getMessage(), ((CommonCompletedMessage) reply).getWhoCompleted(), ((CommonCompletedMessage) reply).getFirst());
            }
            case SHOWOTHERS -> {
                reply = OtherPlayersMessage.decrypt(message);
                String nick = ((OtherPlayersMessage) reply).getP().getNickname();
                Player p = ((OtherPlayersMessage) reply).getP();
                controller.getOthers().put(nick,p);
                if((controller.getNPlayers()-1)== controller.getOthers().keySet().size()){
                    virtualView.showOthers(controller.getOthers());
                }
            }
            case TOKENS -> {
                reply= ReloadTokensMessage.decrypt(message);
                virtualView.reloadTokens(((ReloadTokensMessage) reply).getYourself());
            }
            case C -> {
                reply = ChatMessage.decrypt(message);
                virtualView.showChat(reply.getUsername() + " -> you: " + ((ChatMessage)reply).getPhrase());
            }
            case CA -> {
                reply=BroadcastMessage.decrypt(message);
                virtualView.showChat(reply.getUsername() + " -> all: " + ((BroadcastMessage)reply).getPhrase());
            }
            case POINTS -> {
                reply = SimpleReply.decrypt(message);
                virtualView.showPoints(reply.getMessage());
            }
            case WINNER -> {
                reply = SimpleReply.decrypt(message);
                virtualView.winner(reply.getMessage());
            }
            case ENDGAME -> {
                controller.setIdGame(0);
                virtualView.endGame();
            }
            case ENDGAMETOKEN -> {
                reply = SimpleReply.decrypt(message);
                virtualView.endGameToken(reply.getMessage());
            }
            case PING -> {
                //RMI
                ping++;
                if(!socket){
                    Thread.sleep(pingTime*1000);
                    stub.RMIsend(new PingMessage(controller.getNickname()).toString());
                }
            }
            default -> reply = SimpleReply.decrypt(message);
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
            clientMessage = controller.checkMessageShape(message);
            Action curr_action = clientMessage.getAction();
            String toSend = clientMessage.toString();
            //If the format is wrong the function checkMessageShape return an error message
            if (curr_action.equals(Action.ERROR)) {
                virtualView.displayMessage(toSend);
            } else if (curr_action.equals(Action.SHOWPERSONAL)) {
                virtualView.showPersonal(controller.getSimpleGoal());
            } else if (curr_action.equals(Action.SHOWSHELF)) {
                virtualView.showShelf(controller.getShelf());
            } else if (curr_action.equals(Action.SHOWBOARD)) {
                virtualView.showBoard(controller.getBoard());
            } else if (curr_action.equals(Action.SHOWCOMMONS)) {
                virtualView.showCommons(controller.getCc());
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
        IPv4 = virtualView.askIP();
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
        Client.connection(IPv4, 23450);
        socket = true;
        String username;
        GeneralMessage nick;
        String mex;

        //Request unique nickname for the client
        Action action = Action.SETNAME;
        System.out.println(in.readLine());
        do {
            username = virtualView.askNickname();
            nick = new SetNameMessage(username,true);
            out.println(nick);
            try{
                mex = in.readLine();
                action = GeneralMessage.identify(mex);
                if(action.equals(Action.SETNAME)){
                    nick = SetNameMessage.decrypt(mex);
                } else {
                    nick = ReconnectMessage.decrypt(mex);
                }
            } catch(Exception ignored){}
            virtualView.checkNickname(nick.getUsername(), nick.isAvailable());
        } while (!nick.isAvailable());
        //If the nickname is available the clientController is created
        controller = new clientController(nick.getUsername());
        if(!action.equals(Action.SETNAME)){
            if(nick.getIdLobby()>0){
                controller.setIdLobby(nick.getIdLobby());
            }
            if(nick.getGameId()>0){
                controller.setIdGame(nick.getGameId());
            }
        }

        //Connection starts with a pool of thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(()-> {
            try {
                listenSocket();
            } catch (IOException | ParseException | InterruptedException e) {
                System.out.println(RED + "Connection error" + RESET);
            }
        });
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //Task for the ping messages
        Runnable sendPing = () -> {
            while (connected) {
                synchronized (SocketLock) {
                    out.println(new PingMessage(controller.getNickname()));
                }
                try {
                    Thread.sleep( pingTime*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        //Task for the input
        Runnable input = () -> {
            while (true) {
                String toSend = virtualView.getInput();
                try {
                    synchronized (SocketLock) {
                        sendMessage(toSend, true);
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable checkPing =() ->{
            try {
                ping();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        //Both tasks are executed in parallel
        executorService.submit(sendPing);
        executorService.submit(checkPing);
        executorService.submit(input);
        executor.shutdown();
    }

    /** It starts a RMI connection with the server. */
    public static void RMI(){
        controller = new clientController();
        try {
            System.setProperty("sun.rmi.transport.tcp.responseTimeout", "10000");
            Registry registry = LocateRegistry.getRegistry(IPv4, 23451);
            stub = (RMIconnection) registry.lookup("RMIServer");
            RMIclient = new RMIclientImpl(controller);
            System.out.println(BLUE + "Welcome to MyShelfie!" + RESET);
            setName();

            //Pool of thread for the ping messages
            ExecutorService executor1 = Executors.newSingleThreadExecutor();
            executor1.submit(()-> {
                try {
                    stub.RMIsend(new PingMessage(controller.getNickname()).toString());
                } catch (RemoteException e) {
                    virtualView.disconnected();
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
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /** Counts pings and catches eventual disconnections of the clients. */
    private static void ping() throws InterruptedException {
        int x = -1;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while(x < ping) {
            x = ping;
            Thread.sleep(pingTime*1000);
        }
        System.out.printf("Ping non arrivato");
        virtualView.disconnected();
        connected = false;
    }

    /** Gets name via RMI connection. */
    public static void setName() throws RemoteException {
        String input;
        SetNameMessage nick;
        input = virtualView.askNickname().replaceAll(" ", "");
        nick = new SetNameMessage(input, true);
        stub.RMIsendName(nick.toString(), RMIclient);
    }

    /**
     * It starts the GUI's thread.
     * @param stage current GUI's stage.
     */
    @Override
    public void start(Stage stage) {
        ((GUI)virtualView).startGuiConnection(stage);
    }

    /** It gets the view of the client. */
    public static View getVirtualView() {
        return virtualView;
    }
}