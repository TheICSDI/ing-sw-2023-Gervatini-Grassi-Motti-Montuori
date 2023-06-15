package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class lobbySceneController implements Initializable {
    @FXML
    public Label Name;
    @FXML
    public ListView<String> Lobbies;
    @FXML
    public Button CreateLobby;
    @FXML
    public Button Refresh;
    @FXML
    public Label text;
    @FXML
    public Button Join;
    @FXML
    public RadioButton limit2;
    @FXML
    public RadioButton limit3;
    @FXML
    public RadioButton limit4;
    public ToggleGroup Prova;
    @FXML
    public Button Start;
    @FXML
    public Label createLobbytext;
    private List<Lobby> AvailableLobbies;
    private int limit = 2;

    private GUI gui;

    Font font;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            font = Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/Poppins-Regular.ttf")).openStream(), 12);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Name.setFont(font);
        text.setFont(font);
        Start.setFont(font);
        Join.setFont(font);
        Refresh.setFont(font);
        limit2.setFont(font);
        limit3.setFont(font);
        limit4.setFont(font);
        createLobbytext.setFont(font);
        CreateLobby.setFont(font);
    }

    public void setName(String name){
        Name.setText("Your name: " + name);
    }

    public void showLobbies(List<Lobby> Lobbies){
        AvailableLobbies = Lobbies;
        this.Lobbies.getItems().clear();
        StringBuilder Lobby;
        for (Lobby l: Lobbies) {
            Lobby = new StringBuilder("Lobby " + l.lobbyId + " ("+ l.Players.size() + "/" + l.limit + "): ");
            for (Player p: l.Players){
                Lobby.append(p.getNickname()).append(" ");
            }
            this.Lobbies.getItems().add(Lobby.toString());
        }
    }

    public void createLobby() throws InterruptedException {
        synchronized (gui.Lock) {
            gui.message = "createlobby " + limit;
            gui.Lock.notifyAll();
        }
        //showLobbies(gameController.allLobbies);
        TimeUnit.MILLISECONDS.sleep(250);
        showLobbies();
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void showLobbies() {
        synchronized (gui.Lock) {
            gui.message = "showlobby";
            gui.Lock.notifyAll();
        }
    }

    public void Join() throws InterruptedException {
        ObservableList<Integer> selectedIndices = this.Lobbies.getSelectionModel().getSelectedIndices();
        for(Integer o : selectedIndices){
            synchronized (gui.Lock) {
                gui.message = "joinlobby " + AvailableLobbies.get(o).lobbyId;
                gui.Lock.notifyAll();
            }
        }
        //showLobbies(gameController.allLobbies);
        TimeUnit.MILLISECONDS.sleep(250);
        showLobbies();
    }

    public void setLimit() {
        if(limit2.isSelected()){
            limit = 2;
        } else if(limit3.isSelected()) {
            limit = 3;
        }else {
            limit = 4;
        }
    }

    @FXML
    public void StartGame() {
        synchronized (gui.Lock) {
            gui.message = "startgame";
            gui.Lock.notifyAll();
        }
    }

    public void setGUI(GUI gui){
        this.gui=gui;
    }
}
