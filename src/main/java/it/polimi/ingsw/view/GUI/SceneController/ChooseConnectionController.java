package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ChooseConnectionController {

    private GUI gui;

    @FXML
    protected void socketButtonAction(ActionEvent event)
    {
        System.out.println("Socket Connection");
        send("1");
    }
    @FXML
    protected void rmiButtonAction(ActionEvent event)
    {
        System.out.println("RMI Connection");
        send("2");
    }

    protected void send(String type){
        synchronized (gui.ConnectionLock) {
            gui.connectionChosen = type;
            gui.ConnectionLock.notifyAll();
        }
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
