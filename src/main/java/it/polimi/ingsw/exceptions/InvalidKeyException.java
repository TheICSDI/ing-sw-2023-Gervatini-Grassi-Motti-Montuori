/** Exception for invalid key in a JSON
 * @author Giulio Montuori
 */
package main.java.it.polimi.ingsw.exceptions;

public class InvalidKeyException extends Exception{
    public InvalidKeyException(String message){
        super(message);
    }
}