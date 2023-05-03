package it.polimi.ingsw.exceptions;
//exception called when for some reason the message is wrong and haven't to be sent
public class InvalidCommandException extends Exception{
    public InvalidCommandException(String message) {
        super(message);
    }
}
