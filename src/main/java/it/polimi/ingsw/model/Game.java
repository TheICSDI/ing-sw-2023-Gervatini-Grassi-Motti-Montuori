/** Represents a game. It is identified by a unique id.
 * Each game has some players (from 2 to 4), a board, 12 common goal cards and 12 personal goal cards.
 * @author Andrea Grassi, Caterina Motti
 */
package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidColumnException;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Cards.*;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.messages.*;

import java.rmi.RemoteException;
import java.util.*;

public class Game {
    private static int count = 0;
    public int id;
    private final List<Player> players;
    private final Board board;
    private final List<CCStrategy> allCC = new ArrayList<>();
    private final List<CommonCard> CommonCards = new ArrayList<>();
    List<Integer> ccId = new ArrayList<>();

    private final List<PersonalCard> allPC = new ArrayList<>();
    public final gameController controller;

    /** Creates a game given a list of players.
     * It initializes the board for the first time.
     * It picks the first player and give randomically each player a personal goal card.
     * It randomically choose two common goal cards.
     */
    public Game(List<Player> players,gameController controller){
        this.controller = controller;
        //Each game is represented by a unique id that can't be changed
        count++;
        this.id = count;
        this.players = players;


        //Initializes the new board
        this.board = new Board(players.size());
        this.board.fillBoard();

        //Initializes the common and personal goal cards
        resetCards();

        //Shuffle the list of players in order to randomically pick the first one
        Collections.shuffle(players);
        players.get(0).setFirstToken(true);
        //Shuffle the personal goal cards in order to randomically give them to the players
        Collections.shuffle(allPC);
        for(int i = 0; i < players.size(); i++){
            //Sets the turn of each player
            players.get(i).setTurn(i);
            players.get(i).setPersonalCard(allPC.get(i));
        }

        //Shuffle the common goal cards to randomically draws two of them
        Collections.shuffle(allCC);
        CommonCards.add(new CommonCard(allCC.get(0),true));
        CommonCards.add(new CommonCard(allCC.get(1),false));
        ccId.add(allCC.get(0).getId());
        ccId.add(allCC.get(1).getId());
    }


