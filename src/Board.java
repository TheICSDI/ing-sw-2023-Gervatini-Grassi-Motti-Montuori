import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import Tile.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Board {
    private final int numCols = 9;
    private final int numRows = 9;
    private final int numTiles = 120;
    public Tile body[][];
    //diventerà una mappa
    private List<Tile> tilesList[];
    public Tile[][] body;

    private List<Tile>[] tilesList;
    //per riempire correttamente la board posso fare una map che mappa ogni coppai di indici (casella della board) al
    //corrispondente numero di giocatori.
    //la map deve essere final e private, ed essere inizializzata una volta soltanto

    //alcune caselle saranno vuote di default, magari un placeholder che identifica casella non accessibile
    //visto che la board non è quadrata

    //forse è meglio utilizzare una map anche per la board
    //map(K, T, N), con
    //      K (key) l'indice numerato (da noi) della plancia
    //      T (tiles) il contenuto effettivo della plancia
    //      N (numPlayers) il numero di giocatori necessari per "riempire" quell'indice
    // oppure due mappe, una per le tiles e una per il numero di giocatori
    public Board(){
        body = new Tile[numCols][numRows];
        tilesList = new ArrayList<>[numTiles];
        //nel costruttore devo già riempire tilesList con tutte le possibili tiles del gioco
        //magari da un file con tutte le possbili tessere
        boardParser();
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
            for(int j=0; j<numRows-1; j++){
                //if the object in the board is accessible and not null
                if(!body[i][j].getCategory().equals(type.NOT_ACCESSIBLE) && board[i][j] != null){
                    //se l'oggetto nella colonna adiacente o nella riga sottostante è accessibile e non vuoto
                    //metto value=false (posso prendere queste tiles)
                    if((!body[i+1][j].getCategory().equals(type.NOT_ACCESSIBLE) && body[i+1][j] != null) ||
                            (!body[i][j+1].getCategory().equals(type.NOT_ACCESSIBLE) && body[i][j+1] != null)){
                        value=false;
                    }
                }
            }
        }
        return value;
    }

    //ritorna una lista di tiles (in ordine da start ad end)
    //controlla che le tiles da start a end siano effetivamente "prendibili" dalla board (throws illegalArgumentexp (?))
    //rimuove le tiles selezionate dalla board
    public List<Tile> getTiles(Position start, Position end){
        return null;
    }

    private void boardParser(){
        JSONParser parser = new JSONParser();
        JSONArray board_na_File = null;

        try
        {
            FileInputStream pathFile = new FileInputStream("JSON/board_na.json");
            board_na_File = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        for(int index=0; index < board_na_File.size(); index++) {
            JSONObject tmp = (JSONObject) board_na_File.get(index);

            int indexRow = Integer.parseInt(tmp.get("x").toString());
            int indexCol = Integer.parseInt(tmp.get("y").toString());
            this.body[indexRow][indexCol] = new Tile(tmp.get("type").toString());
        }

        //in base al numero di giocatori li metterli inaccessibili
    }
}
