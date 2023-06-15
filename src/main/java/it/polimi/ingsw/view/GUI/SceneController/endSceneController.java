package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class endSceneController {
    public Label Winner;
    public VBox Points;
    public GUI gui;

    public void initialize() {
    }

    public void showPoints(String message){
        Text cartoonText = new Text(message);

        // Impostazione dello stile del testo
        cartoonText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        cartoonText.setFill(Color.WHITE);
        cartoonText.setStroke(Color.BLACK);
        cartoonText.setStrokeWidth(1);

        Label playerPoints=new Label();
        playerPoints.setGraphic(cartoonText);
        playerPoints.setAlignment(Pos.CENTER);
        playerPoints.setWrapText(true);
        Points.getChildren().add(playerPoints);
    }

    public void setWinner(String message){
        Text cartoonText = new Text(message);

        // Impostazione dello stile del testo
        cartoonText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
        cartoonText.setFill(Color.WHITE);
        cartoonText.setStroke(Color.BLACK);
        cartoonText.setStrokeWidth(1);
        Winner.setGraphic(cartoonText);
        //Winner.setText(message);
    }

    public void BackToLobby(ActionEvent actionEvent) {
        Platform.runLater(() -> gui.openLobbyScene());
    }
    public void setGui(GUI gui){
        this.gui=gui;
    }
}
