package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

import it.polimi.ingsw.network.messages.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverController {
   public gameController controller = new gameController();
   GeneralMessage message;
   /*
   ogni thread di questo pool permette di svolgere un game, numero di thread e' il massimo numero di partite
   inizialibili in parallelo
    */
   ExecutorService executorsService = Executors.newFixedThreadPool(10);

   /*
   executeMessage take a message from the client and provide to do the request action for all the not in-game commands
   and to pass the in-game requests to the game controller, return a message that communicate the ending of the operation
    */

   public String executeMessage(GeneralMessage received_message){
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int gameId = message.getGameId();
      int idLobby = message.getLobby_id();
      switch(message.getAction()){
         case CREATELOBBY -> {
            //Create a new lobby whose first player is who have called the command
            if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){
               Player pl = gameController.allPlayers.get(message.getUsername());
               int limit=message.getLimit();//the lobby has an amount of player less than can't start the game,
               // represents the maximum capacity too
               Lobby NewLobby=new Lobby(pl,limit);
               gameController.allLobbies.add(NewLobby);//add in the list of lobbies
               return new CreateLobbyReplyMessage("Lobby created", limit).toString();
            }else{
               return new ReplyMessage("Already in a lobby",Action.ERROR).toString();
            }

         }
         case SHOWLOBBY -> {
            return new ShowLobbyReplyMessage("show",gameController.allLobbies).toString();

         }
         case JOINLOBBY -> {
            if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){//verifica che il player non
               // sia in nessuna lobby
               for (Lobby l: gameController.allLobbies) {
                  if(l.lobbyId==message.getLobby_id()){//trova la lobby scelta
                     l.Join(gameController.allPlayers.get(message.getUsername()));//aggiunge il nome del player ai giocatori di quella lobby
                  }
               }
               return new ReplyMessage("Lobby Joined",Action.JOINLOBBY).toString();
            }else{
               return new ReplyMessage("Already in a lobby",Action.ERROR).toString();
            }
         }
         case STARTGAME -> {
            for (Lobby l:  gameController.allLobbies) {
               if(idLobby==l.lobbyId){
                  if(l.Players.size()==l.limit){
                     Game g = new Game(l.Players, controller);
                     gameController.allGames.put(g.id,g);
                     gameController.allLobbies.remove(l);
                     executorsService.submit(g::startGame);
                     for (Player p:
                          l.Players) {
                        p.getOut().println(new StartGameReplyMessage("Game started"));
                     }
                     return new OkReplyMessage("").toString();
                  }else{
                     return new ReplyMessage("Not enough ot too many players",Action.ERROR).toString();
                  }
               }
            }
            return new ReplyMessage("Not in a Lobby",Action.ERROR).toString();
         }
         case PICKTILES-> {
            List<Position> pos;
            pos = ((PickTilesMessage) message).getPos();
            controller.pickTiles(player, action, pos, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
            return new OkReplyMessage("Ok").toString();

         }
         case SELECTORDER -> {
            List<Integer> order = ((SelectOrderMessage)message).getOrder();
            controller.selectOrder(player, action, order, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
            return new OkReplyMessage("Ok").toString();
         }
         case SELECTCOLUMN -> {
            int numCol = ((SelectColumnMessage) message).getCol();
            controller.selectColumn(player, action, numCol, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
            return new OkReplyMessage("Ok").toString();

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
