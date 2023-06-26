package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class endSceneController {
    public Label Winner;
    public VBox Points;
    public GUI gui;


    /**
     * Shows player's points
     * @param message player and his points
     */
    public void showPoints(String message){
        Label playerPoints=new Label();
        playerPoints.setStyle("-fx-font-family: 'Comic Sans MS';"+
                "-fx-font-size: 20 px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #ecd8c4;"
        );

        playerPoints.setText(message);
        playerPoints.setAlignment(Pos.CENTER);
        playerPoints.setWrapText(true);
        Points.getChildren().add(playerPoints);
    }

    /**
     * Shows winner
     * @param message winner
     */
    public void setWinner(String message){
        Winner.setStyle("-fx-font-family: 'Comic Sans MS';"+
                "-fx-font-size: 20 px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #ecd8c4;"
        );
        Winner.setText(message);
    }

    /**
     * Brings back to lobby scene
     */
    public void BackToLobby() {
        Platform.runLater(() -> gui.openLobbyScene());
    }
    public void setGui(GUI gui){
        this.gui=gui;
    }
}
