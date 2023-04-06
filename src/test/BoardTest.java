package test;

import main.java.it.polimi.ingsw.model.Board;
import main.java.it.polimi.ingsw.model.Tile.Tile;
import main.java.it.polimi.ingsw.model.Tile.type;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
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

    @org.junit.jupiter.api.Test
    void isBoardEmpty() {
        Board b = new Board(4);
        assertTrue(b.isBoardEmpty());

        //Limit case: only two near tiles
        b.board[1][1] = new Tile("cats");
        b.board[1][2] = new Tile("cats");
        assertFalse(b.isBoardEmpty());

    }

    @org.junit.jupiter.api.Test
    void availableTiles() {
        Board b = new Board(4);
        //No available tiles: the board is empty
        assertTrue(b.AvailableTiles().isEmpty());

        //All tiles are available: the board is full
        b.fillBoard();
        assertFalse(b.AvailableTiles().isEmpty());

        //Limit case: the board is filled with many tiles, but all of them are unavailable to be taken
        //scacchiera utile anche pee fill board
    }

    @org.junit.jupiter.api.Test
    void removeTiles() {
    }
}