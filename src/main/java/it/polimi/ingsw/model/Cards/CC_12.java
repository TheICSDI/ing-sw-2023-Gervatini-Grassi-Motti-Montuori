package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;

/*
    check if the shape of the full part of the shelf is a composition of order high columns.
 */
public class CC_12 implements CCStrategy {
public boolean isCompleted(Player p) {
    int i, j, k;
    int[] set_point = {0, 1, 4, 5};//numero di spazi vuoti in ogni prima colonna di ogni diagonale
    Tile[][] curr_shelf = p.getShelf();
    int num_row = curr_shelf.length;
    int num_col = curr_shelf[0].length;
    type curr_tile_type;
    boolean end = false;
    // le condizioni && end e && !end nei for servono a non fare controlli inutile e ad avere una sola istruzione return
    //possono essere eventualmente tagliati per pulire il codice
    for (k = 0; k < set_point.length && !end; k++) {
        end = true;
        for (j = 0; j < num_col && end; j++) {//scorre per colonna
            for (i = 0; i < num_row && end; i++) {
                    /*
                    scorre la colonna e controlla che i primi set_point[k] spazi siano empty
                     */
                curr_tile_type = curr_shelf[i][j].getCategory();
                if (i < set_point[k]) {// se siamo prima del set point ci aspettiamo tutte caselle empty
                    if (!curr_tile_type.equals(type.EMPTY)) {
                        end = false;
                    }
                } else {//se siamo dopo il set point ci aspettiamo tutte caselle piene
                    if (curr_tile_type.equals(type.EMPTY)) {
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
