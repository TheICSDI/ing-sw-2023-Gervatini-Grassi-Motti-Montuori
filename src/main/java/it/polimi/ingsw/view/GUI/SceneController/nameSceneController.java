package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import static it.polimi.ingsw.view.GUI.GUI.NameLock;

public class nameSceneController {
    
    
    @FXML
    public Button ConfirmNickname;
    @FXML
    public Label Information;
    public ImageView Title;
    @FXML
    public AnchorPane anchorpane;
    @FXML
    private TextField GetNickname;

    @FXML
    public void getText(){
        synchronized (NameLock) {
            GUI.Name = GetNickname.getText();
            NameLock.notifyAll();
        }
    }

    @FXML
    public void showName(String name){
        Information.setText(name);
    }



}