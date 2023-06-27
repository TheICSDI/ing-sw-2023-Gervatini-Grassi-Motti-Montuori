package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.view.GUI.GUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/** Controller for the connection scene. */
public class chooseConnectionController implements Initializable {
    @FXML
    public Button ConfirmIP;
    @FXML
    private TextField GetIP;
    @FXML
    public Label Information;
    private GUI gui;
    Font font = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            font = Font.loadFont(Objects.requireNonNull(getClass().getResource("/fonts/Poppins-Regular.ttf")).openStream(), 12);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //Setting the font
        GetIP.setFont(font);
        //Setting color of the text
        GetIP.setPromptText("Enter IP,  Empty for localhost");
        GetIP.setFocusTraversable(false);
        GetIP.setStyle("-fx-text-fill: WHITE;-fx-background-color: transparent; -fx-border-width: 0 0 1 0; -fx-border-color: WHITE; -fx-prompt-text-fill: WHITE");
        HBox.setHgrow(GetIP, Priority.ALWAYS);
        HBox.setHgrow(ConfirmIP, Priority.ALWAYS);
        this.GetIP.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                getText();
            }
        });
    }

    /** Sets connection to socket. */
    @FXML
    protected void socketButtonAction() {
        getText();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        send("1");
        System.out.println("Socket Connection");
    }

    /** Sets connection to RMI. */
    @FXML
    protected void rmiButtonAction()
    {
        getText();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        send("2");
        System.out.println("RMI Connection");
    }

    /** Sends chosen connection type, passed by parameter.*/
    protected void send(String type){
        synchronized (gui.ConnectionLock) {
            gui.connectionChosen = type;
            gui.ConnectionLock.notifyAll();
        }
    }

    /** Gets the text (chosen IP).*/
    @FXML
    public void getText(){
        synchronized (gui.IPLock) {
            gui.IPv4 = GetIP.getText();
            gui.IPLock.notifyAll();
        }
    }

    /**
     * It displays the chosen ip and the outcome of the connection.
     * @param msg message to be shown.
     * @param color text color.
     */
    @FXML
    public void showIP(String msg, String color){
        Information.setStyle("-fx-text-fill: " + color + ";-fx-background-color: transparent; -fx-prompt-text-fill: " + color);
        Information.setText(msg);
        Information.setFont(font);
    }

    /** Sets the text passed by parameter. */
    public void setText(String text){
        this.Information.setText(text);
    }

    /** Sets the GUI passed by parameter */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
