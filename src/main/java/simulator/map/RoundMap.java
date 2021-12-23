package simulator.map;

import simulator.map_elements.Vector2D;

public class RoundMap extends AbstractWorldMap{

    //constructor
    public RoundMap(int height, int width, double jungleRatio, int moveEnergy, int plantEnergy, int startEnergy) {
        super(height, width, jungleRatio, moveEnergy, plantEnergy, startEnergy);
    }

    //methods
    @Override
    public Vector2D getNewPosition(Vector2D position) {
        return new Vector2D((position.x + this.getWidth()) % this.getWidth(), (position.y + this.getHeight()) % getHeight());
    }
}
