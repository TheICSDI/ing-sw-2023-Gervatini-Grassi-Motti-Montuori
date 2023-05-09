package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.messages.Action;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
/*
game-controller e' una classe che ha due macro funzionalita'
la prima permette al server controller di creare dei nuovi orderbook e di aggiungerli alla lista di quelli non ancora usati
la seconda permette ai game in corso di richiedere e ottenere le informazioni che necessitano in quel momento
 */
public class gameController {
    /*
    le hash map conservano: tutti i game iniziati, i giocatori connessi abbiamo poi due liste
    una per le partite non iniziate(lobby) e una per gli orderbook, ossia i comandi inviati da input non ancora
    usati dal game
     */
    public static Map<String, Player> allPlayers = new HashMap<>();
    public static Map<Integer, Game> allGames = new HashMap<>();
    public static List<command> queue = new ArrayList<>();
    public static List<Lobby> allLobbies=new ArrayList<>();
    private final Object queueLock = new Object();
    /*
    QUESTA E LA PARTE DI METODI DEL MODEL CHE NECESSITA DI INFORMAZIONI DA FUORI E CHE LE RICHIEDE AL GAMECONTROLLER

     */
    //Ritorna il numero di colonna richiesto dalla partita
    public int chooseColumn(String player, int gameId){
        Optional<command> order;
        order = findTheRequest(player,gameId,Action.SC);
        return order.get().getNumCol();
    }
    public List<Integer> chooseOrder(String player, int gameId){
        Optional<command> order;
        order = findTheRequest(player,gameId,Action.SO);
        return order.get().getOrder();
    }
    //rimuove dall'orderbook il comando che dice quali posizioni di quali tiles ha scelto il player
    //rimuove eventuali vecchie richieste rimaste inevase nell'orderbook
    //restituisce le posizioni al model
    public  Set<Position> chooseTiles(String player , int gameId){
        Optional<command> order;
        order = findTheRequest(player,gameId,Action.PT);
        return new HashSet<>(order.get().getPos());
    }

    //trova la richiesta che match tra gli order-book, restituisce sempre un optional non null, se trova piu richieste che vanno bene
    //le cancella tutte e prende solo quella piu recente( in realta' non servirebbe ma la metto per sicurezza questa feature

    private Optional<command> findTheRequest(String player, int gameId, Action a){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        List<command> toFind;
        Optional<command> found = Optional.empty();
        //int i=0;
        while(found.isEmpty()){
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            }catch(Exception ignored){}
            List<command> toFilter = queue;
            //applica un filtro a tutti i comandi inevasi e trova quelli che corrispondono all'azione corretta del giocatore
            //corretto nel game corretto, se ne trova piu di una per un qualche errore prende la piu recente id mes piu alto
            toFind = toFilter.stream()
                    .filter(ob -> ob.g.id == g.id)
                    .filter(ob -> ob.p.getNickname().equals(p.getNickname()))
                    .filter(ob -> ob.a.equals(a))
                    .collect(Collectors.toList());
            if(!toFind.isEmpty()){// se ci sono pending piu richiesta della stessa azione dello stesso giocatore prendo la piu recente
                synchronized (queueLock) {
                    queue.removeAll(toFind);//rimuovo l'ordine scelto e anche tutti quelli residuali doppioni
                }
                found = toFind.stream()
                        .reduce((ob1,ob2) -> ob1.numMess > ob2.numMess ? ob1 : ob2);
            }
            //System.out.println(i);
            //i++;
        }
        return found;
    }





    /*
    QUELLA CHE SEGUE LA PARTE DI METODI DEL GAMECONTROLLER CHE BASANDOSI SUI PARAMETRI (ESTRATTI IN UN MOMENTO
    PRECEDENTE DAI MESSAGE RICEVUTI DAL SERVER) MODIFICA E CHIAMA I METODI DEL MODEL
     */


    public void pickTiles(String player, Action action, List<Position> pos, int id_game,int num_mess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(id_game);
        command pending = new command(g,p,action,num_mess);
        pending.setPos(pos);
        synchronized (queueLock) {
            queue.add(pending);
        }

    }
    public  void selectOrder(String player, Action action, List<Integer> order, int gameId, int num_mess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g,p,action,num_mess);
        pending.setOrder(order);
        synchronized (queueLock) {
            queue.add(pending);
        }
    }
    public  void selectColumn(String player, Action action, int numCol, int gameId, int num_mess){
        Player p = allPlayers.get(player);
        Game g = allGames.get(gameId);
        command pending = new command(g,p,action,num_mess);
        pending.setNumCol(numCol);
        synchronized (queueLock) {
            queue.add(pending);
        }
    }


    /*permette di uscire da una partita a patto che non sia cominciata

    public void leaveLobby(String player, int gameId) throws GameStartedException {
        Game g = allGames.get(gameId);
        Player p = allPlayers.get(player);
        if(g.isStarted()){
            g.getPlayers().remove(p);
        }
        else{
            throw new GameStartedException("The game is started you can't leave now!");
        }
    }

     */



}
