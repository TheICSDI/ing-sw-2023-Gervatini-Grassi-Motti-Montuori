package it.polimi.ingsw.view.GUI.SceneController;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Tile.Tile;
import it.polimi.ingsw.model.Tile.type;
import it.polimi.ingsw.view.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;



public class gameSceneController implements Initializable {
    @FXML
    public GridPane myShelfToken;
    @FXML
    public GridPane p4Token;
    @FXML
    public GridPane p2Token;
    @FXML
    public GridPane p3Token;
    @FXML
    public ImageView endGameToken;
    @FXML
    public Pane col1;
    @FXML
    public Pane col2;
    @FXML
    public Pane col3;
    @FXML
    public Pane col4;
    @FXML
    public Pane col5;
    @FXML
    public Label yourShelfText;
    @FXML
    public Label goalText;
    private List<String> players=new ArrayList<>();
    @FXML
    public Label YourName;
    @FXML
    public Label p2Name;
    @FXML
    public Label p3Name;
    @FXML
    public Label p4Name;
    @FXML
    public ImageView p3ImageShelf;
    @FXML
    public ImageView p4ImageShelf;
    @FXML
    public ImageView myShelfFirst;
    @FXML
    public ImageView p2First;
    @FXML
    public ImageView p3First;
    @FXML
    public ImageView p4First;
    @FXML
    public Label ingameEvents;
    @FXML
    public Label turn;
    @FXML
    public TextField Chat;
    @FXML
    public MenuButton SendTo;


    private String recipient= "ca ";
    private boolean firstOthers;
    private final int dim = 9;
    private int pickedTiles;
    private int tilesOrdered;
    private boolean toShow=true;
    private final List<Integer> newOrder = new ArrayList<>();
    @FXML
    public Button pickTiles;
    @FXML
    public ImageView personal;
    @FXML
    public ImageView common1;
    @FXML
    public ImageView common1points;
    private int c1Index;
    @FXML
    public ImageView common2points;
    private int c2Index;
    @FXML
    public ImageView common2;

    private List<Image> CommonPoints=new ArrayList<>();
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
    private List<Position> Chosen = new ArrayList<>();
    private final Tile[][] localBoard = new Tile[dim][dim];
    @FXML
    public GridPane board;
    @FXML
    public GridPane myShelf;
    private final List<GridPane> othersShelf = new ArrayList<>();
    @FXML
    public GridPane p2Shelf;
    @FXML
    public GridPane p3Shelf;
    @FXML
    public GridPane p4Shelf;
    private GUI gui;

