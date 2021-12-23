package simulator.map;

import simulator.animal.Animal;
import simulator.map_elements.Grass;
import simulator.helper.RNG;
import simulator.map_elements.Vector2D;

import java.util.*;

public abstract class AbstractWorldMap {
    private final int height;
    private final int width;
    private final Vector2D jungleStart;
    private final Vector2D jungleEnd;
    private final int jungleWidth;
    private final int jungleHeight;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public Map<Vector2D, ArrayList<Animal>> animals = new HashMap<>();
    public Map<Vector2D, Grass> grassMap = new HashMap<>();
    public Set<Vector2D> freeJungleTiles = new HashSet<>();
    public Set<Vector2D> freeSavannahTiles = new HashSet<>();

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
            this.removeFromFreeTiles(animal.getPosition());
        }
    }

    public void removeAnimal(Animal animal, Vector2D oldPosition) {
        if(animals.get(oldPosition).size() == 1) {
            animals.remove(oldPosition);
            this.addToFreeTiles(oldPosition);
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
        return false;
    }

    public Vector2D getNewPosition(Vector2D position){
        return position;
    }

    public void addAllFreeTiles() {
        for(int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Vector2D position = new Vector2D(x, y);
                if(!animals.containsKey(position)) {
                    addToFreeTiles(position);
                }
            }
        }
    }

    public void addToFreeTiles(Vector2D position) {
        if(position.isInRectangle(jungleStart, jungleEnd)) freeJungleTiles.add(position);
        else freeSavannahTiles.add(position);
    }

    public void removeFromFreeTiles(Vector2D position) {
        if(position.isInRectangle(jungleStart, jungleEnd)) freeJungleTiles.remove(position);
        else freeSavannahTiles.remove(position);
    }

    public void generateGrass() {
        List<Set<Vector2D>> listOfSets = new ArrayList<>();
        listOfSets.add(freeJungleTiles);
        listOfSets.add(freeSavannahTiles);
        for(Set<Vector2D> set : listOfSets){
            if (!set.isEmpty()) {
                int size = set.size();
                int index = RNG.rng(0, size - 1);
                int i = 0;
                for (Vector2D position : set) {
                    if (i == index) {
                        Grass grass = new Grass(position);
                        grassMap.put(position, grass);
                        set.remove(position);
                        break;
                    }
                    i++;
                }
            }
        }
    }
}
