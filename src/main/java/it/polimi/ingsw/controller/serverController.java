package main.java.it.polimi.ingsw.controller;

import main.java.it.polimi.ingsw.exceptions.AlreadyInAGameException;
import main.java.it.polimi.ingsw.exceptions.GameStartedException;
import main.java.it.polimi.ingsw.exceptions.NotAvaibleTilesException;
import main.java.it.polimi.ingsw.model.Position;
import main.java.it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;
import java.util.List;

public class serverController {
   public gameController controller = new gameController();
   GeneralMessage message;


   //getMessage inizializza il game controller in base all'azione richiesta dal client
   //TODO DA COMPLETARE
   public void executeMessage(GeneralMessage received_message){
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int gameId = message.getGameId();
      switch(message.getClass().getSimpleName()){
         case "PickTilesMessage": {
            ArrayList<Position> pos;
            pos = ((PickTilesMessage) message).getPos();
            try {
               controller.pickTiles(player, action, pos, gameId, id);
               //manda un messaggio di esito positivo con dentro
               sendOk(message);
            } catch (NotAvaibleTilesException e) {
               //manda un messaggio al client che la richiesta non è stata soddisfatta
               sendKo(message);
            }
         }
         case "SelectOrderMessage":{
            List<Integer> order;
            order = ((SelectOrderMessage) message).getOrder();
               controller.selectOrder(player, action, order, gameId, id);
               sendOk(message);
               //TODO per i sendKo() bisogna collegare le exception del model con dei try catch
         }
         case "SelectColumnMessage":{
            int numCol;
            numCol = ((SelectColumnMessage) message).getCol();
               controller.selectColumn(player,action,numCol,gameId,id);
               sendOk(message);

         }
         case "CreateLobbyMessage":{
            try{
               controller.createLobby(player);
               sendOk(message);
            } catch (AlreadyInAGameException e) {
               sendKo(message);
            }
         }
         case "JoinLobbyMessage":{
            try {
               controller.joinLobby(player, gameId);
               sendOk(message);
            } catch (AlreadyInAGameException e) {
               sendKo(message);
            }
         }
         case "LeaveLobbyMessage":{
            try {
               controller.leaveLobby(player, gameId);
               sendOk(message);
            } catch (GameStartedException e) {
               sendKo(message);
            }
         }
            sendKo(message);
            break;
      }
   }
    //todo CONTROLLO CONDIZIONI MESSAGGI
   private void sendOk(GeneralMessage message) {
      //messaggio di ok rispetto al comando mandato message
   }

   private void sendKo(GeneralMessage message) {
      //messaggio che va rifatto il comando, riferito a message
   }


}
