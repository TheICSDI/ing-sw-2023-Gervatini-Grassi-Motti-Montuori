package it.polimi.ingsw.view.Gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*
questa Classe dovrebbe gestire la finestra corrente e qui dovremmo mandare i vari cambi di scena dalla classe GUI
in base alla chiamata
 */
public class JFXScene extends Application {

    @Override //obbligatorio per gestire gli "stage"
    public void start(Stage stage) {
        GUI view = new GUI();
        FXMLLoader loader = new FXMLLoader();

        //non riesco a runnare i file da un altra cartella e comunque ogni tanto da come errore "Exception in Application start method"
        //non ho ancora capito il perché
        loader.setLocation(getClass().getResource("test_3.fxml"));
        Parent root = null;

        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /* test con la struttura del file test_1
        try {
            loader.setLocation(new URL("test_1.fxml"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        VBox vbox = null;
        try {
            vbox = loader.<VBox>load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(vbox);
         */
        stage.setTitle("Test");
        stage.setScene(new Scene(root, 300, 275));
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
        launch(args);
        //metodo per lanciare la javafx
    }

    @Override //per gestire la chiusura
    public void stop() {
        Platform.exit();
        System.exit(0);
    }
}
