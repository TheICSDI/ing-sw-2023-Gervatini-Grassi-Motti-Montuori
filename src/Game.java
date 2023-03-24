import java.util.*;
import Cards.*;


public class Game {
    private final int nCard=12; //numero di carte (personali e comuni) totali
    private final int nCC=2; //n carte comuni da pescare( si è sempre due ma almeno è estendibile)

    String id;
    Board board=new Board();
    List<personalCard> personalCards=new ArrayList<>();
    List<CommonCard> commonCards=new ArrayList<>();
    List<CommonCard> ccPescate= new ArrayList<>(); //carte comuni pescate per il game
    int idTurn;
    int nPlayers;
    List<Player> players= new ArrayList<>();


    public Game(List<Player> players,int nPlayers){
        this.nPlayers=nPlayers;
        this.players=players;

    }
    public void startGame(){
        board.fillBoard(nPlayers);
        resetCC(commonCards);
        resetPC(personalCards);
        int index=0;

        //Assegnazioni carte personali casuale, diverse fra loro
        for (Player p:
             players) {
            int temp=(int) (Math.random() * (nCard - index));
            p.PersonalCard= personalCards.get(temp);
            personalCards.remove(temp);
            index++;
        }
        //Pescata carte comuni
        for (int i=0; i<nCC;i++){
            int temp=(int) (Math.random() * (nCard - i));
            ccPescate.add(commonCards.get(temp));
            commonCards.remove(temp);
        }
        boolean endGame=false;
        int firstToPlay=(int)(Math.random()*nPlayers);
        Turns turn=new Turns(board,players,nPlayers);
        int nTurno=1;
        /*Gestione turni
        * Ogni turno considera la mossa di ognuno dei player,
        * partendo da chi ha la first player sit fino a che non si torna da lui
        * il gioco finisce appena un giocatore riempe la board e ottiene l'end game token
        * ma comunque deve finire il turno in questione fino a che non si torna al primo player
        * con due cicli è possibile gestire questa cosa*/
        while(!endGame){
            System.out.println("Turno "+nTurno+": ");
            for(int i=0;i<nPlayers;i++){
                for (Player p:
                     players) {
                    if(p.endToken){
                        endGame=true;
                    }
                }
                int playerTurn=(firstToPlay+i)%nPlayers;
                turn.play(playerTurn);

            }
            nTurno++;
        }

    }
    public void leaveGame(){}

    public void resetCC(List<CommonCard> cc){} //"Rimette tutte le carte comuni disponibili come di default"
    public void resetPC(List<personalCard> pc){} // stessa cosa per le personali

}
