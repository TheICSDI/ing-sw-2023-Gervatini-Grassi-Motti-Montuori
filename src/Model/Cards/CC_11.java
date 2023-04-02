package Model.Cards;

import Model.Player;
import Model.Tile.*;

public class CC_11 implements CCStrategy {
    public boolean isCompleted(Player p) {
        Tile[][] curr_shelf=p.getShelf();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if(curr_shelf[i][j].getCategory()!=type.EMPTY) {
                    type curr_type = curr_shelf[i][j].getCategory();
                    if(curr_shelf[i+2][j].getCategory()==curr_type &&
                        curr_shelf[i][j+2].getCategory()==curr_type &&
                        curr_shelf[i+1][j+1].getCategory()==curr_type &&
                        curr_shelf[i+2][j+2].getCategory()==curr_type){
                        return true;
                    }
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
