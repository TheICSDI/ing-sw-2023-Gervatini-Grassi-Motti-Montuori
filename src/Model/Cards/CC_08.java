package Model.Cards;

import Model.Player;
import Model.Tile.type;

public class CC_08 implements CCStrategy {
    private int ValidColumns, DiffTypes, index;
    private type[] Types;
    private boolean NewType,Valid;

    /**
     * Counts complete rows that have 3 or less different tile types in them.
     * @param p Checks if player p's shelf meets common card 8 mission
     * @return true as soon as it finds 4 valid columns
     */
    public boolean isCompleted(Player p) {
        ValidColumns=0; //Numero colonne Valide
        for (int i = 0; i < 6; i++) { //ciclo per le righe
            DiffTypes=0; //Numero di tipi diversi in una singola colonna
            Types=new type[3]; //Lista di tipi diversi già incontrati in una colonna
            index=0;
            Valid=true;
            for (int j = 0; j < 5; j++) {
                if(p.getShelf()[i][j].getCategory()==type.EMPTY){
                    Valid=false;
                    break;
                }//se la colonna non è piena, non è valida automaticamente

                NewType=true;
                for (int k = 0; k < index; k++) {
                    if(p.getShelf()[i][j].getCategory()==Types[k]){
                        NewType=false; //Se il tipo della tile presa in considerazione è già in tyles non è nuova
                        break;
                    }
                }
                if(NewType){            // Se il tipo della tile è nuovo
                    if(DiffTypes<3) {   // e ho trovato meno di 3 tipi diversi
                        Types[index] = p.getShelf()[i][j].getCategory(); //aggiungo il tipo a tile
                        index++;
                        DiffTypes++;    // e conto un tipo diverso in più
                    }else{              //se ho già trovato tre tipi non è valida
                        Valid=false;
                        break;
                    }
                }

            }
            if(Valid){ // se la colonna non ha trovato problemi la aggiungo a quelle valide
                ValidColumns++;
                if(ValidColumns==4){ // basta chiudere quà il ciclo perchè non potendo chiudere due righe alla volta
                    // il caso con esattamente 4 colonne giuste verrà trovato il turno in cui succede
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
