package main.java.it.polimi.ingsw.controller;

import main.java.it.polimi.ingsw.exceptions.InvalidCommandException;
import main.java.it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;
import java.util.List;

public class clientController{
    private int idMex=0;
    private final String nickname;
    //countMove represents a counter of the three mandatory action a player have to do in a turn
    //is useful to control that for example selectOrder is called only one time and only after chooseTiles
    private int countMove = 0;
    public clientController(String nickname){
        this.nickname = nickname;
    }
    //controlla che la stringa che rappresenta l'azione scelta corrisponda a un comando esistente e chiamabile dal client
    //in quel momento
    public GeneralMessage checkMessageShape(String m) throws InvalidCommandException {
        Action curr_action;
        List<String> curr_params;
        //parsing dell'input string e separazione se contiene più parametri
        String[] words = m.split(" ");
        String action = words[0];
        action = action.toUpperCase();
        try{
            //vediamo se il comando esiste prima di tutto
            curr_action = Action.valueOf(action);
            //Per ora conto i messaggi qua ma è da rivedere perchè solo lato client
            idMex++;
            //in base al tipo di messaggio controlla se il numero di parametri è giusto(forse avremo da controllare che siano effettivamente corretti anche i param)
            //e formatta il messaggio 
            switch (curr_action){
                case CREATELOBBY -> {
                    if(words.length==1){
                        new CreateLobbyMessage(idMex,nickname);
                    }
                }
                case JOINLOBBY -> {
                    if(words.length==2){
                        new JoinLobbyMessage(idMex,Integer.parseInt(words[1]),nickname);
                    }
                }
                //TODO da completare switch e da qualche parte i controlli per vedere se puoi mandare il messaggio in quel momento
            }
            return null;

        }
        catch(IllegalArgumentException e){
            //throw new InvalidCommandException("invalid command");
            System.out.println("Invalid command");
            return null;
        }

    }
    /*
        La stringa da convertire in MESSAGE deve essere cosi composto:
        -prima parola il comando (maiuscolo o minuscolo va bene)
        -delimitatore spazio
        -eventuali parametri delimitati dallo spazio
        andranno definiti in seguito i migliori modi di formattare i parametri
         */

    /*
    checkArgs controlla inizialmente se il giocatore puo chiamare lo specifico comando in questo momento,
    esistendo comandi asincroni come chat exit e join che posso essere chiamati in momenti off game o in qualsiasi momento in game.
    A seconda del comando controlla con uno switch case se i parametri sono nel numero corretto
    eventuali controlli sul accettabilità dei parametri verrà valutata lato server
    */
    // DEPRECATED, SARA' DA TOGLIERE
    public List<String> checkArgs(Action a, String[] p) throws InvalidCommandException{
        //the first element of p is the action but in string form, we can modify the methods and pass
        //directly only the params in a second moment
        List<String> params = new ArrayList<>();
        int i;
        //maybe the switch statement could be refactored to delete some lines of code
        switch(a) {
            case JOINLOBBY:
                if(!imInALobby()){
                    if(p.length == 1){
                        return params;
                    }
                    else{
                        throw new InvalidCommandException("Command formulation is wrong");
                    }
                }
                else{
                    throw new InvalidCommandException("Command cannot be called");
                }
                case EXITLOBBY:// assuming that I can exit from a lobby even if the game is started
                    if(imInALobby()){
                        if(p.length == 1){
                            return params;
                        }
                        else{
                            throw new InvalidCommandException("Command formulation is wrong");
                        }
                    }
                    else{
                        throw new InvalidCommandException("Command cannot be called");
                    }
            case CREATELOBBY:
                if(!imInALobby()){
                    if(p.length == 1){
                        return params;
                    }
                    else{
                        throw new InvalidCommandException("Command formulation is wrong");
                    }
                }
                else{
                    throw new InvalidCommandException("Cannot create a lobby");
                }
            case PICKTILES:
                if(imInALobby()){
                    if(isGameStarted()){
                        if(isMyTurn() && countMove == 0){
                            if(p.length > 1 && p.length <= 4 ){
                                for(i = 1; i < p.length;i++){
                                    params.add(p[i]);
                                }
                                countMove = 1;
                            }
                            else{
                                throw new InvalidCommandException("Command formulation is wrong");
                            }
                        }else{
                            throw new InvalidCommandException("You can choose only in your turn");
                        }
                    }else{
                        throw new InvalidCommandException("Command cannot be called");
                    }
                }else{
                    throw new InvalidCommandException("Command cannot be called");
                }
            case SELECTORDER:
                if(imInALobby()){
                    if(isGameStarted()){
                        if(isMyTurn() && countMove == 1){
                            if(p.length > 1 && p.length <= 4 ){
                                for(i = 1; i < p.length;i++){
                                    params.add(p[i]);
                                }
                                countMove = 2;
                            }
                            else{
                                throw new InvalidCommandException("Command formulation is wrong");
                            }
                        }else{
                            throw new InvalidCommandException("You can choose only in your turn");
                        }
                    }else{
                        throw new InvalidCommandException("Command cannot be called");
                    }
                }else{
                    throw new InvalidCommandException("Command cannot be called");
                }
            case SETNAME://sbagliato per scelta

                if(imInALobby()){
                    if(isGameStarted()){
                        if(isMyTurn() && countMove == 2){
                            if(p.length > 1 && p.length <= 4 ){
                                for(i = 1; i < p.length;i++){
                                    params.add(p[i]);
                                }
                                countMove = 3;
                            }
                            else{
                                throw new InvalidCommandException("Command formulation is wrong");
                            }
                        }else{
                            throw new InvalidCommandException("You can choose only in your turn");
                        }
                    }else{
                        throw new InvalidCommandException("Command cannot be called");
                    }
                }else{
                    throw new InvalidCommandException("Command cannot be called");
                }
            default:
                throw new InvalidCommandException("Command not found");
        }
    }

    /*
    return true if the game is started only it is the player's turn
    return false in every other case
    if it returns false reset the countMove to 0
     */
    public Boolean isMyTurn(){
        /*
        potremmo richiedere al server un ping istantaneo se e' il mio turno o no

        oppure calcolarlo lato client
         */
        return true;
    }
    /*
    return true only if the player is already assigned to a lobby
     */
    public Boolean imInALobby(){
        /*
        sempre stesso problema possiamo chiedere al server o decidere d'implementare il controllo qui
         */
        return true;
    }

    /*
    return true only if the lobby's game is started
     */
    public Boolean isGameStarted(){
        /*
        sempre stesso problema possiamo chiedere al server o decidere d'implementare il controllo qui
         */
        return true;
    }
}
