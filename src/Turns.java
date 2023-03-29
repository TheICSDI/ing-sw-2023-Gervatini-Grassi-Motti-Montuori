import Model.Board;
import Model.Player;
import Model.Tile.*;

import java.util.*;
public class Turns {
    Board board;
    List<Player> players=new ArrayList<>();
    private int nPlayers;
    private List<Tile> TilesTaken= new ArrayList<>();
    public Turns(Board board, List<Player> players, int nPlayers){
        this.board=board;
        this.players=players;
        this.nPlayers=nPlayers;
    }
    public void play(int playerTurn){
        TilesTaken=players.get(playerTurn).PickTiles();
        //Inserisci tilestaken nella shelf, con tutti i controlli del caso

    }
    public void nextHasTokenEnd(Player player){
        for (Player p:
                players) {
            if (!p.isEndToken()) {
                player.setEndToken(true);
            }
        }
    }
}
