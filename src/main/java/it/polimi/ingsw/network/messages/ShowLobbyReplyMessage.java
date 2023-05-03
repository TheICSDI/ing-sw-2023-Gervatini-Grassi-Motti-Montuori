package it.polimi.ingsw.network.messages;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ShowLobbyReplyMessage extends ReplyMessage {

    private List<Lobby> Lobbies;

    public ShowLobbyReplyMessage(String msg, List<Lobby> Lobbies) {
        super(msg);
        this.Lobbies=Lobbies;
    }
    @Override
    public String toString(){
//        String reply;
//        reply="{[";
//        for (Lobby l:
//             Lobbies) {
//            reply="\"Lobby_id\":"+l.lobbyId +","+
//                    "\"Players\":["
//        }
        return new Gson().toJson(this);
    }
    public static ShowLobbyReplyMessage decrypt(String json){
        return new Gson().fromJson(json,ShowLobbyReplyMessage.class);
    }

    @Override
    public void print() {
        System.out.println("Lobby disponibili: ");
        for (Lobby l:
             Lobbies) {
            System.out.print("Lobby "+l.lobbyId + ": ");
            for (Player p:
                    l.Players) {
                System.out.print(p.getNickname()+ " ");
            }
            System.out.println();
        }
    }

    /*public static void main(String[] args) {
        List<Lobby> Lobbi=new ArrayList<>();
        Lobby l=new Lobby(new Player("marco"));
        l.Join(new Player("MArcio"));
        Lobbi.add(l);
        Lobbi.add(l);
        ShowLobbyReplyMessage Prova=new ShowLobbyReplyMessage("ciao",Lobbi);
        String x=Prova.toString();
        System.out.println(x);
        ShowLobbyReplyMessage Retrieve= Prova.decrypt(x);
        System.out.println(Retrieve.getMessaggio());

    }*/
}