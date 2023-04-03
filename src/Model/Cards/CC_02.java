package Model.Cards;

import Model.Player;
import Model.Tile.type;

public class CC_02 implements CCStrategy {
    public boolean isCompleted(Player p) {
        /*
        inserire alg
         */
        /*Tile[][] current_shelf = p.getShelf();
        type color;
        boolean end=true;
        /*
        check if any of the four tile is empty.

        if (current_shelf[0][0].getCategory() == type.EMPTY ||
                current_shelf[0][5].getCategory() == type.EMPTY ||
                current_shelf[4][0].getCategory() == type.EMPTY ||
                current_shelf[4][5].getCategory() == type.EMPTY){
            end=false;
        }
        else{
            /*
            check if all the tiles have the same type of the most low-left one.

            color = current_shelf[0][0].getCategory();
            if(color!= current_shelf[0][5].getCategory() ||
                    color != current_shelf[4][0].getCategory() ||
                    color != current_shelf[4][5].getCategory()){
                end = false;
            }
        }
        return end;*/
        return true;
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
