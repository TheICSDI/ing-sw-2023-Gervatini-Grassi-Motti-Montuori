package it.polimi.ingsw.model;

import java.util.Objects;

/** Represents a pair of indices used to indicate a position on the board.
 * @author Caterina Motti. */
public class Position {
    private final int x;
    private final int y;

    /** It creates a position given a pair of indexes. */
    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }

    /** Gets the x-index of the current position. */
    public int getX() {
        return x;
    }

    /** Gets the y-index of the current position. */
    public int getY() { return y;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
