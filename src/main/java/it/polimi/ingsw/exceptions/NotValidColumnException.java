/** Exception for invalid column passed by parameter
 * @author Marco Gervatini
 */
package main.java.it.polimi.ingsw.exceptions;

public class NotValidColumnException extends Exception{
    public NotValidColumnException(String message){
        super(message);
    }
}