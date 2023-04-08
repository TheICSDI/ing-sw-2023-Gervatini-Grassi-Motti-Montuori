/** Represents a game. It is identified by a unique id.
 * Each game has some players (from 2 to 4), a board, 12 common goal cards and 12 personal goal cards.
 * @author Andrea Grassi, Caterina Motti
 */
package main.java.it.polimi.ingsw.model;
import java.util.*;

import main.java.it.polimi.ingsw.exceptions.InvalidColumnException;
import main.java.it.polimi.ingsw.exceptions.InvalidPositionException;
import main.java.it.polimi.ingsw.model.Cards.*;

public class Game {
    private static int count = 0;
    public int id;
    private final int nPlayers;
    private List<Player> players;
    private final Board board;
    private List<CCStrategy> allCC;
    private final List<CommonCard> CommonCards = new ArrayList<>();
    private final List<Integer> nPC = new ArrayList<>();

    /** Creates a game given a list of players.
     * It initializes the board for the first time.
     * It picks the first player and give randomically each player a personal goal card.
     * It randomically choose two common goal cards.
     */
    public Game(List<Player> players){
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
        Collections.shuffle(nPC);
        for(int i = 0; i < nPlayers; i++){
            //Sets the turn of each player
            players.get(i).setTurn(i);
            players.get(i).setPersonalCard(nPC.get(i));
        }

        //Shuffle the common goal cards to randomically draws two of them
        Collections.shuffle(allCC);
        CommonCards.add(new CommonCard(allCC.get(0),true));
        CommonCards.add(new CommonCard(allCC.get(1),false));
    }


    /*Double loop to have the game continue until someone gets the EndGameToken and is the turn of the player
    * with the FirstPlayerSit again, turn order is the same as the order in players list, firstPlayer is
    * the first in the List, every turn a player gets to pick 1-3 the tiles on the board and put them in his shelf,
    * the game checks if he has the endGameToken,if he has completed a common task or if it needs to refill
    *  the board*/
    /**
     * Manages turns, token, common cards and board checks.
     *
     * @see Player,CommonCard,PersonalCard
     */
    public void startGame() throws InvalidColumnException, InvalidPositionException {
        boolean endGame = false;//end game token non pescato

        while(!endGame){
            for (Player p: players) {
                //The player can pick some tiles from the board
                p.pickTiles(board);
                //If the board is empty it will be randomically filled
                if(board.isBoardEmpty()){
                    board.fillBoard();
                }
                //At each turn the common card goals are calculated
                if(CommonCards.get(0).DoControl(p)){
                    CommonCards.get(0).CalculatePoints(p);
                }
                if(CommonCards.get(1).DoControl(p)){
                    CommonCards.get(1).CalculatePoints(p);
                }
                //If the current player has the token end
                if(p.getEndToken()){
                    p.addPoints(1);
                    endGame = true;
                }
            }
        }
        for(Player p : players){
            p.calculateGeneralPoints();
        }
        //Manca il conteggio di personal card
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
            nPC.add(i);
        }
    }

    /** Shows the board in a graphical way. Each tile is represented by a symbol.
     *
     * @param board the board that has to be represented.
     */
    public void showBoard(Board board){
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

}
