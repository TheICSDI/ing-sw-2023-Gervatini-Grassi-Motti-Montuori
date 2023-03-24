import java.util.*;

public class Lobby {
    int lobbyNumber;
    List<Player> Players = new ArrayList<>();

    public Lobby(Player Creator,int name){
        lobbyNumber=name;
        Players.add(Creator);
    }
    public void Join(Player Join){
        Players.add(Join);
    }
    public void Leave(Player Leaver){Players.remove(Leaver);}
    public void Show(){
        System.out.print("Lobby"+this.lobbyNumber + ":");
        for (Player p: Players) {
            System.out.print(" " +p.Nickname);
        }
        System.out.println("");
    }

}


// Lobby1 : Mayhem, Fozy
// LObby2: Andrea Grassi, Andrew FAts, AndreDeGrass
// Lobby3: Gynephobia
