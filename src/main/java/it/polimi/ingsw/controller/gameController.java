/** The clients have to call the controller's method to interact with the game's model.*/
package main.java.it.polimi.ingsw.controller;

import main.java.it.polimi.ingsw.exceptions.*;
import main.java.it.polimi.ingsw.model.Game;
import main.java.it.polimi.ingsw.model.Player;
import main.java.it.polimi.ingsw.model.Position;
import main.java.it.polimi.ingsw.network.messages.Action;

import java.util.*;
import java.util.stream.Collectors;

public class gameController {
    public static List<orderBook> pending_orders = new ArrayList<>();
    //mettiamo delle hash map per tutti i giocatori e tutti i game attualmente in svolgimento, in modo da ricercare
    // piu velocemente queste informazioni quando il game-controller deve capire per conto di chi sta effettuando le modifiche
    //la chiave dei player è il nick, quella dei game è l'id e ogni volta che un giocatore entra/ esce o si comincia/finisce una
    //partita allora si va a modificare la hashmap
    public static Map<String, Player> all_players = new HashMap<>();
    public static Map<Integer, Game> all_games = new HashMap<>();

    /*
    QUESTA E LA PARTE DI METODI DEL MODEL CHE NECESSITA DI INFORMAZIONI DA FUORI E CHE LE RICHIEDE AL GAMECONTROLLER

     */

    //RIROTNA IL NUMERO DI COLONNA RICHIESTO NELLA PARTITA
    public int ChooseColumn(String player, int gameId){
        Optional<orderBook> order;
        order = findTheRequest(player,gameId,Action.PICKTILES);
        return order.get().getNum_col();
    }

    //rimuove dall'orderbook il comando che dice quali posizioni di quali tiles ha scelto il player
    //rimuove eventuali vecchie richieste rimaste inevase nell'orderbook
    //restituisce le posizioni al model
    public Set<Position> Choose(String player , int gameId){
        Optional<orderBook> order;
        order = findTheRequest(player,gameId,Action.PICKTILES);
        return new HashSet<>(order.get().getPos());
    }

    //trova la richiesta che match tra gli orderbook, restituisce sempre un optional non null, se trova piu richieste che vanno bene
    //le cancella tutte e prende solo quella piu recente ( in realta non servirebbe ma la metto per sicurezza questa feature
    public Optional<orderBook> findTheRequest(String player,int gameId, Action a){
        Player p = all_players.get(player);
        Game g = all_games.get(gameId);
        List<orderBook> toFind;
        Optional<orderBook> found = Optional.empty();
        while(found.isEmpty()){
            List<orderBook> toFilter = pending_orders;
            //applica un filtro a tutti i comandi inevasi e trova quelli che corrispondono all'azione corretta del giocatore
            //corretto nel game corretto, se ne trova piu di una per un qualche errore prende la piu recente id mes piu alto
            toFind = toFilter.stream()
                    .filter(ob -> ob.g.id == g.id)
                    .filter(ob -> ob.p.getNickname().equals(p.getNickname()))
                    .filter(ob -> ob.a.equals(a))
                    .collect(Collectors.toList());
            if(!toFind.isEmpty()){// se ci sono pending piu richiesta della stessa azione dello stesso giocatore prendo la piu recente
                pending_orders.removeAll(toFind);//rimuovo l'ordine scelto e anche tutti quelli residuali doppioni
                found = toFind.stream()
                        .reduce((ob1,ob2) -> ob1.num_mess > ob2.num_mess ? ob1 : ob2);
            }
        }
        return found;
    }




    /*
    QUELLA CHE SEGUE LA PARTE DI METODI DEL GAMECONTROLLER CHE BASANDOSI SUI PARAMETRI (ESTRATTI IN UN MOMENTO
    PRECEDENTE DAI MESSAGE RICEVUTI DAL SERVER) MODIFICA E CHIAMA I METODI DEL MODEL
     */


    public void pickTiles(String player, Action action, ArrayList<Position> pos, int id_game,int num_mess) throws NotAvaibleTilesException {
        Player p = all_players.get(player);
        Game g = all_games.get(id_game);
        orderBook pending = new orderBook(g,p,action,num_mess);
        pending.setPos(pos);
        pending_orders.add(pending);

    }
    //se il giocatore non e' in una partita ne crea una nuova questa viene aggiunta all'elenco dei game disponibili
    public void createLobby(String player) throws AlreadyInAGameException {
        for(Game g : all_games.values()){
            if(g.getPlayers().contains(all_players.get(player))){
                throw new AlreadyInAGameException("The player is already in a game");
            }
        }
        List<Player> onlyTheFirstList = new ArrayList<>();
        onlyTheFirstList.add(all_players.get(player));
        Game g = new Game(onlyTheFirstList);
        all_games.put(g.id,g);

    }

    public void joinLobby(String player, int gameId)throws AlreadyInAGameException{
        Game game = all_games.get(gameId);
        Player p = all_players.get(player);
        for(Game g : all_games.values()){
            if(g.getPlayers().contains(all_players.get(player))){
                throw new AlreadyInAGameException("The player is already in a game");
            }
        }
        try {
            game.addPlayer(p);
        } catch (CannotAddPlayerException e) {
            throw new RuntimeException(e);
        }
    }
    //permette di uscire da una partita a patto che non sia cominciata
    public void leaveLobby(String player, int gameId) throws GameStartedException {
        Game g = all_games.get(gameId);
        Player p = all_players.get(player);
        if(g.isStarted()){
            g.getPlayers().remove(p);
        }
        else{
            throw new GameStartedException("The game is started you can't leave now!");
        }
    }

    public void startGame(String player, int idGame) throws InvalidColumnException, InvalidPositionException {
        Game g = all_games.get(idGame);
        Player p = all_players.get(player);
        if(g.getPlayers().contains(p)){
            if(g.isStarted()){
                g.startGame();
            }
        }
    }

}
