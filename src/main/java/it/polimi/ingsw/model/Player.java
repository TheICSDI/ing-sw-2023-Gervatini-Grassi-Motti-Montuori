/** Represents a player.
 *  Each player has a shelf and a personal goal card.
 * @Author Caterina Motti, Andrea Grassi, Marco Gervatini
 */
package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.SimpleReply;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class Player implements Serializable {
    private final String nickname;
    private final int numRows = 6;
    private final int numCols = 5;
    private Tile[][] Shelf;
    private PersonalCard PersonalCard = null;
    private boolean firstToken, endToken;
    private int scoreToken1, scoreToken2;
    private int totalPoints;
    private int turn;
    private boolean connected = true;

    /**
     * Create a player with a specified id and nickname.
     * The nickname is final, so it can't be changed. It is unique.
     * It initializes total points to 0.
     * It initializes all tiles in shelf to "empty".
     * @param nick nickname of the player.
     */
    public Player(String nick){
        this.nickname = nick;
        this.totalPoints = 0;
        this.Shelf = new Tile[numRows][numCols];
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                Shelf[i][j] = new Tile("empty",0);
            }
        }
    }

    /**
     * Orders the selected tiles as order passed as a parameter.
     * The order is represented by a list in which each element refers to the desired position for the ordered tiles.
     * Example: selected("frames", "cats", "books"), order(3, 2, 1) -> ordered("books", "cats", "frames").
     * @param selected list of selected tiles to order.
     * @param order represents the order in which the tiles have to be put in shelf. It is a preference of the player.
     * @return orderedTiles list of selected tiles in order.
     */
    public List<Tile> orderTiles(List<Tile> selected, List<Integer> order) throws RemoteException, InputMismatchException {
        List<Tile> orderedTiles = new ArrayList<>();
        if (selected.size() > order.size()) {
            serverController.sendMessage(new SimpleReply("The selected order is wrong, you have selected more tiles!", Action.INGAMEEVENT), nickname);
            throw new InputMismatchException();
        } else if (selected.size() < order.size()){
            serverController.sendMessage(new SimpleReply("The selected order is wrong, you have selected less tiles!", Action.INGAMEEVENT), nickname);
            throw new InputMismatchException();
        } else {
            for (int i = 0; i < selected.size(); i++) {
                int x = order.get(i);
                x--;
                orderedTiles.add(selected.get(x));
            }
        }
        return orderedTiles;
    }

    /**
     * Check if the chosen column has enough space for the given tiles.
     * @param numTiles number of tiles to be inserted.
     * @param col chosen column from the player. It goes from 0 to 5.
     * @return true only if the shelf has enough space for the given tiles, false otherwise.
     */
    private boolean checkColumn(int numTiles, int col) {
        for(int j = 0; j < numRows; j++){
            //Check how many empty spaces there are in the selected column
            if(this.Shelf[j][col].getCategory().equals(type.EMPTY)) {
                //For each empty space it decreases numTiles
                numTiles --;
                if(numTiles <= 0) return true;
            }
        }
        return false;
    }

    /** Inserts the selected tiles in a single column of the shelf.
     * The tiles are already ordered, from the bottom to the top: the first tile (index 0) goes in the first empty spot.
     * @param toInsert a list of tiles, ordered, to be put in Shelf.
     * @param col chosen column.
     * @throws InputMismatchException if the selected column has no enough space.
     */
    public void insertInShelf(List<Tile> toInsert, int col) throws InputMismatchException {
        if(col < 0 || col >= this.getNumCols()){
            throw new InputMismatchException("Selected column is out of bound!");
        } else if(!checkColumn(toInsert.size(), col)){
            throw new InputMismatchException("Selected column has no enough space!");
        } else {
            //For each tile in toInsert
            for(Tile t : toInsert){
                for (int i = numRows - 1; i >= 0; i--){
                    //If the element in the selected column is empty then it put the new tile
                    if (Shelf[i][col].getCategory().equals(type.EMPTY)) {
                        Shelf[i][col] = t;
                        break;
                    }
                }
            }
        }
    }

    /**
     * It checks if the selected tiles are available to be taken, and after taking the tiles it removes them from the board.
     * @param chosen a set of position that the player has chosen.
     * @param b board from which the player can take the tiles.
     * @return a list of tiles chosen by the player to be taken from the board.
     */
    public List<Tile> pickTiles(List<Position> chosen, Board b, Player player) throws RemoteException {
        List<Tile> choice = new ArrayList<>();
        if(maxSpaceInShelf() < chosen.size()){
            serverController.sendMessage(new SimpleReply("Not enough space in the shelf", Action.INGAMEEVENT), player.getNickname());
        } else {
            for (Position p : chosen) {
                choice.add(b.board[p.getX()][p.getY()]);
            }
            b.RemoveTiles(chosen);
        }
        return choice;
    }

    /** It calculates the maximum empty space in the shelf.
     * @return the maximum free space. */
    public int maxSpaceInShelf(){
       int maxSpace = 0;
       boolean firstFullRow = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < numCols; j++) {
                if (Shelf[i][j].getCategory().equals(type.EMPTY)) {
                    firstFullRow = false;
                    break;
                }
            }
            if(!firstFullRow){
                maxSpace++;
                if(maxSpace == 3) return maxSpace;
            }
            firstFullRow = true;
        }
        return maxSpace;
    }

    /** Gets the shelf of the player. */
    public Tile[][] getShelf() {
        return Shelf;
    }

    /** Sets the shelf of the player. */
    public void setShelf(Tile[][] shelf) {
        Shelf = shelf;
    }


    /**
     * Resets player stats and shelf
     */
    public void reset(){
        this.scoreToken1=0;
        this.scoreToken2=0;
        this.totalPoints=0;
        this.PersonalCard=null;
        this.endToken=false;
        this.firstToken=false;
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                Shelf[i][j] = new Tile("empty",0);
            }
        }
    }

    /** Return true only if the shelf of the player is full, false otherwise. */
    public boolean isShelfFull(){
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if(this.Shelf[i][j].getCategory().equals(type.EMPTY)) return false;
            }
        }
        return true;
    }

    /**
     * Update the total points of the player.
     * @param toSum points to be summed to the total of the player
     */
    public void addPoints(int toSum){
        this.totalPoints += toSum;
    }

    /** Gets the total points of the player */
    public int getTotalPoints() {
        return totalPoints;
    }

    /** Sets the total points of the player */
    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    /** Calculate the points based on the rule wrote on the board, that refer to the general clustering of the shelf. */
    public void calculateGeneralPoints(){
        boolean[][] checked = new boolean[numRows][numCols];
        //Remove all element from checked before starting the calculation
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                checked[i][j] = false;
            }
        }
        //For every tile in the shelf
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                int currClusterDimension = 0;
                //If the tile does not already belong to a cluster, adds it to the checked tiles
                if(!checked[i][j]){
                    checked[i][j] = true;
                    //If the tile is not empty, it calls clusteringRes that research for a cluster of tiles of the same type
                    if(!this.Shelf[i][j].getCategory().equals(type.EMPTY)){
                        currClusterDimension = clusteringRes(i, j, checked);
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

    /** Calculates the total points gained from the common card goals. */
    public void calculateCCPoints(){
        this.totalPoints += this.scoreToken1 + this.scoreToken2;
    }

    /** Calculates the points given by the personal card. */
    public void calculatePCPoint(){
        int matches = 0;
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                if (!(this.Shelf[i][j].getCategory().equals(type.EMPTY)) &&
                        (this.Shelf[i][j].getCategory().equals(this.getPersonalCard().getCard()[i][j].getCategory()))){
                    matches++;
                }
            }
        }
        switch (matches){
            case 1 -> totalPoints++;
            case 2 -> totalPoints += 2;
            case 3 -> totalPoints += 4;
            case 4 -> totalPoints += 6;
            case 5 -> totalPoints += 9;
            case 6 -> totalPoints += 12;
        }
    }

    /** Recursive function that calculate the dimension of the current cluster.*/
    private int clusteringRes(int x, int y, boolean[][] checked){
        Tile t = this.Shelf[x][y];
        int clusterDim = 1;
        //Explores the shelf in every direction, if the tile is of the same type as t it calls itself recursively
        try {
            if (!checked[x][y + 1] && t.getCategory().equals(this.Shelf[x][y + 1].getCategory())) {
                checked[x][y + 1] = true;
                clusterDim = clusterDim + clusteringRes(x, y + 1, checked);
            }
        } catch (IndexOutOfBoundsException ignored){}
        try {
            if (!checked[x + 1][y] && t.getCategory().equals(this.Shelf[x + 1][y].getCategory())) {
                checked[x + 1][y] = true;
                clusterDim = clusterDim + clusteringRes(x + 1, y, checked);
            }
        } catch (IndexOutOfBoundsException ignored){}
        try {
            if (!checked[x][y - 1] && t.getCategory().equals(this.Shelf[x][y - 1].getCategory())) {
                checked[x][y - 1] = true;
                clusterDim = clusterDim + clusteringRes(x, y - 1, checked);
            }
        } catch (IndexOutOfBoundsException ignored){}
        try {
            if (!checked[x - 1][y] && t.getCategory().equals(this.Shelf[x - 1][y].getCategory())) {
                checked[x - 1][y] = true;
                clusterDim = clusterDim + clusteringRes(x - 1, y, checked);
            }
        } catch (IndexOutOfBoundsException ignored){}
        return clusterDim;
    }

    /** @return true only if the player has the end token, false otherwise. */
    public boolean getEndToken() {
        return endToken;
    }

    /** Sets the value of end token of the player. */
    public void setEndToken(boolean value) {
        this.endToken = value;
    }

    /** Gets the nickname of the player. */
    public String getNickname() {
        return nickname;
    }

    /** Gets the personal card of the player. */
    public PersonalCard getPersonalCard() {
        return PersonalCard;
    }

    /** Sets the personal card of the player according to the parameter id. */
    public void setPersonalCard(PersonalCard p){
        PersonalCard = p;
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

    /** Sets the connection status of the player. */
    public boolean isConnected() {
        return connected;
    }

    /** Gets the connection status of the player. */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

}
