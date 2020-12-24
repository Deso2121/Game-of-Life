package agh.cs.gameOfLife.mapObjects;

import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.interfaces.IMapElement;

import java.util.Objects;

public class Grass implements IMapElement {
    private Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grass grass = (Grass) o;

        return Objects.equals(position, grass.position);
    }

    @Override
    public int hashCode() {
        return position != null ? position.hashCode() : 0;
    }
}
