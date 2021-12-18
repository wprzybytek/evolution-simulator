package simulator;

public class FlatMap extends AbstractWorldMap{
    public FlatMap(int height, int width, double jungleRatio, int moveEnergy, int plantEnergy, int startEnergy) {
        super(height, width, jungleRatio, moveEnergy, plantEnergy, startEnergy);
    }

    @Override
    public boolean canMoveTo(Vector2D position) {
        return position.x < this.getWidth() && position.x >= 0 && position.y < this.getHeight() && position.y >= 0;
    }
}
