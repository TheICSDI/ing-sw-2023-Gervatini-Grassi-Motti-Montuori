package it.polimi.ingsw.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** Remote interface. It has to be implemented by the RMI server. */
public interface RMIconnection extends Remote {
    void RMIsendName(String m, RMIconnection reply) throws RemoteException;
    void RMIsend(String m) throws RemoteException;
}
