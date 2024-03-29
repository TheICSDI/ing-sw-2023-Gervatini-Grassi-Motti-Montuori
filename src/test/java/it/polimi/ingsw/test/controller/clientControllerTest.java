package it.polimi.ingsw.test.controller;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.CreateLobbyMessage;
import it.polimi.ingsw.network.messages.GeneralMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for class clientController.java.
 * @author Caterina Motti.
 */
class clientControllerTest {
    clientController controller = new clientController();
    Player p1 = new Player("Mario");
    Player p2 = new Player("Luigi");
    clientController CC1 = new clientController(p1.getNickname());
    clientController CC2 = new clientController(p2.getNickname());

    @Test
    void checkMessageShape() {
        String mex;
        GeneralMessage rsp;

        //Case: CREATELOBBY
        mex = "createlobby 2";
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.CREATELOBBY, rsp.getAction());
        assertEquals(2, ((CreateLobbyMessage) rsp).getLimit());
        Lobby l = new Lobby(p1, 2);
        CC1.setIdLobby(l.lobbyId);

        mex = "createlobby";
        rsp = controller.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        //Case: SHOWLOBBY
        mex = "showlobby";
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SHOWLOBBY, rsp.getAction());

        //Case: JOINLOBBY
        mex = "joinlobby";
        rsp = CC2.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "joinlobby " + l.lobbyId;
        rsp = CC2.checkMessageShape(mex);
        assertEquals(Action.JOINLOBBY, rsp.getAction());
        assertEquals(l.lobbyId, rsp.getIdLobby());
        l.Join(p2);
        CC2.setIdLobby(l.lobbyId);

        //Case: STARTGAME
        mex = "startgame";
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.STARTGAME, rsp.getAction());
        Game g = new Game(l.Players, new gameController());
        gameController.allGames.put(g.id, g);
        CC1.setIdGame(g.id);
        CC2.setIdGame(g.id);

        //Case: PT
        mex = "pt 3 1 4 1";
        rsp = controller.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction()); //Error: controller is not bind with a player in a game

        mex = "pt 3"; //Wrong parameters
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "pt 3 2 4 1"; //Not adjacent tiles
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "pt 3 1 4 1";
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.PT, rsp.getAction());

        //Case: SO
        mex = "SO 1 2";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "SO"; //Wrong parameter
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "SO 4 3"; //Wrong order
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "SO 1 2";
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SO, rsp.getAction());

        //Case: SC
        mex = "SC 3";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "SC"; //Wrong parameter
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "SC 8"; //Wrong parameter
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "SC 3";
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SC, rsp.getAction());

        //Case: SHOWPERSONAL
        mex = "showpersonal";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SHOWPERSONAL, rsp.getAction());

        //Case: SHOWCOMMONS
        mex = "showcommons";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SHOWCOMMONS, rsp.getAction());

        //Case: SHOWOTHERS
        mex = "showothers";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SHOWOTHERS, rsp.getAction());

        //Case: SHOWSHELF
        mex = "showshelf";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SHOWSHELF, rsp.getAction());

        //Case: SHOWBOARD
        mex = "showboard";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.SHOWBOARD, rsp.getAction());

        //Case: C
        mex = "C Mario ciao";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex); //Error: CC1 is "Mario"
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "C Luigi"; //Blank message
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "C Luigi ciao"; //Blank message
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.C, rsp.getAction());

        //Case: CA
        mex = "CA ciao";
        rsp = controller.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.ERROR, rsp.getAction());

        rsp = CC1.checkMessageShape(mex); //Error: controller is not bind with a player in a game
        assertEquals(Action.CA, rsp.getAction());

        mex = "CA"; //Blank message
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());

        mex = "lala"; //Error: wrong format
        rsp = CC1.checkMessageShape(mex);
        assertEquals(Action.ERROR, rsp.getAction());
    }

    @Test
    void acceptableOrder() {
        //Case 1: empty order
        ArrayList<Integer> order = new ArrayList<>();
        assertFalse(controller.acceptableOrder(order));

        //Case 2: acceptable order
        order.add(1);
        order.add(2);
        order.add(3);
        assertTrue(controller.acceptableOrder(order));

        //Case 3: wrong order
        order.clear();
        order.add(2);
        order.add(3);
        assertFalse(controller.acceptableOrder(order));

        order.clear();
        order.add(1);
        order.add(3);
        assertFalse(controller.acceptableOrder(order));

        order.clear();
        order.add(1);
        order.add(2);
        order.add(4);
        assertFalse(controller.acceptableOrder(order));
    }

    @Test
    void isStraightLineTiles() {
        //Case 1: no tiles selected
        ArrayList<Position> ptX = new ArrayList<>();
        ArrayList<Position> ptY = new ArrayList<>();
        assertFalse(controller.isStraightLineTiles(ptX));
        assertFalse(controller.isStraightLineTiles(ptY));

        Position p1 = new Position(3, 1);
        Position p2 = new Position(3, 2);
        Position p3 = new Position(3, 3);
        Position p4 = new Position(4, 1);
        Position p5 = new Position(5, 1);
        Position p6 = new Position(6, 1);
        Position p7 = new Position(3, 6);

        //Case 1: one tile
        ptX.add(p1);
        assertTrue(controller.isStraightLineTiles(ptX));

        //Case 2: two tiles
        ptX.add(p2);
        assertTrue(controller.isStraightLineTiles(ptX));

        //Case 3: three tiles
        ptX.add(p3);
        ptY.add(p4);
        ptY.add(p6);
        ptY.add(p5);
        assertTrue(controller.isStraightLineTiles(ptX));
        assertTrue(controller.isStraightLineTiles(ptY));

        //Case 4:
        ptX.clear();
        ptX.add(p1);
        ptX.add(p3);
        ptX.add(p2);
        assertTrue(controller.isStraightLineTiles(ptX));

        //Case 5:
        ptY.clear();
        ptY.add(p1);
        ptY.add(p4);
        ptY.add(p5);
        assertTrue(controller.isStraightLineTiles(ptY));

        //Case 6:
        ptX.clear();
        ptX.add(p2);
        ptX.add(p3);
        ptX.add(p1);
        assertTrue(controller.isStraightLineTiles(ptX));

        //Case 7: not adjacent
        ptX.remove(2);
        ptX.add(p5);
        assertFalse(controller.isStraightLineTiles(ptX));
    }

    @Test
    void isAdjacent() {
        Position p1 = new Position(3, 1);
        Position p2 = new Position(3, 2);
        Position p3 = new Position(4, 1);
        Position p4 = new Position(4, 2);

        //All possible combinations
        assertTrue(controller.isAdjacentOnX(p1, p2));
        assertFalse(controller.isAdjacentOnY(p1, p2));
        assertTrue(controller.isAdjacentOnX(p3, p4));
        assertFalse(controller.isAdjacentOnY(p3, p4));
        assertTrue(controller.isAdjacentOnY(p1, p3));
        assertFalse(controller.isAdjacentOnX(p1, p3));
        assertTrue(controller.isAdjacentOnY(p2, p4));
        assertFalse(controller.isAdjacentOnX(p2, p4));
    }

    @Test
    void reset(){
        for (int i = 0; i < controller.getNumRows(); i++) {
            for (int j = 0; j < controller.getNumCols(); j++) {
                controller.getShelf()[i][j] = new Tile("plants");
                assertNotEquals(type.EMPTY, controller.getShelf()[i][j].getCategory());
            }
        }
        for (int i = 0; i < controller.getDim(); i++) {
            for (int j = 0; j < controller.getDim(); j++) {
                controller.getBoard()[i][j] = new Tile("plants");
                assertNotEquals(type.EMPTY, controller.getBoard()[i][j].getCategory());
            }
        }
        List<Integer> cc = new ArrayList<>();
        cc.add(1);
        cc.add(2);
        controller.setCC(cc);
        assertTrue(controller.getCc().contains(cc.get(0)));
        assertTrue(controller.getCc().contains(cc.get(1)));
        controller.reset();
        for (int i = 0; i < controller.getNumRows(); i++) {
            for (int j = 0; j < controller.getNumCols(); j++) {
                assertEquals(type.EMPTY, controller.getShelf()[i][j].getCategory());
            }
        }
        for (int i = 0; i < controller.getDim(); i++) {
            for (int j = 0; j < controller.getDim(); j++) {
                assertEquals(type.EMPTY, controller.getBoard()[i][j].getCategory());
            }
        }
        assertTrue(controller.getCc().isEmpty());
    }
}