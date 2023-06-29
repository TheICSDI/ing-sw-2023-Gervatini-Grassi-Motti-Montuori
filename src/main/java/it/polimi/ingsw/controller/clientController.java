package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.messages.*;

import java.util.*;

/** It checks the correctness of the input client side. Each client has an instance of the class.
 * @author Marco Gervatini, Andrea Grassi, Caterina Motti. */
public class clientController{
    private String nickname;
    private int idMex = 0;
    private int idLobby = 0;
    private int idGame = 0;
    private final Map<String, Player> others = new HashMap<>();
    private boolean firstTurn = false;
    private int nPlayers;
    private PersonalCard simpleGoal;
    private List<Integer> cc = new ArrayList<>();
    private final int numRows = 6;
    private final int numCols = 5;
    private final int dim = 9;
    private Tile[][] shelf = new Tile[numRows][numCols];
    private Tile[][] board = new Tile[dim][dim];

    /** It creates a clientController specific for a client given its nickname.
     * @param nickname of the client. */
    public clientController(String nickname){
        this.nickname = nickname;
        emptyShelf();
    }

    /** It empties the local shelf of the player, that is then updated at each turn. */
    public void emptyShelf(){
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                this.shelf[i][j] = new Tile("empty", 1);
            }
        }
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.board[i][j] = new Tile("empty", 1);
            }
        }
    }

    /** It creates a generic clientController. */
    public clientController(){}

    /** Checks if the command called by the client has the right number of parameters and create the message in the
     * JSON format, ready to be sent to the server.
     * @param input message to check.
     * @return subclass of general message based on the action type.
     */
    public GeneralMessage checkMessageShape(String input){
        //Input parsing
        input = input.trim();
        //Words delimiter
        String[] words = input.split(" ");
        //First word is the action
        Action currAction;
        String a = words[0];
        try{
            currAction = Action.valueOf(a.toUpperCase());
            idMex++;
            switch (currAction){
                case CREATELOBBY -> {
                    //It contains action and a parameter between 2 and 4, that is the limit of players in a lobby
                    if(words.length==2 && Integer.parseInt(words[1])>=2 && Integer.parseInt(words[1])<=4){
                        return new CreateLobbyMessage(idMex, nickname, Integer.parseInt(words[1]));
                    } else {
                        return new DefaultErrorMessage("Insert number of players (between 2 and 4)");
                    }
                }

                case SHOWLOBBY -> {
                    return new ShowLobbyMessage(idMex, nickname);
                }

                case JOINLOBBY -> {
                    //It contains action and number of lobby
                    if(words.length == 2){
                        return new JoinLobbyMessage(idMex, Integer.parseInt(words[1]), nickname);
                    }
                    else{
                        return new DefaultErrorMessage("Insert a valid lobby number!");
                    }
                }

                case STARTGAME -> {
                    return new StartGameMessage(idMex, idLobby, nickname);
                }

                case PT -> {
                    List<Position> pos = new ArrayList<>();
                    //It contains action and between 2 and 6 parameters that are the positions of tiles
                    if(idGame == 0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    if (words.length <= 7 && words.length%2 == 1 && words.length >= 3) {
                        for (int i = 1; i < words.length; i=i+2) {
                            pos.add(new Position(Integer.parseInt(words[i]),Integer.parseInt(words[i+1])));
                        }
                        if(!isStraightLineTiles(pos)){
                            return new DefaultErrorMessage("Tiles are not Adjacent!");
                        }
                    } else {
                        return new DefaultErrorMessage("Number of parameters is wrong!");
                    }
                    return new PickTilesMessage(idMex, nickname, pos, idGame);
                }

                case SO -> {
                    if(idGame == 0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    List<Integer> order =  new ArrayList<>();
                    //It contains action and between 2 and 3 parameters, that represent the order
                    for (int i = 1; i < words.length; i++) {
                        order.add(Integer.parseInt(words[i]));
                    }
                    if(!acceptableOrder(order)){
                        return new DefaultErrorMessage("Invalid format!");
                    }
                    return new SelectOrderMessage(idMex, nickname, order, idGame);
                }

                case SC -> {
                    if(idGame == 0){
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                    int col;
                    if(words.length == 2){
                        col = Integer.parseInt(words[1]);
                    } else{
                        return new DefaultErrorMessage("Number of parameters is wrong!");
                    }
                    if(col<1 || col>5){
                        return new DefaultErrorMessage("Invalid column!");
                    }else{
                        return new SelectColumnMessage(idMex, nickname, col, idGame);
                    }
                }

                case SHOWPERSONAL -> {
                    if(idGame > 0){
                        return new ShowPersonalCardMessage("");
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case SHOWCOMMONS -> {
                    if(idGame > 0){
                        return new ShowCommonCards();
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case SHOWOTHERS -> {
                    if(idGame > 0){
                        return new SimpleReply("", Action.SHOWOTHERS);
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case SHOWSHELF -> {
                    if(idGame > 0){
                        return new UpdateBoardMessage(Action.SHOWSHELF, this.shelf);
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case SHOWBOARD -> {
                    if(idGame > 0){
                        return new UpdateBoardMessage(Action.SHOWBOARD, this.board);
                    } else {
                        return new DefaultErrorMessage("You are not in a game!");
                    }
                }

                case C -> {
                    if(words.length > 2){
                        String recipient = words[1];
                        if(recipient.equals(this.nickname)){
                            return new DefaultErrorMessage("You can't send a message to yourself!");
                        }
                        String phrase = "";
                        for (int i = 2; i < words.length; i++) {
                            phrase = phrase.concat(words[i]);
                            phrase = phrase.concat(" ");
                        }
                        if (idLobby > 0 || idGame > 0) {
                            return new ChatMessage(nickname, phrase, recipient);
                        } else {
                            return new DefaultErrorMessage("You are not in a game or lobby!");
                        }
                    } else {
                        return new DefaultErrorMessage("Blank message");
                    }
                }

                case CA -> {
                    if(words.length > 1){
                        String phrase = "";
                        for (int i = 1; i < words.length; i++) {
                            phrase = phrase.concat(words[i]);
                            phrase = phrase.concat(" ");
                        }
                        if (idLobby > 0 || idGame > 0) {
                            return new BroadcastMessage(idGame, idLobby, nickname, phrase);
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
        return new DefaultErrorMessage("Invalid command: Write /help for commands list");
    }

    /** It checks if the given order is correct.
     * @param order the list of Integer that represent an order.
     * @return true only if the order is acceptable, false otherwise. */
    public boolean acceptableOrder(List<Integer> order) {
        if (order.size() > 3 || order.size() == 0) {
            return false;
        } else {
            if (!order.contains(1)) {
                return false;
            } else if (order.size() > 1 && !order.contains(2)) {
                return false;
            } else if (order.size() > 2 && !order.contains(3)){
                return false;
            } else {
                return true;
            }
        }
    }

    /** It checks if the chosen positions are in a straight line.
     * @param tiles the list of chosen positions.
     * @return true only if the tiles are in a straight line, false otherwise.*/
    public boolean isStraightLineTiles(List<Position> tiles){
        if(tiles.size() == 0) {
            return false;
        } else if (tiles.size() == 1) {
            return true;
        } else if(tiles.size() == 2) {
            return isAdjacentOnX(tiles.get(0), tiles.get(1)) || isAdjacentOnY(tiles.get(0), tiles.get(1));
        } else {
            if(isAdjacentOnX(tiles.get(0), tiles.get(1)) && isAdjacentOnX(tiles.get(1), tiles.get(2))){
                return true;
            } else if (isAdjacentOnX(tiles.get(0), tiles.get(2)) && isAdjacentOnX(tiles.get(1), tiles.get(2))) {
                return true;
            } else if (isAdjacentOnX(tiles.get(0), tiles.get(2)) && isAdjacentOnX(tiles.get(1), tiles.get(0))){
                return true;
            } else if (isAdjacentOnY(tiles.get(0), tiles.get(1)) && isAdjacentOnY(tiles.get(1), tiles.get(2))){
                return true;
            } else if (isAdjacentOnY(tiles.get(0), tiles.get(2)) && isAdjacentOnY(tiles.get(1), tiles.get(2))) {
                return true;
            } else return isAdjacentOnY(tiles.get(0), tiles.get(2)) && isAdjacentOnY(tiles.get(1), tiles.get(0));
        }
    }

    /**
     * It checks if two tiles are adjacent horizontally.
     * @param a position of the first tile.
     * @param b position of the second tile.
     * @return true only if the two tiles are adjacent horizontally, false otherwise.
     */
    public boolean isAdjacentOnX(Position a, Position b){
        return a.getX() == b.getX() && ((a.getY() == b.getY() - 1) || (a.getY() == b.getY() + 1));
    }

    /**
     * It checks if two tiles are adjacent vertically.
     * @param a position of the first tile.
     * @param b position of the second tile.
     * @return true only if the two tiles are adjacent vertically, false otherwise.
     */
    public boolean isAdjacentOnY(Position a, Position b){
        return a.getY() == b.getY() && ((a.getX() == b.getX() - 1) || (a.getX() == b.getX() + 1));
    }

    /** It gets a map of the other players that are in the same game of the client. */
    public Map<String, Player> getOthers() {
        return others;
    }
    /** It gets the personal goal card of the client. */
    public PersonalCard getSimpleGoal(){return this.simpleGoal;}
    /** It sets the id of the personal goal card of the client, passed by parameter. */
    public void setSimpleGoal(int id) {
        this.simpleGoal = new PersonalCard(id);
    }
    /** It gets the value of firstTurn. */
    public boolean isFirstTurn() {
        return firstTurn;
    }
    /** It sets the value of firstTurn passed by parameter. It is true only if the turn is the first of the client. */
    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }
    /** It sets the id of the lobby that the client joined passed by parameter. */
    public void setIdLobby(int idLobby) {
        this.idLobby = idLobby;
    }
    /** It sets the id of the game that the client is playing passed by parameter. */
    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }
    /** It gets the nickname. */
    public String getNickname(){
        return this.nickname;
    }
    /** It sets the nickname passed by parameter. */
    public void setNickname(String nick){
        this.nickname = nick;
    }
    /** It gets the list of common cards' id.*/
    public List<Integer> getCc() {
        return this.cc;
    }
    /** It gets the shelf of the player. */
    public Tile[][] getShelf() {
        return this.shelf;
    }
    /** It sets the shelf of the player passed by parameter. */
    public void setShelf(Tile[][] shelf) {
        this.shelf = shelf;
    }
    /** Gets the number of rows of the shelf. */
    public int getNumRows() {
        return numRows;
    }
    /** Gets the number of columns of the shelf. */
    public int getNumCols() {
        return numCols;
    }
    /** It gets the board of the game (in which the player is). */
    public Tile[][] getBoard() {
        return board;
    }
    /** It sets the board of the game (in which the player is) passed by parameter. */
    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    /**Sets number of players in the game.*/
    public int getNPlayers() {
        return nPlayers;
    }

    /**Gets number of players in the game.*/
    public void setNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }
}