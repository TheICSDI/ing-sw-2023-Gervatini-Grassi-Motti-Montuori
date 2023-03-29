package exceptions;

public class NotValidColumnException extends Exception{
    public NotValidColumnException(String message){
        super(message);
    }
}
