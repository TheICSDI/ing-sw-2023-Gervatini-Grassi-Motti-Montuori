package it.polimi.ingsw.view.Gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/*
questa Classe serve invece per controllare le azioni che avvengono scene iniziale
"handeleTestButton" ad esempio scrive poco sotto il bottone il messaggio "Button pressed" e il collegamento
viene fatto nel file "test_2.fxml" in questo caso (basta cliccare sull'immagine del file alle linea 13 per trovarlo subito)
 */
public class JFXStartController {
    @FXML private Text actiontarget;

    @FXML protected void handleTestButton(ActionEvent event) {
        actiontarget.setText("Button pressed");
    }

}
