package it.polimi.ingsw.view.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static it.polimi.ingsw.network.client.Client.RMI;
import static it.polimi.ingsw.network.client.Client.socket;

/*
questa Classe serve invece per controllare le azioni che avvengono scene iniziale
"handeleTestButton" ad esempio scrive poco sotto il bottone il messaggio "Button pressed" e il collegamento
viene fatto nel file "test_2.fxml" in questo caso (basta cliccare sull'immagine del file alle linea 13 per trovarlo subito)
 */
public class JFXStartController {
    //@FXML private Text actiontarget;

    /*
    @FXML protected void handleTestButton(ActionEvent event) {
        actiontarget.setText("Button pressed");
    }

     */
    @FXML
    private Button socket_button;
    @FXML
    private Button rmi_button;

    @FXML
    protected void socketButtonAction(ActionEvent event)
    {
        System.out.println("bottone socket premuto");
        try {
            socket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void rmiButtonAction(ActionEvent event)
    {
        System.out.println("bottone rmi premuto");
        RMI();
    }

}
