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
   ExecutorService executorsService = Executors.newFixedThreadPool(10);

   //getMessage inizializza il game controller in base all'azione richiesta dal client
   //TODO DA COMPLETARE
   public String executeMessage(GeneralMessage received_message){
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int idLobby= message.getLobby_id();
      int gameId = message.getGameId();
      switch(message.getAction()/*getClass().getSimpleName()??*/){
         case CREATELOBBY -> {
            //Crea una nuova lobby che ha come creatore chi invia il messaggio
            if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){
               Player pl=gameController.allPlayers.get(message.getUsername());
               int limit=message.getLimit();
               Lobby NewLobby=new Lobby(pl,limit);
               gameController.allLobbies.add(NewLobby);
               return new CreateLobbyReplyMessage("Lobby created", NewLobby.lobbyId).toString();
            }else{
               return new ReplyMessage("Already in a lobby",Action.ERROR).toString();
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
               return new JoinLobbyReplyMessage("Lobby Joined", message.getLobby_id()).toString();
            }else{
               return new ReplyMessage("Already in a lobby",Action.ERROR).toString();
            }
         }
         case STARTGAME -> {
            for (Lobby l:
                 gameController.allLobbies) {
               if(idLobby==l.lobbyId){
                  int b=l.Players.size();
                  int a=l.limit;
                  if(l.Players.size()==l.limit){
                     Game g = new Game(l.Players, controller);
                     gameController.allGames.put(g.id,g);
                     gameController.allLobbies.remove(l);
                     executorsService.submit(g::startGame);
                     for (Player p:
                          l.Players) {
                        p.getOut().println(new StartGameReplyMessage("Game started").toString());
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
