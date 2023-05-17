package it.polimi.ingsw.view.GUI.SceneController;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class gameSceneController {
    @FXML
    public GridPane board;

    public void showBoard(){
        Image image=new Image("/Images/item tiles/Cornici1.1.png");
        ImageView Tile=new ImageView(image);
        Tile.setFitHeight(30);
        Tile.setFitWidth(30);
        board.add(Tile,0,3);
    }
}
