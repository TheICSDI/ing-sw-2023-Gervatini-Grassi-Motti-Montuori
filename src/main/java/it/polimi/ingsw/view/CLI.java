package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** It implements the view interface managing CLI's output.*/
public class CLI implements View{
    //"\u001b[48;2;<R code>;<G code>;<B code>m" COLORS
    public static final String RESET = "\033[0m";
    public static final String DARK_BROWN= "\u001b[48;2;117;61;34m";
    private final static String RED = "\u001B[31m";
    private final PrintStream out;

    public CLI(){
        this.out = System.out;
    }

    @Override
    public String chooseConnection(){
        out.println("\u001b[34mWelcome to MyShelfie!\u001b");
        String reply;
        do {
            System.out.println("""
                Choose connection type and ip address:\s
                For local host just the type of connection
                [1]: for Socket
                [2]: for RMI""");
            Scanner input = new Scanner(System.in);
            reply = input.next();
        } while(!(reply.equals("1") || reply.equals("2")));
        if (reply.equals("1")) {
            System.out.println("Socket connection chosen");
        } else {
            System.out.println("RMI connection chosen");
        }
        return reply;
    }

    @Override
    public String askIP(){
        // Regex Pattern for well formatted IPv4
        Pattern correct_ip = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^0$");
        boolean correct = false;
        Matcher matcher;
        String ip;
        do {
            System.out.println("""
                            Select IPv4 address
                            For localhost you can also write 0""");
            Scanner input = new Scanner(System.in);
            ip = input.next();
            // The matcher() method is used to search for the pattern in a string.
            matcher = correct_ip.matcher(ip);
            // The find() method returns true if the pattern was found in the string and false if it was not found.
            correct = matcher.find();
        } while(!correct);
        if(ip.equals("0")){
            ip = "127.0.0.1";
        }
        System.out.println(ip + " chosen");
        return ip;
    }

    @Override
    public String askNickname(){
        Scanner input = new Scanner(System.in);
        out.print("Enter your nickname: ");
        return input.nextLine();
    }

    @Override
    public void checkNickname(String nickname, boolean isAvailable){
        if(isAvailable) {
            out.println("Username set correctly\n");
            out.println("Write /help to see commands list.");
        } else {
            out.println("Username not available\n");
        }
    }

    @Override
    public void showLobby(List<Lobby> Lobbies){
        System.out.println("Available lobbies: ");
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
    public void startGame(String message){
        System.out.println(message);
    }

    @Override
    public void playersTurn(String msg, boolean firstTurn){
        System.out.println(msg);
    }

    @Override
    public void showBoard(Tile[][] board){
        System.out.println("    0  1  2  3  4  5  6  7  8");
        //It prints out the board
        for (int i = 0; i < board.length; i++){
            for (int j = -1; j < board[0].length; j++) {
                if(j==-1){
                    out.print(" " + i + " ");
                }else {
                    out.print(board[j][i].getColor() + " " + board[j][i].getInitial() + " ");
                }
            }
            out.println(RESET);
        }
        out.println();
    }

    @Override
    public void showShelf(Tile[][] shelf){
        out.println("  Your shelf:  ");
        printShelf(shelf);
    }

    @Override
    public void showPersonal(PersonalCard PC){
        out.println("  Your personal goal:  ");
        printShelf(PC.getCard());
    }

    /**
     * Show a generic shelf. Used for both shelves and personal goal cards.
     * @param shelf to be showed.
     */
    private void printShelf(Tile[][] shelf){
        for (int i = 0; i < shelf.length; i++){
            out.print(RESET + "  ");
            for (int j = -1; j < shelf[0].length; j++) {
                if(j==-1) j++;
                out.print(shelf[i][j].getColor() + " " + shelf[i][j].getInitial() + " ");
                if(j< shelf[0].length - 1){
                    out.print(DARK_BROWN+" ");
                }
            }
            out.println(RESET);
        }
        out.println("\033[48;5;94m   1   2   3   4   5   " + RESET);
        out.println();
    }

    @Override
    public void showCommons(List<Integer> cc){
        out.println("Common goals:");
        int x = 1;
        for (int i: cc) {
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

    @Override
    public void showChosenTiles(List<Tile> tiles,boolean toOrder){
        out.print("Tiles chosen:");
        for (Tile t:
             tiles) {
            out.print(RESET + " " + t.getColor() + " " + t.getInitial() + " " );
        }
        out.println(RESET);
    }

    @Override
    public void commonCompleted(String msg, String whoCompleted, boolean first) {
        System.out.println(msg);
    }

    @Override
    public void showOthers(Map<String, Player> others){
        for (String nick: others.keySet()) {
            System.out.println("  " + nick + "'s shelf");
            for (int i = 0; i < others.get(nick).getShelf().length; i++){
                out.print(RESET + "  ");
                for (int j = -1; j < others.get(nick).getShelf()[0].length; j++) {
                    if(j ==-1) j++;
                    out.print(others.get(nick).getShelf()[i][j].getColor() + " " + others.get(nick).getShelf()[i][j].getInitial() + " ");
                    if(j < others.get(nick).getShelf()[0].length - 1){
                        out.print(DARK_BROWN+" ");
                    }
                }
                out.println(RESET);
            }
            out.println("\033[48;5;94m   1   2   3   4   5   " + RESET);
            out.println();
        }
    }

    @Override
    public void reloadTokens(Player p) {
        System.out.println("Tokens reloaded");
    }

    @Override
    public void endGameToken(String player) {
        System.out.println(player + " filled his shelf first " +
                "and gained a point! This is the last turn.");
    }

    @Override
    public void endGame() {
        System.out.println("GAME ENDED\nRESULTS: \n");
    }


    @Override
    public void showPoints(String message) {
        System.out.println(message);
    }

    @Override
    public void winner(String message) {
        System.out.println(message);
    }

    @Override
    public void displayMessage(String msg){
        out.println(msg);
    }

    @Override
    public void showChat(String msg) {
        System.out.println(msg);
    }

    @Override
    public void help(){
        out.println("""
                \u001B[31mThis is the command list:\s
                createlobby <Number of players>
                joinlobby <Lobby id>
                showlobby
                C <recipient name> <message>
                CA <message>
                startgame
                pt <Row1> <Column1> (pick tiles)
                so <First> <Second> <Third> (select order)
                sc <ShelfColumn> (select column)
                showshelf
                showboard
                showothers
                showpersonal
                showcommons
                \u001B[0m""");
    }

    @Override
    public String getInput() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    @Override
    public void disconnected() {
        System.out.println(RED + "Disconnected from server." + RESET);
    }
}
