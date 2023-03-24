import Cards.*;

import java.util.*;

public class Player {

    String Nickname;
    Tiles[][] Shelf= new Tiles[5][6]; // Controllare la dimensione
    //@see Cards.*
    Card PersonalCard= new personalCard();
    boolean firstToken,endToken;
    boolean scoreToken1,scoreToken2; //Credo sia maeglio averli come integer
    int id;

    public Player(String nick){
        Nickname=nick;
    }
}
