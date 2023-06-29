package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

/** CC_01 class implements the logic for checking if the common goal card number 1 is completed by a player.
 * It requires the player to have at least six groups, each containing at least two tiles of the same type.
 * @author Giulio Montuori */
public class CC_01 implements CCStrategy {
    private final int id=1;
    private Tile[][] Shelf;
    @Override
    public int getId(){return this.id;}

    /**
     * Checks if the common goal is completed.
     * @param p a player.
     * @return true only if count the player has at least six groups that respect the rule of the card.
     */
    public boolean isCompleted(Player p) {
        this.Shelf = p.getShelf();
        int count = 0;
        int numRows = p.getNumRows();
        int numCols = p.getNumCols();
        int c = 0;


        boolean[][] checked = new boolean[numRows][numCols];
        //Remove all element from checked before starting the calculation
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                checked[i][j] = false;
            }
        }
        //For every tile in the shelf
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                int currClusterDimension = 0;
                //If the tile does not already belong to a cluster, adds it to the checked tiles
                if (!checked[i][j]) {
                    checked[i][j] = true;
                    //If the tile is not empty, it calls clusteringRes that research for a cluster of tiles of the same type
                    if (!this.Shelf[i][j].getCategory().equals(type.EMPTY)) {
                        currClusterDimension = clusteringRes(i, j, checked);
                    }
                }
                //It assigns points based on the dimension of the found cluster
                if (currClusterDimension >= 2) c++;
            }
            if (c >= 6) return true;
        }
        return false;
    }

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
}