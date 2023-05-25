package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class endSceneController {
    public Label Winner;
    public VBox Points;
    public GUI gui;

    public void initialize() {
    }

    public void showPoints(String message){
        Label playerPoints=new Label(message);
        playerPoints.setAlignment(Pos.CENTER);
        playerPoints.setWrapText(true);
        Points.getChildren().add(playerPoints);
    }

    public void setWinner(String message){
        Winner.setText(message);
    }

    public void BackToLobby(ActionEvent actionEvent) {
        Platform.runLater(() -> gui.openLobbyScene());
    }
    public void setGui(GUI gui){
        this.gui=gui;
    }
}
