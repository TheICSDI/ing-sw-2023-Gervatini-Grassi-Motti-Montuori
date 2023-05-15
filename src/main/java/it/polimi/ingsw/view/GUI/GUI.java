package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.network.messages.Action;
import it.polimi.ingsw.view.GUI.SceneController.ShowMainController;
import it.polimi.ingsw.view.GUI.SceneController.lobbySceneController;
import it.polimi.ingsw.view.GUI.SceneController.nameSceneController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
//TODO SCHERMATE: 1-GetName 2-Lobbies 3-Schermata picktiles/order/column
public class GUI implements View
{

    private Stage stage;
    public static String Name="";
    public static String message;
    public static final Object NameLock = new Object();
    public static final Object Lock = new Object();
    public nameSceneController nsc;
    public lobbySceneController lsc;
    private Stage primaryStage;
//sencojone -Emi
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
    public void showLobby(List<String> usrs, int num_usrs) {
    }

    @Override
    public void showBoard(type[][] simpleBoard, Action action) {

    }

    @Override
    public void showChosenTiles(List<Tile> tiles) {

    }

    @Override
    public void showCommons(List<Integer> cc) {

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
    public void startGame(int lobby_id) {

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
            lsc.setText(msg);//TODO controlli per vedere in che pagina sei forse
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
        }
        return message;
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
        stage.setTitle("NameScene");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openLobbyScene(){
        FXMLLoader loader = new FXMLLoader();
        Parent root1 = null;
        loader.setLocation(getClass().getResource("/fxml/LobbyScene.fxml"));
        try {
            root1 = loader.load();
        } catch (Exception ignored) {
        }
        lsc = loader.getController();
        lsc.setName(Name);
        stage.setTitle("NameScene");
        stage.setScene(new Scene(root1));
    }

}
