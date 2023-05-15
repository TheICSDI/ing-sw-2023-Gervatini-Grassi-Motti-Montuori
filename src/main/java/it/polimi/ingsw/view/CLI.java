package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CLI implements View{
    //"\u001b[48;2;<R code>;<G code>;<B code>m" BACKGROUND COLORS
    public static final String RESET = "\033[0m";

    public static final String DARK_BROWN= "\u001b[48;2;117;61;34m";

    private final PrintStream out;

    public CLI(){
        this.out = System.out;
    }

    @Override
    public String showMain(){
        out.println("\u001b[34mWelcome to MyShelfie!\u001b[0m");
        String reply;
        do {
            System.out.println("""
                Choose connection type:\s
                [1]: for Socket
                [2]: for RMI""");
            Scanner input = new Scanner(System.in);
            reply = input.next();
        }while(!(reply.equals("1") || reply.equals("2")));
        if (reply.equals("1")) {
            System.out.println("Socket connection chosen");
        } else {
            System.out.println("RMI connection chosen");
        }
        return reply;
    }
    @Override
    public String askUsername(){
        Scanner input = new Scanner(System.in);
        out.print("Enter your nickname: ");
        return input.nextLine();
    }

    @Override
    public void printUsername(String username, boolean isAvailable){
        if(isAvailable) {
            out.println("Username set correctly\n");
        }
        else {
            out.println("Username not available\n");
        }
    }

    @Override
    public void createLobby(String lobbyName/*, int maxPlayers*/) {
        System.out.println(lobbyName);
    }

    @Override
    public void showBoard(type[][] board,Action action){
        Tile[][] shelf=recreateShelf(board);
        printBoard(shelf,action);
    }

    public void printBoard(Tile[][] shelf,Action action){
        if(action.equals(Action.UPDATEBOARD)){
            System.out.println("    0  1  2  3  4  5  6  7  8");
        }
        for (int i = 0; i < shelf.length; i++){  //stampa della board
            if(action.equals(Action.UPDATESHELF)) {
                out.print(RESET + "  ");
            }
            for (int j = -1; j < shelf[0].length; j++) {
                if(action.equals(Action.UPDATEBOARD)){
                    if(j==-1){
                        out.print(" " + i + " ");
                    }else {
                        out.print(shelf[i][j].getColor() + " " + shelf[i][j].getInitial() + " ");
                    }
                }else if(action.equals(Action.UPDATESHELF)){
                    if(j==-1) j++;
                    out.print(shelf[i][j].getColor() + " " + shelf[i][j].getInitial() + " ");
                    if(j< shelf[0].length - 1){
                        out.print(DARK_BROWN+" ");
                    }
                }
            }
            out.println(RESET);
        }
        if(action.equals(Action.UPDATESHELF)){
            out.println("\033[48;5;94m   1   2   3   4   5   " + RESET);
        }
        out.println();
    }
    @Override
    public void showChosenTiles(List<Tile> tiles){
        out.print("Tiles chosen:");
        for (Tile t:
             tiles) {
            out.print(RESET + " " + t.getColor() + " " + t.getInitial() + " " );
        }
        out.println(RESET);
    }
    
    @Override
    public void showCommons(List<Integer> cc){
        int x=1;
        for (int i:
             cc) {
            switch (i){
                case(1)-> out.println("Goal " + x + ": Six groups each containing at least\n" +
                        "2 tiles of the same type (not necessarily\n" +
                        "in the depicted shape).\n" +
                        "The tiles of one group can be different\n" +
                        "from those of another group.");
                case(2)-> out.println("Goal " + x + ": Four tiles of the same type in the four\n" +
                        "corners of the bookshelf. ");
                case(3)-> out.println("Goal " + x + ": Four groups each containing at least\n" +
                        "4 tiles of the same type (not necessarily\n" +
                        "in the depicted shape).\n" +
                        "The tiles of one group can be different\n" +
                        "from those of another group");
                case(4)-> out.println("Goal " + x + ": Two groups each containing 4 tiles of\n" +
                        "the same type in a 2x2 square. The tiles\n" +
                        "of one square can be different from\n" +
                        "those of the other square.");
                case(5)-> out.println("Goal " + x + ": Three columns each formed by 6 tiles Five tiles of the same type forming an X.\n" +
                        "of maximum three different types. One\n" +
                        "column can show the same or a different\n" +
                        "combination of another column.");
                case(6)-> out.println("Goal " + x + ": Eight tiles of the same type. There’s no\n" +
                        "restriction about the position of these\n" +
                        "tiles.");
                case(7)-> out.println("Goal " + x + ": Five tiles of the same type forming a\n" +
                        "diagonal.");
                case(8)-> out.println("Goal " + x + ": Four lines each formed by 5 tiles of\n" +
                        "maximum three different types. One\n" +
                        "line can show the same or a different\n" +
                        "combination of another line.");
                case(9)-> out.println("Goal " + x + ": Two columns each formed by 6\n" +
                        "different types of tiles. ");
                case(10)-> out.println("Goal " + x + ": Two lines each formed by 5 different\n" +
                        "types of tiles. One line can show the\n" +
                        "same or a different combination of the\n" +
                        "other line.");
                case(11)-> out.println("Goal " + x + ": Five tiles of the same type forming an X.");
                case(12)-> out.println("Goal " + x + ": Five columns of increasing or decreasing\n" +
                        "height. Starting from the first column on\n" +
                        "the left or on the right, each next column\n" +
                        "must be made of exactly one more tile.\n" +
                        "Tiles can be of any type. ");

            }
            out.println();
            x++;
        }
    }

    /*
    @Override
    public void createLobby(String lobbyName, int maxPlayers){

    }
     */

    @Override
    public void showLobby(List<Lobby> Lobbies){
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

    @Override
    public void showLobby(List<String> usrs, int num_usrs) {

    }

    @Override
    public void showOthers(Map<String,Player> others){
        for (String nick:others.keySet()) {
            System.out.println("  " + nick + "'s shelf");
            printBoard(others.get(nick).getShelf(),Action.UPDATESHELF);
        }
    }

    @Override
    public void joinLobby(int lobby_id){

    }

    @Override
    public void exitLobby(int lobby_id){

    }

    @Override
    public void startGame(int lobby_id){

    }

    @Override
    public void pickTiles(int lobby_id, Position pos){

    }

    @Override
    public void selectOrder(int lobby_id, List<Integer> order){

    }

    @Override
    public void selectColumn(int lobby_id, int col){

    }

    @Override
    public void updateBoard(int lobby_id, Board board){

    }

    // void endGame(int lobbyId, List<PlayerScore> scores); // da controllare input

    @Override
    public void updateLobby(int lobby_id, List<String> players){

    }

    @Override
    public void displayError(String msg){

    }

    @Override
    public void displayMessage(String msg){
        out.println(msg);
    }

    @Override
    public void help(){
        out.println("""
                \u001B[31mThis is the command list:\s
                createlobby <Number of players>
                joinlobby <Lobby id>
                showlobby
                exitlobby (WIP)
                C <Player's name> <message>
                CA <message>
                startgame
                pt <Row1> <Column1> (up to three tiles)
                so <First> <Second> <Third>
                sc <ShelfColumn>
                showothers (WIP)
                showpersonal
                showcommons
                exit (WIP)\u001B[0m""");
    }


    @Override
    public String getInput() {
        Scanner input=new Scanner(System.in);
        return input.nextLine();
    }

    public Tile[][] recreateShelf(type[][] simpleShelf){
        Tile[][] shelf=new Tile[simpleShelf.length][simpleShelf[0].length];
        for (int i = 0; i < simpleShelf.length; i++) {
            for (int j = 0; j < simpleShelf[0].length; j++) {
                shelf[i][j]=new Tile(String.valueOf(simpleShelf[i][j]));
            }
        }
        return shelf;
    }

}
