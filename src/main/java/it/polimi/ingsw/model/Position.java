/** Represents a pair of indices used to indicate a position on the board */
package it.polimi.ingsw.model;

import java.util.Objects;

public class Position {
    private final int x;
    private final int y;
    public Position(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

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
