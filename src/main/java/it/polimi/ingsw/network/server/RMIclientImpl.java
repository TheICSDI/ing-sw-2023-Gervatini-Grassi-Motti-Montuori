package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIclientImpl extends UnicastRemoteObject implements RMIconnection {
    clientController cc;
    public RMIclientImpl(clientController cc) throws RemoteException {
        super();
        this.cc=cc;
    }

    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        cc.getName(m);
    }

    @Override
    public void RMIsend(String m) throws RemoteException {

    }

}
