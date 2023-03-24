import Cards.*;

import java.util.*;

public class Player {

    private final String Nickname;
    private int id;

    private Tile[][] Shelf= new Tile[5][6]; // Controllare la dimensione
    //@see Cards.*

    Card PersonalCard= new personalCard();

    private boolean firstToken, endToken;
    private boolean scoreToken1, scoreToken2; //Credo sia meglio averli come integer
    private int totalPoints;

    public Player(String nick){
        this.Nickname=nick;
    }

    public void setShelf(int numCol,int numTiles, List<Tile> toInsert){
        /* */
    }

    //addPoints aggiorna i punti del giocatore, richiede i punti di una carta di cui e' rispettata la condizione.
    public void addPoints(int toSum){
        setTotalPoints(getTotalPoints()+toSum);
    }
    public boolean isEndToken() {
        return endToken;
    }

    public void setEndToken(boolean endToken) {
        this.endToken = endToken;
    }

    public String getNickname() {
        return Nickname;
    }

    public int getId() {
        return id;
    }

    public Tile[][] getShelf() {
        return Shelf;
    }

    public Card getPersonalCard() {
        return PersonalCard;
    }

    public boolean isFirstToken() {
        return firstToken;
    }

    public boolean isScoreToken1() {
        return scoreToken1;
    }

    public boolean isScoreToken2() {
        return scoreToken2;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

}
