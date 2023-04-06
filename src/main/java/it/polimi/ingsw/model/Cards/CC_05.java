package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.model.Tile.*;

import java.util.HashSet;
import java.util.Set;

public class CC_05 implements CCStrategy {
    private int ValidColumns;
    private Set<type> Types;
    private boolean Valid;

    /**
     * Counts complete columns that have 3 or less different tile types in them.
     * @param p Checks if player p's shelf meets common card 5 mission
     * @return true as soon as it finds 3 valid columns
     */
    public boolean isCompleted(Player p) {
        ValidColumns = 0; //Numero colonne Valide
        for (int j = 0; j < 5; j++) { //ciclo per le colonne
            Types = new HashSet<>(); //Lista di tipi diversi già incontrati in una colonna
            Valid = true;
            for (int i = 0; i < 6 && Valid; i++) {
                if(p.getShelf()[i][j].getCategory().equals(type.EMPTY)){
                    Valid = false;
                }//se la colonna non è piena, non è valida automaticamente
                Types.add(p.getShelf()[i][j].getCategory());
                if(Types.size() > 3){
                    Valid = false;
                }
            }
            if(Valid){ // se la colonna non ha trovato problemi la aggiungo a quelle valide
                ValidColumns++;
                if(ValidColumns == 3){ // basta chiudere quà il ciclo perchè non potendo chiudere due righe alla volta
                                    // il caso con esattamente 3 colonne giuste verrà trovato il turno in cui succede
                    return true;
                }
            }
        }
        return false;
    }
}
