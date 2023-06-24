/** It implements the view interface managing GUI's output.*/
package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.view.GUI.SceneController.*;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI implements View {
    private int nPage=1;
    private Stage stage;
    public String nick = "";
    public String IPv4 = "";
    public String message;
    public final Object NameLock = new Object();
    public final Object IPLock = new Object();
    public final Object Lock = new Object();
    public ChooseConnectionController ccc;
    public nameSceneController nsc;
    public lobbySceneController lsc;
    public gameSceneController gsc;
    public endSceneController esc;
    public String connectionChosen;
    public final Object ConnectionLock = new Object();
    private final GUI currGui;

    public GUI() {
        this.currGui = this;
    }

    @Override
    public String chooseConnection() {
        synchronized (ConnectionLock) {
            try{
                ConnectionLock.wait();
            }catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Platform.runLater(this::startGUI);
        return connectionChosen;
    }

    @Override
    public String askIP() {
        // Regex Pattern for well formatted IPv4
        Pattern correct_ip = Pattern.compile("^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^0$|^$");
        boolean correct = false;
        Matcher matcher;
        synchronized (IPLock) {
            while(!correct){
                try{
                    IPLock.wait();
                }catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // The matcher() method is used to search for the pattern in a string.
                matcher = correct_ip.matcher(IPv4);
                // The find() method returns true if the pattern was found in the string and false if it was not found.
                correct = matcher.find();
                if(!correct){
                    Platform.runLater(() -> {
                        ccc.showIP("Ip wrongly formatted", "RED");
                    });
                }
            }
            if(IPv4.equals("0") || IPv4.equals("")){
                IPv4 = "127.0.0.1";
            }
            Platform.runLater(() -> {
                ccc.showIP("DONE", "GREEN");
            });
            return IPv4;
        }
    }

    @Override
    public String askNickname() {
        nick = "";
        synchronized (NameLock) {
            while (nick.equals("") || nick.charAt(0) == '\\') {
                try {
                    NameLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return nick;
        }
    }

    @Override
    public void checkNickname(String nickname, boolean isAvailable) {
        Platform.runLater(() -> {
            if (isAvailable) {
                nick = nickname;
                openLobbyScene();
                synchronized (Lock) {
                    message = "showlobby";
                    Lock.notifyAll();
                }
            } else {
                nsc.showName("NotAvailable");
            }
        });
    }

    @Override
    public void showLobby(List<Lobby> Lobbies) {
        Platform.runLater(() -> lsc.showLobbies(Lobbies));
    }

    @Override
    public void startGame(String message) {
        Platform.runLater(this::openGameScene);
    }

    @Override
    public void playersTurn(String msg, boolean firstTurn){
        Platform.runLater(()-> gsc.Turn(msg, firstTurn));
    }

    @Override
    public void showBoard(Tile[][] board) {
        Platform.runLater(() -> gsc.showBoard(board));
    }

    @Override
    public void showShelf(Tile[][] shelf) {
        Platform.runLater(() -> gsc.showShelf(shelf));
    }

    @Override
    public void showPersonal(PersonalCard PC) {
        Platform.runLater(() -> gsc.showPersonal(PC.getId()));
    }

    @Override
    public void showCommons(List<Integer> cc) {
        Platform.runLater(() -> gsc.showCommons(cc));
    }

    @Override
    public void showChosenTiles(List<Tile> tiles, boolean toOrder) {
        Platform.runLater(() -> gsc.showChosenTiles(tiles,toOrder));
    }

    @Override
    public void commonCompleted(String msg, String whoCompleted, boolean first) {
        Platform.runLater(()-> gsc.commonCompleted(msg, whoCompleted, first));
    }

    @Override
    public void showOthers(Map<String,Player> others) {
        Platform.runLater(() ->gsc.showOthers(others));
    }

    @Override
    public void endGameToken(String player) {
        Platform.runLater(() -> gsc.endGameToken(player));
    }


    @Override
    public void endGame() {
        Platform.runLater(this::openEndScene);
    }


    @Override
    public void showPoints(String message) {
        Platform.runLater(() -> esc.showPoints(message));
    }

    @Override
    public void winner(String message) {
        Platform.runLater(() -> esc.setWinner(message));
    }

    @Override
    public void displayMessage(String msg) {
        Platform.runLater(() -> {
            if(nPage == 2) {
                nsc.setText(msg);
            } else if (nPage == 3) {
                lsc.setText(msg);
            } else if(nPage == 4) {
                gsc.setIngameEvents(msg);
            }
        });
    }

    @Override
    public void showChat(String msg) {
        Platform.runLater(() -> gsc.newMessage(msg));
    }


    @Override
    public void help() {

    }

    @Override
    public String getInput() {
        synchronized (Lock){
            try {
                Lock.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return message;
        }
    }

    /**
     * Loads an FXML file and returns the associated controller instance.
     * This method assumes that the FXML file is stored in the "src/resources/fxml/" directory.
     *
     * @param fxmlFileName    The name of the FXML file to load.
     * @param controllerClass The class of the associated controller.
     * @param <T>             The type of the controller.
     * @return The controller instance associated with the loaded FXML file.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    private <T> T loadFXML(String fxmlFileName, Class<T> controllerClass) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/" + fxmlFileName));
        loader.load();
        return loader.getController();
    }

    public void startGuiConnection(Stage primaryStage){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/ChooseConnectionScene.fxml"));
        Parent root=null;

        try{
            root=loader.load();
        }catch(Exception ignored){}

        ccc=loader.getController();
        ccc.setGui(currGui);
        stage=primaryStage;
        stage.setTitle("My Shelfie");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Publisher material/Box 280x280px.png"))));
        //stage.setFullScreen(true);
        //stage.setMaximized(true);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void startGUI(){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NameScene.fxml"));
        Parent root=null;
        try{
            root=loader.load();
        }catch(Exception ignored){}
        nsc=loader.getController();
        nsc.setGUI(this.currGui);
        //stage=primaryStage;
        stage.setTitle("My Shelfie");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Publisher material/Box 280x280px.png"))));
        //stage.setFullScreen(true);
        //stage.setMaximized(true);
        nPage=2;
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void openLobbyScene(){
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        loader.setLocation(getClass().getResource("/fxml/LobbyScene.fxml"));
        try {
            root = loader.load();
        } catch (Exception ignored) {
        }
        lsc = loader.getController();
        lsc.setName(nick);
        lsc.setGUI(this.currGui);
        stage.centerOnScreen();
        nPage=3;
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openGameScene(){
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        loader.setLocation(getClass().getResource("/fxml/GameScene.fxml"));
        try {
            root = loader.load();
        } catch (Exception ignored) {
        }
        gsc = loader.getController();
        gsc.setGui(this.currGui);
        if(stage.isIconified()){
            stage.setIconified(false);
        }

        stage.setFullScreen(false);
        stage.setX(0);
        stage.setY(0);

        stage.setResizable(false);
        //stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        nPage=4;
        //stage.setFullScreenExitHint("");

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openEndScene(){
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        loader.setLocation(getClass().getResource("/fxml/EndScene.fxml"));
        try {
            root = loader.load();
        } catch (Exception ignored) {
        }
        esc = loader.getController();
        esc.setGui(currGui);
        if(stage.isIconified()){
            stage.setIconified(false);
        }
        stage.centerOnScreen();
        stage.setResizable(false);
        nPage=5;
        stage.setFullScreenExitHint("");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
