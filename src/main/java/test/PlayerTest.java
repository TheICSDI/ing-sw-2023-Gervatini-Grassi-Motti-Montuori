/** Tests for class Player.java.
 * @author Caterina Motti.
 */
package test;

import it.polimi.ingsw.exceptions.InvalidColumnException;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player p1 = new Player("CLR");
    Tile[][] s = p1.getShelf();
    Tile t1 = new Tile("cats");
    Tile t2 = new Tile("plants");
    Tile t3 = new Tile("books");

    /** Parser for shelf_test.json. It returns a player with a full shelf. */
    private Player Parser(){
        Player p = new Player("Jhonny");
        JSONParser parser = new JSONParser();
        JSONArray shelf_test_File = null;

        try {
            FileInputStream pathFile = new FileInputStream("JSON/shelf_test.json");
            shelf_test_File = (JSONArray) parser.parse(new InputStreamReader(pathFile));

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        for(int index = 0; index < shelf_test_File.size(); index++) {
            JSONObject tmp = (JSONObject) shelf_test_File.get(index);

            int indexX = Integer.parseInt(tmp.get("x").toString());
            int indexY = Integer.parseInt(tmp.get("y").toString());
            String t = tmp.get("type").toString();
            p.getShelf()[indexX][indexY] = new Tile(t);
        }
        return p;
    }

    @Test
    void orderTiles() {
        List<Tile> selected = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        List<Tile> ordered;
        //Empty selected and order
        ordered = p1.orderTiles(selected, order);
        assertTrue(ordered.isEmpty());

        //Add some tiles in selected
        selected.add(t1);
        selected.add(t2);
        selected.add(t3);
        //Specify an order (inversion)
        order.add(3);
        order.add(2);
        order.add(1);

        ordered = p1.orderTiles(selected, order);

        for (int i = 0; i < selected.size(); i++) {
            assertEquals(selected.get(order.get(i) - 1), ordered.get(i));
        }

        //Mismatch between the inputs
        //case 1: more element in order
        order.add(4);
        Throwable ex1 = assertThrows(InputMismatchException.class, () -> {
            List<Tile> o = p1.orderTiles(selected, order);
        });
        assertEquals("The selected order is wrong, you have selected less tiles!", ex1.getMessage());

        //case 2: more element in selected
        order.remove(0);
        order.remove(1);
        Throwable ex2 = assertThrows(InputMismatchException.class, () -> {
            List<Tile> o = p1.orderTiles(selected, order);
        });
        assertEquals("The selected order is wrong, you have selected more tiles!", ex2.getMessage());
    }


    @Test
    void InsertInShelf() throws InvalidColumnException {
        List<Tile> toInsert = new ArrayList<>();
        int col = 2;

        //Empty toInsert: it inserts anything in the shelf
        p1.insertInShelf(toInsert, col);
        for (int i = 0; i < p1.getNumRows(); i++) {
            for (int j = 0; j < p1.getNumCols(); j++) {
                assertEquals(type.EMPTY, s[i][j].getCategory());
            }
        }

        //Add some tiles in toInsert
        toInsert.add(t1);
        toInsert.add(t2);
        toInsert.add(t3);
        p1.insertInShelf(toInsert, col);

        for (int i = 0; i < p1.getNumRows(); i++) {
            for (int j = 0; j < p1.getNumCols(); j++) {
                if(j != col){
                    //All other tiles are unchanged
                    assertEquals(type.EMPTY, s[i][j].getCategory());
                } else {
                    if(i <= p1.getNumRows() - toInsert.size() - 1){
                        //All other tiles are unchanged
                        assertEquals(type.EMPTY, s[i][j].getCategory());
                    } else {
                        assertNotEquals(type.EMPTY, s[i][j].getCategory());
                        //The tiles are inserted so that t1 is at the "bottom" and t3 is at the "top"
                        assertEquals(toInsert.get(p1.getNumRows() - 1 - i).getCategory(), s[i][j].getCategory());
                    }
                }
            }
        }

        //Wrong column selected
        //case 1: column out of bound
        Throwable ex1 = assertThrows(InvalidColumnException.class, () -> p1.insertInShelf(toInsert, 8));
        assertEquals("Selected column is out of bound!", ex1.getMessage());
        //case 2: column is full
        p1 = Parser();
        Throwable ex2 = assertThrows(InvalidColumnException.class, () -> p1.insertInShelf(toInsert, col));
        assertEquals("Selected column has no enough space!", ex2.getMessage());
    }

    @Test
    void pickTiles() throws InvalidPositionException {
        Board b = new Board(4);
        b.fillBoard();

        //Add some available position
        Set<Position> chosen = new HashSet<>();
        Position pos1 = new Position(0,4);
        Position pos2 = new Position(0, 5);
        chosen.add(pos1);
        chosen.add(pos2);

        List<Tile> expectedTiles = new ArrayList<>();
        expectedTiles.add(b.getTile(pos1));
        expectedTiles.add(b.getTile(pos2));
        List<Tile> chosenTiles = p1.pickTiles(chosen, b);
        assertEquals(type.EMPTY, b.getTile(pos1).getCategory());
        assertEquals(type.EMPTY, b.getTile(pos2).getCategory());
        assertEquals(expectedTiles, chosenTiles);

        //Exception
        //Chosen position by the player are not available to be taken
        Position pos3 = new Position(0,0);
        chosen.add(pos3);
        Throwable ex = assertThrows(InputMismatchException.class, () ->
                p1.pickTiles(chosen, b));
        assertEquals("The chosen tiles are not available to be taken!", ex.getMessage());
    }

    @Test
    void calculateGeneralPoints() {
        //Empty shelf, so no points are assigned
        p1.calculateGeneralPoints();
        assertEquals(0, p1.getTotalPoints());

        //Full shelf with 2 cluster of 3 tiles (4pt), 1 cluster of 4 tiles (3pt) and 1 cluster of 6 tiles (6pt)
        //for a total of 4 + 3 + 8 = 15 pt
        p1 = Parser();
        for (int i = 0; i < p1.getNumRows(); i++) {
            for (int j = 0; j < p1.getNumCols(); j++) {
                assertNotEquals(type.EMPTY, p1.getShelf()[i][j].getCategory());
            }
        }
        p1.calculateGeneralPoints();
        assertEquals(15, p1.getTotalPoints());
    }

    @Test
    void isShelfFull(){
        //The shelf is empty
        assertFalse(p1.isShelfFull());

        //The shelf has some tiles but is not full
        p1.getShelf()[5][0] = t1;
        p1.getShelf()[4][0] = t2;
        assertFalse(p1.isShelfFull());

        //The shelf is completely full
        p1 = Parser();
        assertTrue(p1.isShelfFull());
    }
}