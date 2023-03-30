package Model.Cards;

import Model.Tile.Tile;
import Model.Tile.type;

public class CC_06 implements CCStrategy {
    //serve un check che i punti token del giocatore siano =0 per fare i controlli.
    private int current_points = 8;

    public int Mission_6(Tile[][] Shelf){
        int i=0,j=0;
        int esito = 0;
        int max_color=0;
        for(type current_type: )
        return esito;
    }

    @Override
    public int isCompleted(){
        int esito = Mission_6();
        if(esito == 1){
            rescale();
        }
        return esito;
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
