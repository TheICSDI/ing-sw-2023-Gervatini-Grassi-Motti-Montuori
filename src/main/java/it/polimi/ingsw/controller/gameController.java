/** The clients have to call the controller's method to interact with the game's model.*/
package main.java.it.polimi.ingsw.controller;

import main.java.it.polimi.ingsw.model.Lobby;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Position;
import java.util.*;

public class gameController {
    Player p;

    //Non so se serve il costruttore
    public gameController() {

    }

    public static HashSet<Position> Choose(){
        return new HashSet<>();//placeholder
    }
    public static ArrayList<Integer> ChooseOrder(){//ritorna l'ordine delle tessere d inserire
        return new ArrayList<>();
    }

    public static int ChooseColumn(){//ritorna la colonna scelta per l'inserimento
        return 0;//placeorder
    }

    public void createLobby() {
    }

    public void joinLobby() {
    }

    public void leaveLobby() {
    }

    public void start() {
    }
}
