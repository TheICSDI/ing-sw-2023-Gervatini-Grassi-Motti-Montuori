package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;

import it.polimi.ingsw.network.messages.*;

import java.io.PrintWriter;
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

   /**
    *  ExecuteMessage takes a message from the client and provide to do the requested action for all commands divided by lobby and game commands
    *    and pass the in-game requests to the game controller writing it in the orderbook, sends a reply if needed
    * @param received_message message to elaborate
    * @param out Output stream
    * @author Marco,Andrea
    */

   public void executeMessage(GeneralMessage received_message, PrintWriter out){
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int gameId = message.getGameId();
      int idLobby = message.getLobby_id();
      //Different action based on message type
      switch(message.getAction()){
         case CREATELOBBY -> {
            //Player must not be already in game
            if (isInGame(gameController.allPlayers.get(message.getUsername()))){
               out.println(new ReplyMessage("Already in game!", Action.ERROR).toString());
            //Create a new lobby whose first player is who have called the command
            }else if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){//player cannot be in a lobby
               Player pl = gameController.allPlayers.get(message.getUsername());
               int limit=message.getLimit();//lobby has a set amount of players needed to start
               Lobby NewLobby=new Lobby(pl,limit);
               gameController.allLobbies.add(NewLobby);//add in the list of lobbies
               out.println(new CreateLobbyReplyMessage("Lobby created with id " +NewLobby.lobbyId, NewLobby.lobbyId, limit));
            }else{
               out.println(new ReplyMessage("Already in a lobby",Action.ERROR));
            }

         }
         case SHOWLOBBY -> {
            if(isInGame(gameController.allPlayers.get(message.getUsername()))) {
               out.println(new ReplyMessage("Invalid command",Action.ERROR));
            }else{
               out.println(new ShowLobbyReplyMessage("show",gameController.allLobbies));
            }


         }
         case JOINLOBBY -> {
            boolean found=false;
            //Player must not be already in game
            if(isInGame(gameController.allPlayers.get(message.getUsername()))) {
               out.println(new ReplyMessage("Already in game!",Action.ERROR));
            }else if(!isInALobby(gameController.allPlayers.get(message.getUsername()))){//verifica che il player non
               // sia in nessuna lobby
               for (Lobby l: gameController.allLobbies) {
                  if(l.lobbyId==message.getLobby_id()){//trova la lobby scelta
                     found=true;
                     l.Join(gameController.allPlayers.get(message.getUsername()));//aggiunge il nome del player ai giocatori di quella lobby
                     out.println(new JoinLobbyReplyMessage("Lobby "+l.lobbyId+" Joined",l.lobbyId));
                  }
               }
               if(!found) out.println(new ReplyMessage("Lobby does not exist",Action.ERROR));
            }else{
               out.println(new ReplyMessage("Already in a lobby",Action.ERROR));
            }
         }
         case STARTGAME -> {
            boolean notInLobby=true, gameStarted=false;
            for (Lobby l:  gameController.allLobbies) {
               if(idLobby==l.lobbyId){
                  notInLobby=false;
                  if(l.Players.size()==l.limit){
                     Game g = new Game(l.Players, controller);
                     gameController.allGames.put(g.id,g);
                     gameController.allLobbies.remove(l);
                     executorsService.submit(g::startGame);
                     for (Player p:
                          l.Players) {
                        p.getOut().println(new StartGameReplyMessage(message.getUsername() + " started the game!"));
                     }
                     gameStarted=true;
                     break; // se il game inizia le liste all lobbies e all games vengono modificate e java non gestisce un foreach su una lista che viene modificata
                  }else{
                     out.println(new ReplyMessage("Not enough ot too many players",Action.ERROR));
                  }
               }
            }
            if(!gameStarted) {
               if (isInGame(gameController.allPlayers.get(message.getUsername()))) {
                  out.println(new ReplyMessage("Already in game!", Action.ERROR));
               }else if (notInLobby) out.println(new ReplyMessage("Not in a Lobby", Action.ERROR));
            }
         }
         case PICKTILES-> {
            List<Position> pos;
            pos = ((PickTilesMessage) message).getPos();
            controller.pickTiles(player, action, pos, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
         }
         case SELECTORDER -> {
            List<Integer> order = ((SelectOrderMessage)message).getOrder();
            controller.selectOrder(player, action, order, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
         }
         case SELECTCOLUMN -> {
            int numCol = ((SelectColumnMessage) message).getCol();
            controller.selectColumn(player, action, numCol, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
         }
      }
   }

    //todo CONTROLLO CONDIZIONI MESSAGGI

   /**
    * Search player in all lobbies
    * @param p player to look for
    * @return true if found
    */
   private boolean isInALobby(Player p){
         for (Lobby l: gameController.allLobbies) {
            if(l.isPlayerInLobby(gameController.allPlayers.get(p.getNickname()))){
               return true;
            }
         }
         return false;
   }
   /**
    * Search player in all games
    * @param p player to look for
    * @return true if found
    */
   public boolean isInGame(Player p){
      for (Integer key:
           gameController.allGames.keySet()) {
         for (Player pl:
                 gameController.allGames.get(key).getPlayers()) {
            if(pl.getNickname().equals(p.getNickname())){
               return true;
            }
         }
      }
      return false;
   }
}
