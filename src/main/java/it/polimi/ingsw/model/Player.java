/** Represents a player.
 *  Each player has a shelf and a personal goal card.
 * @Author Marco Gervatini, Caterina Motti, Andrea Grassi
 */
package main.java.it.polimi.ingsw.model;

import main.java.it.polimi.ingsw.exceptions.NotValidPositionException;
import main.java.it.polimi.ingsw.model.Cards.PersonalCard;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.controller.Controller;
import main.java.it.polimi.ingsw.exceptions.NotValidColumnException;
import java.util.*;

public class Player {
    private String nickname;
    private final int id;
    private final int numRows = 5;
    private final int numCols = 6;
    private Tile[][] Shelf;
    private PersonalCard PersonalCard;
    private boolean firstToken, endToken;
    private int scoreToken1, scoreToken2;
    private int totalPoints;
    private int turn;

    /** Create a player with a specified id and nickname.
     * The id is final, so it can't be changed, otherwise the nickname can be changed using the setter.
     * It initializes total points to 0.
     * It initializes all tiles in shelf to "empty".
     *
     * @param id id of the player.
     * @param nick nickname of the player.
     */
    public Player(int id, String nick){
        this.id = id;
        this.nickname = nick;
        this.totalPoints = 0;
        this.Shelf = new Tile[numRows][numCols];
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                Shelf[i][j] = new Tile("empty");
            }
        }
    }

    /**
     * Orders the selected tiles as order passed as a parameter.
     *
     * @param selected list of selected tiles to order.
     * @param order represents the order in which the tiles have to be put in shelf. It is a preference of the player.
     * @return orderedTiles list of selected tiles in order.
     */
    private List<Tile> orderTiles(List<Tile> selected, List<Integer> order){
        List<Tile> orderedTiles = new ArrayList<>();
        /*order is formalized as follows
        the player see a list of the tiles he chose like
        1 cats
        2 books
        3 plants
        and write the sequence in which he wants to insert them, like
        213, where the first corresponds to the lower he will position in his shelf column
        2->1->3 is the arraylist passed under order name.
        here aren't controls over order, like size and range of values.

        In ordered_select append the next value from selected, the tile chosen is determined

        by the first element of order, in the example the first new element has to be the second of the old list
        that, the head of order is removed, and then the second element to place is the first element of the original
        list and so on.
        it continues until the order list is empty (which appropriate controls it shouldn't be more than three iterations).
        */
        while(!order.isEmpty()) {
            orderedTiles.add(selected.get(order.get(0)));
            order.remove(0);
        }
        return orderedTiles;
    }

    /**
     * Check if the chosen column has enough space for the given tiles.
     *
     * @param numTiles number of tiles to be inserted.
     * @param col chosen column from the player. It goes from 0 to 5.
     * @throws NotValidColumnException if the parameter col is out of bound.
     * @return true only if the shelf has enough space for the given tiles, false otherwise.
     */
    private boolean checkColumn(int numTiles, int col) throws NotValidColumnException{
        if(col < 0 || col >= numCols){
            throw new NotValidColumnException("Selected column is out of bound!");
        } else {
            for(int j = 0; j < numRows; j++){
                //Check how many empty spaces there are in the selected column
                if(Shelf[col][j].getCategory().equals(type.EMPTY)) {
                    //For each empty space it decreases numTiles
                    numTiles --;
                    //If numTiles < 0 then there is no enough space
                    if(numTiles < 0) return false;
                }
            }
            return true;
        }
    }

    /** Inserts the selected tiles in a single column of the shelf.
     * The tiles are already ordered, from the bottom to the top: the first tile (index 0) goes in the first empty spot.
     *
     * @param toInsert a list of tiles, ordered, to be put in Shelf.
     * @param col chosen column.
     * @throws NotValidColumnException if the selected column has no enough space.
     */
    public void changeShelf(List<Tile> toInsert, int col) throws NotValidColumnException {
        if(!checkColumn(toInsert.size(), col)){
            throw new NotValidColumnException("Selected column has no enough space!");
        } else {
            //For each tile in toInsert
            for (Tile t: toInsert){
                for(int j = 0; j < numCols; j++){
                    //If the element in the selected column is empty then it put the new tile
                    if(Shelf[col][j].getCategory().equals(type.EMPTY)) {
                        Shelf[col][j] = t;
                    }
                }
            }
        }
    }

    //DA rivedere la parte del controller
    /**
     *
     * @param b board from which the player can take the tiles.
     */
    public void pickTiles(Board b) throws NotValidColumnException, NotValidPositionException {
        //Position chosen by the player
        Set<Position> chosen;
        do{
            chosen = Controller.Choose();
        } while(!b.AvailableTiles().containsAll(chosen));

        List<Tile> ChosenTiles = new ArrayList<>();
        for (Position p: chosen) {
            ChosenTiles.add(b.board[p.getX()][p.getY()]);
        }
        b.RemoveTiles(chosen);

        List<Integer> order = Controller.ChooseOrder();
        orderTiles(ChosenTiles,order);
        int col;
        do{
            col = Controller.ChooseColumn();
        } while(checkColumn(ChosenTiles.size(),col));
        changeShelf(ChosenTiles, col);
    }

    /** Gets the shelf of the player. */
    public Tile[][] getShelf() {
        return Shelf;
    }

    /**
     * Update the total points of the player.
     *
     * @param toSum points to be summed to the total of the player
     */

    public void addPoints(int toSum){
        this.totalPoints += toSum;
    }

    /** Gets the total points of the player */
    public int getTotalPoints() {
        return totalPoints;
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
        for(int i=0;i<5;i++){// should pass for every tile
            for(int j=0;j<6;j++){
                curr_cluster_dimension=0;
                curr_tile = curr_shelf[i][j]; //take the tile as the base of a cluster
                position = new int[]{i,j}; //take his position
                if(!checked_positions.contains(position)){//check if the tiles belongs yet to a cluster
                    checked_positions.add(position);//if not add it to the already checked tiles

                    if(!curr_tile.getCategory().equals( type.EMPTY)){//if the tiles is not empty and not in another cluster
                        curr_cluster_dimension++;//the current cluster has dim = 1 now
                        curr_cluster_dimension = clustering_rec(curr_tile,i,j,checked_positions,curr_cluster_dimension);
                        //reserch adiacent tiles of the same color
                    }
                }
                if(curr_cluster_dimension >= 6) total_points += 8;//based on cluster dimension assign points
                else if(curr_cluster_dimension == 5) total_points += 5;
                else if(curr_cluster_dimension == 4) total_points += 3;
                else if(curr_cluster_dimension == 3) total_points += 2;
            }
        }
        return total_points;
    }

    private int clustering_rec(Tile t, int x, int y, List<int[]> already_checked,int cluster_dim){
        Tile[][] curr_shelf = Shelf;
        Tile next;
        int[] position;
        int[][] direction={{0,1},{1,0},{0,-1},{-1,0}};
        /*
        direction is used as refactor, it permits to explore all the directions with only a for loop (regulated by variable k)
         */
        for(int k=0;k<4;k++) {//as k change, change the direction of the inspection
            position = new int[]{x + direction[k][0], y + direction[k][1]};
            //position is the position of the next tile to check, as x and y coo (coo of the previour adiacente tile
            //plus the direction in which as been moved the indices
            if (position[0] >= 0 && position[0] < numRows && position[1] >= 0 && position[1] < numCols) {
                //the if statement controls that next position is not out of bound from the shelf
                next = curr_shelf[x + direction[k][0]][y + direction[k][1]];//take the next tile, sure that it exits
                if (!already_checked.contains(position)) {
                    //if it has not been yet controlled
                    if (next.getCategory() == t.getCategory()) {//and is of the same type of the cluster
                        already_checked.add(position);
                        //add to checked list
                        cluster_dim = clustering_rec(next, position[0], position[1], already_checked, cluster_dim + 1);
                        /*increment the passed dim and call the recursive method on the next tile, checking if its adjacent tiles
                        could be part of the cluster, when a next tile as no more adjacent cluster-addable tiles, it will not enter
                        the if statement as will not call the recursive method and so the k-loop of the previous tile can go forward
                        but with an incremented dimension due by his next tile search.
                         */
                    }
                }
            }
        }
        return cluster_dim;// when no more tiles can be explored the cluster has been identified

        /*
        IMPORTANT could exist a problem to report the already_checked list in the main method, idj if this case java modify the original
        arraylist
        in case of troubles we will fix that issue
         */
    }
    /** @return true only if the player has the end token, false otherwise. */
    public boolean getEndToken() {
        return endToken;
    }

    /** Sets the value of end token of the player. */
    public void setEndToken(boolean value) {
        this.endToken = value;
    }

    /** Sets a new nickname for the player. */
    public void setNickname(String nick) {
        this.nickname = nick;
    }

    /** Gets the nickname of the player. */
    public String getNickname() {
        return nickname;
    }

    /** Gets the id of the player. */
    public int getId() {
        return id;
    }

    /** Gets the personal card of the player. */
    public PersonalCard getPersonalCard() {
        return PersonalCard;
    }

    /** Sets the personal card of the player according to the parameter id. */
    public void setPersonalCard(int id){
        PersonalCard = new PersonalCard(id);
    }

    /** Sets the value of end token of the player. */
    public void setFirstToken(boolean value) {
        this.firstToken = value;
    }

    /** @return true only if the player has the end token, false otherwise. */
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

    /** Gets the number of rows of the shelf. */
    public int getNumRows() {
        return numRows;
    }

    /** Gets the number of columns of the shelf. */
    public int getNumCols() {
        return numCols;
    }

    /** Gets the number of turn of the player. */
    public int getTurn() {
        return turn;
    }

    /** Sets the number of turn of the player. */
    public void setTurn(int turn) {
        this.turn = turn;
    }
}
