/** It implements the RMIconnection interface, in order to be able to establish a RMI connection. */
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

    //TODO: Come idea ho fatto la stessa cosa del server anche da lato client così ho l'oggetto rmiclient in socketClient che serve a mandare i messaggi al client,
    //inoltre differenzio i messaggi che aspettano una risposta da quelli che non lo fanno(probabilmente non serve però, credo basti passare null quando non aspetti risposte e
    //con controlli fatti bene si risolve con una funzione) però praticamente dal client chiamo la funzione che manda un messaggio e li passo come parametro l'oggetto che serve al server
    //per rispondere

    //TODO aggiungere ai player il modo in cui si sono connessi e gli oggetti che serveno all'rmi per comunicare(rmiclient e stub)
    //TODO cambiare execute message in server e send message in client con rmi(probs serve rifare lo switch sull'action del messaggio qua dentro e anche per il send dal server

    //TODO stessa cosa opposta in RMIclientImp, con skeleton invece di stub

    //TODO thread per il multi client nel server
    @Override
    public void RMIsendName(String m, RMIconnection reply) throws RemoteException {
        try {
            s.getName(m, false, null, reply);
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

