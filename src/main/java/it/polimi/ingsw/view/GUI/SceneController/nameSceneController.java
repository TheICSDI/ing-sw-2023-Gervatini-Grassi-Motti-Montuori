package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** Controller for name scene */
public class nameSceneController implements Initializable {
    @FXML
    public Button ConfirmNickname;
    @FXML
    public Label Information;
    public ImageView Title;
    @FXML
    public AnchorPane anchorpane;
    @FXML
    public Pane Button;
    @FXML
    private TextField GetNickname;
    Font font = null;
    private GUI gui;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            font = Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/Poppins-Regular.ttf")).openStream(), 12);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Setting the font
        GetNickname.setFont(font);
        //Setting color of the text
        GetNickname.setPromptText("Enter your nickname");
        GetNickname.setFocusTraversable(false);
        GetNickname.setStyle("-fx-text-fill: WHITE;-fx-background-color: transparent; -fx-border-width: 0 0 1 0; -fx-border-color: WHITE; -fx-prompt-text-fill: WHITE");
        HBox.setHgrow(GetNickname, Priority.ALWAYS);
        HBox.setHgrow(ConfirmNickname, Priority.ALWAYS);
        this.GetNickname.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                getText();
            }
        });
    }

    /** Gets the nickname written in the text field. */
    @FXML
    public void getText(){
        synchronized (gui.NameLock) {
            gui.nick = GetNickname.getText();
            gui.NameLock.notifyAll();
        }
    }

    /** Notifies if the name is unavailable. */
    @FXML
    public void unavailable(){
        Information.setText("Nickname is not available!");
        Information.setFont(font);
    }

    /** Writes the chosen name (if available), passed by parameter. */
    public void setText(String text){
        this.Information.setText(text);
    }

    /** Sets the GUI passed by parameter */
    public void setGUI(GUI gui){
        this.gui=gui;
    }
}
