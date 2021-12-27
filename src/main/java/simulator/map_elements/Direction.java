package simulator.map_elements;

public enum Direction {
    NORTH(0),
    NORTH_EAST(45),
    EAST(90),
    SOUTH_EAST(135),
    SOUTH(180),
    SOUTH_WEST(225),
    WEST(270),
    NORTH_WEST(315)
    ;

    Direction(int angle) {
        this.angle = angle;
    }

    public final int angle;

    public static Direction getDirection(int angle) {
        switch(angle) {
            case 0 -> {
                return NORTH;
            }
            case 45 -> {
                return NORTH_EAST;
            }
            case 90 -> {
                return EAST;
            }
            case 135 -> {
                return SOUTH_EAST;
            }
            case 180 -> {
                return SOUTH;
            }
            case 225 -> {
                return SOUTH_WEST;
            }
            case 270 -> {
                return WEST;
            }
            case 315 -> {
                return NORTH_WEST;
            }
            default -> throw new IllegalArgumentException("Not a proper angle: " + angle);
        }
    }
}
