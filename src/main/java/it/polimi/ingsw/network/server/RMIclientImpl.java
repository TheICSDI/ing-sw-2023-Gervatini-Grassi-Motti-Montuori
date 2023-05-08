package it.polimi.ingsw.network.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIclientImpl extends UnicastRemoteObject implements RMIconnection {

    public RMIclientImpl() throws RemoteException {
        super();
    }

    @Override
    public void RMIsend(String m, RMIconnection reply) throws RemoteException {


    }

    @Override
    public void RMIsendOnly(String m) throws RemoteException {
        System.out.println(m);
    }
}
