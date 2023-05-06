package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;
import java.util.List;

public class clientController{
    private int idMex=0;//ogni messaggio ha un numero che dipende viene assegnato in ordine crescente dal client
    private final String nickname;
    private int idLobby=0;

    public int getIdLobby() {
        return idLobby;
    }

    public void setIdLobby(int idLobby) {
        this.idLobby = idLobby;
    }
    private int idGame=0;

    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    public clientController(String nickname){
        this.nickname = nickname;
    }


    /*controlla che la stringa che rappresenta l'azione scelta corrisponda a un comando esistente e chiamabile dal client
    in quel momento verifica che il numero di parametri sia adeguato e procede a eseguire i comandi esterni alla partita
    e restituisce al client un message del tipo giusto per l'azione che verra' poi messo in json format e inviato al server
    */

    /**
     * Checks if the command called by the client is accepted by the server, with the right number of parameters and formats
     * the message to be ready to send
     * @param m message to check
     * @param controller sender client's controller
     * @return subclass of general message based on the action type
     */
    public GeneralMessage checkMessageShape(String m,clientController controller){
        Action curr_action;
        List<String> curr_params;
        //parsing dell'input string
        String[] words = m.split(" "); //il delimitatore delle parole e' lo spazio
        String action = words[0];
        action = action.toUpperCase();
        try{
            //vediamo se il comando esiste prima di tutto
            curr_action = Action.valueOf(action);
            //return new GeneralMessage(curr_action,curr_params);

            idMex++;//indice del messaggio, ogni player ha il suo perchè vengono contati da lato client
            switch (curr_action){
                case CREATELOBBY -> {//words deve contenere action, numero di giocatori partita
                    if(words.length==2 && Integer.parseInt(words[1])>=2 && Integer.parseInt(words[1])<=4){ //un parametro int compreso tra 2 e 4 (limite giocatori)
                        return new CreateLobbyMessage(idMex,nickname,Integer.parseInt(words[1]));
                    }else{
                        return new DefaultErrorMessage("Insert number of players for this lobby between 2 and 4");
                    }
                }
                case SHOWLOBBY -> {
                    if(words.length==1){//words deve contenere action
                        return new ShowLobbyMessage(idMex, nickname);
                    }
                }
                case JOINLOBBY -> {
                    if(words.length==2){//words deve contenere action, numero di lobby
                        return new JoinLobbyMessage(idMex, Integer.parseInt(words[1]), nickname);
                    }
                    else{
                        return new DefaultErrorMessage("Insert a valid lobby number");
                    }
                }
                case STARTGAME -> {//words deve contenere action
                    return new StartGameMessage(idMex,controller.getIdLobby(),nickname);
                }
                case PICKTILES -> {//words deve contenere action e poi da 2 a 6 numeri che sono le coordinate delle tiles
                    //scelte sul tabellone, es. picktiles 2 3 2 4 5 6

                    List<Position> pos=new ArrayList<>();
                    if (words.length<=7 && words.length%2==1 && words.length>=3) {//serve a fare le position
                        for (int i = 1; i < words.length; i=i+2) {
                            pos.add(new Position(Integer.parseInt(words[i]),Integer.parseInt(words[i+1])));
                        }
                    }else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                    return new PickTilesMessage(idMex, nickname, pos,controller.getIdGame());
                }
                case SELECTORDER -> {//action, da 1 a 3 interi es. selectorder 2 1 3
                    List<Integer> order =  new ArrayList<>();
                    if((words.length==3 && words[1].equals(words[2]) ||
                            (words.length==4 && (words[1].equals(words[2]) || (words[1].equals(words[3]) || (words[3].equals(words[2]))))))){
                        return new DefaultErrorMessage("Can't use two equal numbers!");
                    }
                    if (words.length<=4 && words.length>=2) {
                        for (int i = 1; i < words.length; i++) {//riempie order
                            if(Integer.parseInt(words[i])> (words.length-1)){
                                return new DefaultErrorMessage("One or more numbers out of bound");
                            }
                            order.add(Integer.parseInt(words[i]));
                        }
                    }else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                    return new SelectOrderMessage(idMex, nickname, order,controller.getIdGame());
                }
                case SELECTCOLUMN -> {
                    int col;
                    if(words.length == 2){//action, un numero
                        col = Integer.parseInt(words[1]);
                    }
                    else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                    if(col<1 || col>5){
                        return new DefaultErrorMessage("Column not valid");
                    }else{
                        return new SelectColumnMessage(idMex, nickname,col ,controller.getIdGame());
                    }

                }

            }


        }
        catch(IllegalArgumentException e){
            idMex--;//M:secondo me e' tagliabile tanto gli id vanno solo in ordine cresciente anche se ne perdiamo
            // uno l'importante e' che non ce ne siano due ugali// A:boh mi sembra scema come cosa avere i messaggi con id: 1,2,7 perchè il giocatore non sa scrivere
            return new DefaultErrorMessage("Invalid command");
        }

        return null;
    }
}
