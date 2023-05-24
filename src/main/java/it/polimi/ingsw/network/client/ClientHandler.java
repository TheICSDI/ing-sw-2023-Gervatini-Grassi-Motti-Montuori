/** It manages multiple client connections via socket.
 * @author Caterina Motti*/
package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.SetNameMessage;
import it.polimi.ingsw.controller.connectionType;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final serverController SC;

    /** Constructor that specify the client for the entire class.
     * @param socket the client's socket.
     * @param controller the server's controller. */
    public ClientHandler(Socket socket, serverController controller){
        this.clientSocket = socket;
        this.SC = controller;
    }

    /** It overrides the run() method to manage multiple thread, each one rearguing a single socket connection. */
    @Override
    public void run() {
        SetNameMessage nickname = null;
        try {
            //Creates the socket between client and sever via input/output
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Firstly it ask for a nickname until the given one is available
            //In this way each client is represented by a unique nickname
            out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
            nickname = SetNameMessage.decrypt(in.readLine());
            while(gameController.allPlayers.containsKey(nickname.getUsername())){
                out.println(new SetNameMessage("",false));
                nickname = SetNameMessage.decrypt(in.readLine());
            }

            //If the nickname is not already taken, it is added to the static hashmap allPlayers and connections
            gameController.allPlayers.put(nickname.getUsername(), new Player(nickname.getUsername()));
            out.println(new SetNameMessage(nickname.getUsername(),true ));
            serverController.connections.put(nickname.getUsername(), new connectionType(true, out, null));

            //Loop that enable the reception of messages from the client
            String input;
            while((input = in.readLine()) != null){
                SC.getMessage(input);
            }

        } catch (IOException e) {
            //If the client disconnects it is notified to the server
            if(nickname != null) {
                System.out.println(nickname.getUsername() + " has disconnected!");
                if(gameController.allPlayers.containsKey(nickname.getUsername())){
                    gameController.allPlayers.get(nickname.getUsername()).setConnected(false);
                }
            }else{
                System.out.println("Client has disconnected!");
            }
        } catch (ParseException | InvalidKeyException | InvalidActionException ignored ){
        } finally {
            try {
                //If the client disconnect the socket connection is closed
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }
}