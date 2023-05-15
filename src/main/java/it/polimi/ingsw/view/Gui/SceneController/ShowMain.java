package it.polimi.ingsw.view.Gui.SceneController;

import it.polimi.ingsw.view.Gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ShowMain {

    @FXML
    private BorderPane showMain;
    private GUI Gui;

    public void init(GUI Gui) {
        this.Gui = Gui;
    }

    public Scene getScene() {
        return showMain.getScene();
    }
}
