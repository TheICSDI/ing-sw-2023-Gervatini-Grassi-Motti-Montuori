package it.polimi.ingsw.network.server;
import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.chrono.IsoEra;

public class RMIserverImpl extends UnicastRemoteObject implements RMIconnection {

    //
    public RMIserverImpl() throws RemoteException {
        super();
    }


    @Override
    public void RMIsend(String m) throws RemoteException {
        //server.getMessage(m)
        GeneralMessage mex;
        Action action=Action.ERROR;
        try{
            action=GeneralMessage.identify(m);


        if(action.equals(Action.SETNAME)){
            mex=new SetNameMessage(m);
            if(gameController.allPlayers.containsKey(mex.getUsername())){
                //Nickname already taken
                System.out.println("The nickname is already taken!");
            }else{
                //Nickname available
                System.out.println("Nickname correctly set.");
            }
        }
        }catch(InvalidKeyException | InvalidActionException | ParseException ignored){}

    }

}

