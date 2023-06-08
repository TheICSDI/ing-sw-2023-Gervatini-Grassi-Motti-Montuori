package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
        synchronized (GUI.ConnectionLock) {
            gui.connectionChosen = type;
            GUI.ConnectionLock.notifyAll();
        }
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