    /** It initialises the version of the local board to not_accessible type. */
    // Non ho capito quando lo chiama però se non lo metto non funziona :P
    public gameSceneController(){
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                this.localBoard[i][j] = new Tile("NOT_ACCESSIBLE");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myShelfToken.setAlignment(Pos.CENTER);
        this.othersShelf.add(p2Shelf);
        p2Token.setAlignment(Pos.CENTER);
        this.othersShelf.add(p3Shelf);
        p3Token.setAlignment(Pos.CENTER);
        this.othersShelf.add(p4Shelf);
        p4Token.setAlignment(Pos.CENTER);
        this.firstOthers=true;
        this.Chat.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendChatMessage();
            }
        });
        setLabelText(this.ChosenText,"Comic Sans MS",20,"Your Tiles:");
        this.ChosenText.setVisible(false);
        setLabelText(goalText,"Comic Sans MS", 20,"Your personal goal");
        setLabelText(yourShelfText,"Comic Sans MS", 20, "Your shelf:");
        this.scrollChat.setMaxHeight(Double.MAX_VALUE);
        if(players.size()==4) {
            CommonPoints.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/scoring tokens/scoring_2.jpg"))));
        }
        CommonPoints.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/scoring tokens/scoring_4.jpg"))));
        if(players.size()>=3) {
            CommonPoints.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/scoring tokens/scoring_6.jpg"))));
        }
        CommonPoints.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/scoring tokens/scoring_8.jpg"))));

        //immagini tiles
    }


    /** It updates the board on the panel with the right image. */
    @FXML
    public void showBoard(Tile[][] board){
        //For each element in the board
        this.board.getChildren().clear();
        clearChosen();
        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                ImageView Tile = new ImageView();
                HBox pane= new HBox();
                pane.setMaxWidth(47);
                pane.setMaxHeight(47);
                Tile.setFitHeight(43);
                Tile.setFitWidth(43);
                if(!board[i][j].getCategory().equals(type.EMPTY) && !board[i][j].getCategory().equals(type.NOT_ACCESSIBLE)) {
                    Image image = new Image(board[i][j].getImage());
                    System.out.println(image);
                    Tile.setImage(image);
                    pane.getChildren().add(Tile);
                    pane.setAlignment(Pos.CENTER);

                    int finalJ = j;
                    int finalI = i;
                    pane.setOnMouseClicked(event -> {
                        boolean alreadyChosen = false;
                        for (Position p :
                                this.Chosen) {
                            if (p.getY() == finalI && p.getX() == finalJ) {
                                pane.setStyle("");
                                this.Chosen.remove(p);
                                alreadyChosen = true;
                                break;
                            }
                        }
                        if (!alreadyChosen) {
                            pane.setStyle("-fx-background-color: #ff0000;");
                            if (this.Chosen.size() == 3) {
                                Node node = this.board.getChildren().stream()
                                        .filter(child -> GridPane.getColumnIndex(child) == this.Chosen.get(0).getY() && GridPane.getRowIndex(child) == this.Chosen.get(0).getX())
                                        .findFirst()
                                        .orElse(null);
                                HBox toDes = null;
                                if (node instanceof HBox) {
                                    toDes = (HBox) node;
                                }
                                if(toDes!=null) {
                                    toDes.setStyle("");
                                    this.Chosen.remove(0);
                                }else{
                                    System.out.println("toDes == null");
                                }
                            }
                            this.Chosen.add(new Position(finalJ, finalI));
                        }
                    });
                    this.board.add(pane, i, j);
                    this.board.setAlignment(Pos.CENTER);
                }
            }
        }
        //
        /*for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < this.dim; j++) {
                //If the current tile is accessible and different from the local board
                //its corresponding image is set
                if(!board[i][j].getCategory().equals(type.NOT_ACCESSIBLE) &&
                        (!board[i][j].getCategory().equals(this.localBoard[i][j].getCategory()))){
                    ImageView Tile = new ImageView();
                    HBox pane= new HBox();
                    pane.setMaxWidth(47);
                    pane.setMaxHeight(47);
                    Tile.setFitHeight(43);
                    Tile.setFitWidth(43);
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
                                    this.Chosen) {
                                if(p.getY()==finalI && p.getX()==finalJ)
                                {
                                    finalPane.setStyle("");
                                    this.Chosen.remove(p);
                                    alreadyChosen=true;
                                    break;
                                }
                            }
                            if(!alreadyChosen){
                                finalPane.setStyle("-fx-background-color: #ff0000;");
                                if(this.Chosen.size()==3) {
                                    Node node = this.board.getChildren().stream()
                                            .filter(child -> GridPane.getColumnIndex(child) == this.Chosen.get(0).getY() && GridPane.getRowIndex(child) == this.Chosen.get(0).getX())
                                            .findFirst()
                                            .orElse(null);
                                    HBox toDes = null;
                                    if (node instanceof HBox) {
                                        toDes = (HBox) node;
                                    }
                                    toDes.setStyle("");
                                    this.Chosen.remove(0);
                                }
                                this.Chosen.add(new Position(finalJ, finalI));
                            }
                        });
                        this.board.add(pane, i, j);
                        this.board.setAlignment(Pos.CENTER);
                    }
                    this.localBoard[i][j] = board[i][j];
                }
            }
        }*/


    }

    private void clearChosen() {
        for (Position p:
                this.Chosen) {
            Node node = this.board.getChildren().stream()
                    .filter(child -> GridPane.getColumnIndex(child) == p.getY() && GridPane.getRowIndex(child) == p.getX())
                    .findFirst()
                    .orElse(null);
            HBox toDes = null;
            if (node instanceof HBox) {
                toDes = (HBox) node;
            }
            if(toDes != null) {
                toDes.setStyle("");
            }
        }
        this.Chosen=new ArrayList<>();
    }

    @FXML
    public void showShelf(Tile[][] shelf) {
        //reset variables for picked tiles
        this.ChosenText.setVisible(false);
        this.OrderText.setVisible(false);
        this.chosenTiles.getChildren().clear();
        this.orderedTiles.getChildren().clear();
        this.toShow = true;

        for (int i = 0; i < shelf.length; i++) {
            for (int j = 0; j < shelf[0].length; j++) {
                if (!shelf[i][j].getCategory().equals(type.EMPTY)) {
                    ImageView Tile = new ImageView();
                    Tile.setFitHeight(34);
                    Tile.setFitWidth(34);
                    Image image = new Image(shelf[i][j].getImage());
                    Tile.setImage(image);
                    this.myShelf.setAlignment(Pos.CENTER);
                    this.myShelf.add(Tile, j, i);
                }
            }
        }
    }


    @FXML
    //forse ho capito perché funziona solo con le mappe, allora "others" dovrebbe avere dei problemi nel riempimento
    // ma solo dei player, motivo per cui con un lista non funziona, e la parte delle String lo salva
    public void showOthers(Map<String, Player> others){
        if(firstOthers){
            firstOthers=false;
            YourName.setText(gui.Name);
            players.add(gui.Name);
            List<String> names= new ArrayList<>(others.keySet());//sperando gli metta in ordine
            for (String s:
                    names) {
                System.out.println(s);
            }
            setLabelText(p2Name,"Comic Sans MS", 20, names.get(0) + "'s shelf:");
            //p2Name.setText(names.get(0) + "'s shelf:");
            players.add(names.get(0));
            common1points.setImage(CommonPoints.get(CommonPoints.size()-1));
            c1Index=CommonPoints.size()-1;
            common2points.setImage(CommonPoints.get(CommonPoints.size()-1));
            c2Index=CommonPoints.size()-1;
            if(names.size()>1){ //3 giocatori
                setLabelText(p3Name,"Comic Sans MS", 20, names.get(1) + "'s shelf:");
                //p3Name.setText(names.get(1) + "'s shelf:");
                players.add(names.get(1));
                p3ImageShelf.setVisible(true);
            }
            if(names.size()>2){//4 giocatori
                setLabelText(p4Name,"Comic Sans MS", 20, names.get(2) + "'s shelf:");
                //p4Name.setText(names.get(2) + "'s shelf:");
                players.add(names.get(2));
                p4ImageShelf.setVisible(true);
            }
            for (String n: names) {
                MenuItem player=new MenuItem(n);
                player.setOnAction(event->{
                    recipient="c " + n + " ";
                    SendTo.setText(n);
                });
                SendTo.getItems().add(player);
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
                        Tile.setFitHeight(35);
                        Tile.setFitWidth(35);
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
    public void sendAll(){
        recipient="ca ";
        SendTo.setText("All");
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
                case 1 -> commonURL= "/Images/common goal cards/4.jpg";
                case 2 -> commonURL = "/Images/common goal cards/8.jpg";
                case 3 -> commonURL = "/Images/common goal cards/3.jpg";
                case 4 -> commonURL = "/Images/common goal cards/1.jpg";
                case 5 -> commonURL = "/Images/common goal cards/5.jpg";
                case 6 -> commonURL = "/Images/common goal cards/9.jpg";
                case 7 -> commonURL = "/Images/common goal cards/11.jpg";
                case 8 -> commonURL = "/Images/common goal cards/7.jpg";
                case 9 -> commonURL = "/Images/common goal cards/2.jpg";
                case 10 -> commonURL = "/Images/common goal cards/6.jpg";
                case 11 -> commonURL = "/Images/common goal cards/10.jpg";
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

    public void commonCompleted(String msg,boolean first,String whoCompleted){
        newMessage(msg);//temporaneo
        if(first){
            ImageView pointsWon=new ImageView(CommonPoints.get(c1Index));
            pointsWon.setFitWidth(45);
            pointsWon.setFitHeight(45);
            if(whoCompleted.equals(players.get(0))){
                myShelfToken.add(pointsWon,0,0);
            }else if(whoCompleted.equals(players.get(1))){
                p2Token.add(pointsWon,0,0);
            }else if(whoCompleted.equals(players.get(2))){
                p3Token.add(pointsWon,0,0);
            }else if(whoCompleted.equals(players.get(3))){
                p4Token.add(pointsWon,0,0);
            }
            c1Index--;
            if(c1Index>=0){
                common1points.setImage(CommonPoints.get(c1Index));
            }else{
                common1points.setImage(null);
            }
        }else{
            ImageView pointsWon=new ImageView(CommonPoints.get(c2Index));
            pointsWon.setFitWidth(45);
            pointsWon.setFitHeight(45);
            if(whoCompleted.equals(players.get(0))){
                myShelfToken.add(pointsWon,1,0);
            }else if(whoCompleted.equals(players.get(1))){
                p2Token.add(pointsWon,1,0);
            }else if(whoCompleted.equals(players.get(2))){
                p3Token.add(pointsWon,1,0);
            }else if(whoCompleted.equals(players.get(3))){
                p4Token.add(pointsWon,1,0);
            }
            c2Index--;
            if(c2Index>=0){
                common2points.setImage(CommonPoints.get(c2Index));
            }else{
                common2points.setImage(null);
            }

        }

    }

    @FXML
    public void pickTiles(ActionEvent actionEvent) {
        StringBuilder pt= new StringBuilder("pt");
        for (Position p:
                this.Chosen) {
            pt.append(" ").append(p.getY()).append(" ").append(p.getX());
        }
        System.out.println(pt);
        synchronized (gui.Lock){
            gui.message= String.valueOf(pt);
            gui.Lock.notifyAll();
        }
        clearChosen();
    }

    @FXML
    public void showChosenTiles(List<Tile> tiles,boolean toOrder) {
        if(this.toShow) {
            if(toOrder){
                this.OrderText.setVisible(true);
                setLabelText(this.OrderText,"Comic Sans MS", 20, "Choose insert order: ");
                //this.OrderText.setText("Choose the order to insert them : ");
            }
            this.ChosenText.setVisible(true);
            this.pickedTiles = tiles.size();
            for (int i = 0; i < this.pickedTiles; i++) {
                Image image = new Image(tiles.get(i).getImage());
                ImageView Tile = new ImageView(image);
                Tile.setFitHeight(50);
                Tile.setFitWidth(50);
                this.chosenTiles.setAlignment(Pos.CENTER);
                if (toOrder) {
                    int finalI = i;
                    Tile.setOnMouseClicked(event -> {
                        Tile.setOnMouseClicked(null);
                        this.orderedTiles.add(Tile, tilesOrdered, 0);
                        this.newOrder.add(finalI + 1);
                        this.tilesOrdered++;
                        Node node = this.chosenTiles.getChildren().stream()
                                .filter(child -> GridPane.getColumnIndex(child) ==  finalI)
                                .findFirst()
                                .orElse(null);
                        ImageView toCancel = null;
                        if (node instanceof ImageView) {
                            toCancel = (ImageView) node;
                        }
                        this.chosenTiles.getChildren().remove(toCancel);
                        if (this.tilesOrdered == this.pickedTiles) {
                            StringBuilder so = new StringBuilder("so");
                            for (int o :
                                    this.newOrder) {
                                so.append(" ").append(o);
                            }
                            System.out.println(so);
                            synchronized (this.gui.Lock) {
                                this.gui.message = String.valueOf(so);
                                this.gui.Lock.notifyAll();
                            }
                            this.tilesOrdered=0;
                            this.newOrder.clear();
                            this.ChosenText.setVisible(false);
                            setLabelText(this.OrderText,"Comic Sans MS", 20, "Your ordered Tiles: ");
                            //this.OrderText.setText("Your ordered Tiles: ");
                        }
                    });
                }
                chosenTiles.add(Tile, i, 0);
            }
            toShow = false;
        }
    }

    public void choose1(MouseEvent mouseEvent) {
        synchronized (this.gui.Lock){
            this.gui.message= "sc 1";
            this.gui.Lock.notifyAll();
        }
    }

    public void choose2(MouseEvent mouseEvent) {
        synchronized (this.gui.Lock){
            this.gui.message= "sc 2";
            this.gui.Lock.notifyAll();
        }
    }
    public void choose3(MouseEvent mouseEvent) {
        synchronized (this.gui.Lock){
            this.gui.message= "sc 3";
            this.gui.Lock.notifyAll();
        }
    }

    public void choose4(MouseEvent mouseEvent) {
        synchronized (this.gui.Lock){
            this.gui.message= "sc 4";
            this.gui.Lock.notifyAll();
        }
    }
    public void choose5(MouseEvent mouseEvent) {
        synchronized (this.gui.Lock){
            this.gui.message= "sc 5";
            this.gui.Lock.notifyAll();
        }
    }

    public void newMessage(String message){
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setTextFill(Paint.valueOf("#ffffff"));
        this.chat.getChildren().add(0,messageLabel);
    }

    public void Turn(String msg,boolean firstTurn){
        if(firstTurn){
            ImageView firstPlayerToken=new ImageView(new Image("/Images/misc/firstplayertoken.png"));
            firstPlayerToken.setFitHeight(45);
            firstPlayerToken.setFitWidth(45);

            if(msg.equals("It's your turn!")){
                myShelfToken.add(firstPlayerToken,0,1);
            }else if(msg.equals("It's " + players.get(1) + "'s turn!")){
                p2Token.add(firstPlayerToken,1,1);
            }else if(msg.equals("It's " + players.get(2) + "'s turn!")){
                p3Token.add(firstPlayerToken,1,1);
            }else if(msg.equals("It's " + players.get(3) + "'s turn!")){
                p4Token.add(firstPlayerToken,0,1);
            }
        }
        setLabelText(turn,"Comic Sans MS",50,msg);
        //turn.setText(cartoonText);

    }

    public void setIngameEvents(String msg){
        setLabelText(ingameEvents,"Comic Sans MS", 24,msg);
        //this.ingameEvents.setText(msg);
    }

    public void sendChatMessage(){

        String message= this.Chat.getText();
        newMessage("You -> " + SendTo.getText() + ": " + message);
        this.Chat.setText("");
        if(!Objects.equals(message, "")) {
            message= recipient + message;
            System.out.println(message);
            synchronized (this.gui.Lock) {
                this.gui.message = message;
                this.gui.Lock.notifyAll();
            }
        }
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public void endGameToken(String player) {
        endGameToken.setImage(null);
        ImageView endToken=new ImageView(new Image("/Images/scoring tokens/end game.jpg"));
        endToken.setFitHeight(45);
        endToken.setFitWidth(45);
        if(player.equals(players.get(0))){
            myShelfToken.add(endToken,1,1);
        }else if(player.equals(players.get(1))){
            p2Token.add(endToken,0,1);
        }else if(player.equals(players.get(2))){
            p3Token.add(endToken,0,1);
        }else if(player.equals(players.get(3))){
            p4Token.add(endToken,1,1);
        }
    }

    public void highlightCol1(MouseEvent mouseEvent) {
        col1.setStyle("-fx-background-color: rgba(255,251,2,0.42)");
    }

    public void highlightCol2(MouseEvent mouseEvent) {
        col2.setStyle("-fx-background-color: rgba(255,251,2,0.42)");
    }

    public void highlightCol3(MouseEvent mouseEvent) {
        col3.setStyle("-fx-background-color: rgba(255,251,2,0.42)");
    }

    public void highlightCol4(MouseEvent mouseEvent) {
        col4.setStyle("-fx-background-color: rgba(255,251,2,0.42)");
    }

    public void highlightCol5(MouseEvent mouseEvent) {
        col5.setStyle("-fx-background-color: rgba(255,251,2,0.42)");
    }

    public void desCol1(MouseEvent mouseEvent) {
        col1.setStyle("");
    }

    public void desCol2(MouseEvent mouseEvent) {
        col2.setStyle("");
    }

    public void desCol3(MouseEvent mouseEvent) {
        col3.setStyle("");
    }

    public void desCol4(MouseEvent mouseEvent) {
        col4.setStyle("");
    }

    public void desCol5(MouseEvent mouseEvent) {
        col5.setStyle("");
    }

    public void setLabelText(Label label, String font, int size, String msg){
        Text cartoonText = new Text(msg);

        // Impostazione dello stile del testo
        cartoonText.setFont(Font.font(font, FontWeight.BOLD, size));
        cartoonText.setFill(Color.WHITE);
        cartoonText.setStroke(Color.BLACK);
        cartoonText.setStrokeWidth(1);
        label.setGraphic(cartoonText);
    }
}