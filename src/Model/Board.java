package Model;
/** Represent the board of a game. It has to be declared in each game.
 * @author Motti Caterina
 */
import java.io.*;
import java.util.*;
import Model.Tile.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Board {
    private final int numCols = 9;
    private final int numRows = 9;
    public Tile[][] board;
    private final List<Tile> tilesList;

    /** Gets the number of columns of the board */
    public int getNumCols() {
        return numCols;
    }

    /** Gets the number of rows of the board */
    public int getNumRows() {
        return numRows;
    }

    /** Create a board for a specified number of players.
     *
     * @param numPlayers number of players
     */
    public Board(int numPlayers){
        board = new Tile[numCols][numRows];
        tilesList = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                this.board[i][j]=new Tile("empty");
            }
        }
        boardParser(numPlayers);
        fillTilesList();
    }

    /**
     * Fills the empty cells of the board, if accessible.
     * It randomically takes the tiles from tilesList, add them to the board and remove them from the list.
     * In this way there is no possibility for a tile to be used two times in the same game.
     */
    public void fillBoard(){
        Random r = new Random();
        for(int i=0; i<numCols; i++) {
            for (int j=0; j<numRows; j++) {
                if(!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE) && board[i][j].getCategory().equals(type.EMPTY)){
                    //Generate a random int between 0 (inclusive) and tilesList.size() (exclusive)
                    int k = r.nextInt(0, tilesList.size());
                    board[i][j] = tilesList.get(k);
                    tilesList.remove(k);
                }
            }
        }
    }

    /**
     * Determine if the board has to be filled.
     * @return  true only if the board has to be filled, false otherwise.
     */
    public boolean isBoardEmpty(){
        for(int i=0; i<numCols; i++){
            for(int j=0; j<numRows; j++){
                //if the object in the board is accessible and not null
                try {
                    if (!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE) && !board[i][j].getCategory().equals(type.EMPTY)) {
                        //if the object in the right column or in the row below is accessible and not null then it means that
                        //it is possible to "take" these tiles
                        if ((!board[i + 1][j].getCategory().equals(type.NOT_ACCESSIBLE) && !board[i + 1][j].getCategory().equals(type.EMPTY)) ||
                                (!board[i][j + 1].getCategory().equals(type.NOT_ACCESSIBLE) && !board[i][j + 1].getCategory().equals(type.EMPTY))) {
                            //in this case the board does not need to get filled, so it return false
                            return false;
                        }
                    }
                } catch (IndexOutOfBoundsException ignored){}// Not sure if it works 100% of the times
            }
        }
        return true;
    }

    /** Returns a set of tiles that the player has selected, if it is a legal move.
     * It removes those tiles from the board.
     *
     * @param start indicates the starting point on the board from which the player wants to take the tiles.
     * @param end indicates the ending point on the board until which the player wants to take the tiles.
     * @return a set of tiles.
     */
    public Set<Tile> getTiles(Position start, Position end){
        return null;
    }

    /** Parse board_na.jason. It initializes the board by making not accessible some cells according to the
     * number of players that will use the board.
     *
     * @param numPlayers number of players, used to determine if an element of the board should be accessible or not
     */
    private void boardParser(int numPlayers){
        JSONParser parser = new JSONParser();
        JSONArray board_na_File = null;

        try
        {
            FileInputStream pathFile = new FileInputStream("JSON/board_na.json");
            board_na_File = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for(int index=0; index<board_na_File.size(); index++) {
            JSONObject tmp = (JSONObject) board_na_File.get(index);

            int indexRow = Integer.parseInt(tmp.get("x").toString());
            int indexCol = Integer.parseInt(tmp.get("y").toString());
            int n = Integer.parseInt(tmp.get("type").toString());
            if(n < numPlayers) {
                this.board[indexRow][indexCol] = new Tile("not_accessible");
            }
        }
    }

    /** Fill tilesList with all the possible tiles of the game: 22 tiles of each type (except not_accessible and empty). */
    private void fillTilesList(){
        for(type t: type.values()){
            if(!t.equals(type.NOT_ACCESSIBLE) && !t.equals(type.EMPTY)) {
                for (int i = 0; i < 22; i++) {
                    Tile tmp = new Tile(t.toString());
                    this.tilesList.add(tmp);
                }
            }
        }
    }

    public Set<Position> AvailableTiles(){
        Set<Position> Available=new HashSet<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                try{
                    if(!board[i][j].getCategory().equals(type.EMPTY)  //Condizione orribile ma stacce non vedo altro
                       && !board[i][j].getCategory().equals(type.NOT_ACCESSIBLE)){
                        if(board[i-1][j].getCategory().equals(type.EMPTY) ||
                                board[i+1][j].getCategory().equals(type.EMPTY) ||
                                board[i][j+1].getCategory().equals(type.EMPTY) ||
                                board[i][j-1].getCategory().equals(type.EMPTY) ||
                                board[i-1][j].getCategory().equals(type.NOT_ACCESSIBLE) ||
                                board[i+1][j].getCategory().equals(type.NOT_ACCESSIBLE) ||
                                board[i][j+1].getCategory().equals(type.NOT_ACCESSIBLE) ||
                                board[i][j-1].getCategory().equals(type.NOT_ACCESSIBLE)){
                            Available.add(new Position(i,j));
                            }
                        }
                    }catch(IndexOutOfBoundsException ignored){}
            }

        }
        return Available;
    }

    public void RemoveTiles(Set<Position> ToRemove){
        for (Position p:
             ToRemove) {
            board[p.getX()][p.getY()]=new Tile("empty");
        }
    }
}
