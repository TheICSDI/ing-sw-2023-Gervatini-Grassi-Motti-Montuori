package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidColumnException;
import it.polimi.ingsw.exceptions.InvalidPositionException;
import it.polimi.ingsw.exceptions.NotAvaibleTilesException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverController {
   public gameController controller = new gameController();
   GeneralMessage message;
   ExecutorService executorsService = Executors.newFixedThreadPool(10);

   //getMessage inizializza il game controller in base all'azione richiesta dal client
   //TODO DA COMPLETARE
   public String executeMessage(GeneralMessage received_message){
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int gameId = message.getGameId();
      switch(message.getAction()/*getClass().getSimpleName()??*/){
         case CREATELOBBY -> {
            //Crea una nuova lobby che ha come creatore chi invia il messaggio
            if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){
               Player pl=gameController.allPlayers.get(message.getUsername());
               int limit=message.getLimit();
               gameController.allLobbies.add(new Lobby(pl,limit));
               return new ReplyMessage("Lobby created").toString();
            }else{
               return new ReplyMessage("Already in a lobby").toString();
            }

         }
         case SHOWLOBBY -> {
            return new ShowLobbyReplyMessage("show",gameController.allLobbies).toString();

         }
         case JOINLOBBY -> {
            boolean found=false;
            if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){
               for (Lobby l:
                       gameController.allLobbies) {
                  if(l.lobbyId==message.getLobby_id()){
                     l.Join(gameController.allPlayers.get(message.getUsername()));
                  }
               }
               return new ReplyMessage("Lobby Joined").toString();
            }else{
               return new ReplyMessage("Already in a lobby").toString();
            }
         }
         case STARTGAME -> {
            if(isInALobby(gameController.allPlayers.get(message.getUsername()))){
               for (Lobby l:
                    gameController.allLobbies) {
                  if(l.isPlayerInLobby(gameController.allPlayers.get(message.getUsername()))){
                     if(l.Players.size()==l.limit){
                        Game g = new Game(l.Players, controller);
                        gameController.allGames.put(g.id,g);
                        gameController.allLobbies.remove(l);
                        executorsService.submit(g::startGame);//riga suggerita dal IDE prima era scritta diversa va provata
                        return new StartGameReplyMessage("Game started").toString();
                     }else{
                        return new DefaultErrorMessage("Not enough players").toString();//Probabile da rivedere(saranno tipo Reply)
                     }
                  }
               }
            }else{
               return new ReplyMessage("Not in a Lobby").toString();
            }
         }
         case PICKTILES-> {
            List<Position> pos;
            pos = ((PickTilesMessage) message).getPos();
            controller.pickTiles(player, action, pos, gameId, id);
            //manda un messaggio di esito positivo con dentro
            return new OkReplyMessage("Ok").toString();

         }
         case SELECTORDER -> {
            List<Integer> order = ((SelectOrderMessage)message).getOrder();
            controller.selectOrder(player, action, order, gameId, id);
         }
         case SELECTCOLUMN -> {
            int numCol = ((SelectColumnMessage) message).getCol();
            controller.selectColumn(player, action, numCol, gameId, id);

         }
         default -> {

         }
      }
      return "";
   }

    //todo CONTROLLO CONDIZIONI MESSAGGI

   private boolean isInALobby(Player p){
         for (Lobby l: gameController.allLobbies) {
            if(l.isPlayerInLobby(gameController.allPlayers.get(p.getNickname()))){
               return true;
            }
         }
         return false;

   }


}
