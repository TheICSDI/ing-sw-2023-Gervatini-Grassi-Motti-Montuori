/** Represents a game. It is identified by a unique id.
 * Each game has some players (from 2 to 4), a board, 12 common goal cards and 12 personal goal cards.
 * @author Andrea Grassi, Caterina Motti
 */
package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.exceptions.CannotAddPlayerException;
import it.polimi.ingsw.exceptions.InvalidColumnException;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Cards.*;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.ChosenTilesMessage;
import it.polimi.ingsw.network.messages.ReplyMessage;
import it.polimi.ingsw.network.messages.UpdateBoardMessage;

import java.util.*;

public class Game {
    private static int count = 0;
    public int id;
    private Boolean started = false;
    private int nPlayers;// non puo essere final perche' il numero di player puo cambiare se qualcuno entra ed esce
    private final List<Player> players;
    private final Board board;
    private final List<CCStrategy> allCC = new ArrayList<>();
    private final List<CommonCard> CommonCards = new ArrayList<>();
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
        this.nPlayers = players.size();
        this.players = players;


        //Initializes the new board
        this.board = new Board(nPlayers);
        this.board.fillBoard();

        //Initializes the common and personal goal cards
        resetCards();

        //Shuffle the list of players in order to randomically pick the first one
        Collections.shuffle(players);
        players.get(0).setFirstToken(true);
        //Shuffle the personal goal cards in order to randomically give them to the players
        Collections.shuffle(allPC);
        for(int i = 0; i < nPlayers; i++){
            //Sets the turn of each player
            players.get(i).setTurn(i);
            players.get(i).setPersonalCard(allPC.get(i));
        }

        //Shuffle the common goal cards to randomically draws two of them
        Collections.shuffle(allCC);
        CommonCards.add(new CommonCard(allCC.get(0),true));
        CommonCards.add(new CommonCard(allCC.get(1),false));
    }


    /**
     * Manages all the game logic from start to end.
     * It calculates the total points of each player at the end of every turn.
     *
     * @see Player,Board,CommonCard,PersonalCard
     */
    public void startGame(){
        //At the starting point no player has the endgame token
        boolean endGame = false;
        boolean check = false;
        started = true;
        while(!endGame){
            for (Player p: players) {
                for (Player p1:
                     players) {
                    if(p1.getNickname().equals(p.getNickname())){
                        p1.getOut().println(new ReplyMessage("It's your turn!",Action.INGAMEEVENT));
                        sendElement(board.board, List.of(p1),Action.UPDATEBOARD);
                    }else{
                        sendElement(board.board, List.of(p1),Action.UPDATEBOARD);
                        p1.getOut().println(new ReplyMessage("It's " + p.getNickname() + "'s turn!",Action.INGAMEEVENT));
                    }
                }
                p.getOut().println(new ReplyMessage("  Your shelf" , Action.INGAMEEVENT));
                sendElement(p.getShelf(), List.of(p),Action.UPDATESHELF);
                p.getOut().println(new ReplyMessage("Select tile you want to pick: ",Action.INGAMEEVENT));

                //The player can pick some tiles from the board and insert it inside its shel//
                List<Tile>  toInsert = new ArrayList<>();

                while(toInsert.isEmpty()) { //todo è possibile prendere due tiles disponibili ma non adiacenti, è da fixare
                    Set<Position> chosen = controller.chooseTiles(p.getNickname(),id); //più che un ciclo infinito qua è meglio fare un listener o simili, più pulito

                    try{
                        toInsert = p.pickTiles(chosen, board,p);
                    } catch (InvalidPositionException e) {
                        //bisogna attivare un thread per comunicare che non ce la tiles
                    }
                }
                sendElement(board.board, List.of(p),Action.UPDATEBOARD);
                p.getOut().println(new ChosenTilesMessage(toInsert));

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
                    p.getOut().println(new ReplyMessage("Choose the order you want to insert them in : ",Action.INGAMEEVENT));
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
                    p.getOut().println(new ChosenTilesMessage(toInsert));
                }
                p.getOut().println(new ReplyMessage("Choose column: ",Action.INGAMEEVENT));
                int col = -1;
                while(col == -1) {
                    col= controller.chooseColumn(p.getNickname(),id);
                    try {
                        p.insertInShelf(toInsert, (col-1));
                    } catch (InvalidColumnException e) {
                        col = -1;//serve per il while
                        //bisogna attivare un thread per comunicare che non ce spazio
                    }
                }
                p.getOut().println(new ReplyMessage("Tiles inserted ",Action.INGAMEEVENT));
                p.getOut().println(new ReplyMessage("  Your shelf" , Action.INGAMEEVENT));
                sendElement(p.getShelf(), List.of(p),Action.UPDATESHELF);
                //If the board is empty it will be randomically filled
                if(board.isBoardEmpty()){
                    board.fillBoard();
                }

                //At each turn the common card goals are calculated
                if(CommonCards.get(0).control(p)){
                    CommonCards.get(0).givePoints(p);
                }
                if(CommonCards.get(1).control(p)){
                    CommonCards.get(1).givePoints(p);
                }

                //If the end game token has not been assigned and the current player has completed his shelf
                //it assigns the end token and add 1 point
                if (!check && p.isShelfFull()) {
                    p.setEndToken(true);
                    p.addPoints(1);
                    check = true;
                }

                //If the next player has the end game token the game ends
                if (players.get((players.indexOf(p) + 1) % players.size()).getEndToken()) {
                    endGame = true;
                }
            }
        }
        for(Player p : players){
            p.calculateGeneralPoints();
        }
        //Manca il conteggio di personal card
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

    /** Shows the board in a graphical way. Each tile is represented by a symbol. */
    public void showBoard(){
        for(int i=0;i<board.getNumRows();i++){
            for(int j=0;j<board.getNumCols();j++){
                switch (board.board[i][j].getCategory()) {
                    case GAMES -> System.out.println("G");
                    case CATS -> System.out.println("C");
                    case BOOKS -> System.out.println("B");
                    case FRAMES -> System.out.println("F");
                    case PLANTS -> System.out.println("P");
                    case TROPHIES -> System.out.println("T");
                    //case not_accessible or empty
                    default -> System.out.println("-");
                }
            }
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

    public void addPlayer(Player p) throws CannotAddPlayerException {
        if(!started){
            if(players.size()<4){
                players.add(p);
            }
            else throw  new CannotAddPlayerException("too many players already in the lobby");
        }
        else throw new CannotAddPlayerException("the game is already started");
    }

    public boolean isStarted() {
        return started;
    }
    //return true if the order is a list of length between 1 and 3 and is composed only from 1 instance of 1 ,2 and 3
    public boolean acceptableOrder(List<Integer> order) {
        if (order.size() > 3) {
            return false;
        } else {
            if (!order.contains(1)) {
                return false;
            } else if (order.size() > 1 && !order.contains(2)) {
                return false;
            } else return order.contains(3);
        }
    }

    public void sendElement(Tile[][] element, List<Player> playersToSendTo, Action action){
        type[][] information = new type[element.length][element[0].length];
        for(int i = 0; i< element.length;i++ ){
            for(int j = 0; j<element[0].length;j++){
                information[i][j] = element[i][j].getCategory();
            }
        }
        for (Player p: playersToSendTo) {
            p.getOut().println(new UpdateBoardMessage(action, information));
        }
    }
}
