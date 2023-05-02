package main.java.it.polimi.ingsw.controller;

import main.java.it.polimi.ingsw.exceptions.NotAvaibleTilesException;
import main.java.it.polimi.ingsw.model.Game;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Position;
import main.java.it.polimi.ingsw.network.messages.Action;
import main.java.it.polimi.ingsw.network.messages.GeneralMessage;
import main.java.it.polimi.ingsw.network.messages.PickTilesMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class serverController {
   gameController controller = new gameController();
   GeneralMessage message;


   //getMessage inizializza il game controller in base all'azione richiesta dal client
   public void executeMessage(GeneralMessage received_message){
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int game_id = message.getGameId();
      switch(message.getClass().getSimpleName()){
         case "PickTilesMessage":
            ArrayList<Position> pos;
            pos = ((PickTilesMessage) message).getPos();
            try {
               controller.pickTiles(player, action, pos,game_id,id);
               //manda un messaggio di esito positivo con dentro
               sendOk(message);
            }
            catch(NotAvaibleTilesException e){
               //manda un messaggio al client che la richiesta non è stata soddisfatta
               sendKo(message);
            }
            break;
         case "SelectOrderMessage":
            /*

             */
            break;

         default :
            break;
      }
   }

   private void sendOk(GeneralMessage message) {
   }

   private void sendKo(GeneralMessage message) {

   }


}
