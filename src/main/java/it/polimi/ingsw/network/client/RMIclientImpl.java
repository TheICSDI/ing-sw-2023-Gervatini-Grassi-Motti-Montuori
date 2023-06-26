/** It implements the RMIconnection interface, in order to be able to establish RMI connection client side.
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.clientController;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.ReconnectMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import it.polimi.ingsw.network.server.RMIconnection;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIclientImpl extends UnicastRemoteObject implements RMIconnection {
    clientController CC;
    /** Creates a RMIserverImpl given a client controller by parameter. */
    public RMIclientImpl(clientController CC) throws RemoteException {
        super();
        this.CC = CC;
    }

    /** It gets the reply to the chosen nickname from the server via RMI connection.
     * @param m message from the server (JSON format).
     * @param reply way to talk with the server. */
    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        Action action;
        try {
            action = GeneralMessage.identify(m);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(action.equals(Action.SETNAME)){
            if(!SetNameMessage.decrypt(m).isAvailable()){
                Client.getVirtualView().checkNickname(SetNameMessage.decrypt(m).getUsername(), SetNameMessage.decrypt(m).isAvailable());
                Client.setName();
            } else {
                Client.getVirtualView().checkNickname(SetNameMessage.decrypt(m).getUsername(), SetNameMessage.decrypt(m).isAvailable());
                this.CC.setNickname(SetNameMessage.decrypt(m).getUsername());
            }
        } else {
            Client.getVirtualView().checkNickname(ReconnectMessage.decrypt(m).getUsername(), ReconnectMessage.decrypt(m).isAvailable());
            this.CC.setNickname(ReconnectMessage.decrypt(m).getUsername());
            if(ReconnectMessage.decrypt(m).getIdLobby()>0){
                this.CC.setIdLobby(ReconnectMessage.decrypt(m).getIdLobby());
            }
            if(ReconnectMessage.decrypt(m).getGameId()>0){
                this.CC.setIdGame(ReconnectMessage.decrypt(m).getGameId());
            }
        }
    }

    /**
     * It gets a message from the server and send it to the client, that elaborate the command.
     * @param m message (JSON format).
     */
    @Override
    public void RMIsend(String m) throws RemoteException {
        try {
            Client.elaborate(m);
        } catch (ParseException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
