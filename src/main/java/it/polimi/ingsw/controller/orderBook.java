/** It manages the pending commands not yet fully executed. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Action;

import java.util.List;

public class orderBook{
    public Game g;
    public Player p;
    public Action a;
    public int numMess;
    private List<Position> pos = null;
    private List<Integer> order = null;
    private int numCol = 42;

    /** Constructor that creates a book of commands associated to a specific gmae, plater, action and a unique number
     * of a message. */
    public orderBook(Game g, Player p, Action a, int numMess){
        //si potrebbero conservare game e player solo come id e nickname per velocizzare l'esecuzione
        this.g = g;
        this.p = p;
        this.a = a;
        this.numMess = numMess;
    }

    /** It sets the list of position. */
    public void setPos(List<Position> pos) {
        this.pos = pos;
    }

    /** It sets the list of order. */
    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    /** It sets the number of column. */
    public void setNumCol(int numCol) {
        this.numCol = numCol;
    }

    /** It gets the list of position. */
    public List<Position> getPos() {
        return pos;
    }

    /** It gets the list of order. */
    public List<Integer> getOrder() {
        return order;
    }

    /** It gets the number of column. */
    public int getNumCol() {
        return numCol;
    }
}
