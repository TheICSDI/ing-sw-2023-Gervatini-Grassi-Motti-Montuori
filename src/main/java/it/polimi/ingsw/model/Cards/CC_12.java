/**
 * CC_12 class implements the logic for checking if the common goal card number 12 is completed by a player.
 * It requires the player to have five columns of increasing or decreasing height.
 * Starting from the first column on the left or on the right, each next column must be made of exactly one more tile.
 * Tiles can be of any type.
 * @author Marco Gervatini
 */
package it.polimi.ingsw.model.Cards;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.type;

public class CC_12 implements CCStrategy {
    /**
     * Checks if the common goal is completed.
     *
     * @param p a player.
     * @return true only if the common goal card is completed.
     */
    public boolean isCompleted(Player p) {
        int[] set_point = {0, 1, 4, 5};//numero di spazi vuoti in ogni prima colonna di ogni diagonale
        type currType;
        boolean end = false;
        // le condizioni && end e && !end nei for servono a non fare controlli inutile e ad avere una sola istruzione return
        //possono essere eventualmente tagliati per pulire il codice
        for (int k = 0; k < set_point.length && !end; k++) {
            end = true;
            for (int j = 0; j < p.getNumCols() && end; j++) {
                for (int i = 0; i < p.getNumRows() && end; i++) {
                        /*
                        scorre la colonna e controlla che i primi set_point[k] spazi siano empty
                         */
                    currType = p.getShelf()[i][j].getCategory();
                    if (i < set_point[k]) {// se siamo prima del set point ci aspettiamo tutte caselle empty
                        if (!currType.equals(type.EMPTY)) {
                            end = false;
                        }
                    } else {//se siamo dopo il set point ci aspettiamo tutte caselle piene
                        if (currType.equals(type.EMPTY)) {
                            end = false;
                        }
                    }
                }
                    /* if else aggiorna il numero di spazi vuoti nella colonna successiva
                    se la piramide è ascendente gli spazi vuoti devono essere di meno
                    se è discendente gli spazi vuoti devono essere di piu'
                    k = 0 e k =1 corrispondono alle piramidi discendenti
                     */
                if (k < 2) {
                    set_point[k]++;
                } else {
                    set_point[k]--;
                }
            }
        }
        return end;
        }
    }
