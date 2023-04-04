package main.java.it.polimi.ingsw.model.Cards;

import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Tile.type;
import main.java.polimi.ingsw.Model.Tile.*;

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
        ValidColumns=0; //Numero colonne Valide
        for (int j = 0; j < 5; j++) { //ciclo per le colonne
            Types=new HashSet<>(); //Lista di tipi diversi già incontrati in una colonna
            Valid=true;
            for (int i = 0; i < 6 && Valid; i++) {
                if(p.getShelf()[i][j].getCategory()==type.EMPTY){
                    Valid=false;
                }//se la colonna non è piena, non è valida automaticamente
                Types.add(p.getShelf()[i][j].getCategory());
                if(Types.size()>3){
                    Valid=false;
                }
            }
            if(Valid){ // se la colonna non ha trovato problemi la aggiungo a quelle valide
                ValidColumns++;
                if(ValidColumns==3){ // basta chiudere quà il ciclo perchè non potendo chiudere due righe alla volta
                                    // il caso con esattamente 3 colonne giuste verrà trovato il turno in cui succede
                    return true;
                }
            }
        }

        return false;
    }
    /*
    Le carte possono essere classificare in 5 macro categorie in modo da creare algoritmi diversi per il
    conteggio dei punti, le categoria sono così divise:
    1. Numero di adiacenze ripetute n volte dello stesso tipo di tessere (n. 1, 3)
    2. Posizione relativa fissa ripetute n volte dello stesso tipo di tessere (n. 2, 4, ,7, 9, 10, 11)
    3. Posizione relativa fissa ripetute n volte anche di tipo tiverso di tessere (n. 5, 8)
    4. Tessera singola n. 6
    5. Tessera singola n. 12
     */
    /*
    Per la categoria 4 e 5 si nel detta gli la struttura richiesta
    Per la categoria 1 si cercano il numero di adiacenze e le sue ripetute tenendo fisso il tipo di tessera
    Per la categoria 2 si cerca la struttura relativa e le sue ripetute tenendo fisso il tipo di tessera
    Per la categoria 3 si cercano la struttura relativa e le sue ripetute tenendo variabile il nunero di tessera
     */
    //probabilmente la categoria 2,3 possono essere unite
}