    /**
     * Manages all the game logic from start to end.
     * It calculates the total points of each player at the end of every turn.
     *
     * @see Player,Board,CommonCard,PersonalCard
     */
    public void startGame() throws RemoteException, InterruptedException {
        //At the starting point no player has the endgame token
        boolean endGame = false;
        boolean check = false;

        for (Player p: players) {
            controller.sendElement(p.getPersonalCard().getCard(),List.of(p),Action.SHOWPERSONAL);
            serverController.sendMessage(new SendCommonCards(ccId), p.getNickname());
            for (Player other: players) {
                if(!other.getNickname().equals(p.getNickname())){
                    serverController.sendMessage(new OtherPlayersMessage(other),p.getNickname());
                }
            }
        }
        //Start turns
        while(!endGame){
            for (Player p: players) {
                if(p.isConnected()){
                    for (Player p1: players) {
                        if(p1.getNickname().equals(p.getNickname())){
                            serverController.sendMessage(new ReplyMessage("It's your turn!",Action.INGAMEEVENT), p1.getNickname());
                            controller.sendElement(board.board, List.of(p1),Action.UPDATEBOARD);
                        }else{
                            controller.sendElement(board.board, List.of(p1),Action.UPDATEBOARD);
                            serverController.sendMessage(new ReplyMessage("It's " + p.getNickname() + "'s turn!",Action.INGAMEEVENT), p1.getNickname());
                        }
                    }
                    serverController.sendMessage(new ReplyMessage("  Your shelf" , Action.INGAMEEVENT), p.getNickname());
                    controller.sendElement(p.getShelf(), List.of(p),Action.UPDATESHELF);
                    serverController.sendMessage(new ReplyMessage("Select tile you want to pick: ",Action.INGAMEEVENT), p.getNickname());

                    //The player can pick some tiles from the board and insert it inside its shel//
                    List<Tile>  toInsert = new ArrayList<>();
                    while(toInsert.isEmpty()) {
                        Set<Position> chosen = controller.chooseTiles(p.getNickname(),id); //più che un ciclo infinito qua è meglio fare un listener o simili, più pulito
                        try{
                            toInsert = p.pickTiles(chosen, board,p);
                        } catch (InvalidPositionException e) {
                            //bisogna attivare un thread per comunicare che non ce la tiles
                        }
                    }
                    controller.sendElement(board.board, List.of(p),Action.UPDATEBOARD);
                    serverController.sendMessage(new ChosenTilesMessage(toInsert), p.getNickname());

                    //CONTROLLO SE SERVE CHIEDERE L'ORDINE, ALTRIMENTI SALTO IL PASSAGGIO
                    boolean allTheSame=true;
                    for (Tile t1:
                         toInsert) {
                        for (Tile t2:
                             toInsert) {
                            if ((!t1.equals(t2)) && (!t1.getCategory().equals(t2.getCategory()))) {
                                allTheSame = false;
                                break;
                            }
                        }
                        if(!allTheSame) break;
                    }
                    if(!allTheSame){
                        serverController.sendMessage(new ReplyMessage("Choose the order you want to insert them in : ",Action.INGAMEEVENT), p.getNickname());
                        List<Integer> order = new ArrayList<>();
                        while(order.isEmpty()){
                            order = controller.chooseOrder(p.getNickname(), id);
                            try{
                                toInsert = p.orderTiles(toInsert, order);
                            }
                            catch (InputMismatchException e){
                                order.clear();//serve per la condizione del while
                            }

                        }
                        serverController.sendMessage(new ChosenTilesMessage(toInsert), p.getNickname());
                    }
                    serverController.sendMessage(new ReplyMessage("Choose column: ",Action.INGAMEEVENT), p.getNickname());
                    int col = -1;
                    while(col == -1) {
                        col= controller.chooseColumn(p.getNickname(),id);
                        try {
                            p.insertInShelf(toInsert, (col-1));
                        } catch (InvalidColumnException e) {
                            col = -1;//serve per il while
                        }
                    }
                    serverController.sendMessage(new ReplyMessage("Tiles inserted ",Action.INGAMEEVENT), p.getNickname());
                    serverController.sendMessage(new ReplyMessage("  Your shelf" , Action.INGAMEEVENT), p.getNickname());
                    controller.sendElement(p.getShelf(), List.of(p),Action.UPDATESHELF);

                    for (Player other: players) {
                        if(!other.getNickname().equals(p.getNickname())){
                            serverController.sendMessage(new OtherPlayersMessage(p),other.getNickname());
                        }
                    }
                    //If the board is empty it will be randomically filled
                    if(board.isBoardEmpty()){
                        board.fillBoard();
                        for (Player pb : players) {
                            serverController.sendMessage(new ReplyMessage("Board has been refilled!",Action.INGAMEEVENT), pb.getNickname());
                        }
                    }

                    //At each turn the common card goals are calculated
                    if(CommonCards.get(0).control(p)){
                        for (Player pcc : players) {
                            serverController.sendMessage(new ReplyMessage(p.getNickname() + " completed the first common goal and gained " + CommonCards.get(0).getPoints() +
                                    "! Points for this goal are being reduced to " + (CommonCards.get(0).getPoints()-2),Action.INGAMEEVENT), pcc.getNickname());
                        }
                        CommonCards.get(0).givePoints(p);
                    }
                    if(CommonCards.get(1).control(p)){
                        for (Player pcc : players) {
                            serverController.sendMessage(new ReplyMessage(p.getNickname() + " completed the second common goal and gained " + CommonCards.get(1).getPoints() +
                                    "! Points for this goal are being reduced to " + (CommonCards.get(1).getPoints()-2),Action.INGAMEEVENT), pcc.getNickname());
                        }
                        CommonCards.get(1).givePoints(p);
                    }

                    //If the end game token has not been assigned and the current player has completed his shelf
                    //it assigns the end token and add 1 point
                    if (!check && p.isShelfFull()) {
                        for (Player pe : players) {
                            serverController.sendMessage(new ReplyMessage(p.getNickname() + " filled his shelf first " +
                                    "and gained a point! This is the last turn.",Action.INGAMEEVENT), pe.getNickname());
                        }
                        p.setEndToken(true);
                        p.addPoints(1);
                        check = true;
                        endGame = true;
                    }
                //ToDo inviare a ogni player a fine partita le shelf di tutti i player cosicchè le possano stamapre in locale
                }
            }
        }
        for(Player p : players){
            p.calculateGeneralPoints();
            p.personalPoint();
            p.calculateCCPoints();
        }
        for (Player p : players) {
            serverController.sendMessage(new ReplyMessage("Players' total points: ", Action.INGAMEEVENT), p.getNickname());
            for (Player ptp:players) {
                serverController.sendMessage(new ReplyMessage(ptp.getNickname() + ": " + ptp.getTotalPoints(), Action.INGAMEEVENT), p.getNickname());
            }
        }
        for (Player p : players) {
            serverController.sendMessage(new ReplyMessage("The winner is " + calculateWinner().getNickname(),Action.INGAMEEVENT), p.getNickname());
            serverController.sendMessage(new ReplyMessage("",Action.ENDGAME), p.getNickname());
            gameController.allGames.remove(id);
        }
    }

    /** Returns the winner of the game.
     * The player who scored most points wins the game. In case of a tie, the player sitting further from the
     * first one wins the game.
     */
    public Player calculateWinner(){
        //By default, the winner is the last player to play
        // (if everybody has the same total points he is the winner)
        Player winner = players.get(players.size() - 1);
        //For each player, if the number of total points is grater than the current winner, the winner is updated
        for(Player p : players){
            if(p.getTotalPoints() > winner.getTotalPoints()){
                winner = p;
            } else if (p.getTotalPoints() == winner.getTotalPoints()){
                //If two players have the same amount of points, then the winner is the one sitting further from
                // the first player.
                if(players.indexOf(p) > players.indexOf(winner)) {
                    winner = p;
                }
            }
        }
        return winner;
    }


    /**
     * Initializes all common and personal goal cards.
     * Each personal goal card is represented by an index between 0 and 11.
     * In this way each game has its own set of cards.
     */
    private void resetCards(){
        allCC.add(new CC_01());
        allCC.add(new CC_02());
        allCC.add(new CC_03());
        allCC.add(new CC_04());
        allCC.add(new CC_05());
        allCC.add(new CC_06());
        allCC.add(new CC_07());
        allCC.add(new CC_08());
        allCC.add(new CC_09());
        allCC.add(new CC_10());
        allCC.add(new CC_11());
        allCC.add(new CC_12());

        //Add index from 0 to 11 that represents the personal goal cards
        for(int i = 0; i < 12; i++){
            allPC.add(new PersonalCard(i));
        }
    }


    /** Gets the list of player. */
    public List<Player> getPlayers() {
        return players;
    }

    /** Gets the list of common goal cards fot the cal. */
    public List<CommonCard> getCommonCards() {
        return CommonCards;
    }

    /** Gets the list of personal goal cards. */
    public List<PersonalCard> getAllPC() {
        return allPC;
    }

    /** Gets the list of all common goal cards. */
    public List<CCStrategy> getAllCC() {
        return allCC;
    }
    
}
