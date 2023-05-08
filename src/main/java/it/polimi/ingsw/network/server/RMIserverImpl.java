/** It implements the RMIconnection interface, in order to be able to establish a RMI connection. */
package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.gameController;
import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.network.messages.GeneralMessage;
import it.polimi.ingsw.network.messages.SetNameMessage;
import org.json.simple.parser.ParseException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIserverImpl extends UnicastRemoteObject implements RMIconnection {

    public RMIserverImpl() throws RemoteException {
        super();
    }

    //TODO: Come idea ho fatto la stessa cosa del server anche da lato client così ho l'oggetto rmiclient in socketClient che serve a mandare i messaggi al client,
    //inoltre differenzio i messaggi che aspettano una risposta da quelli che non lo fanno(probabilmente non serve però, credo basti passare null quando non aspetti risposte e
    //con controlli fatti bene si risolve con una funzione) però praticamente dal client chiamo la funzione che manda un messaggio e li passo come parametro l'oggetto che serve al server
    //per rispondere

    //TODO aggiungere ai player il modo in cui si sono connessi e gli oggetti che serveno all'rmi per comunicare(rmiclient e stub)
    //TODO cambiare execute message in server e send message in client con rmi(probs serve rifare lo switch sull'action del messaggio qua dentro e anche per il send dal server

    //TODO stessa cosa opposta in RMIclientImp, con skeleton invece di stub

    //TODO thread per il multi client nel server
    @Override
    public void RMIsend(String m, RMIconnection reply) throws RemoteException {
        //server.getMessage(m)
        GeneralMessage mex;
        Action action=Action.ERROR;
        try{
            action=GeneralMessage.identify(m);


        if(action.equals(Action.SETNAME)){
            mex=new SetNameMessage(m);
            if(gameController.allPlayers.containsKey(mex.getUsername())){
                //Nickname already taken
                reply.RMIsendOnly("The nickname is already taken!");

            }else{
                //Nickname available
                reply.RMIsendOnly("Nickname correctly set.");
            }
        }
        }catch(InvalidKeyException | InvalidActionException | ParseException ignored){}

    }

    @Override
    public void RMIsendOnly(String m) throws RemoteException {

    }
}

