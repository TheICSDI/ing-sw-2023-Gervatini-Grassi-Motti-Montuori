/** It is used by the client to check the correctness of the input.
 * @author Marco Gervatini, Andrea Grassi. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.*;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.util.*;

public class clientController{
    private String nickname;
    private int idMex = 0;
    private int idLobby = 0;
    private int idGame = 0;
    private Map<String,Player> others = new HashMap<>();
    private boolean firstTurn = false;
    private PersonalCard simpleGoal;
    public List<Integer> cc = new ArrayList<>();

    public clientController(String nickname){
        this.nickname = nickname;
    }
    public clientController(){}


    /*controlla che la stringa che rappresenta l'azione scelta corrisponda a un comando esistente e chiamabile dal client
    in quel momento verifica che il numero diListrsia adeguato e prnew ArrayList<>()e i comandi esterni alla partita
    e restituisce al client un message del tipo giusto per l'azione che verra' poi messo in json format e inviato al server
    */

    /**
     * Checks if the command called by the client has the right number of parameters and create the message to be
     * ready to send.
     * @param input message to check.
     * @param controller sender client's controller.
     * @return subclass of general message based on the action type.
     */
    public GeneralMessage checkMessageShape(String input, clientController controller){
        //Input parsing
        input = input.trim();
        //Words delimiter
        String[] words = input.split(" ");
        //First word is the action
        Action currAction = Action.valueOf(words[0].toUpperCase());
        try{
            idMex++;
            switch (currAction){
                case CREATELOBBY -> {
                    //It contains action and a parameter between 2 and 4, that is the limit of players in a lobby
                    if(words.length==2 && Integer.parseInt(words[1])>=2 && Integer.parseInt(words[1])<=4){
                        return new CreateLobbyMessage(idMex,nickname,Integer.parseInt(words[1]));
                    } else {
                        return new DefaultErrorMessage("Insert number of players (between 2 and 4)");
                    }
                }
                case SHOWLOBBY -> {
                    if(words.length == 1){
                        return new ShowLobbyMessage(idMex, nickname);
                    }
                }
                case JOINLOBBY -> {
                    //It contains action and number of lobby
                    if(words.length==2){
                        return new JoinLobbyMessage(idMex, Integer.parseInt(words[1]), nickname);
                    }
                    else{
                        return new DefaultErrorMessage("Insert a valid lobby number!");
                    }
                }
                case STARTGAME -> {
                    return new StartGameMessage(idMex,controller.getIdLobby(), nickname);
                }
                case PT -> {
                    List<Position> pos = new ArrayList<>();
                    //It contains action and between 2 and 6 parameters that are the positions of tiles
                    if(controller.getIdGame() == 0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    if (words.length<=7 && words.length%2 == 1 && words.length>=3) {
                        for (int i = 1; i < words.length; i=i+2) {
                            pos.add(new Position(Integer.parseInt(words[i]),Integer.parseInt(words[i+1])));
                        }
                        if(!isStraightLineTiles(pos)){
                            return new DefaultErrorMessage("Tiles are not Adjacent!");
                        }
                    } else {
                        return new DefaultErrorMessage("Number of parameters is wrong!");
                    }
                    return new PickTilesMessage(idMex, nickname, pos, controller.getIdGame());
                }

                case SO -> {
                    List<Integer> order =  new ArrayList<>();
                    //It contains action and between 2 and 3 parameters, that represent the order
                    for (int i = 1; i < words.length; i++) {
                        order.add(Integer.parseInt(words[i]));
                    }
                    if(!acceptableOrder(order)){
                        return new DefaultErrorMessage("Invalid format!");
                    }
                    return new SelectOrderMessage(idMex, nickname, order,controller.getIdGame());
                }

                case SC -> {
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    int col;
                    if(words.length == 2){
                        col = Integer.parseInt(words[1]);
                    }
                    else{
                        return new DefaultErrorMessage("Number of parameters is wrong!");
                    }
                    if(col<1 || col>5){
                        return new DefaultErrorMessage("Invalid column!");
                    }else{
                        return new SelectColumnMessage(idMex, nickname, col, controller.getIdGame());
                    }
                }

                case SHOWPERSONAL -> {
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    if(idGame>0){
                        return new ShowPersonalCardMessage();
                    }else{
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case SHOWCOMMONS -> {
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    if(idGame>0){
                        return new ShowCommonCards();
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case SHOWOTHERS -> {
                    if(idGame>0){
                        return new ReplyMessage("", Action.SHOWOTHERS);
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case C ->{
                    if(words.length > 2){
                        String phrase = "";
                        String recipient = words[1];
                        //TODO: cosa succede se il player non esiste? possiamo scrivegli che è un problema?
                        for (int i = 2; i < words.length; i++) {
                            phrase = phrase.concat(words[i]);
                            phrase = phrase.concat(" ");
                        }
                        if (idLobby > 0) {
                            return new ChatMessage(nickname, phrase, recipient);
                        } else if (idGame > 0) {
                            return new ChatMessage(nickname, phrase, recipient);
                        } else {
                            return new DefaultErrorMessage("You are not in a game or lobby!");
                        }
                    } else {
                        return new DefaultErrorMessage("Blank message");
                    }
                }

                case CA ->{
                    if(words.length > 1){
                        String phrase = "";
                        for (int i = 1; i < words.length; i++) {
                            phrase = phrase.concat(words[i]);
                            phrase = phrase.concat(" ");
                        }
                        if (idLobby > 0) {
                            return new BroadcastMessage(-1, idLobby, nickname, phrase);
                        } else if (idGame > 0) {
                            return new BroadcastMessage(idGame, -1, nickname, phrase);
                        } else {
                            return new DefaultErrorMessage("You are not in a game or lobby!");
                        }
                    } else {
                        return new DefaultErrorMessage("Blank message");
                    }
                }
            }
        } catch(IllegalArgumentException e){
            idMex--;
            return new DefaultErrorMessage("Invalid command: Write /help for commands list");
        }
        return null;
    }

    public boolean acceptableOrder(List<Integer> order) {
        if (order.size() > 3 || order.size()==0) {
            return false;
        } else {
            if (!order.contains(1)) {
                return false;
            } else if (order.size() > 1 && !order.contains(2)) {
                return false;
            } else{
                return order.size() <= 2 || order.contains(3);
            }
        }
    }

    public boolean isStraightLineTiles(List<Position> tiles){
        if(tiles.size() == 1){
            return true;
        }
        else if(tiles.size() ==2){
            return isAdjacentOnX(tiles.get(0),tiles.get(1)) || isAdjacentOnY(tiles.get(0),tiles.get(1));
        }
        else{
            if(isAdjacentOnX(tiles.get(0),tiles.get(1)) && isAdjacentOnX(tiles.get(1),tiles.get(2))){
                return true;
            } else if (isAdjacentOnX(tiles.get(0),tiles.get(2)) && isAdjacentOnX(tiles.get(1),tiles.get(2))) {
                return true;
            } else if (isAdjacentOnX(tiles.get(0),tiles.get(2)) && isAdjacentOnX(tiles.get(1),tiles.get(0))){
                return true;
            } else if(isAdjacentOnY(tiles.get(0),tiles.get(1)) && isAdjacentOnY(tiles.get(1),tiles.get(2))){
                return true;
            } else if (isAdjacentOnY(tiles.get(0),tiles.get(2)) && isAdjacentOnY(tiles.get(1),tiles.get(2))) {
                return true;
            } else return isAdjacentOnY(tiles.get(0), tiles.get(2)) && isAdjacentOnY(tiles.get(1), tiles.get(0));
        }
    }

    /**
     * Checks if two tiles are adjacent 0 1 2
     * @param a first tile
     * @param b second tile
     * @return true if they are
     */
    //Are three tiles in a line
    public boolean isAdjacentOnX(Position a ,Position b){
        return a.getX() == b.getX() && ((a.getY() == b.getY() - 1) || (a.getY() == b.getY() + 1));
    }
    public boolean isAdjacentOnY(Position a ,Position b){
        return a.getY() == b.getY() && ((a.getX() == b.getX() - 1) || (a.getX() == b.getX() + 1));
    }

    /** It elaborates the nickname from a client connected via RMI.*/
    public void getName(String input) throws RemoteException {
        if(!SetNameMessage.decrypt(input).isAvailable()){
            Client.getVirtualView().printUsername(SetNameMessage.decrypt(input).getUsername(),SetNameMessage.decrypt(input).isAvailable());
            Client.setName();
        } else {
            Client.getVirtualView().printUsername(SetNameMessage.decrypt(input).getUsername(),SetNameMessage.decrypt(input).isAvailable());
            this.nickname = SetNameMessage.decrypt(input).getUsername();
        }
    }

    /** It elaborates a message from a client connected via RMI. */
    public void getMessage(String m) throws ParseException, InvalidKeyException, RemoteException, InterruptedException {
        Client.elaborate(m);
    }

    //SETTER and GETTER methods
    public Map<String, Player> getOthers() {
        return others;
    }
    public PersonalCard getSimpleGoal(){return this.simpleGoal;}
    public void setSimpleGoal(int id) {
        this.simpleGoal = new PersonalCard(id);
    }
    public boolean isFirstTurn() {
        return firstTurn;
    }
    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }
    public int getIdLobby() {
        return idLobby;
    }
    public void setIdLobby(int idLobby) {
        this.idLobby = idLobby;
    }
    public int getIdGame() {
        return idGame;
    }
    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
    public String getNickname(){return this.nickname;}
}