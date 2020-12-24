package agh.cs.gameOfLife.mapObjects;

import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.interfaces.IMapElement;

import java.util.Objects;

public class Jungle implements IMapElement {
    @Override
    public Vector2d getPosition() {
        return null;
    }

    private Vector2d position;

    public Jungle(Vector2d position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Jungle jungle = (Jungle) o;

        return Objects.equals(position, jungle.position);
    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "*";
    }
}
