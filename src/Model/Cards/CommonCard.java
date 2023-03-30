package Model.Cards;

public abstract class CommonCard{
    private int current_points = 8;
    public void rescale(){
        current_points = current_points -2 ;
    }
    abstract public int isCompleted();
}
