
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numLobby=0;
        int joinLobby;

        Player Player1=new Player("Mayhem");
        Scanner in = new Scanner(System.in);
        String x;
        List<Lobby> Lobbies=new ArrayList<>();
        System.out.print("Enter: ");
        x=in.nextLine();
        while(!x.equals("Close")){
            switch (x){
                case "Create":
                    numLobby++;
                    Lobbies.add(new Lobby(Player1,numLobby));
                    System.out.println("Lobby creata");
                    break;
                case "Join":
                    System.out.print("Numero lobby: ");
                    joinLobby= in.nextInt();
                    for (Lobby l: Lobbies) {
                        if(l.lobbyNumber==joinLobby){
                            l.Join(Player1);
                        }
                    }
                    break;
                case "Leave":
                    for(Lobby l:Lobbies){
                        if(l.Players.contains(Player1)){
                            l.Leave(Player1);
                        }
                    }
                    break;
                case "Show":
                    availableLobbies(Lobbies);
                    break;
                case "Start":
                    System.out.println("Games Started");//Create Game, rimuovi lobby dalla lista

                    for (Lobby l:
                         Lobbies) {
                        if(l.Players.contains(Player1)){
                            List<Player> players =l.Players;
                            int nPlayers=l.Players.size();
                            Lobbies.remove(l);
                            //Thread(?)
                            Game game=new Game(players,nPlayers);

                            //startGame(players,nPlayers);
                        }
                    }
                    break;
            }

            x=in.nextLine();
        }
    }

    public static void availableLobbies(List<Lobby> Lobbies){
        for (Lobby l:Lobbies){
            l.Show();
        }
    }
    public static void startGame(List<Player> players, int nPlayers){

    }
}