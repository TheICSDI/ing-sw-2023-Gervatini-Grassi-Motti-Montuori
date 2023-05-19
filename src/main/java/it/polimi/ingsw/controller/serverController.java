/** It interacts with the server and the gameController, in order to modify the instance of the game.
 * @author Caterina Motti, Andrea Grassi, Marco Gervatini. */
package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidActionException;
import it.polimi.ingsw.exceptions.InvalidKeyException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.server.RMIconnection;
import it.polimi.ingsw.network.server.connectionType;
import org.json.simple.parser.ParseException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class serverController {
   public static Map<String, connectionType>  connections = new HashMap<>();
   public static gameController controller = new gameController();

   //Each thread of this pool allows maximum 10 games to be played at the same time.
   ExecutorService executorsService = Executors.newFixedThreadPool(10);

   /** It takes a message from the client and does the requested action.
    * It sends a reply if needed.
    * @param message the received message to be elaborated
    */
   public void executeMessage(GeneralMessage message) throws RemoteException {
      //constants to lighten the code
      final int id = message.getMessage_id();
      final int gameId = message.getIdGame();
      final int idLobby = message.getIdLobby();
      final Action action = message.getAction();
      final String player = message.getUsername();

      //Based on the message's action type
      switch(action){
         //It creates a new lobby whose first player is who have called the command
         case CREATELOBBY -> {
            //It checks if the player is already in a game or in a lobby
            if (isInAGame(gameController.allPlayers.get(player))){
               sendMessage(new ReplyMessage("Already in a game!", Action.ERROR), player);
            } else if(isInALobby(gameController.allPlayers.get(player))){
               sendMessage(new ReplyMessage("Already in a lobby!",Action.ERROR), player);
            } else {
               //Otherwise it creates the lobby
               Player pl = gameController.allPlayers.get(player);
               int limit = message.getLimit();//lobby has a set amount of players needed to start
               Lobby NewLobby = new Lobby(pl, limit);
               gameController.allLobbies.add(NewLobby);
               sendMessage(new CreateLobbyReplyMessage("Lobby created with id " + NewLobby.lobbyId, NewLobby.lobbyId, limit), player);
            }
         }

         //It shows all the available lobbies
         case SHOWLOBBY -> {
            //If the player is already in a game the command cannot be executed
            if(isInAGame(gameController.allPlayers.get(player))) {
               sendMessage(new ReplyMessage("Invalid command", Action.ERROR), player);
            } else {
               sendMessage(new ShowLobbyReplyMessage("Show", gameController.allLobbies), player);
            }
         }

         //It makes the player join a designated lobby
         case JOINLOBBY -> {
            boolean found = false;
            //It checks if the player is already in a game or in a lobby
            if(isInAGame(gameController.allPlayers.get(player))) {
               sendMessage(new ReplyMessage("Already in a game!", Action.ERROR), player);
            }else if(isInALobby(gameController.allPlayers.get(player))){
               sendMessage(new ReplyMessage("Already in a lobby!", Action.ERROR), player);
            }else{
               //Otherwise, it found the chosen lobby by the given id, and it added the player
               for (Lobby l: gameController.allLobbies) {
                  if(l.lobbyId == idLobby){
                     found = true;
                     try{
                        l.Join(gameController.allPlayers.get(player));
                        sendMessage(new JoinLobbyReplyMessage("Lobby "+ l.lobbyId +" joined", l.lobbyId), player);
                     }catch(InputMismatchException x){
                        sendMessage(new ReplyMessage("The selected lobby is full!", Action.ERROR), player);
                     }
                  }
               }
               //If the lobby is not found it is reported to the client
               if(!found){
                  sendMessage(new ReplyMessage("The selected lobby does not exist!", Action.ERROR), player);
               }
            }
         }

         //It starts a game
         case STARTGAME -> {
            boolean notInLobby = true, gameStarted = false;
            //It checks each lobby in gameController
            for (Lobby l: gameController.allLobbies) {
               if(idLobby == l.lobbyId){
                  //If it found the lobby of the player
                  notInLobby = false;
                  //If the lobby is full, the game is created
                  if(l.Players.size() == l.limit){
                     Game g = new Game(l.Players, controller);
                     gameController.allGames.put(g.id,g);
                     gameController.allLobbies.remove(l);
                     for (Player p: l.Players) {
                        sendMessage(new StartGameReplyMessage(player + " started the game!" ,
                                g.id), p.getNickname());
                     }
                     executorsService.submit(() ->{
                        try{
                           g.startGame();
                        } catch (RemoteException | InterruptedException e) {
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
            //Otherwise it checks if the player is already in a game or is not in a lobby
            if(!gameStarted) {
               if (isInAGame(gameController.allPlayers.get(player))) {
                  sendMessage(new ReplyMessage("Already in game!", Action.ERROR), player);
               } else if (notInLobby){
                  sendMessage(new ReplyMessage("Not in a Lobby!", Action.ERROR), player);
               }
            }
         }

         //Pick some tiles from the main board
         case PT -> {
            System.out.println("Operating the pick of tiles");
            List<Position> pos = new ArrayList<>();
            ((PickTilesMessage)message).getPos(pos);
            controller.pickTiles(player, action, pos, gameId, id);
         }

         //Select the order of the selected tiles
         case SO -> {
            List<Integer> order = new ArrayList<>();
            ((SelectOrderMessage)message).getOrder(order);
            controller.selectOrder(player, action, order, gameId, id);
         }

         //Select che column in which to put the tiles
         case SC -> {
            int numCol = ((SelectColumnMessage)message).getCol();
            controller.selectColumn(player, action, numCol, gameId, id);
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
                        if(p.getNickname().equals(player)) {
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

   /** It gets a nickname from the server via RMI connection.
    * If the nickname is not already taken it is putted in the list of players (in gameController) and in the map of
    * connections (in this class). Otherwise, it replies with false. */
   public void getName(String input, RMIconnection reply) throws ParseException, InvalidKeyException, InvalidActionException, RemoteException {
      GeneralMessage mex;
      mex = SetNameMessage.decrypt(input);
      if(gameController.allPlayers.containsKey(mex.getUsername())){
          //Nickname already taken
          reply.RMIsendName(new SetNameMessage("Nickname already taken!",false).toString(), null);//TODO funziona ma a volte tira una serie di errori così potente che mi esplode il pc
      }else{
          //Nickname available
          reply.RMIsendName(new SetNameMessage(mex.getUsername(),true).toString(), null);
          gameController.allPlayers.put(mex.getUsername(), new Player(mex.getUsername()));
          connections.put(mex.getUsername(), new connectionType(false,null,reply));
          ExecutorService executor = Executors.newSingleThreadExecutor();
          executor.submit(() ->{
                int x=-1;
                while(x<connections.get(mex.getUsername()).getPing()) {
                   x=connections.get(mex.getUsername()).getPing();
                   //System.out.println("Ping n^" + connections.get(mex.getUsername()).getPing());
                   try {
                      TimeUnit.SECONDS.sleep(30);
                   } catch (InterruptedException e) {
                      throw new RuntimeException(e);
                   }
                }
                System.out.println(mex.getUsername() + " disconnected");
                gameController.allPlayers.get(mex.getUsername()).setConnected(false);
          });

      }
   }

   /** It gets a message from the server, and it calls the method execute. */
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
         case C -> mex = ChatMessage.decrypt(input);
         case CA -> mex = BroadcastMessage.decrypt(input);
         case PING-> {
            mex = PingMessage.decrypt(input);
            connections.get(mex.getUsername()).addPing();
            sendMessage(new PingMessage(""), mex.getUsername());
         }
      }
      //If the message is valid the command is executed by the serverController
      if (!(mex == null)) {
         this.executeMessage(mex);
      }
   }

   /** It sends a message to a designated client.
    * @param m the message to be sent.
    * @param nick the client. */
   public static void sendMessage(GeneralMessage m, String nick) throws RemoteException {
      if(connections.get(nick).isSocket()){
         System.out.println("Sending message "+ m.getAction() + " to " + nick);
         connections.get(nick).getOut().println(m);
      } else {
         connections.get(nick).getReply().RMIsend(m.toString());
      }
   }

}
