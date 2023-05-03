/** Exception for invalid position (not_accessible)
 * @author Caterina Motti
 */
package it.polimi.ingsw.exceptions;

public class InvalidPositionException extends Throwable {
    public InvalidPositionException(String message){
        super(message);
    }
}
