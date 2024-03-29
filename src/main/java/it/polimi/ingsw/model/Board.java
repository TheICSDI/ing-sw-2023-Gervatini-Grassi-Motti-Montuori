package it.polimi.ingsw.model;

import java.io.*;
import java.util.*;

import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/** Represent the board of a game. It has to be declared in each game.
 * @author Caterina Motti. */
public class Board {
    private final int numCols = 9;
    private final int numRows = 9;
    public Tile[][] board;
    private final List<String> tilesList;

    /** Create a board for a specified number of players.
     * It initializes the board with empty tiles.
     * It initializes tilesList with all the possible tiles.
     * @param numPlayers number of players
     */
    public Board(int numPlayers){
        board = new Tile[numCols][numRows];
        tilesList = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                this.board[i][j] = new Tile("empty" ,0);
            }
        }
        boardParser(numPlayers);
        fillTilesList();
    }

    /** Fills the empty cells of the board, if accessible.
     * It randomically takes the tiles from tilesList, add them to the board and remove them from the list.
     * In this way there is no possibility for a tile to be used two times in the same game. */
    public void fillBoard(){
        for(int i=0; i < numCols; i++) {
            for (int j=0; j < numRows; j++) {
                if(board[i][j].getCategory().equals(type.EMPTY)){
                    //Shuffle tilesList
                    Collections.shuffle(tilesList);
                    //Put the first element type in board (randomically)
                    int imageType= (new Random().nextInt(3)) + 1 ;
                    this.board[i][j] = new Tile(tilesList.get(0),imageType);
                    this.tilesList.remove(0);
                }
            }
        }
    }

    /** Determine if the board has to be filled.
     * @return  true only if the board has to be filled, false otherwise.
     */
    public boolean isBoardEmpty(){
        for(int i=0; i < numCols - 1; i++){
            for(int j=0; j < numRows - 1; j++){
                //if the object in the board is accessible and not null
                if (!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE) && !board[i][j].getCategory().equals(type.EMPTY)) {
                    //if the object in the right column or in the row below is accessible and not null then it means that
                    //it is possible to "take" these tiles
                    if ((!board[i + 1][j].getCategory().equals(type.NOT_ACCESSIBLE) && !board[i + 1][j].getCategory().equals(type.EMPTY))
                            || (!board[i][j + 1].getCategory().equals(type.NOT_ACCESSIBLE) && !board[i][j + 1].getCategory().equals(type.EMPTY))) {
                        //in this case the board does not need to get filled, so it return false
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /** Parse board_na.JSON. It initializes the board by making not accessible some cells according to the
     * number of players that will use the board.
     * @param numPlayers number of players, used to determine if an element of the board should be accessible or not
     */
    private void boardParser(int numPlayers){
        JSONParser parser = new JSONParser();
        JSONArray board_na_File = null;
        try {
            //FileInputStream pathFile = new FileInputStream("JSON/board_na.json");
            board_na_File = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/JSON/board_na.json"))));
        } catch (ParseException | IOException ignored) {}

        for(int index=0; index<board_na_File.size(); index++) {
            JSONObject tmp = (JSONObject) board_na_File.get(index);

            int indexX = Integer.parseInt(tmp.get("x").toString());
            int indexY = Integer.parseInt(tmp.get("y").toString());
            int n = Integer.parseInt(tmp.get("type").toString());
            if(n > numPlayers) {
                this.board[indexX][indexY] = new Tile("not_accessible",0);
            }
        }
    }

    /** Fill tilesList with all the possible tiles of the game: 22 tiles of each type (except not_accessible and empty). */
    private void fillTilesList(){
        for(type t: type.values()){
            if(!t.equals(type.NOT_ACCESSIBLE) && !t.equals(type.EMPTY)) {
                for (int i = 0; i < 22; i++) {
                    this.tilesList.add(t.toString());
                }
            }
        }
    }

    /** Returns a set of positions in which the tiles can be picked from the player.
     * A tile can be picked if it has an empty or not accessible side on the board.
     * @return a set of available positions.
     */
    public Set<Position> AvailableTiles(){
        Set<Position> Available = new HashSet<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                if(!board[i][j].getCategory().equals(type.EMPTY) && !board[i][j].getCategory().equals(type.NOT_ACCESSIBLE)) {
                    try{
                        if(board[i-1][j].getCategory().equals(type.EMPTY) || board[i-1][j].getCategory().equals(type.NOT_ACCESSIBLE)){
                            Available.add(new Position(i,j));
                        }
                    } catch (IndexOutOfBoundsException ignored){}
                    try{
                        if(board[i][j-1].getCategory().equals(type.EMPTY) || board[i][j-1].getCategory().equals(type.NOT_ACCESSIBLE)){
                            Available.add(new Position(i,j));
                        }
                    } catch (IndexOutOfBoundsException ignored){}
                    try{
                        if(board[i+1][j].getCategory().equals(type.EMPTY) || board[i+1][j].getCategory().equals(type.NOT_ACCESSIBLE)){
                            Available.add(new Position(i,j));
                        }
                    } catch (IndexOutOfBoundsException ignored){}
                    try{
                        if(board[i][j+1].getCategory().equals(type.EMPTY) || board[i][j+1].getCategory().equals(type.NOT_ACCESSIBLE)){
                            Available.add(new Position(i,j));
                        }
                    } catch (IndexOutOfBoundsException ignored){}
                }
            }
        }
        return Available;
    }

    /** Remove tiles from the board indexed by the given positions.
     * @param ToRemove a set of positions. */
    public void RemoveTiles(List<Position> ToRemove) {
        for (Position p: ToRemove) {
            board[p.getX()][p.getY()] = new Tile("empty",0);
        }
    }

    /** It copies the current board into a given one passed by parameter. */
    public void cloneBoard(Board newBoard){
        for (int i = 0; i < this.numRows; i++) {
            for (int j = 0; j < this.numCols; j++) {
                this.board[i][j] = newBoard.board[i][j];
            }
        }
    }

    /** Gets the tile on the position passed by parameter. */
    public Tile getTile(Position p){
        return this.board[p.getX()][p.getY()];
    }

    /** Gets the number of columns of the board */
    public int getNumCols() {
        return numCols;
    }

    /** Gets the number of rows of the board */
    public int getNumRows() {
        return numRows;
    }
}
