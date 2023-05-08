package it.polimi.ingsw.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Remote interface. It has to be implemented by the RMI server. It is the service offered by the server. */
public interface RMIconnection extends Remote {

    void RMIsend(String m, RMIconnection reply) throws RemoteException;
    void RMIsendOnly(String m) throws RemoteException;
}
