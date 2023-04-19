package main.java.it.polimi.ingsw.controller;

public class inputController {
   gameController gameController = new gameController();

    private void receive(String m){
        switch (m) {
            case "help" -> System.out.println("valid commands: ");
            //Print all the possible commands
            case "create lobby" -> {
                System.out.println("creating a lobby..");
                gameController.createLobby();
            }
            case "join lobby" -> {
                System.out.println("joining lobby.. ");
                gameController.joinLobby();
            }
            case "leave lobby" -> {
                System.out.println("leaving lobby.. ");
                gameController.leaveLobby();
            }
            case "start game" -> {
                System.out.println("starting game.. ");
                gameController.start();
            }
            default -> System.err.println("invalid command, write help for the list of valid command");
        }
    }
}
