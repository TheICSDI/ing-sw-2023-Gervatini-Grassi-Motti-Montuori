/** Exception for invalid column passed by parameter
 * @author Marco Gervatini
 */
package main.java.it.polimi.ingsw.exceptions;

public class InvalidColumnException extends Exception{
    public InvalidColumnException(String message){
        super(message);
    }
}