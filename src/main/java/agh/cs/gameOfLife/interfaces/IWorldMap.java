package agh.cs.gameOfLife.interfaces;

import agh.cs.gameOfLife.animal.Animal;
import agh.cs.gameOfLife.Vector2d;
import agh.cs.gameOfLife.data.DataStorage;

public interface IWorldMap {
    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    /**
     * Place a animal on the map.
     *
     * @param animal
     *            The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the map is already occupied.
     */
    void place(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position
     *            Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

//    /**
//     * Return an object at a given position.
//     *
//     * @param position
//     *            The position of the object.
//     * @return Object or null if the position is not occupied.
//     */
//    Object objectAt(Vector2d position);

    int getWidth();
    int getHeight();
    DataStorage getDataStorage();
}
