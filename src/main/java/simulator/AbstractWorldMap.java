package simulator;

import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractWorldMap {
    private final int height;
    private final int width;
    private final Vector2D jungleStart;
    private final Vector2D jungleEnd;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public Map<Vector2D, ArrayList<Animal>> animals;
    public Map<Vector2D, Grass> grassMap;

    public AbstractWorldMap(int height, int width, double jungleRatio, int moveEnergy, int plantEnergy, int startEnergy) {
        this.width = width;
        this.height = height;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;

        int jungleWidth = (int) Math.floor(this.width * jungleRatio);
        int jungleHeight = (int) Math.floor(this.height * jungleRatio);
        this.jungleStart = new Vector2D((int) (Math.floor(this.width - jungleWidth) / 2), (int) (Math.floor(this.height - jungleHeight) / 2));
        this.jungleEnd = this.jungleStart.add(new Vector2D(jungleWidth, jungleHeight));
    }

    public void addAnimal(Animal animal) {
        if(animals.containsKey(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
        }
        else {
            ArrayList<Animal> location = new ArrayList<>();
            location.add(animal);
            animals.put(animal.getPosition(), location);
        }
    }

    public void removeAnimal(Animal animal) {
        if(animals.get(animal.getPosition()).size() == 1) {
            animals.remove(animal.getPosition());
        }
        else {
            animals.get(animal.getPosition()).remove(animal);
        }
    }

    public void grassEaten(Grass grass) {
        this.grassMap.remove(grass.getPosition());
    }
}
