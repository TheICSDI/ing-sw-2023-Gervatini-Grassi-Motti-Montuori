package main.java.it.polimi.ingsw.network.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// composed by the command to call and possible params that it needs
//Serializable implementation may be changed or fixed
public class ClientMessage implements Serializable {
    main.java.it.polimi.ingsw.network.messages.action action;
    List<String> params = new ArrayList<>();

    public ClientMessage(action action, List<String> params){
        this.action = action;
        this.params = params;
    }
}
