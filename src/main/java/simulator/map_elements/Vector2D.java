package simulator.map_elements;

import java.util.Objects;

public class Vector2D {
    public final int x;
    public final int y;

    //constructor
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //methods
    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D next(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return this.add(new Vector2D(0, 1));
            }
            case NORTH_EAST -> {
                return this.add(new Vector2D(1, 1));
            }
            case EAST -> {
                return this.add(new Vector2D(1, 0));
            }
            case SOUTH_EAST -> {
                return this.add(new Vector2D(1, -1));
            }
            case SOUTH -> {
                return this.add(new Vector2D(0, -1));
            }
            case SOUTH_WEST -> {
                return this.add(new Vector2D(-1, -1));
            }
            case WEST -> {
                return this.add(new Vector2D(-1, 0));
            }
            case NORTH_WEST -> {
                return this.add(new Vector2D(-1, 1));
            }
            default -> throw new IllegalArgumentException(direction + " is not a valid direction");
        }
    }

    public Vector2D previous(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return this.subtract(new Vector2D(0, 1));
            }
            case NORTH_EAST -> {
                return this.subtract(new Vector2D(1, 1));
            }
            case EAST -> {
                return this.subtract(new Vector2D(1, 0));
            }
            case SOUTH_EAST -> {
                return this.subtract(new Vector2D(1, -1));
            }
            case SOUTH -> {
                return this.subtract(new Vector2D(0, -1));
            }
            case SOUTH_WEST -> {
                return this.subtract(new Vector2D(-1, -1));
            }
            case WEST -> {
                return this.subtract(new Vector2D(-1, 0));
            }
            case NORTH_WEST -> {
                return this.subtract(new Vector2D(-1, 1));
            }
            default -> throw new IllegalArgumentException(direction + " is not a valid direction");
        }
    }

    public boolean isInRectangle(Vector2D lowerLeft, Vector2D upperRight) {
        return this.x >= lowerLeft.x && this.y >= lowerLeft.y
                && this.x <= upperRight.x && this.y <= upperRight.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
