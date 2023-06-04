package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

import static it.polimi.ingsw.network.client.Client.RMI;
import static it.polimi.ingsw.network.client.Client.socket;

public class ChooseConnectionController {

    @FXML
    private Button socket_button;
    @FXML
    private Button rmi_button;
    private GUI gui;

    @FXML
    protected void socketButtonAction(ActionEvent event)
    {
        System.out.println("Socket Connection");
        send("1");
        /*
        try {
            socket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
         */
    }
    @FXML
    protected void rmiButtonAction(ActionEvent event)
    {
        System.out.println("RMI Connection");
        send("2");
        //RMI();
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