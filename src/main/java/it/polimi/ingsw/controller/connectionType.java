/** Represent a connection with is type (socket or RMI) and the way to talk with the client (PrintWriter or RMIconnection).
 * @author Caterina Motti, Andrea Grassi. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.network.server.RMIconnection;

import java.io.PrintWriter;

public class connectionType {
     private boolean socket;
     private PrintWriter out;
     private RMIconnection reply;
     private int ping;

     /** It creates a connectionType by specify the type (socket or RMI) and the way to talk with the client.
      * @param socket true only if the connection is of type socket.
      * @param out the PrintWriter of the client if connected via socket, null otherwise.
      * @param reply the RMIConnection of the client if connected via RMI, null otherwise. */
     public connectionType(boolean socket, PrintWriter out, RMIconnection reply) {
        this.socket = socket;
        this.out = out;
        this.reply = reply;
        this.ping = 0;
     }

     /** It changes the connectionType. It is used when a clients reconnect after disconnection.
      *  @param socket true only if the new connection is of type socket.
      *  @param out the PrintWriter of the client if connected via socket, null otherwise.
      *  @param reply the RMIConnection of the client if connected via RMI, null otherwise.*/
     public void changeConnection(boolean socket, PrintWriter out, RMIconnection reply){
         this.socket = socket;
         this.out = out;
         this.reply = reply;
     }

    public boolean isSocket() {
        return socket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public RMIconnection getReply() {return reply;}

    public int getPing(){return ping;}

    public void addPing(){ping++;}
}
