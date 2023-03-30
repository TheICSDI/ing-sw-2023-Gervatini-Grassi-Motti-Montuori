package Model;

import Model.Cards.*;
import Model.Tile.*;
import exceptions.NotValidColumnException;

import java.util.*;

public class Player {

    private final String Nickname;
    private int id;

    private Tile[][] Shelf= new Tile[5][6]; // Controllare la dimensione
    //@see Model.Cards.*

    PersonalCard PersonalCard = new PersonalCard(); // servirebbe un assegna_personalCard()

    private boolean firstToken, endToken;
    private int scoreToken1, scoreToken2; //Credo sia meglio averli come integer
    private int totalPoints;

    public Player(String nick){
        this.Nickname=nick;
    }
    public List<Tile> PickTiles(){
        /* it has to be in the controller architecture*/
        return null;
    }

    /**
     * order the selected tiles
     * @param selected
     * @param order
     * @return ordered_selected
     */
    public List<Tile> order_tiles(List<Tile> selected,List<Integer> order){
        List<Tile> ordered_selected = new ArrayList<>();
        while(order.get(0)!=null) {
            ordered_selected.add(selected.get(order.get(0)));
            order.remove(0);
        }
        return ordered_selected;
    }

    /**
     * check if the choose column has enough space
     * @param numTiles
     * @param numcol
     * @throws NotValidColumnException
     */
    public void  check_Column(int numTiles,int numcol) throws NotValidColumnException{
        Tile[][] current_shelf = getShelf();
        int valid =1,first_free_row=0;
        Scanner keyboard = new Scanner(System.in);
        //System.out.println("enter an integer");
        while(valid==1) {
            while(current_shelf[first_free_row][numcol]!=null){// maybe null will not be the standard for blank space in shelf
                first_free_row++;
            }
            if(first_free_row + numTiles > 5) {
                valid = 0;
            }
        }
        if (valid == 0) {
            throw new NotValidColumnException("Not enough space in the column!");
        }
    }

    public void changeShelf(int numCol, List<Tile> toInsert){

        int first_free_row=0,i=0,size;
        Tile[][] current_shelf = getShelf();
        size=toInsert.size();
        while(current_shelf[first_free_row][numCol]!=null){// maybe null will not be the standard for blank space in shelf
            first_free_row++;
        }
        while(i<size){
            current_shelf[first_free_row][numCol] = toInsert.get(0);
            toInsert.remove(0);
            i++;
        }
        setShelf(current_shelf);


    }


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

    public PersonalCard getPersonalCard() {
        return PersonalCard;
    }

    public boolean isFirstToken() {
        return firstToken;
    }

    public int isScoreToken1() {
        return scoreToken1;
    }

    public int isScoreToken2() {
        return scoreToken2;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setFirstToken(boolean firstToken) {
        this.firstToken = firstToken;
    }

    public void setScoreToken1(int scoreToken1) {
        this.scoreToken1 = scoreToken1;
    }

    public void setScoreToken2(int scoreToken2) {
        this.scoreToken2 = scoreToken2;
    }

    public void setShelf(Tile[][] shelf) {
        Shelf = shelf;
    }
}
