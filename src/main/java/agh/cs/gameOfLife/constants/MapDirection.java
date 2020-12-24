package agh.cs.gameOfLife.constants;

import agh.cs.gameOfLife.Vector2d;

public enum MapDirection {
    NORTH(0),
    NORTHEAST(1),
    NORTHWEST(7),
    EAST(2),
    SOUTH(4),
    SOUTHEAST(3),
    SOUTHWEST(5),
    WEST(6);

    public final int value;

    MapDirection(int value) {
        this.value = value;
    }

    public static MapDirection getDirectionByNumber(int number) {
        for (MapDirection e : MapDirection.values()) {
            if (number == e.value)
                return e;
        }
        return null;
    }

    public String toString() {
        switch (this) {
            case NORTH: return "Północ";
            case SOUTH: return "Południe";
            case WEST: return "Zachód";
            case EAST: return "Wschód";
            case NORTHEAST: return "Północny Wschód";
            case NORTHWEST: return "Północny Zachód";
            case SOUTHEAST: return "Południowy Wschód";
            case SOUTHWEST: return "Południowy Zachód";
            default: return null;
        }

    }

    public MapDirection next() {
        switch (this) {
            case NORTH: return NORTHEAST;
            case EAST: return SOUTHEAST;
            case SOUTH: return SOUTHWEST;
            case WEST: return NORTHWEST;
            case NORTHEAST: return EAST;
            case NORTHWEST: return NORTH;
            case SOUTHEAST: return SOUTH;
            case SOUTHWEST: return WEST;
        }
        return null;
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH: return NORTHWEST;
            case EAST: return NORTHEAST;
            case SOUTH: return SOUTHEAST;
            case WEST: return SOUTHWEST;
            case NORTHEAST: return NORTH;
            case NORTHWEST: return WEST;
            case SOUTHEAST: return EAST;
            case SOUTHWEST: return SOUTH;
        }
        return null;
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case EAST: return new Vector2d(1,0);
            case SOUTH: return new Vector2d(0, -1);
            case WEST: return new Vector2d(-1, 0);
            case NORTHEAST: return new Vector2d(1, 1);
            case NORTHWEST: return new Vector2d(-1, 1);
            case SOUTHEAST: return new Vector2d(1, -1);
            case SOUTHWEST: return new Vector2d(-1, -1);
        }
        return new Vector2d(0, 1);
    }
}
