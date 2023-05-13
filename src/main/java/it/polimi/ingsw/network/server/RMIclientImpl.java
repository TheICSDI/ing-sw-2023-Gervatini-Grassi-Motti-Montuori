/** It implements the RMIconnection interface, in order to be able to establish RMI connection client side.
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
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
        try {
            cc.getMessage(m);
        } catch (ParseException | InvalidKeyException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
