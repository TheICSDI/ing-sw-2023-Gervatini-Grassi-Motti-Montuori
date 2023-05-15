package it.polimi.ingsw.view.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/*
Imposta l'inizio della gui e di javafx
 */
public class JFXStart extends Application {

    @Override //obbligatorio per gestire gli "stage"
    public void start(Stage stage) {
        GUI view = new GUI();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/NameScene.fxml"));
        //load the .fxml file

        Parent root= null;

        try {
            root = loader.load();
            //root=FXMLLoader.load(Objects.requireNonNull(getClass().getResource("src/resources/fxml/test_2.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }


        stage.setTitle("Test");
        stage.setScene(new Scene(root));
        stage.show();
    }

/* finestra con scritto "Hello World"
    private Parent createContent() {
        return new StackPane(new Text("Hello World"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent(), 300, 300));
        stage.show();
    }
 */
    public static void main(String[] args) {
        Application.launch(args);
        //metodo per lanciare la javafx
    }

    @Override //per gestire la chiusura
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
