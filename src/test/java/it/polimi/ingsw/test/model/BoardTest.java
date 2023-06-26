/** Tests for class Board.java.
 * @author Caterina Motti.
 */
package it.polimi.ingsw.test.model;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    /** Parser for board_test.json. It returns a board with tiles shaped like a chessboard.
     * In this way every tile has all four sides empty. */
    private Board Parser(){
        Board b = new Board(4);
        JSONParser parser = new JSONParser();
        JSONArray board_test_File = null;

        try {
            //FileInputStream pathFile = new FileInputStream("JSON/board_test.json");
            board_test_File = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/JSON/board_test.json"))));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for(int index = 0; index < board_test_File.size(); index++) {
            JSONObject tmp = (JSONObject) board_test_File.get(index);

            int indexX = Integer.parseInt(tmp.get("x").toString());
            int indexY = Integer.parseInt(tmp.get("y").toString());
            String t = tmp.get("type").toString();
            b.board[indexX][indexY] = new Tile(t,1);
        }
        return b;
    }

    @Test
    void fillBoard() {
        Board b = new Board(4);
        for (int i = 0; i < b.getNumCols(); i++) {
            for (int j = 0; j < b.getNumRows(); j++) {
                assertNotNull(b.board[i][j]);
                assertNotNull(b.board[i][j].getCategory());
            }
        }
        b.fillBoard();
        //After calling the method there is no empty tile in board or null element
        for (int i = 0; i < b.getNumCols(); i++) {
            for (int j = 0; j < b.getNumRows(); j++) {
                assertNotEquals(b.board[i][j].getCategory(), type.EMPTY);
                assertNotNull(b.board[i][j]);
                assertNotNull(b.board[i][j].getCategory());
            }
        }
    }

    @Test
    void isBoardEmpty() {
        Board b = new Board(4);
        assertTrue(b.isBoardEmpty());

        //Limit case: only one tile
        b.board[5][5] = new Tile("cats",1);
        assertTrue(b.isBoardEmpty());

        //Limit case: only two near tiles
        b.board[5][6] = new Tile("cats",1);
        assertFalse(b.isBoardEmpty());

        //Limit case: the board is full of tiles, but all of them have empty sides
        b = Parser();
        assertTrue(b.isBoardEmpty());
    }

    @Test
    void availableTiles() {
        Board b = new Board(4);
        //No available tiles: the board is empty
        assertTrue(b.AvailableTiles().isEmpty());

        //Some tiles are available
        b.board[5][5] = new Tile("cats",1);
        b.board[5][6] = new Tile("cats",1);
        assertFalse(b.AvailableTiles().isEmpty());
        assertTrue(b.AvailableTiles().contains(new Position(5, 5)));
        assertTrue(b.AvailableTiles().contains(new Position(5, 6)));

        //All tiles are available: the board is full
        b.fillBoard();
        assertFalse(b.AvailableTiles().isEmpty());
    }

    @Test
    void removeTiles() {
        Board b = new Board(4);
        b.fillBoard();

        ArrayList<Position> remove = new ArrayList<>();
        //Remove set passed by parameter is empty: it is not removing anything
        b.RemoveTiles(remove);
        for (int i = 0; i < b.getNumCols(); i++) {
            for (int j = 0; j < b.getNumRows(); j++) {
                assertNotEquals(b.board[i][j].getCategory(), type.EMPTY);
            }
        }

        //Adding some position to be removed
        Position p1 = new Position(0,4);
        Position p2 = new Position(0,5);
        remove.add(p1);
        remove.add(p2);

        //After removing the selected tiles, the corresponding positions in board are empty
        b.RemoveTiles(remove);
        assertEquals(b.getTile(p1).getCategory(), type.EMPTY);
        assertEquals(b.getTile(p2).getCategory(), type.EMPTY);
    }
}