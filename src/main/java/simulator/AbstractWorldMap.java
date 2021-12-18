package simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorldMap {
    private final int height;
    private final int width;
    private final Vector2D jungleStart;
    private final Vector2D jungleEnd;
    private int jungleWidth;
    private int jungleHeight;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public Map<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    public Map<Vector2D, Grass> grassMap = new HashMap<>();

    public AbstractWorldMap(int height, int width, double jungleRatio, int moveEnergy, int plantEnergy, int startEnergy) {
        this.width = width;
        this.height = height;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.startEnergy = startEnergy;

        this.jungleWidth = (int) Math.floor(this.width * jungleRatio);
        this.jungleHeight = (int) Math.floor(this.height * jungleRatio);
        this.jungleStart = new Vector2D((int) (Math.floor(this.width - jungleWidth) / 2), (int) (Math.floor(this.height - jungleHeight) / 2));
        this.jungleEnd = this.jungleStart.add(new Vector2D(jungleWidth, jungleHeight));
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
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

    public void removeAnimal(Animal animal, Vector2D oldPosition) {
        if(animals.get(oldPosition).size() == 1) {
            animals.remove(oldPosition);
        }
        else {
            animals.get(oldPosition).remove(animal);
        }
    }

    public boolean canMoveTo(Vector2D position) {
        return true;
    }

    public void grassEaten(Grass grass) {
        this.grassMap.remove(grass.getPosition(), grass);
    }

    public void generateGrassJungle() {
        boolean generated = false;
        int counter = 0;

        while(!generated) {
            Vector2D position = new Vector2D(RNG.rng(jungleStart.x, jungleEnd.x), RNG.rng(jungleStart.y, jungleEnd.y));
            counter += 1;
            if(!(grassMap.containsKey(position) || animals.containsKey(position))) {
                Grass grass = new Grass(position);
                grassMap.put(position, grass);
                generated = true;
                counter = 0;
            }
            if(counter >= jungleHeight * jungleWidth) {
                generated = true;
                generateGrassJungleIteratively();
            }
        }
    }

    public void generateGrassJungleIteratively() {
        boolean generated = false;
        for(int x = jungleStart.x; x <= jungleEnd.x; x++) {
            for(int y = jungleStart.y; y<= jungleEnd.y; y++) {
                Vector2D position = new Vector2D(x, y);
                if (!(grassMap.containsKey(position) || animals.containsKey(position))) {
                    Grass grass = new Grass(position);
                    grassMap.put(position, grass);
                    generated = true;
                    break;
                }
            }
            if(generated) break;
        }
    }

    public void generateGrassSavannah() {
        boolean generated = false;
        while(!generated) {
            int x;
            if(RNG.rng(0, 1) == 0) {
                x = RNG.rng(0, jungleStart.x - 1);
            }
            else {
                x = RNG.rng(jungleEnd.x + 1, width - 1);
            }
            int y;
            if(RNG.rng(0, 1) == 0) {
                y = RNG.rng(0, jungleStart.y - 1);
            }
            else {
                y = RNG.rng(jungleEnd.y + 1, height - 1);
            }
            Vector2D position = new Vector2D(x, y);
            if(!(grassMap.containsKey(position) || animals.containsKey(position))) {
                Grass grass = new Grass(position);
                grassMap.put(position, grass);
                generated = true;
            }
        }
    }
}
