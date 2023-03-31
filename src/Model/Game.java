package Model;
import java.util.*;

import Model.Cards.*;



public class Game {
    private final int nCard=12; //numero di carte (personali e comuni) totali
    private final int nCC=2; //n carte comuni da pescare( si è sempre due ma almeno è estendibile)
    int nPlayers;
    String id;
    Board board=new Board(nPlayers);
    List<PersonalCard> personalCards=new ArrayList<>();
    List<CCStrategy> allCC;
    List<CommonCard> CommonCards =new ArrayList<>();//carte comuni pescate per il game
    List<Player> players = new ArrayList<>();
    List<Integer> nPC=new ArrayList<>(); //numero carte personali


    public Game(List<Player> players,int nPlayers){
        this.nPlayers=nPlayers;
        this.players=players;

    }

    /**
     * Fills board for the first time, give players their random personal cards, draw 2 random commons,
     * sort players list in their turn order
     *
     * @see Board,CommonCard,CCStrategy,PersonalCard,Player
     *
     */
    public void createGame(){
        board.fillBoard(); //funzione che inizializza la board
        for(int i=0;i<nCard;i++){
            nPC.add(i);
        }
        Collections.shuffle(nPC);
        Collections.shuffle(players);//mischia la lista dei players
        for(int i=0;i<nPlayers;i++){ //assegna l'id in base al loro turno e una carta personale random diversa dalle altre
            players.get(i).setId(i);
            players.get(i).setPersonalCard(nPC.get(i));
        }

        //Common cards random draw *Non è estendibile così dato che comunque sono sempre solo 2 carte e il resto del programma
        //                          non è estindibile di base, lascio così?*
        resetCC();
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
     * Manages turns, token, common cards and board checks
     *
     * @see Player,CommonCard,PersonalCard
     */
    public void startGame(){
        boolean endGame=false;//end game token non pescato
        int nTurno=1;

        while(!endGame){
            //System.out.println("Turno "+nTurno+": "); commentato perchè non so cosa dobbiamo fare con la cli
            for (Player p:  players) {
                p.PickTiles();
                if(board.isBoardEmpty()){
                    board.fillBoard();
                }
                if(CommonCards.get(0).DoControl(p)){
                    CommonCards.get(0).CalculatePoints(p);
                }
                if(CommonCards.get(1).DoControl(p)){
                    CommonCards.get(1).CalculatePoints(p);
                }
                if(p.isEndToken()){
                    p.setTotalPoints(p.getTotalPoints()+1);
                    endGame=true;
                }
            }
            nTurno++;
        }

        //Manca solo il conteggio dei punti finale(Personal cards e same type tiles adiacenti)
    }
    public void leaveGame(){}

    /**
     * Iniatilizes all common cards
     *
     * @see CCStrategy
     */
    public void resetCC(){ //"Rimette tutte le carte comuni disponibili come di default"
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
    }


    public void showBoard(Board board){
        for(int i=0;i<board.getNumRows();i++){
            for(int j=0;j<board.getNumCols();j++){
                if(board.board[i][j]==null){
                    System.out.print(" ");
                }
                switch (board.board[i][j].getCategory()) {
                    case NOT_ACCESSIBLE -> System.out.println("-");
                    case GAMES -> System.out.println("G");
                    case CATS -> System.out.println("C");
                    case BOOKS -> System.out.println("B");
                    case FRAMES -> System.out.println("F");
                    case PLANTS -> System.out.println("P");
                    case TROPHIES -> System.out.println("T");
                }
            }
        }
    }

}
