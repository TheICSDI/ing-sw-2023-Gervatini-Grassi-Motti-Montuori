/** Represents a player.
 *  Each player has a shelf and a personal goal card.
 * @Author Caterina Motti, Marco Gervatini, Andrea Grassi
 */
package main.java.it.polimi.ingsw.model;

import main.java.it.polimi.ingsw.exceptions.InvalidPositionException;
import main.java.it.polimi.ingsw.model.Cards.PersonalCard;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.controller.Controller;
import main.java.it.polimi.ingsw.exceptions.InvalidColumnException;
import java.util.*;

public class Player {
    private String nickname;
    private final int id;
    private final int numRows = 6;
    private final int numCols = 5;
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
     * The order is represented by a list in which each element refers to the desired position for the ordered tiles.
     * Example: selected("frames", "cats", "books"), order(3, 2, 1) -> ordered("books", "cats", "frames").
     *
     * @param selected list of selected tiles to order.
     * @param order represents the order in which the tiles have to be put in shelf. It is a preference of the player.
     * @return orderedTiles list of selected tiles in order.
     */
    public List<Tile> orderTiles(List<Tile> selected, List<Integer> order) {
        List<Tile> orderedTiles = new ArrayList<>();
        if (selected.size() > order.size()) {
            throw new InputMismatchException("The selected order is wrong, you have selected more tiles!");
        } else if (selected.size() < order.size()){
            throw new InputMismatchException("The selected order is wrong, you have selected less tiles!");
        } else {
            for (int i = 0; i < selected.size(); i++) {
                orderedTiles.add(selected.get(order.get(i) - 1));
            }
        }
        return orderedTiles;
    }

    /**
     * Check if the chosen column has enough space for the given tiles.
     *
     * @param numTiles number of tiles to be inserted.
     * @param col chosen column from the player. It goes from 0 to 5.
     * @throws InvalidColumnException if the parameter col is out of bound.
     * @return true only if the shelf has enough space for the given tiles, false otherwise.
     */
    private boolean checkColumn(int numTiles, int col) throws InvalidColumnException {
        if(col < 0 || col >= numCols){
            throw new InvalidColumnException("Selected column is out of bound!");
        } else {
            for(int j = 0; j < numRows; j++){
                //Check how many empty spaces there are in the selected column
                if(this.Shelf[j][col].getCategory().equals(type.EMPTY)) {
                    //For each empty space it decreases numTiles
                    numTiles --;
                    //If numTiles < 0 then there is no enough space
                    if(numTiles < 0) return true;
                }
            }
            return false;
        }
    }

    /** Inserts the selected tiles in a single column of the shelf.
     * The tiles are already ordered, from the bottom to the top: the first tile (index 0) goes in the first empty spot.
     *
     * @param toInsert a list of tiles, ordered, to be put in Shelf.
     * @param col chosen column.
     * @throws InvalidColumnException if the selected column has no enough space.
     */
    public void insertInShelf(List<Tile> toInsert, int col) throws InvalidColumnException {
        if(!checkColumn(toInsert.size(), col)){
            throw new InvalidColumnException("Selected column has no enough space!");
        } else {
            //For each tile in toInsert
            for (int i = 0; i < toInsert.size(); i++){
                for(int j = 0; j < numCols; j++){
                    //If the element in the selected column is empty then it put the new tile
                    if(Shelf[j][col].getCategory().equals(type.EMPTY)) {
                        Shelf[j][col] = toInsert.get(i);
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
    public void pickTiles(Board b) throws InvalidColumnException, InvalidPositionException {
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
        insertInShelf(ChosenTiles, col);
    }

    /*public List<Tile> pickTiles(Set<Position> chosen, Board b) throws InvalidPositionException {
     //Position chosen by the player
     if (!b.AvailableTiles().containsAll(chosen)) {
     throw new InputMismatchException("The chosen tiles are not available to be taken!");
     } else {
     List<Tile> choice = new ArrayList<>();
     for (Position p : chosen) {
     choice.add(b.board[p.getX()][p.getY()]);
     }
     b.RemoveTiles(chosen);
     return choice;
     }
     }*/

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

    private final List<Position> checked = new ArrayList<>();
    /** Calculate the points based on the rule wrote on the board, that refer to the general clustering of the shelf.
     */
    public void calculateGeneralPoints(){
        //Remove all element from checked before starting the calculation
        checked.clear();
        //For every tile in the shelf
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                //Take the tile as base for a cluster
                int currClusterDimension = 1;
                Tile tmp = this.Shelf[i][j];
                Position p = new Position(i,j); //take its position

                //If the tile does not already belong to a cluster, adds it to the checked tiles
                if(!checked.contains(p)){
                    checked.add(p);
                    //If the tile is not empty, it calls clusteringRes that research for a cluster of tiles of the same type
                    if(!tmp.getCategory().equals(type.EMPTY)){
                        currClusterDimension = clusteringRes(p, currClusterDimension);
                    }
                }
                //It assigns points based on the dimension of the found cluster
                if(currClusterDimension >= 6) this.totalPoints += 8;
                else if(currClusterDimension == 5) this.totalPoints += 5;
                else if(currClusterDimension == 4) this.totalPoints += 3;
                else if(currClusterDimension == 3) this.totalPoints += 2;
            }
        }
    }

    private int clusteringRes(Position position, int clusterDim){
        Tile t = this.Shelf[position.getX()][position.getY()];
        List<Position> directions = new ArrayList<>();
        directions.add(new Position(1, 0));
        directions.add(new Position(0, 1));
        for (Position k : directions) {
            //Position of the next tile to check according to the direction
            Position p = new Position(position.getX() + k.getX(), position.getY() + k.getY());
            //if the position is not out of bound
            if (p.getX() >= 0 && p.getX() < numRows && p.getY() >= 0 && p.getY() < numCols) {
                //it takes the next tile
                Tile next = this.Shelf[p.getX()][p.getY()];
                //if it has not been checked yet and is of the same type of the cluster, it is added to the already checked tiles
                if (!checked.contains(p) && next.getCategory().equals(t.getCategory())) {
                    checked.add(p);
                    //Call recursively the method by passing the next tile, its position, the list of already checked
                    //tiles and the incremented cluster dimension.
                    return clusteringRes(p, clusterDim + 1);
                    // When the next tile as no more adjacent cluster-addable tiles, it will not enter
                    // the if statement and will not call the recursive method
                }
            }
        }
        System.out.println(clusterDim);
        return clusterDim;
        //When no more tiles can be explored, the cluster has been identified
        /*
        PROBLEMA: se metto le direzioni che tornano indietro va in loop, ma se non le metto non conta correttamente i cluster
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
