package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO CHAT, SCHERMATA DI ENDGAME, PUNTEGGI SULLE COMMON
public class gameSceneController {
    @FXML
    public Label YourName;
    @FXML
    public Label p2Name;
    @FXML
    public Label p3Name;
    @FXML
    public Label p4Name;
    @FXML
    public ImageView p4ImageShelf;
    @FXML
    public ImageView p3ImageShelf;
    private boolean firstOthers;

    private final int dim = 9;
    private int pickedTiles;
    private int tilesOrdered;
    private boolean toShow=true;
    private List<Integer> newOrder=new ArrayList<>();
    @FXML
    public Button pickTiles;
    @FXML
    public ImageView personal;
    @FXML
    public ImageView common1;
    @FXML
    public ImageView common2;
    @FXML
    public VBox chat;
    @FXML
    public ScrollPane scrollChat;
    @FXML
    public Label ChosenText;
    @FXML
    public GridPane chosenTiles;
    @FXML
    public Label OrderText;
    @FXML
    public GridPane orderedTiles;
    private List<Position> Chosen=new ArrayList<>();
    private final Tile[][] localBoard = new Tile[dim][dim];
    @FXML
    public GridPane board;
    @FXML
    public GridPane myShelf;
    @FXML
    private List<GridPane> othersShelf=new ArrayList<>();
    @FXML
    public GridPane p2shelf;
    @FXML
    public GridPane p3shelf;
    @FXML
    public GridPane p4shelf;

