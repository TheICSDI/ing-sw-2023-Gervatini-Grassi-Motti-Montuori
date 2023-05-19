package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.model.Lobby;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class lobbySceneController {
    @FXML
    public Label Name;
    @FXML
    public ListView<String> Lobbies;
    @FXML
    public Button CreateLobby;
    @FXML
    public Label text;
    @FXML
    public Button Refresh;
    @FXML
    public Button Join;
    public RadioButton limit2;
    public RadioButton limit3;
    public RadioButton limit4;
    public ToggleGroup Prova;
    @FXML
    public Button Start;

    private List<Lobby> AvailableLobbies;
    private int limit=2;

    public void setName(String name){
        Name.setText("Name set: " + name);
    }
    public void showLobbies(List<Lobby> Lobbies){
        AvailableLobbies=Lobbies;
        this.Lobbies.getItems().clear();
        StringBuilder Lobby;
        for (Lobby l:
             Lobbies) {
            Lobby = new StringBuilder("Lobby " + l.lobbyId + " ("+ l.Players.size() + "/" + l.limit + "): ");
            for (Player p: l.Players){
                Lobby.append(p.getNickname()).append(" ");
            }
            this.Lobbies.getItems().add(Lobby.toString());
        }
    }

    public void createLobby(ActionEvent actionEvent) {
        synchronized (GUI.Lock) {
            GUI.message = "createlobby " + limit;
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

    public void Join(ActionEvent actionEvent) {
        ObservableList<Integer> selectedIndices = this.Lobbies.getSelectionModel().getSelectedIndices();

        for(Integer o : selectedIndices){
            synchronized (GUI.Lock) {
                GUI.message = "joinlobby " + AvailableLobbies.get(o).lobbyId;
                GUI.Lock.notifyAll();
            }
        }
    }

    public void setLimit(ActionEvent actionEvent) {
        if(limit2.isSelected()){
            limit=2;
        } else if(limit3.isSelected()) {
            limit=3;
        }else{
            limit=4;
        }
    }

    @FXML
    public void StartGame(ActionEvent actionEvent) {
        synchronized (GUI.Lock) {
            GUI.message = "startgame";
            GUI.Lock.notifyAll();
        }
    }
}
