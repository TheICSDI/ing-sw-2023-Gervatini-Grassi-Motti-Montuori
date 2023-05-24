/** Remote interface. It has to be implemented by the RMI sever and client.
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.network.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIconnection extends Remote {
    void RMIsendName(String m, RMIconnection reply) throws RemoteException;
    void RMIsend(String m) throws RemoteException;
}
