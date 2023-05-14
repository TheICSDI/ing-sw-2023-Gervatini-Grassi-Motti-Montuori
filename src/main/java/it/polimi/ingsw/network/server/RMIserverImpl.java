/** It implements the RMIconnection interface, in order to be able to establish RMI connection server side.
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.serverController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIserverImpl extends UnicastRemoteObject implements RMIconnection {
    serverController s;
    public RMIserverImpl(serverController s) throws RemoteException {
        super();
        this.s=s;
    }

    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        try {
            s.getName(m, reply);
        }catch(InvalidActionException | InvalidKeyException | ParseException ignored){}
    }

    @Override
    public void RMIsend(String m) throws RemoteException {
        try {
            s.getMessage(m);
        } catch (ParseException | InvalidKeyException | InvalidActionException e) {
            throw new RuntimeException(e);
        }
    }
}

