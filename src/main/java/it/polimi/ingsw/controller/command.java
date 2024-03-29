package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Action;

import java.util.List;

/** It manages the pending commands not yet fully executed.
 * @author Marco Gervatini. */
public class command {
    public Game g;
    public Player p;
    public Action a;
    public int numMess;
    private List<Position> pos = null;
    private List<Integer> order = null;
    private int numCol;

    /** It creates a book of commands associated to a specific game, player, action and a unique message's number. */
    public command(Game g, Player p, Action a, int numMess){
        this.g = g;
        this.p = p;
        this.a = a;
        this.numMess = numMess;
    }

    /** It sets the list of position passed by parameter. */
    public void setPos(List<Position> pos) {
        this.pos = pos;
    }

    /** It sets the list of order passed by parameter. */
    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    /** It sets the number of column passed by parameter. */
    public void setNumCol(int numCol) {this.numCol = numCol;}

    /** It gets the list of position. */
    public List<Position> getPos() {return pos;}

    /** It gets the list of order. */
    public List<Integer> getOrder() {
        return order;
    }

    /** It gets the number of column. */
    public int getNumCol() {
        return numCol;
    }
}
