import java.util.*;
public class Board {
    private final int numCols = 9;
    private final int numRows = 9;
    private final int numTiles = 120;
    public Tile board[][];
    //deve restare una matrice per poterla indicizzare nei confronti durante il "prelievo di tiles"
    private List<Tile> tilesList[];
    //per riempire correttamente la board posso fare una map che mappa ogni coppia di indici (casella della board) al
    //corrispondente numero di giocatori.
    //la map deve essere final e private, ed essere inizializzata una volta soltanto

    //alcune caselle saranno vuote di default, magari un placeholder che identifica casella non accessibile
    //visto che la board non è quadrata

    public Board(){
        board = new Tile[numCols][numRows];
        tilesList = new ArrayList<>[numTiles];
        //nel costruttore devo già riempire tilesList con tutte le possibili tiles del gioco
        //magari da un file con tutte le possbili tessere
    }

    //inizializza la board ogni volta che viene chiamata, in base al numero di giocatori
    //prende le tiles in modo randomico dalla lista di tiles
    //rimuove quindi le tiles prese dalla lista (per non avere ripetizioni)
    public void fillBoard(int numPlayers){

    }

    //ritorna true sse la board è da riempiere nuovamente, oppure è vuota (inizio partita)
    public boolean isBoardEmpty(){
        boolean value = true;
        for(int i=0; i<numCols-1; i++){
            for(int j=0; i<numRows-1; j++){
                //se l'oggetto è accessbile e non vuoto
                if(!board[i][j].equals(type.NOT_ACCESSIBLE) && board[i][j] != null){
                    //se l'oggetto nella colonna adiacente o nella riga sottostante è accessibile e non vuoto
                    //metto value=false (posso prendere queste tiles)
                    if((!board[i+1][j].equals(type.NOT_ACCESSIBLE) && board[i+1][j] != null) ||
                            (!board[i][j+1].equals(type.NOT_ACCESSIBLE) && board[i][j+1] != null)){
                        value=false;
                    }
                }
            }
        }
        return value;
    }

    //ritorna una lista di tiles (in ordine da start ad end)
    //controlla che le tiles da start a end siano effetivamente "prendibili" dalla board (throws illegalArgumentexp (?))
    //rimuove le tiles selezionate dalla board sostituendole con un placeholder (magari null)

    //il controllo sullo spazio nella libreria del giocatore si fa direttamente alla scelta di start e end
    // prima di chiamare questa funzione
    public List<Tile> getTiles(Position start, Position end){
        List<Tile> result = null;
        if(board[start.x][start.y] != null && !board[start.x][start.y].equals(type.NOT_ACCESSIBLE)){
            if(board[end.x][end.y] != null && !board[end.x][end.y].equals(type.NOT_ACCESSIBLE)){
                result.add(board[start.x][start.y]);
            }
        }

        return null;
    }
}
