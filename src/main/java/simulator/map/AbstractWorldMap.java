package simulator.map;

import simulator.animal.Animal;
import simulator.map_elements.Grass;
import simulator.helper.RNG;
import simulator.map_elements.Vector2D;

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

    //constructor
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

    //getters
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    //setters
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

    public void grassEaten(Grass grass) {
        this.grassMap.remove(grass.getPosition(), grass);
    }

    //methods
    public boolean canMoveTo(Vector2D position) {
        return true;
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
        int counter = 0;

        while(!generated) {
            Vector2D position = new Vector2D(RNG.rng(0, width - 1), RNG.rng(0, height - 1));
            counter += 1;
            if(!((position.x >= jungleStart.x && position.x <= jungleEnd.x
                    && position.y >= jungleStart.y && position.y <= jungleEnd.y)
                    && (grassMap.containsKey(position) || animals.containsKey(position)))) {
                Grass grass = new Grass(position);
                grassMap.put(position, grass);
                generated = true;
                counter = 0;
            }
            if(counter >= height * width) {
                generated = true;
                generateGrassSavannahIteratively();
            }
        }
    }

    private void generateGrassSavannahIteratively() {
        boolean generated = false;
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                Vector2D position = new Vector2D(x, y);
                if (!((position.x >= jungleStart.x && position.x <= jungleEnd.x
                        && position.y >= jungleStart.y && position.y <= jungleEnd.y)
                        && (grassMap.containsKey(position) || animals.containsKey(position)))) {
                    Grass grass = new Grass(position);
                    grassMap.put(position, grass);
                    generated = true;
                    break;
                }
            }
            if(generated) break;
        }
    }
}
