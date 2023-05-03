package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Action;

import java.util.List;
//serve ad aggiungere comandi pendenti non ancora completamenti eseguiti.
public class orderBook{
    public Game g;
    public Player p;
    public Action a;
    public int num_mess;
    private List<Position> pos = null;
    private List<Integer> order = null;
    private int num_col = 42;

    public orderBook(Game g, Player p, Action a,int num_mess){
        this.g = g;
        this.p = p;
        this.a = a;
        this.num_mess = num_mess;
    }

    public void setPos(List<Position> pos) {
        this.pos = pos;
    }

    public void setOrder(List<Integer> order) {
        this.order = order;
    }

    public void setNum_col(int num_col) {
        this.num_col = num_col;
    }

    public List<Position> getPos() {
        return pos;
    }

    public List<Integer> getOrder() {
        return order;
    }

    public int getNum_col() {
        return num_col;
    }
}
