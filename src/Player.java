import Cards.*;
import Tile.*;
import java.util.*;

public class Player {

    private final String Nickname;
    private int id;

    private Tile[][] Shelf= new Tile[5][6]; // Controllare la dimensione
    //@see Cards.*

    Card PersonalCard= new personalCard(); // servirebbe un assegna_personalCard()

    private boolean firstToken, endToken;
    private boolean scoreToken1, scoreToken2; //Credo sia meglio averli come integer
    private int totalPoints;

    public Player(String nick){
        this.Nickname=nick;
    }
    public List<Tile> PickTiles(){
        /* idk if we will implement this method in player or in board*/
        return null;
    }

    public List<Tile> choose_order_tiles(List<Tile> selected){
        List<Tile> ordered_selected = new ArrayList<>();
        int size = selected.size(),nextValue=0;
        for(int i=0;i<size;i++) {
            System.out.println(i);
        }
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter the indices in the desired order");
        for(int i=0;i<size;i++){
             nextValue = keyboard.nextInt();//serve un check che il valore acquisito sia in indice da 1 a 3
                                            //serve un check che gli indici siano tutti diversi
             ordered_selected.add(selected.get(nextValue));
        }
        return ordered_selected;
    }
    //check if the choose column has enough space
    public int  choose_Column(int numTiles){
        Tile[][] current_shelf = getShelf();
        int numcol=0,ok=0,first_free_row=0;
        Scanner keyboard = new Scanner(System.in);
        //System.out.println("enter an integer");
        while(ok==0) {
            numcol = keyboard.nextInt();//serve un try catch
            while(current_shelf[first_free_row][numcol]!=null){// maybe null will not be the standard for blank space in shelf
                first_free_row++;
            }
            if(first_free_row + numTiles < 5) {
                ok = 1;
            }
        }
        return numcol;

    }
    public void setShelf(int numCol,int numTiles, List<Tile> toInsert){
        //admit that the column has enough space for the number of tiles
        //and the order of the tiles is already been chosen.

        int first_free_row=0;
        Tile[][] current_shelf = getShelf();
        while(current_shelf[first_free_row][numCol]!=null){// maybe null will not be the standard for blank space in shelf
            first_free_row++;
        }
        while(toInsert!=null){
            current_shelf[first_free_row][numCol] = toInsert.get(0);
            toInsert.remove(0);
        }


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

    public void setFirstToken(boolean firstToken) {
        this.firstToken = firstToken;
    }

    public void setScoreToken1(boolean scoreToken1) {
        this.scoreToken1 = scoreToken1;
    }

    public void setScoreToken2(boolean scoreToken2) {
        this.scoreToken2 = scoreToken2;
    }
}
