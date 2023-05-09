/** It interacts with the server and the gameController, in order to modify the instance of the game. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.*;
import org.json.simple.parser.ParseException;
import it.polimi.ingsw.network.server.RMIconnection;
import it.polimi.ingsw.network.server.RMIserverImpl;
import it.polimi.ingsw.network.server.connectionType;
import org.json.simple.parser.ParseException;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class serverController {
   public static Map<String, connectionType>  connections = new HashMap<>();

   public gameController controller = new gameController();
   GeneralMessage message;
   private RMIserverImpl sRMI;

   /** Each thread of this pool allows maximum 10 games to be played at the same time. */
   ExecutorService executorsService = Executors.newFixedThreadPool(10);

   /** It takes a message from the client and does the requested action for all commands divided by lobby
    * and game commands and pass the in-game requests to the game controller writing it in the orderBook.
    * It sends a reply if needed
    * @param received_message message to elaborate
    * @author Marco,Andrea
    */
   public void executeMessage(GeneralMessage received_message) throws RemoteException {
      message = received_message;
      int id = message.getMessage_id();
      String player = message.getUsername();
      Action action = message.getAction();
      int gameId = message.getIdGame();
      int idLobby = message.getLobby_id();

      //Based on the message's action type
      switch(message.getAction()){

         //It creates a new lobby whose first player is who have called the command
         case CREATELOBBY -> {
            //It checks if the player is already in a game or in a lobby
            if (isInAGame(gameController.allPlayers.get(message.getUsername()))){
               sendMessage(new ReplyMessage("Already in game!", Action.ERROR), player);
            } else if(isInALobby(gameController.allPlayers.get(message.getUsername()))){
               sendMessage(new ReplyMessage("Already in a lobby",Action.ERROR), player);
            } else {
               //Otherwise it creates the lobby
               Player pl = gameController.allPlayers.get(message.getUsername());
               int limit = message.getLimit();//lobby has a set amount of players needed to start
               Lobby NewLobby = new Lobby(pl, limit);
               gameController.allLobbies.add(NewLobby);
               sendMessage(new CreateLobbyReplyMessage("Lobby created with id " + NewLobby.lobbyId, NewLobby.lobbyId, limit), player);
            }

         }

         //It shows all the available lobbies
         case SHOWLOBBY -> {
            //If the player is already in a game the command cannot be executed
            if(isInAGame(gameController.allPlayers.get(message.getUsername()))) {
               sendMessage(new ReplyMessage("Invalid command",Action.ERROR), player);
            } else {
               sendMessage(new ShowLobbyReplyMessage("Show", gameController.allLobbies), player);
            }
         }

         case JOINLOBBY -> {
            boolean found=false;
            //It checks if the player is already in a game or in a lobby
            if(isInAGame(gameController.allPlayers.get(message.getUsername()))) {
               sendMessage(new ReplyMessage("Already in a game!", Action.ERROR), player);
            }else if(isInALobby(gameController.allPlayers.get(message.getUsername()))){
               sendMessage(new ReplyMessage("Already in a lobby!", Action.ERROR), player);
            }else{
               //Otherwise, it found the chosen lobby by the given id, and it added the player
               for (Lobby l: gameController.allLobbies) {
                  if(l.lobbyId==message.getLobby_id()){
                     //TODO: non va controllato il limite di player per lobby? cioè se una lobby è piena cosa succede?
                     found = true;
                     l.Join(gameController.allPlayers.get(message.getUsername()));
                     sendMessage(new JoinLobbyReplyMessage("Lobby "+ l.lobbyId +" joined", l.lobbyId), player);
                  }
               }
               //If the lobby is not found it is reported to the client
               if(!found){
                  sendMessage(new ReplyMessage("Lobby does not exist", Action.ERROR), player);
               }
            }
         }

         //It starts a game
         case STARTGAME -> {
            boolean notInLobby=true, gameStarted=false;
            //It checks each lobby in gameController
            for (Lobby l: gameController.allLobbies) {
               if(idLobby==l.lobbyId){
                  notInLobby=false;
                  if(l.Players.size()==l.limit){
                     Game g = new Game(l.Players, controller);
                     gameController.allGames.put(g.id,g);
                     gameController.allLobbies.remove(l);
                     for (Player p: l.Players) {
                        sendMessage(new StartGameReplyMessage(message.getUsername() + " started the game!" , g.id), p.getNickname());
                     }
                     executorsService.submit(() ->{
                        try{
                           g.startGame();
                        } catch (RemoteException e) {
                           throw new RuntimeException(e);
                        }
                     });
                     gameStarted=true;
                     break; // se il game inizia le liste all lobbies e all games vengono modificate e java non gestisce un foreach su una lista che viene modificata
                  }else{
                     sendMessage(new ReplyMessage("Not enough or too many players", Action.ERROR), player);
                  }
               }
            }
            //If the game hasn't started it checks if the player is already in a game or is not in a lobby
            if(!gameStarted) {
               if (isInAGame(gameController.allPlayers.get(message.getUsername()))) {
                  sendMessage(new ReplyMessage("Already in game!", Action.ERROR), player);
               } else if (notInLobby){
                  sendMessage(new ReplyMessage("Not in a Lobby!", Action.ERROR), player);
               }
            }
         }

         case PT -> {
            List<Position> pos=new ArrayList<>();
            ((PickTilesMessage) message).getPos(pos);
            controller.pickTiles(player, action, pos, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
         }

         case SO -> {
            List<Integer> order = new ArrayList<>();
            ((SelectOrderMessage)message).getOrder(order);
            controller.selectOrder(player, action, order, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
         }

         case SC -> {
            int numCol = ((SelectColumnMessage) message).getCol();
            controller.selectColumn(player, action, numCol, gameId, id);
            //manda un ok che rappresenta l'inoltro con successo all'interno della partita
            //verra' dopo confermato se le cose scritte nel messaggio erano corrette o se va riscritto
         }

         //Single chat with a specified player
         case C ->{
            String recipient = ((ChatMessage)message).getRecipient();
            sendMessage(message,recipient);
         }

         //Broadcast chat with the lobby or the game in which the player is
         case CA -> {
            //If the player is in a lobby
            if(idLobby>0){
               for(Lobby l: gameController.allLobbies){
                  if(l.lobbyId == idLobby){
                     //It sends the message to each player in lobby
                     for(Player p : l.Players){
                        if(!p.getNickname().equals(player)) {
                           sendMessage(message, p.getNickname());
                        }
                     }
                     break;
                  }
               }
            } else if(gameId>0){
               //If the player is in a game, it sends the message to each player
               Game g = gameController.allGames.get(gameId);
               for(Player p: g.getPlayers()){
                  if(!p.getNickname().equals(player)){
                     sendMessage(message, p.getNickname());
                  }
               }
            }else{
               //Otherwise there is no one to send the message to
               sendMessage(new ReplyMessage("No one to send to",Action.ERROR),player);
            }
         }
      }
   }

   /**
    * It searches a given player in all lobbies.
    * @param p player to look for.
    * @return true only if the player is in a lobby, false otherwise.
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
    * It searches a given player in all games.
    * @param p player to look for.
    * @return true only if the player is in a game, false otherwise.
    */
   public boolean isInAGame(Player p){
      for (Integer key: gameController.allGames.keySet()) {
         for (Player pl: gameController.allGames.get(key).getPlayers()) {
            if(pl.getNickname().equals(p.getNickname())){
               return true;
            }
         }
      }
      return false;
   }

   public void getName(String input, RMIconnection reply) throws ParseException, InvalidKeyException, InvalidActionException, RemoteException {
      GeneralMessage mex;
      System.out.println(input);
      mex = SetNameMessage.decrypt(input);
      if(gameController.allPlayers.containsKey(mex.getUsername())){

          //Nickname already taken
          reply.RMIsendName(new SetNameMessage("Taken",false).toString(), null);
      }else{
          //Nickname available
          reply.RMIsendName(new SetNameMessage(mex.getUsername(),true).toString(), null);
          gameController.allPlayers.put(mex.getUsername(), new Player(mex.getUsername()));
          connections.put(mex.getUsername(), new connectionType(false,null,reply));
      }
   }

   public void getMessage(String input) throws ParseException, InvalidKeyException, InvalidActionException, RemoteException {
      GeneralMessage mex = null;
      switch (GeneralMessage.identify(input)){
         case CREATELOBBY -> mex = new CreateLobbyMessage(input);
         case SHOWLOBBY -> mex = new ShowLobbyMessage(input);
         case JOINLOBBY -> mex = new JoinLobbyMessage(input);
         case STARTGAME -> mex = new StartGameMessage(input);
         case PT -> mex = new PickTilesMessage(input);
         case SO -> mex = new SelectOrderMessage(input);
         case SC -> mex = new SelectColumnMessage(input);
      }
      //If the message is valid the command is executed by the serverController
      if(!(mex == null)){
         this.executeMessage(mex);
      }
   }

   public static void sendMessage(GeneralMessage m, String nick) throws RemoteException {
      if(connections.get(nick).isSocket()){
         System.out.println(m.toString());
         connections.get(nick).getOut().println(m);
      } else {
         connections.get(nick).getReply().RMIsend(m.toString());
      }
   }

}
