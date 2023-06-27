/** Tests for class Player.java.
 * @author Caterina Motti.
 */
package it.polimi.ingsw.test.model;

import it.polimi.ingsw.controller.connectionType;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player p1 = new Player("CLR");
    Tile t1 = new Tile("cats");
    Tile t2 = new Tile("plants");
    Tile t3 = new Tile("books");
    PrintWriter out = new PrintWriter(System.out, true);

    /** Parser for shelf_test.json. It returns a player with a full shelf. */
    private Player Parser(){
        Player p = new Player("Jhonny");
        p.setConnected(true);
        connectionType type = new connectionType(true, out, null);
        serverController.connections.put(p.getNickname(), type);
        gameController.allPlayers.put(p.getNickname(), p);
        JSONParser parser = new JSONParser();
        JSONArray shelf_test_File = null;

        try {
            //FileInputStream pathFile = new FileInputStream("JSON/shelf_test.json");
            shelf_test_File = (JSONArray) parser.parse(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/JSON/shelf_test.json"))));

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
    void orderTiles() throws RemoteException {
        List<Tile> selected = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        List<Tile> ordered;
        gameController controller = new gameController();
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

        p1 = Parser();
        //Mismatch between the inputs
        //case 1: more element in order
        order.add(4);
        Throwable ex1 = assertThrows(InputMismatchException.class, () -> {
            List<Tile> o = p1.orderTiles(selected, order);
        });

        //case 2: more element in selected
        order.remove(0);
        order.remove(1);
        Throwable ex2 = assertThrows(InputMismatchException.class, () -> {
            List<Tile> o = p1.orderTiles(selected, order);
        });
    }

    @Test
    void InsertInShelf() {
        List<Tile> toInsert = new ArrayList<>();
        Player p = new Player("Sof");
        int col = 2;

        //Empty toInsert: it inserts anything in the shelf
        p.insertInShelf(toInsert, col);
        for (int i = 0; i < p.getNumRows(); i++) {
            for (int j = 0; j < p.getNumCols(); j++) {
                assertEquals(type.EMPTY, p.getShelf()[i][j].getCategory());
            }
        }

        //Add some tiles in toInsert
        toInsert.add(t1);
        toInsert.add(t2);
        toInsert.add(t3);
        p.insertInShelf(toInsert, col);

        for (int i = 0; i < p.getNumRows(); i++) {
            for (int j = 0; j < p.getNumCols(); j++) {
                if(j != col){
                    //All other tiles are unchanged
                    assertEquals(type.EMPTY, p.getShelf()[i][j].getCategory());
                } else {
                    if(i <= p.getNumRows() - toInsert.size() - 1){
                        //All other tiles are unchanged
                        assertEquals(type.EMPTY, p.getShelf()[i][j].getCategory());
                    } else {
                        assertNotEquals(type.EMPTY, p.getShelf()[i][j].getCategory());
                        //The tiles are inserted so that t1 is at the "bottom" and t3 is at the "top"
                        assertEquals(toInsert.get(p.getNumRows() - 1 - i).getCategory(), p.getShelf()[i][j].getCategory());
                    }
                }
            }
        }

        //Wrong column selected
        //case 1: column out of bound
        Throwable ex1 = assertThrows(InputMismatchException.class, () -> p1.insertInShelf(toInsert, 8));
        assertEquals("Selected column is out of bound!", ex1.getMessage());
        //case 2: column is full
        p1 = Parser();
        Throwable ex2 = assertThrows(InputMismatchException.class, () -> p1.insertInShelf(toInsert, col));
        assertEquals("Selected column has no enough space!", ex2.getMessage());
    }

    @Test
    void pickTiles() throws RemoteException {
        Board b = new Board(4);
        b.fillBoard();

        //Add some available position
        List<Position> chosen = new ArrayList<>();
        Position pos1 = new Position(0,4);
        Position pos2 = new Position(0, 5);
        chosen.add(pos1);
        chosen.add(pos2);

        List<Tile> expectedTiles = new ArrayList<>();
        expectedTiles.add(b.getTile(pos1));
        expectedTiles.add(b.getTile(pos2));
        List<Tile> chosenTiles = p1.pickTiles(chosen, b, p1);
        assertEquals(type.EMPTY, b.getTile(pos1).getCategory());
        assertEquals(type.EMPTY, b.getTile(pos2).getCategory());
        assertEquals(expectedTiles, chosenTiles);

        //Not enough space in shelf: method pick tiles return an empty array
        p1 = Parser();
        p1.getShelf()[1][1] = new Tile("empty");
        chosenTiles = p1.pickTiles(chosen, b, p1);
        assertTrue(chosenTiles.isEmpty());
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

    @Test
    public void testMaxSpaceInShelf() {
        //Player with an empty shelf
        Player p = new Player("Lola");
        int max = p.maxSpaceInShelf();
        assertEquals(3, max);

        //Player with a full shelf
        p = Parser();
        max = p.maxSpaceInShelf();
        assertEquals(0, max);

        //Remove a single tile from the shelf
        p.getShelf()[0][1] = new Tile("empty");
        max = p.maxSpaceInShelf();
        assertEquals(1, max);

        //Remove another tile from the shelf
        p.getShelf()[1][1] = new Tile("empty");
        max = p.maxSpaceInShelf();
        assertEquals(2, max);

        //Remove another tile from the shelf
        p.getShelf()[2][1] = new Tile("empty");
        max = p.maxSpaceInShelf();
        assertEquals(3, max);
    }

    @Test
    public void calculatePoints() {
        //Empty shelf
        Player p = new Player("Mario");
        p.setPersonalCard(new PersonalCard(0));
        p.calculatePCPoint();
        assertEquals(0, p.getTotalPoints());

        p.getShelf()[5][2] = new Tile("trophies");
        p.calculatePCPoint();
        assertEquals(1, p.getTotalPoints());

        p.setTotalPoints(0);
        p.getShelf()[3][1] = new Tile("games");
        p.calculatePCPoint();
        assertEquals(2, p.getTotalPoints());

        p.setTotalPoints(0);
        p.getShelf()[2][3] = new Tile("books");
        p.calculatePCPoint();
        assertEquals(4, p.getTotalPoints());

        p.setTotalPoints(0);
        p.getShelf()[1][4] = new Tile("cats");
        p.calculatePCPoint();
        assertEquals(6, p.getTotalPoints());

        p.setTotalPoints(0);
        p.getShelf()[0][2] = new Tile("frames");
        p.calculatePCPoint();
        assertEquals(9, p.getTotalPoints());

        p.setTotalPoints(0);
        p.getShelf()[0][0] = new Tile("plants");
        p.calculatePCPoint();
        assertEquals(12, p.getTotalPoints());
    }
}