package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.*;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.util.*;

public class clientController{
    private int idMex=0;//ogni messaggio ha un numero che dipende viene assegnato in ordine crescente dal client


    private String nickname;
    private int idLobby=0;
    private int idGame=0;

    private Map<String,Player> others=new HashMap<>();


    private boolean firstTurn=false;
    private Tile[][] simpleGoal=new Tile[6][5];
    //TODO: secondo me qui ci va una PersonalGoal, perchè in GUI ho bisongo dell'id della carta per poter scegliere l'immagine giusta
    public List<Integer> cc=new ArrayList<>();


    public Tile[][] getSimpleGoal(){return simpleGoal;}
    public void setSimpleGoal(Tile[][] goal) {
        for (int i = 0; i < goal.length; i++) {
            for (int j = 0; j < goal[0].length; j++) {
                this.simpleGoal[i][j]=goal[i][j];
            }
        }
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

    public clientController(String nickname){
        this.nickname = nickname;
    }
    public clientController(){}


    /*controlla che la stringa che rappresenta l'azione scelta corrisponda a un comando esistente e chiamabile dal client
    in quel momento verifica che il numero di parametri sia adeguato e procede a eseguire i comandi esterni alla partita
    e restituisce al client un message del tipo giusto per l'azione che verra' poi messo in json format e inviato al server
    */

    /**
     * Checks if the command called by the client is accepted by the server, with the right number of parameters and
     * formats the message to be ready to send.
     * @param m message to check.
     * @param controller sender client's controller.
     * @return subclass of general message based on the action type.
     */
    public GeneralMessage checkMessageShape(String m, clientController controller){
        Action currAction;
        m = m.trim();
        //parsing dell'input string
        String[] words = m.split(" "); //il delimitatore delle parole e' lo spazio
        String action = words[0];
        action = action.toUpperCase();
        try{
            //vediamo se il comando esiste prima di tutto
            currAction = Action.valueOf(action);
            //return new GeneralMessage(currAction,curr_params);

            idMex++;//indice del messaggio, ogni player ha il suo perchè vengono contati da lato client
            switch (currAction){
                case CREATELOBBY -> {//words deve contenere action, numero di giocatori partita
                    if(words.length==2 && Integer.parseInt(words[1])>=2 && Integer.parseInt(words[1])<=4){ //un parametro int compreso tra 2 e 4 (limite giocatori)
                        return new CreateLobbyMessage(idMex,nickname,Integer.parseInt(words[1]));
                    }else{
                        return new DefaultErrorMessage("Insert number of players for this lobby between 2 and 4");
                    }
                }
                case SHOWLOBBY -> {
                    if(words.length==1){//words deve contenere action
                        return new ShowLobbyMessage(idMex, nickname);
                    }
                }
                case JOINLOBBY -> {
                    if(words.length==2){//words deve contenere action, numero di lobby
                        return new JoinLobbyMessage(idMex, Integer.parseInt(words[1]), nickname);
                    }
                    else{
                        return new DefaultErrorMessage("Insert a valid lobby number");
                    }
                }
                case STARTGAME -> {//words deve contenere action
                    return new StartGameMessage(idMex,controller.getIdLobby(),nickname);
                }
                case PT -> {//words deve contenere action e poi da 2 a 6 numeri che sono le coordinate delle tiles
                    //scelte sul tabellone, es. picktiles 2 3 2 4 5 6
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("Not in game");
                    }
                    List<Position> pos=new ArrayList<>();
                    if (words.length<=7 && words.length%2==1 && words.length>=3) {//serve a fare le position
                        for (int i = 1; i < words.length; i=i+2) {
                            pos.add(new Position(Integer.parseInt(words[i]),Integer.parseInt(words[i+1])));
                        }
                        if(!isStraightLineTiles(pos)){
                            return new DefaultErrorMessage("Not Adjacent");
                        }
                    }else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                    return new PickTilesMessage(idMex, nickname, pos,controller.getIdGame());
                }
                case SO -> {//action, da 1 a 3 interi es. selectorder 2 1 3
                    List<Integer> order =  new ArrayList<>();
                    for (int i = 1; i < words.length; i++) {//riempie order
                        order.add(Integer.parseInt(words[i]));
                    }
                    if(!acceptableOrder(order)){
                        return new DefaultErrorMessage("Invalid format");
                    }
                    return new SelectOrderMessage(idMex, nickname, order,controller.getIdGame());
                }
                case SC -> {
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("Not in game");
                    }
                    int col;
                    if(words.length == 2){//action, un numero
                        col = Integer.parseInt(words[1]);
                    }
                    else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                    if(col<1 || col>5){
                        return new DefaultErrorMessage("Column not valid");
                    }else{
                        return new SelectColumnMessage(idMex, nickname,col ,controller.getIdGame());
                    }

                }
                case SHOWPERSONAL -> {
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("Not in game");
                    }
                    if(idGame>0){
                        return new ShowPersonalCardMessage();
                    }else{
                        return new DefaultErrorMessage("Not in game");
                    }
                }
                case SHOWCOMMONS -> {
                    if(controller.getIdGame()==0){
                        return new DefaultErrorMessage("Not in game");
                    }
                    if(idGame>0){
                        return new ShowCommonCards();
                    }else{
                        return new DefaultErrorMessage("Not in game");
                    }
                }
                case SHOWOTHERS -> {
                    if(idGame>0){
                        return new ReplyMessage("",Action.SHOWOTHERS);
                    }else{
                        return new DefaultErrorMessage("Not in game");
                    }
                }
                case C ->{
                    if(words.length > 2){
                        String phrase = "";
                        String recipient = words[1];
                        for (int i = 2; i < words.length; i++) {
                            phrase = phrase.concat(words[i]);
                            phrase = phrase.concat(" ");
                        }
                        if (idLobby > 0) {
                            return new ChatMessage(nickname, phrase, recipient);
                        } else if (idGame > 0) {
                            return new ChatMessage(nickname, phrase, recipient);
                        } else {
                            return new DefaultErrorMessage("Not in a game nor in a lobby");
                        }
                    }else{
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
                            return new DefaultErrorMessage("Not in a game nor in a lobby");
                        }
                    }else{
                        return new DefaultErrorMessage("Blank message");
                    }
                }

            }


        }
        catch(IllegalArgumentException e){
            idMex--;//M:secondo me e' tagliabile tanto gli id vanno solo in ordine cresciente anche se ne perdiamo
            // uno l'importante e' che non ce ne siano due ugali// A:boh mi sembra scema come cosa avere i messaggi con id: 1,2,7 perchè il giocatore non sa scrivere
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


    public void getName(String input) throws RemoteException {
        if(!SetNameMessage.decrypt(input).isAvailable()){
            System.out.println("Nickname is already taken!");
            Client.setName();
        }else{
            System.out.println("Nickname correctly set!");
            this.nickname=SetNameMessage.decrypt(input).getUsername();
        }
    }

    public void getMessage(String m) throws ParseException, InvalidKeyException, RemoteException, InterruptedException {
        Client.elaborate(m);
    }



    public Map<String,Player> getOthers() {
        return others;
    }

    public String getNickname() {
        return nickname;
    }
}
