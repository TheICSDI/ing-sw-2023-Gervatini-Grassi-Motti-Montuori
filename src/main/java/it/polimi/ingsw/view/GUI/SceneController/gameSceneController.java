package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class gameSceneController {
    private final int dim = 9;
    private Tile[][] localBoard = new Tile[dim][dim];
    @FXML
    public GridPane board;
    @FXML
    public GridPane myShelf;

    /** It initialises the version of the local board to not_accessible type. */
    public gameSceneController(){
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                localBoard[i][j] = new Tile("NOT_ACCESSIBLE");
            }
        }
    }

    /** It updates the board on the panel with the right image. */
    @FXML
    public void showBoard(Tile[][] board){
        //For each element in the board
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                //If the current tile is accessible and different from the local board
                //its corresponding image is set
                if(!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE) &&
                    (!board[i][j].getCategory().equals(this.localBoard[i][j].getCategory()))){
                    ImageView Tile = new ImageView();
                    Tile.setFitHeight(30);
                    Tile.setFitWidth(30);
                    //TODO: centrare le immagini dentro la grid
                    //If the tile is now empty the previous image is removed
                    if(board[i][j].getCategory().equals(type.EMPTY) &&
                            !(this.localBoard[i][j].getCategory().equals(type.EMPTY))){
                        //It removes the previous image
                        Tile.setImage(null);
                        this.board.add(Tile, i, j);
                    } else {
                        Image image = new Image(board[i][j].getImage());
                        Tile.setImage(image);
                        this.board.add(Tile, i, j);
                        this.localBoard[i][j] = board[i][j];
                    }
                    // TODO: indici sbagliati
                    // gli indici sono sbagliati (confrontare out in CLI con out in GUI), è come se la board fosse rotata
                    //sinceramente non penso sia un problema, la soluzione più semplice sarebbe rinominarli tutti direttamente nel model credo
                }
            }
        }
    }

    @FXML
    public void showShelf(Tile[][] shelf){
        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[0].length; j++) {
                if(!shelf[i][j].getCategory().equals(type.EMPTY)) {
                    ImageView Tile = new ImageView();
                    Tile.setFitHeight(30);
                    Tile.setFitWidth(30);
                    Image image = new Image(shelf[i][j].getImage());
                    Tile.setImage(image);
                    this.myShelf.add(Tile, i, j);
                }
            }
        }
    }

    @FXML
    public void showPersonal(int id){
        //Idea dello switch del metodo in base all'id
        ImageView personal = new ImageView();
        Image image = null;
        switch (id){
            case 0 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals.png");
            case 1 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals2.png");
            case 2 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals3.png");
            case 3 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals4.png");
            case 4 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals5.png");
            case 5 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals6.png");
            case 6 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals7.png");
            case 7 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals8.png");
            case 8 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals9.png");
            case 9 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals10.png");
            case 10-> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals11.png");
            case 11 -> image = new Image("src/main/resources/Images/personal goal cards/Personal_Goals12.png");
        }
        personal.setImage(image);
    }
}
