package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIclientImpl extends UnicastRemoteObject implements RMIconnection {

    public RMIclientImpl() throws RemoteException {
        super();
    }

    @Override
    public void RMIsend(String m) throws RemoteException {

    }
}
