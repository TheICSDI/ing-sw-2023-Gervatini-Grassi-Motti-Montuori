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

    public clientController(String nickname){
        this.nickname = nickname;
    }


    /*controlla che la stringa che rappresenta l'azione scelta corrisponda a un comando esistente e chiamabile dal client
    in quel momento verifica che il numero di parametri sia adeguato e procede a eseguire i comandi esterni alla partita
    e restituisce al client un message del tipo giusto per l'azione che verra' poi messo in json format e inviato al server
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
                    if(words.length==2){
                        int x=Integer.parseInt(words[1]);
                        return new CreateLobbyMessage(idMex,nickname,Integer.parseInt(words[1]));
                    }else{
                        return new DefaultErrorMessage("Insert number of players in this lobby");
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
                        return new DefaultErrorMessage("Insert a valid lobby number please");
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
                    return new PickTilesMessage(idMex, nickname, pos);
                }
                case SELECTORDER -> {//action, da 1 a 3 interi es. selectorder 2 1 3
                    List<Integer> order =  new ArrayList<>();
                    if (words.length<=4 && words.length>=2) {
                        for (int i = 1; i < words.length; i++) {//riempie order
                            order.add(Integer.parseInt(words[i]));
                        }
                    }else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                    return new SelectOrderMessage(idMex, nickname, order);
                }
                case SELECTCOLUMN -> {
                    int col;
                    if(words.length == 2){//action, un numero
                        col = Integer.parseInt(words[1]);
                    }
                    else{
                        return new DefaultErrorMessage("Number of parameters is wrong");
                    }
                }

            }


        }
        catch(IllegalArgumentException e){
            System.out.println("Invalid command");
            idMex--;//secondo me e' tagliabile tanto gli id vanno solo in ordine cresciente anche se ne perdiamo
            // uno l'importante e' che non ce ne siano due guiali
            return new DefaultErrorMessage("Error");
        }

        return null;
    }
}
