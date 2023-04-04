/** Represents a player.
 *  Each player has a shelf and a personal card.
 */
package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Cards.PersonalCard;
import it.polimi.ingsw.Model.Tile.Tile;
import it.polimi.ingsw.Model.Tile.type;
import it.polimi.ingsw.exceptions.NotValidColumnException;

import java.util.*;

public class Player {
    private String nickname;
    private final int id;
    private final int numRows = 5;
    private final int numCols = 6;
    private Tile[][] Shelf;
    private it.polimi.ingsw.Model.Cards.PersonalCard PersonalCard; // servirebbe un assegna_personalCard()
    private boolean firstToken, endToken;
    private int scoreToken1, scoreToken2; //Credo sia meglio averli come integer
    private int totalPoints;
    private int turn;

    /** Create a player with a specified id and nickname.
     * The id is final, so it can't be changed, otherwise the nickname can be changed using the setter.
     * It initializes total points to 0.
     * It initializes all tiles in shelf to "empty".
     */
    public Player(int id, String nick){
        this.id=id;
        this.nickname=nick;
        this.totalPoints = 0;
        this.Shelf = new Tile[numRows][numCols];
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                Shelf[i][j] = new Tile("empty");
            }
        }
    }

    /**
     * order the selected tiles as order say, order is a preferences passed from the client.
     * @param selected list of selected tiles to order
     * @param order represents the order in which the tiles have to be put on the shelf
     * @return ordered_selected
     */
    private List<Tile> order_tiles(List<Tile> selected,List<Integer> order){
        List<Tile> ordered_selected = new ArrayList<>();
        /*order is formalized as follows
        the player see a list of the tiles he chose like
        1 cats
        2 books
        3 plants
        and write the sequence in which he wants to insert them, like
        213, where the first corresponds to the lower he will position in his shelf column
        2->1->3 is the arraylist passed under order name.
        here aren't controls over order, like size and range of values.
         */
        /* in ordered_select append the next value from selected, the tile chosen is determined
        by the first element of order, in the example the first new element has to be the second of the old list
        that, the head of order is removed, and then the second element to place is the first element of the original
        list and so on.
        it continues until the order list is empty (which appropriate controls it shouldn't be more than three iterations).
        */
        while(!order.isEmpty()) {
            ordered_selected.add(selected.get(order.get(0)));
            order.remove(0);
        }
        return ordered_selected;
    }

    /**
     * check if the chosen column has enough space
     * @param numTiles
     * @param numcol
     * @throws NotValidColumnException
     */
    private void check_Column(int numTiles, int numcol) throws NotValidColumnException {
        int first_free_row=0;
        boolean valid=true,ok=true;
        while(ok) {
            while(this.Shelf[first_free_row][numcol].getCategory() != type.EMPTY) {
                first_free_row++;
            }
            ok=false; // we find the first free spot of the column
        }
        if(first_free_row + numTiles > 5) {
            valid = false;
        }
        if (valid) {
            throw new NotValidColumnException("Not enough space in the column!");
        }
    }

    /**
     * modifier method that permits to change the state of the shelf, changing the empty tiles to another type of tiles.
     * every change is made taking care about the rules about insertion, in detail it ensures that every tile is put
     * in the lower empty place of the column, and that for a single insertion of multiple tiles these are always in
     * a single column.
     * The method assumes that the tiles have already a order, and that the selected column have enough space to place
     * them.
     * @param numCol number of the column of the insertion
     * @param toInsert ordered list of the tiles to insert
     */
    public void changeShelf(int numCol, List<Tile> toInsert){
        int first_free_row=0,i=0,size;
        Tile[][] current_shelf = getShelf();
        size=toInsert.size();
        //find the first empty card of the column.
        while(current_shelf[first_free_row][numCol].getCategory() != type.EMPTY){// maybe null will not be the standard for blank space in shelf
            first_free_row++;
        }
        //place the ordered tiles on above the other in the selected column (the check is done in another method)
        while(i<size){
            current_shelf[first_free_row][numCol] = toInsert.get(0);
            toInsert.remove(0);
            i++;
        }
        setShelf(current_shelf);
    }


    /*
    method that calculates the remaining points, so those points that belongs to the general clustering of the shelf,
    (the rule wrote on the board of the real game), in other word the remaining points that don't regard neither the
    common cards neither the personal card.
     */
    public int Calculate_general_points(){
        int total_points=0;
        Tile[][] curr_shelf = this.Shelf;
        Tile curr_tile;
        int curr_cluster_dimension;
        int[] position;
        List<int[]> checked_positions = new ArrayList<>();
        for(int i=0;i<5;i++){
            for(int j=0;j<6;j++){
                curr_cluster_dimension=0;
                curr_tile = curr_shelf[i][j];
                position = new int[]{i,j};
                if(!checked_positions.contains(position)){
                    checked_positions.add(position);
                    curr_cluster_dimension++;
                    if(curr_tile.getCategory() != type.EMPTY){
                        curr_cluster_dimension = clustering_rec(curr_tile,i,j,checked_positions,curr_cluster_dimension);
                    }
                }
                if(curr_cluster_dimension >= 6) total_points += 8;
                else if(curr_cluster_dimension == 5) total_points += 5;
                else if(curr_cluster_dimension == 4) total_points += 3;
                else if(curr_cluster_dimension == 3) total_points += 2;
            }
        }
        return total_points;
    }

    private int clustering_rec(Tile t, int x, int y, List<int[]> already_checked,int cluster_dim){
        Tile[][] curr = Shelf;
        Tile up,right,down,left;
        int[] position;
        try{
            up = curr[x][y+1];
            position = new int[]{x,y+1};
            if(! already_checked.contains(position)) {
                if (up.getCategory() == t.getCategory()) {
                    already_checked.add(position);
                    cluster_dim = clustering_rec(curr[x][y + 1], x, y + 1, already_checked,cluster_dim+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){
        // if cant go in this direction is ok, but have to control the others
        }
        try{
            right = curr[x+1][y];
            position = new int[]{x+1,y};
            if(! already_checked.contains(position)) {
                if (right.getCategory() == t.getCategory()) {
                    already_checked.add(position);
                    cluster_dim = clustering_rec(curr[x+1][y], x+1, y, already_checked,cluster_dim+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){
        }
        try{
            down = curr[x][y-1];
            position = new int[]{x,y-1};
            if(! already_checked.contains(position)) {
                if (down.getCategory() == t.getCategory()) {
                    already_checked.add(position);
                    cluster_dim = clustering_rec(curr[x][y-1], x, y-1, already_checked,cluster_dim+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){
        }
        try{
            left = curr[x-1][y];
            position = new int[]{x-1,y};
            if(! already_checked.contains(position)) {
                if (left.getCategory() == t.getCategory()) {
                    already_checked.add(position);
                    cluster_dim = clustering_rec(curr[x-1][y], x-1, y, already_checked,cluster_dim+1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){
        }
        return cluster_dim;
    }

    //Da fare
    public void PickTiles(){}

    private void setShelf(Tile[][] shelf) {
        //maybe should be public for the additional feature of backup of the game.
        Shelf = shelf;
    }

    /**
     * Update the total points of the player.
     * @param toSum points to be summed to the total of the player
     */
    public void addPoints(int toSum){
        this.totalPoints += toSum;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public boolean getEndToken() {
        return endToken;
    }

    public void setEndToken(boolean value) {
        this.endToken = value;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public Tile[][] getShelf() {
        return Shelf;
    }

    //Probabilmente la personal card non la rappresentiamo con un oggetto, bensì con un id tra 0 e 11
    //da rivedere
    public PersonalCard getPersonalCard() {
        return PersonalCard;
    }

    public void setPersonalCard(int n){
        PersonalCard = new PersonalCard(n);
    }

    public void setFirstToken(boolean value) {
        this.firstToken = value;
    }

    public boolean getFirstToken() {
        return firstToken;
    }

    public void setScoreToken1(int value) {
        this.scoreToken1 = value;
    }

    public int getScoreToken1() {
        return scoreToken1;
    }

    public void setScoreToken2(int value) {
        this.scoreToken2 = value;
    }

    public int getScoreToken2() {
        return scoreToken2;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
