package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Tile.Tile;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class PlayerTest extends TestCase {

    @Test
    public void testMaxSpaceInShelf() {
        Tile[][] Shelf;
        Shelf= new Tile[][]{new Tile[]{new Tile("Empty"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("EMPTY")},
                new Tile[]{new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("EMPTY")},
                new Tile[]{new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS")},
                new Tile[]{new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS")},
                new Tile[]{new Tile("FRAMES"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS")},
                new Tile[]{new Tile("TROPHIES"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS"), new Tile("CATS")}};
        Player p=new Player("MarcoGervatarco");
        p.setShelf(Shelf);
        int x=p.maxSpaceInShelf();
        assertEquals(2,x);
    }
}