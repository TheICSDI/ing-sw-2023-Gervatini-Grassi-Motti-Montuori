/** Represents a game. It is identified by a unique id.
 * Each game has some players (from 2 to 4), a board, 12 common goal cards and 12 personal goal cards.
 * @author Andrea Grassi, Caterina Motti
 */
package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Cards.*;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.network.messages.*;

import java.rmi.RemoteException;
import java.util.*;

public class Game {
    private static int count = 0;
    public int id;
    private final List<Player> players;
    private String pTurn="";
    private final Board board;
    private final List<CCStrategy> allCC = new ArrayList<>(); //TODO i punti delle common cambiano in base al n player
    private final List<CommonCard> CommonCards = new ArrayList<>();
    private final List<Integer> ccId = new ArrayList<>();

    private final List<PersonalCard> allPC = new ArrayList<>();
    public final gameController controller;

    /** Creates a game given a list of players.
     * It initializes the board for the first time.
     * It picks the first player and give randomically each player a personal goal card.
     * It randomically choose two common goal cards.
     * @param players list of players participating in the game.
     * @param controller instance of gameController that interacts with the server.
     */
    public Game(List<Player> players, gameController controller){
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
        CommonCards.add(new CommonCard(allCC.get(0),true , players.size()));
        CommonCards.add(new CommonCard(allCC.get(1),false , players.size()));
        ccId.add(allCC.get(0).getId());
        ccId.add(allCC.get(1).getId());
    }


    public void reconnectPlayer(String player) throws RemoteException {
        serverController.sendMessage(new StartGameMessage("You reconnected!" ,this.id), player);
        Player reconnected=null;
        for (Player p:
             players) {
            if(p.getNickname().equals(player)){
                reconnected=p;
            }
        }
        String personalId = String.valueOf(reconnected.getPersonalCard().getId());
        serverController.sendMessage(new ShowPersonalCardMessage(personalId), reconnected.getNickname());
        serverController.sendMessage(new ShowCommonCards(ccId), reconnected.getNickname());
        serverController.sendMessage(new UpdateBoardMessage(Action.UPDATESHELF, reconnected.getShelf()), reconnected.getNickname());
        for (Player other: players) {
            if(!other.getNickname().equals(reconnected.getNickname())){
                serverController.sendMessage(new OtherPlayersMessage(other),reconnected.getNickname());
            }
        }
        if(pTurn.equals(reconnected.getNickname())){
            serverController.sendMessage(new SimpleReply("It's your turn!",Action.TURN), reconnected.getNickname());
            serverController.sendMessage(new UpdateBoardMessage(Action.UPDATEBOARD, board.board), reconnected.getNickname());
        } else {
            serverController.sendMessage(new UpdateBoardMessage(Action.UPDATEBOARD, board.board), reconnected.getNickname());
            serverController.sendMessage(new SimpleReply("It's " + pTurn + "'s turn!",Action.TURN), reconnected.getNickname());
        }
    }

