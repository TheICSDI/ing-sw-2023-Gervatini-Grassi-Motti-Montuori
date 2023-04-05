package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.type;

import java.util.HashSet;
import java.util.Set;

public class CC_10  implements CCStrategy {
    private int ValidRows;
    private Set<type> Types;
    private boolean Valid;
    /**
     * Counts complete rows that have every tile with a different type(5 types).
     * @param p Checks if player p's shelf meets common card 10 mission
     * @return true as soon as it finds 2 valid columns
     */
    public boolean isCompleted(Player p) {
        ValidRows = 0; //Numero righe Valide
        for (int i = 0; i < 6 ; i++) { //ciclo per le righe
            Types= new HashSet<>(); //Lista di tipi diversi già incontrati in una riga
            Valid = true;
            for (int j = 0; j < 5 && Valid; j++) {
                if(p.getShelf()[i][j].getCategory().equals(type.EMPTY)){
                    Valid = false;
                }//se la riga non è piena, non è valida automaticamente
                Types.add(p.getShelf()[i][j].getCategory());
            }
            if(Types.size() == 5){
                ValidRows++;
                if(ValidRows >= 2){
                    return true;
                }
            }
        }
		return false;
    }
}
