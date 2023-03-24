import java.util.*;
public class Turns {
    Board board;
    List<Player> players=new ArrayList<>();
    private int nPlayers;
    public Turns(Board board,List<Player> players, int nPlayers){
        this.board=board;
        this.players=players;
        this.nPlayers=nPlayers;
    }
    public void play(int playerTurn){}
    public void nextHasTokenEnd(Player player){
        for (Player p:
                players) {
            if (!p.isEndToken()) {
                player.setEndToken(true);
            }
        }
    }
}