    /**
     * Manages all the game logic from start to end.
     * It calculates the total points of each player at the end of every turn.
     * @see Player,Board,CommonCard,PersonalCard
     */
    public void startGame() throws RemoteException, InterruptedException {
        //At the start no player has the endgame token
        boolean endGame = false;
        boolean check = false;
        for (Player p: players) {
            String personalId = String.valueOf(p.getPersonalCard().getId());
            serverController.sendMessage(new ShowPersonalCardMessage(personalId), p.getNickname());
            serverController.sendMessage(new ShowCommonCards(ccId), p.getNickname());
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
                    pTurn=p.getNickname();
                    for (Player p1: players) {
                        if(p1.getNickname().equals(p.getNickname())){
                            serverController.sendMessage(new SimpleReply("It's your turn!",Action.TURN), p1.getNickname());
                            serverController.sendMessage(new UpdateBoardMessage(Action.UPDATEBOARD, board.board), p1.getNickname());
                        } else {
                            serverController.sendMessage(new UpdateBoardMessage(Action.UPDATEBOARD, board.board), p1.getNickname());
                            serverController.sendMessage(new SimpleReply("It's " + p.getNickname() + "'s turn!",Action.TURN), p1.getNickname());
                        }
                    }
                    serverController.sendMessage(new UpdateBoardMessage(Action.UPDATESHELF, p.getShelf()), p.getNickname());
                    serverController.sendMessage(new SimpleReply("Select tile you want to pick: ", Action.INGAMEEVENT), p.getNickname());

                    //Handles the action "pick tiles"
                    List<Tile> toInsert = pickTiles(p);

                    //Handles the action "select order"
                    boolean allTheSame = checkTiles(toInsert);
                    serverController.sendMessage(new ChosenTilesMessage(toInsert,!allTheSame), p.getNickname());

                    if(!allTheSame){
                        toInsert = orderTiles(p, toInsert);
                    }
                    serverController.sendMessage(new SimpleReply("Choose column: ",Action.INGAMEEVENT), p.getNickname());

                    //Handles the action "select column"
                    selectColumn(p, toInsert);

                    for (Player other: players) {
                        if(!other.getNickname().equals(p.getNickname())){
                            serverController.sendMessage(new OtherPlayersMessage(p),other.getNickname());
                        }
                    }
                    //If the board is empty it will be randomically filled
                    if(board.isBoardEmpty()){
                        board.fillBoard();
                        for (Player pb : players) {
                            serverController.sendMessage(new SimpleReply("Board has been refilled!",Action.INGAMEEVENT), pb.getNickname());
                        }
                    }

                    //At each turn the common card goals are calculated
                    calculateCC(p);

                    //If the end game token has not been assigned and the current player has completed his shelf
                    //it assigns the end token and add 1 point
                    if (!check && p.isShelfFull()) {
                        for (Player pe : players) {
                            serverController.sendMessage(new SimpleReply(p.getNickname() , Action.ENDGAMETOKEN), pe.getNickname());
                        }
                        p.setEndToken(true);
                        p.addPoints(1);
                        check = true;
                        endGame = true;
                    }
                }
            }
        }
        for(Player p : players){
            p.calculateGeneralPoints();
            p.calculatePCPoint();
            p.calculateCCPoints();
        }
        for (Player p : players) {
            serverController.sendMessage(new SimpleReply("", Action.ENDGAME), p.getNickname());
            serverController.sendMessage(new SimpleReply("Players' total points: ", Action.INGAMEEVENT), p.getNickname());
            for (Player pp: players) {
                serverController.sendMessage(new SimpleReply(pp.getNickname() + ": " + pp.getTotalPoints(), Action.POINTS), p.getNickname());
            }
        }
        for (Player p : players) {
            serverController.sendMessage(new SimpleReply("The winner is " + calculateWinner().getNickname(), Action.WINNER), p.getNickname());
            gameController.allGames.remove(id);
        }
    }

    /** Handles the action "pick tiles".
     * @param p the current player.
     * @return list of chosen tiles. */
    public List<Tile> pickTiles(Player p) throws RemoteException {
        List<Position> chosen = controller.chooseTiles(p.getNickname(), id);
        boolean check = false;
        while(!check && p.isConnected()){
            if(board.AvailableTiles().containsAll(chosen)){
                check = true;
            } else {
                serverController.sendMessage(new SimpleReply("The chosen tiles are not available to be taken!", Action.INGAMEEVENT), p.getNickname());
                chosen = controller.chooseTiles(p.getNickname(), id);
            }
        }
        List<Tile> toInsert = p.pickTiles(chosen, board, p);
        serverController.sendMessage(new UpdateBoardMessage(Action.UPDATEBOARD, board.board), p.getNickname());
        return toInsert;
    }

    /** Checks if the tiles are all the same.
     * @param chosen the tiles to be checked.
     * @return true only if the tiles are all the same, false otherwise. */
    public boolean checkTiles(List<Tile> chosen){
        for (Tile t1: chosen) {
            for (Tile t2: chosen) {
                if ((!t1.equals(t2)) && (!t1.getCategory().equals(t2.getCategory()))) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Handles the action "order tiles".
     * @param p the current player.
     * @return list of ordered tiles. */
    public List<Tile> orderTiles(Player p, List<Tile> toInsert) throws RemoteException {
        serverController.sendMessage(new SimpleReply("Choose the order you want to insert them: ", Action.INGAMEEVENT), p.getNickname());
        List<Integer> order = new ArrayList<>();
        while(order.isEmpty() && p.isConnected()){
            order = controller.chooseOrder(p.getNickname(), id);
            try{
                toInsert = p.orderTiles(toInsert, order);
            } catch (InputMismatchException e){
                order.clear();
            }
        }
        serverController.sendMessage(new ChosenTilesMessage(toInsert,false), p.getNickname());
        return toInsert;
    }

    /** Handles the action "select column".
     * @param p the current player.
     * @param toInsert list of tiles to be inserted*/
    public void selectColumn(Player p, List<Tile> toInsert) throws RemoteException {
        int col = -1;
        while(col == -1 && p.isConnected()) {
            col = controller.chooseColumn(p.getNickname(), id);
            try {
                p.insertInShelf(toInsert, (col-1));
            } catch (InputMismatchException e) {
                col = -1;//serve per il while
            }
        }
        serverController.sendMessage(new SimpleReply("Tiles inserted ",Action.INGAMEEVENT), p.getNickname());
        serverController.sendMessage(new UpdateBoardMessage(Action.UPDATESHELF, p.getShelf()), p.getNickname());
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

    public void calculateCC(Player p) throws RemoteException {
        if(CommonCards.get(0).control(p) && p.getScoreToken1()==0){
            //ToDo LA condizione rimane vera ogni turno e continua ad assegnarli punti, da fixare obv
            for (Player pcc : players) {
                serverController.sendMessage(new CommonCompletedMessage(p.getNickname() + " completed the first " +
                        "common goal and gained " + CommonCards.get(0).getPoints() + "! Points for this goal are being " +
                        "reduced to " + (CommonCards.get(0).getPoints()-2),true,p.getNickname()),pcc.getNickname());
            }
            CommonCards.get(0).givePoints(p);
        }
        if(CommonCards.get(1).control(p) && p.getScoreToken2()==0){
            for (Player pcc : players) {
                serverController.sendMessage(new CommonCompletedMessage(p.getNickname() + " completed the second" +
                        " common goal and gained " + CommonCards.get(1).getPoints() + "! Points for this goal are being " +
                        "reduced to " + (CommonCards.get(1).getPoints()-2),false, p.getNickname()),pcc.getNickname());
            }
            CommonCards.get(1).givePoints(p);
        }
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

    /** Gets the list of all common goal cards by id. */
    public List<Integer> getCCid() {
        return ccId;
    }

    /** Gets the board of the game. */
    public Board getBoard() {
        return board;
    }
}
