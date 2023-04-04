package main.java.it.polimi.ingsw.model;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numLobby=0;
        int joinLobby;

        Player Player1=new Player(1, "Mayhem");
        Scanner in = new Scanner(System.in);
        String Interation_Prompt;
        List<Lobby> Lobbies=new ArrayList<>();
        System.out.print("Enter: ");
        Interation_Prompt=in.nextLine();
        while(!Interation_Prompt.equals("Close")){
            switch (Interation_Prompt){
                case "Create":
                    numLobby++;
                    Lobbies.add(new Lobby(Player1,numLobby));
                    System.out.println("main.java.polimi.ingsw.Model.Lobby creata");
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
                    System.out.println("Games Started");//Create main.java.polimi.ingsw.Model.Game, rimuovi lobby dalla lista

                    for (Lobby l:
                         Lobbies) {
                        if(l.Players.contains(Player1)){
                            List<Player> players =l.Players;
                            Lobbies.remove(l);
                            //Thread(?)
                            Game game=new Game("G1", players);
                        }
                    }
                    break;
            }

            Interation_Prompt=in.nextLine();
        }
    }

    public static void availableLobbies(List<Lobby> Lobbies){
        for (Lobby l:Lobbies){
            l.Show();
        }
    }

}