/** Exception for invalid position (not_accessible)
 * @author Caterina Motti
 */
package main.java.it.polimi.ingsw.exceptions;

public class NotValidPositionException extends Throwable {
    public NotValidPositionException(String message){
        super(message);
    }
}
