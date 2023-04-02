package Model.Cards;

import Model.Player;
import Model.Tile.type;

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
        ValidRows =0; //Numero righe Valide
        for (int i = 0; i < 6 ; i++) { //ciclo per le righe
            Types=new HashSet<>(); //Lista di tipi diversi già incontrati in una riga
            Valid=true;
            for (int j = 0; j < 5 && Valid; j++) {
                if(p.getShelf()[i][j].getCategory()==type.EMPTY){
                    Valid=false;
                }//se la riga non è piena, non è valida automaticamente
                Types.add(p.getShelf()[i][j].getCategory());
                if(Types.size()>3){
                    Valid=false;
                }
            }
            if(Valid){
                ValidRows++;
                if(ValidRows ==4){
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
