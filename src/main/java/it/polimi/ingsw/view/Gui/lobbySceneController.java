package it.polimi.ingsw.view.Gui;

import it.polimi.ingsw.model.Lobby;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.List;

public class lobbySceneController {
    @FXML
    public Label Name;
    @FXML
    public TextArea Lobbies;
    @FXML
    public Button CreateLobby;
    @FXML
    public Label text;
    @FXML
    public Button Refresh;

    public void setName(String name){
        Name.setText("Name set: " + name);
    }
    //TODO to end
    public void showLobbies(List<Lobby> Lobbies){
        this.Lobbies.setText("Available");
        for (Lobby l:
             Lobbies) {
            this.Lobbies.setText(this.Lobbies.getText() +
                    "Lobby " + l.lobbyId + ": ");
        }
    }

    public void createLobby(ActionEvent actionEvent) {
        synchronized (GUI.Lock) {
            GUI.message = "createlobby 2";//TODO DA vedere il limite
            GUI.Lock.notifyAll();
        }
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void showLobbies(ActionEvent actionEvent) {
        synchronized (GUI.Lock) {
            GUI.message = "showlobby";
            GUI.Lock.notifyAll();
        }
    }
}
