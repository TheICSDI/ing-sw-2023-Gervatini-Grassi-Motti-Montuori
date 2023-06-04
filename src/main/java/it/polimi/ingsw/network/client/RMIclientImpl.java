/** It implements the RMIconnection interface, in order to be able to establish RMI connection client side.
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.server.RMIconnection;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIclientImpl extends UnicastRemoteObject implements RMIconnection {
    clientController CC;
    public RMIclientImpl(clientController CC) throws RemoteException {
        super();
        this.CC = CC;
    }

    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        CC.getName(m);
    }

    @Override
    public void RMIsend(String m) throws RemoteException {
        try {
            CC.getMessage(m);
            //Client.elaborate(m);
        } catch (ParseException | InvalidKeyException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