    /** It initialises the version of the local board to not_accessible type. */
    // Non ho capito quando lo chiama però se non lo metto non funziona :P
    public gameSceneController(){
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                localBoard[i][j] = new Tile("NOT_ACCESSIBLE");
            }
        }
    }
    public void initialize(){
        YourName.setText(GUI.Name);
        othersShelf.add(p2shelf);
        othersShelf.add(p3shelf);
        othersShelf.add(p4shelf);
        firstOthers=true;
    }

    /** It updates the board on the panel with the right image. */
    @FXML
    public void showBoard(Tile[][] board){
        //For each element in the board
        //this.board.getChildren().clear();
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                //If the current tile is accessible and different from the local board
                //its corresponding image is set
                if(!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE) &&
                    (!board[i][j].getCategory().equals(this.localBoard[i][j].getCategory()))){
                    ImageView Tile = new ImageView();
                    HBox pane= new HBox();
                    pane.setMaxWidth(32);
                    pane.setMaxHeight(32);
                    Tile.setFitHeight(28);
                    Tile.setFitWidth(28);
                    //If the tile is now empty the previous image is removed
                    if(board[i][j].getCategory().equals(type.EMPTY) &&
                            !(this.localBoard[i][j].getCategory().equals(type.EMPTY))){
                        //It removes the previous image
                        int finalI1 = i;
                        int finalJ1 = j;
                        Node node = this.board.getChildren().stream()
                                .filter(child -> GridPane.getColumnIndex(child) == finalI1 && GridPane.getRowIndex(child) == finalJ1)
                                .findFirst()
                                .orElse(null);

                        if (node instanceof HBox) {
                             pane = (HBox) node;
                        }
                        //this.board.add(Tile, i, j);
                        this.board.getChildren().remove(pane);
                    } else {
                        Image image = new Image(board[i][j].getImage());
                        Tile.setImage(image);
                        pane.getChildren().add(Tile);
                        pane.setAlignment(Pos.CENTER);
                        int finalJ = j;
                        int finalI = i;

                        HBox finalPane = pane;
                        pane.setOnMouseClicked(event -> {
                            boolean alreadyChosen=false;
                            for (Position p:
                                 Chosen) {
                                if(p.getY()==finalI && p.getX()==finalJ)
                                {
                                    finalPane.setStyle("");
                                    Chosen.remove(p);
                                    alreadyChosen=true;
                                    break;
                                }
                            }
                            if(!alreadyChosen){
                                finalPane.setStyle("-fx-background-color: #ff0000;");
                                if(Chosen.size()==3) {
                                    Node node = this.board.getChildren().stream()
                                            .filter(child -> GridPane.getColumnIndex(child) == Chosen.get(0).getY() && GridPane.getRowIndex(child) == Chosen.get(0).getX())
                                            .findFirst()
                                            .orElse(null);
                                    HBox toDes = null;
                                    if (node instanceof HBox) {
                                        toDes = (HBox) node;
                                    }
                                    toDes.setStyle("");
                                    Chosen.remove(0);
                                }
                                Chosen.add(new Position(finalJ, finalI));
                            }
                        });
                        this.board.add(pane, i, j);
                        this.board.setAlignment(Pos.CENTER);
                    }
                    this.localBoard[i][j] = board[i][j];
                }
            }
        }
    }

    @FXML
    public void showShelf(Tile[][] shelf){
        //reset variables for picked tiles
        ChosenText.setVisible(false);
        OrderText.setVisible(false);
        chosenTiles.getChildren().clear();
        orderedTiles.getChildren().clear();
        toShow=true;

        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[0].length; j++) {
                if(!shelf[i][j].getCategory().equals(type.EMPTY)) {
                    ImageView Tile = new ImageView();
                    Tile.setFitHeight(30);
                    Tile.setFitWidth(30);
                    Image image = new Image(shelf[i][j].getImage());
                    Tile.setImage(image);
                    this.myShelf.setAlignment(Pos.CENTER);
                    this.myShelf.add(Tile, j, i);
                }
            }
        }
    }

    @FXML
    public void showOthers(Map<String, Player> others){
        if(firstOthers){
            firstOthers=false;
            List<String> names= new ArrayList<>(others.keySet());//sperando gli metta in ordine
            for (String s:
                 names) {
                System.out.println(s);
            }
            p2Name.setText(names.get(0) + "'s shelf:");
            if(names.size()>1){
                p3Name.setText(names.get(1) + "'s shelf:");
                p3ImageShelf.setVisible(true);
            }
            if(names.size()>2){
                p4Name.setText(names.get(2) + "'s shelf:");
                p4ImageShelf.setVisible(true);
            }
        }
        int k=0;
        for (String s:
             others.keySet()) {
            Tile[][] shelf=others.get(s).getShelf();
            for (int i = 0; i < shelf.length; i++) {
                for (int j = 0; j < shelf[0].length; j++) {
                    if(!shelf[i][j].getCategory().equals(type.EMPTY)) {
                        ImageView Tile = new ImageView();
                        Tile.setFitHeight(30);
                        Tile.setFitWidth(30);
                        Image image = new Image(shelf[i][j].getImage());
                        Tile.setImage(image);
                        othersShelf.get(k).setAlignment(Pos.CENTER);
                        othersShelf.get(k).add(Tile, j, i);
                    }
                }
            }
            k++;
        }
    }

    @FXML
    public void showPersonal(int id){
        //Based on the id of the personal card it chooses the right image
        String personalURL = null;
        switch (id){
            case 0 -> personalURL= "/Images/personal goal cards/Personal_Goals.png";
            case 1 -> personalURL = "/Images/personal goal cards/Personal_Goals2.png";
            case 2 -> personalURL = "/Images/personal goal cards/Personal_Goals3.png";
            case 3 -> personalURL = "/Images/personal goal cards/Personal_Goals4.png";
            case 4 -> personalURL = "/Images/personal goal cards/Personal_Goals5.png";
            case 5 -> personalURL = "/Images/personal goal cards/Personal_Goals6.png";
            case 6 -> personalURL = "/Images/personal goal cards/Personal_Goals7.png";
            case 7 -> personalURL = "/Images/personal goal cards/Personal_Goals8.png";
            case 8 -> personalURL = "/Images/personal goal cards/Personal_Goals9.png";
            case 9 -> personalURL = "/Images/personal goal cards/Personal_Goals10.png";
            case 10 -> personalURL = "/Images/personal goal cards/Personal_Goals11.png";
            case 11 -> personalURL = "/Images/personal goal cards/Personal_Goals12.png";
        }
        Image image = new Image(personalURL);
        this.personal.setImage(image);
    }

    @FXML
    public void showCommons(List<Integer> cc) {
        Image image1 = null;
        Image image2 = null;
        for (int id: cc) {
            String commonURL = null;
            switch (id){
                case 1 -> commonURL= "/Images/common goal cards/1.jpg";
                case 2 -> commonURL = "/Images/common goal cards/2.jpg";
                case 3 -> commonURL = "/Images/common goal cards/3.jpg";
                case 4 -> commonURL = "/Images/common goal cards/4.jpg";
                case 5 -> commonURL = "/Images/common goal cards/5.jpg";
                case 6 -> commonURL = "/Images/common goal cards/6.jpg";
                case 7 -> commonURL = "/Images/common goal cards/7.jpg";
                case 8 -> commonURL = "/Images/common goal cards/8.jpg";
                case 9 -> commonURL = "/Images/common goal cards/9.jpg";
                case 10 -> commonURL = "/Images/common goal cards/10.jpg";
                case 11 -> commonURL = "/Images/common goal cards/11.jpg";
                case 12 -> commonURL = "/Images/common goal cards/12.jpg";
            }
            if(image1 == null){
                image1 = new Image(commonURL);
                this.common1.setImage(image1);
            } else {
                image2 = new Image(commonURL);
                this.common2.setImage(image2);
            }
        }
    }

    @FXML
    public void pickTiles(ActionEvent actionEvent) {
        StringBuilder pt= new StringBuilder("pt");
        for (Position p:
                Chosen) {
            pt.append(" ").append(p.getY()).append(" ").append(p.getX());
        }
        System.out.println(pt);
        synchronized (GUI.Lock){
            GUI.message= String.valueOf(pt);
            GUI.Lock.notifyAll();
        }
        for (Position p:
             Chosen) {
            Node node = this.board.getChildren().stream()
                    .filter(child -> GridPane.getColumnIndex(child) == p.getY() && GridPane.getRowIndex(child) == p.getX())
                    .findFirst()
                    .orElse(null);
            HBox toDes = null;
            if (node instanceof HBox) {
                toDes = (HBox) node;
            }
            toDes.setStyle("");
        }
        Chosen=new ArrayList<>();
    }

    @FXML
    public void showChosenTiles(List<Tile> tiles,boolean toOrder) {
        if(toShow) {
            if(toOrder){
                OrderText.setVisible(true);
                OrderText.setText("Choose the order to insert them : ");
            }
            ChosenText.setVisible(true);
            pickedTiles = tiles.size();
            for (int i = 0; i < pickedTiles; i++) {
                Image image = new Image(tiles.get(i).getImage());
                ImageView Tile = new ImageView(image);
                Tile.setFitHeight(50);
                Tile.setFitWidth(50);
                chosenTiles.setAlignment(Pos.CENTER);
                if (toOrder) {
                    int finalI = i;
                    Tile.setOnMouseClicked(event -> {
                        orderedTiles.add(Tile, tilesOrdered, 0);
                        newOrder.add(finalI + 1);
                        tilesOrdered++;
                        Node node = chosenTiles.getChildren().stream()
                                .filter(child -> GridPane.getColumnIndex(child) ==  finalI)
                                .findFirst()
                                .orElse(null);
                        ImageView toCancel = null;
                        if (node instanceof ImageView) {
                            toCancel = (ImageView) node;
                        }
                        chosenTiles.getChildren().remove(toCancel);
                        if (tilesOrdered == pickedTiles) {

                            StringBuilder so = new StringBuilder("so");
                            for (int o :
                                    newOrder) {
                                so.append(" ").append(o);
                            }
                            System.out.println(so);
                            synchronized (GUI.Lock) {
                                GUI.message = String.valueOf(so);
                                GUI.Lock.notifyAll();
                            }
                            tilesOrdered=0;
                            newOrder.clear();
                            ChosenText.setVisible(false);
                            OrderText.setText("Your ordered Tiles: ");
                        }
                    });
                }
                chosenTiles.add(Tile, i, 0);
            }
            toShow=false;
        }
    }

    public void choose1(MouseEvent mouseEvent) {
        synchronized (GUI.Lock){
            GUI.message= "sc 1";
            GUI.Lock.notifyAll();
        }
    }

    public void choose2(MouseEvent mouseEvent) {
        synchronized (GUI.Lock){
            GUI.message= "sc 2";
            GUI.Lock.notifyAll();
        }
    }
    public void choose3(MouseEvent mouseEvent) {
        synchronized (GUI.Lock){
            GUI.message= "sc 3";
            GUI.Lock.notifyAll();
        }
    }

    public void choose4(MouseEvent mouseEvent) {
        synchronized (GUI.Lock){
            GUI.message= "sc 4";
            GUI.Lock.notifyAll();
        }
    }
    public void choose5(MouseEvent mouseEvent) {
        synchronized (GUI.Lock){
            GUI.message= "sc 5";
            GUI.Lock.notifyAll();
        }
    }

    public void newMessage(String message){
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);

        chat.getChildren().add(messageLabel);
    }
}
