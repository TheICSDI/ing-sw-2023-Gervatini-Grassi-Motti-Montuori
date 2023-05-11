/** Represent a connection with is type (socket or RMI) and the way to talk with the client
 * (PrintWriter or RMIconnection). */
package it.polimi.ingsw.network.server;

import java.io.PrintWriter;

public class connectionType {
     private final boolean socket;
     private final PrintWriter out;
     private final RMIconnection reply;

    public connectionType(boolean socket, PrintWriter out, RMIconnection reply) {
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

    public RMIconnection getReply() {
        return reply;
    }
}
