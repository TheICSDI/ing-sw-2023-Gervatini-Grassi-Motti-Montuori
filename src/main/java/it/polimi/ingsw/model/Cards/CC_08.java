package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.it.polimi.ingsw.model.Player;

import java.util.*;

public class CC_08 implements CCStrategy {
    private int ValidRows;
    private Set<type> Types;
    private boolean Valid;

    /**
     * Counts complete rows that have 3 or less different tile types in them.
     * @param p Checks if player p's shelf meets common card 8 mission
     * @return true as soon as it finds 4 valid columns
     */
    public boolean isCompleted(Player p) {
        ValidRows = 0; //Numero righe Valide
        for (int i = 0; i < 6 ; i++) { //ciclo per le righe
            Types=new HashSet<>(); //Lista di tipi diversi già incontrati in una riga
            Valid = true;
            for (int j = 0; j < 5 && Valid; j++) {
                if(p.getShelf()[i][j].getCategory().equals(type.EMPTY)){
                    Valid = false;
                }//se la riga non è piena, non è valida automaticamente
                Types.add(p.getShelf()[i][j].getCategory());
                if(Types.size() > 3){
                    Valid = false;
                }
            }
            if(Valid){
                ValidRows++;
                if(ValidRows >= 4){
                    return true;
                }
            }
        }

        return false;
        }
}
