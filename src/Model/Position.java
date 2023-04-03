/** Represents a pair of indices used to indicate a position on the board */
package Model;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
