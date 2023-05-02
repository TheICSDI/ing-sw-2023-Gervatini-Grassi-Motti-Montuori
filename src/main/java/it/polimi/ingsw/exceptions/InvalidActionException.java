/** Exception for invalid message encoding
 * @author Giulio Montuori
 */
package main.java.it.polimi.ingsw.exceptions;

public class InvalidActionException extends Exception{
    public InvalidActionException(String message){
        super(message);
    }
}