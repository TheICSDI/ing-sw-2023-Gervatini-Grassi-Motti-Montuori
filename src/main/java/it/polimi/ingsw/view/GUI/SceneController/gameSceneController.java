package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;


public class gameSceneController {
    private Tile[][] oldBoard=new Tile[9][9];
    public gameSceneController(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                oldBoard[i][j]=new Tile("NOT_ACCESSIBLE");
            }
        }
    }
    @FXML
    public GridPane board;

    @FXML
    public void showBoard(Tile[][] board){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE)||
                    (!board[i][j].getCategory().equals(this.oldBoard[i][j].getCategory()))){
                    if(board[i][j].getCategory().equals(type.EMPTY)){
                        Image image=new Image("");
                        ImageView Tile=new ImageView();
                        Tile.setFitHeight(30);
                        Tile.setFitWidth(30);
                        Tile.setImage(image);
                        this.board.add(Tile,i,j);
                    }else{
                        Image image=new Image(board[i][j].getImage());
                        ImageView Tile=new ImageView();
                        Tile.setFitHeight(30);
                        Tile.setFitWidth(30);
                        Tile.setImage(image);
                        this.board.add(Tile,i,j);
                        this.oldBoard[i][j]=board[i][j];
                    }

                }
            }
        }
    }
}
