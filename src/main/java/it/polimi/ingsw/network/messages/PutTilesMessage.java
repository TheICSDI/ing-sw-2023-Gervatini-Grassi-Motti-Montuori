package main.java.it.polimi.ingsw.network.messages;

public class PutTilesMessage extends GeneralMessage{
    private final int num_col;
    public PutTilesMessage(int message_id, String username, int num_col) {
        super(message_id, Action.JOINLOBBY, username);
        this.num_col = num_col;
    }
    //serve per comunicare la colonna d'inserimento
    @Override
    public String toString()
    {
        return super.startMessage() + "," +
                "\"column\":" + this.num_col +
                "}";
    }

    public int getNum_col() {
        return num_col;
    }
}
