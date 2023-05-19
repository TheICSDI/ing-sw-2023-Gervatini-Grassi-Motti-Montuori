package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Cards.PersonalCard;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.view.GUI.SceneController.ShowMainController;
import it.polimi.ingsw.view.GUI.SceneController.gameSceneController;
import it.polimi.ingsw.view.GUI.SceneController.lobbySceneController;
import it.polimi.ingsw.view.GUI.SceneController.nameSceneController;
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

//TODO SCHERMATE: 1-GetName 2-Lobbies 3-Schermata picktiles/order/column 4-fine gioco
public class GUI implements View {
    private int nPage=1;
    private Stage stage;
    public static String Name="";
    public static String message;
    public static final Object NameLock = new Object();
    public static final Object Lock = new Object();
    public nameSceneController nsc;
    public lobbySceneController lsc;
    public gameSceneController gsc;
    private Stage primaryStage;

    @Override
    public String showMain(){
        ShowMainController controller = null;
        try {
            controller = loadFXML("main_menu.fxml", ShowMainController.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        controller.init(this);
        primaryStage.setScene(controller.getScene());
        return null;
    }

    @Override
    public String askUsername() {
        synchronized (NameLock) {
            while (Name.equals("")) {
                try {
                    NameLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return Name;
        }
    }

    @Override
    public void printUsername(String username, boolean isAvailable) {
        Platform.runLater(() -> {
            if (isAvailable) {
                Name = username;
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
    public void createLobby(String lobbyName/*, int maxPlayers non serve?*/) {
        Platform.runLater(() -> {
            lsc.setText(lobbyName);//provvisorio
        });
    }

    @Override
    public void showLobby(List<Lobby> Lobbies) {
        Platform.runLater(() -> lsc.showLobbies(Lobbies));
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
    public void showChosenTiles(List<Tile> tiles,boolean toOrder) {
        Platform.runLater(() -> gsc.showChosenTiles(tiles,toOrder));
    }

    @Override
    public void showCommons(List<Integer> cc) {
        Platform.runLater(() -> gsc.showCommons(cc));
    }

    @Override
    public void showOthers(Map<String, Player> others) {

    }
    @Override
    public void joinLobby(int lobby_id) {

    }

    @Override
    public void exitLobby(int lobby_id) {

    }

    @Override
    public void startGame(String message) {
        Platform.runLater(this::openGameScene);
    }

    @Override
    public void pickTiles(int lobby_id, Position pos) {

    }

    @Override
    public void selectOrder(int lobby_id, List<Integer> order) {

    }

    @Override
    public void selectColumn(int lobby_id, int col) {

    }

    @Override
    public void updateBoard(int lobby_id, Board board) {

    }

    @Override
    public void updateLobby(int lobby_id, List<String> players) {

    }

    @Override
    public void displayError(String msg) {
        Platform.runLater(() -> {
            lsc.setText(msg);//TODO controlli per vedere in che pagina sei forse
        });
    }

    @Override
    public void displayMessage(String msg) {
        Platform.runLater(() -> {
            if(nPage==2) {
                lsc.setText(msg);//TODO controlli per vedere in che pagina sei forse
            }else if(nPage==3) {
                gsc.newMessage(msg);
            }
        });
    }

    @Override
    public void help() {

    }

    //METODO PER MANDARE I MESSAGGI AL SERVER, QUANDO X PULSANTE VIENE PREMUTO BISOGNA SETTARE IL MESSAGE DI QUESTA CLASSE
    // AL MESSAGGIO DA MANDARE E FARE MESSAGELOCK.NOTIFYALL E IL MESSAGGIO VIENE INVIATO
    //TODO TUTTI I PULSANTI CHE INVIANO I MESSAGGI PER LE LOBBY E UNIFORMARE I METODI CHE RICEVONO LE RISPOSTE IN GUI E CONTROLLARE SIANO FATTI IN CLI
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
        loader.setLocation(controllerClass.getClassLoader().getResource("fxml/" + fxmlFileName));
        loader.load();
        return loader.getController();
    }

    public void startGUI(Stage primaryStage){
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NameScene.fxml"));
        Parent root=null;
        try{
            root=loader.load();
        }catch(Exception ignored){}
        nsc=loader.getController();
        stage=primaryStage;
        stage.setTitle("My Shelfie");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/Publisher material/Box 280x280px.png"))));
        //stage.setFullScreen(true);
        //stage.setMaximized(true);

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openLobbyScene(){
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        loader.setLocation(getClass().getResource("/fxml/LobbyScene.fxml"));
        try {
            root = loader.load();
        } catch (Exception ignored) {
        }
        lsc = loader.getController();
        lsc.setName(Name);
        stage.centerOnScreen();
        nPage=2;
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
        //stage.setFullScreen(true);
        stage.setX(0);
        stage.setY(0);
        stage.setResizable(false);
        nPage=3;
        stage.setFullScreenExitHint("");
        stage.setScene(new Scene(root));
        stage.show();
    }

}
