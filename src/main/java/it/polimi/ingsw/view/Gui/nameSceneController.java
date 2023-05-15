package it.polimi.ingsw.view.Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.print.DocFlavor;

import static it.polimi.ingsw.view.Gui.GUI.NameLock;

public class nameSceneController {
    @FXML
    public Button ConfirmNickname;
    @FXML
    public Label Information;
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
