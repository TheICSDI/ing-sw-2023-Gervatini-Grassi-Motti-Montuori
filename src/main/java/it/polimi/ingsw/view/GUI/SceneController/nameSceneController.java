package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

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
    public Label EnterNick;
    @FXML
    private TextField GetNickname;
    Font font = null;


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

    }

    @FXML
    public void getText(){
        synchronized (GUI.NameLock) {
            GUI.Name = GetNickname.getText();
            GUI.NameLock.notifyAll();
        }
    }

    @FXML
    public void showName(String name){
        Information.setText("Nickname is not available!");
        Information.setFont(font);
        /*GetNickname.setStyle("-fx-border-color: red; -fx-border-width: 2px; -fx-padding: 5px;");//figo ma rompe tutto io boh
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        GetNickname.setStyle("");*/
    }



}
