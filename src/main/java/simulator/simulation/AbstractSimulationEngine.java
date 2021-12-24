package simulator.simulation;

import simulator.animal.Genome;
import simulator.helper.RNG;
import simulator.animal.Animal;
import simulator.animal.Copulation;
import simulator.map.AbstractWorldMap;
import simulator.map.FlatMap;
import simulator.map.RoundMap;
import simulator.map_elements.Grass;
import simulator.map_elements.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractSimulationEngine implements Runnable{
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    protected int day;
    protected int animalsNumber;
    protected int sumOfLifetimesForDead = 0;
    protected int deadAnimals = 0;
    protected final int eraTime;
    protected final List<ITurnEndObserver> observerList = new ArrayList<>();
    protected final AbstractWorldMap map;

    protected volatile boolean running = true;
    protected volatile boolean paused = false;
    protected final Object pauseLock = new Object();

    //constructor
    public AbstractSimulationEngine(int width, int height, int startEnergy, int moveEnergy, int plantEnergy,
                                    float jungleRation, int numberOfAnimals, int eraTime, boolean isFlat) {

        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.eraTime = eraTime;

        if (isFlat) {
            map = new FlatMap(height, width, jungleRation, moveEnergy, plantEnergy, startEnergy);
        } else {
            map = new RoundMap(height, width, jungleRation, moveEnergy, plantEnergy, startEnergy);
        }

        int animalCounter = 0;

        while (animalCounter < numberOfAnimals) {
            Vector2D position = new Vector2D(RNG.rng(0, width - 1), RNG.rng(0, height - 1));
            if (!(map.animals.containsKey(position))) {
                Animal animal = new Animal(startEnergy, position, this.map);
                map.addAnimal(animal);
                animalCounter++;
            }
        }

        map.addAllFreeTiles();
    }

    //getters
    public AbstractWorldMap getMap() {
        return this.map;
    }

    public int getDay() {
        return day;
    }

    public int getGrassNumber() {
        return this.map.grassMap.size();
    }

    public int getAnimalsNumber() {
        int size = 0;
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            size += animalsList.getValue().size();
        }
        this.animalsNumber = size;
        return size;
    }

    public String getDominantGenotype() {
        Map<Genome, Integer> genomesMap = new HashMap<>();
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            for (Animal animal : animalsList.getValue()) {
                if(genomesMap.containsKey(animal.getGenome())) {
                    int value = genomesMap.get(animal.getGenome());
                    genomesMap.remove(animal.getGenome());
                    genomesMap.put(animal.getGenome(), value + 1);
                }
                else genomesMap.put(animal.getGenome(), 1);
            }
        }

        Genome dominant = new Genome();
        int value = -1;

        for(Map.Entry<Genome, Integer> entry : genomesMap.entrySet()) {
            if(entry.getValue() > value) {
                dominant = entry.getKey();
                value = entry.getValue();
            }
        }

        return dominant.toString();
    }

    public int getAvgEnergy() {
        int size = this.animalsNumber;
        if(size == 0) return 0;
        int sumOfEnergy = 0;
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            for (Animal animal : animalsList.getValue()) {
                sumOfEnergy += animal.getEnergy();
            }
        }
        return (int) sumOfEnergy/size;
    }

    public int getAvgLifeTime() {
        if(deadAnimals == 0) return 0;
        return (int) sumOfLifetimesForDead/deadAnimals;
    }

    public int getAvgChildren() {
        int size = this.animalsNumber;
        if(size == 0) return 0;
        int sumOfChildren = 0;
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            for (Animal animal : animalsList.getValue()) {
                sumOfChildren += animal.getNumberOfChildren();
            }
        }
        return (int) sumOfChildren/size;
    }

    //setters
    public void addObserver(ITurnEndObserver observer) {
        observerList.add(observer);
    }

    public void addMagicObserver(IMagicObserver observer){
        ;
    }

    //methods
    @Override
    public void run() {
        day = 0;
        while (!(map.animals.isEmpty())) {
            while(running){
                synchronized (pauseLock) {
                    if(!running) {
                        break;
                    }
                    if(paused) {
                        try{
                            synchronized (pauseLock) {
                                pauseLock.wait();
                            }
                        }
                        catch(InterruptedException ex) {
                            break;
                        }
                        if(!running) {
                            break;
                        }
                    }
                }
                day += 1;
                dieAndMove();
                eat();
                copulate();
                generateGrass();
                observerList.forEach(ITurnEndObserver::turnEnded);

                try {
                    Thread.sleep(eraTime);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    protected void dieAndMove() {
        ArrayList<Animal> graveyard = new ArrayList<>();
        ArrayList<Animal> animalsToMoveList = new ArrayList<>();
        ArrayList<Vector2D> oldPositionsOfAnimals = new ArrayList<>();

        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            for (Animal animal : animalsList.getValue()) {
                if (animal.getEnergy() >= moveEnergy) {
                    Vector2D oldPosition = animal.getPosition();
                    animal.move();
                    if(!(oldPosition.equals(animal.getPosition()))) {
                        animalsToMoveList.add(animal);
                        oldPositionsOfAnimals.add(oldPosition);
                    }
                }
                else graveyard.add(animal);
            }
        }
        for (Animal animal : graveyard) {
            this.sumOfLifetimesForDead += animal.getAge();
            this.deadAnimals += 1;
            map.removeAnimal(animal, animal.getPosition());
        }
        for(int i = 0; i < animalsToMoveList.size(); i++) {
            map.removeAnimal(animalsToMoveList.get(i), oldPositionsOfAnimals.get(i));
            map.addAnimal(animalsToMoveList.get(i));
        }
    }

    protected void eat() {
        ArrayList<Grass> graveyard = new ArrayList<>();
        for (Map.Entry<Vector2D, Grass> entry : map.grassMap.entrySet()) {
            Vector2D grassPosition = entry.getKey();
            if (map.animals.containsKey(grassPosition)) {
                graveyard.add(entry.getValue());
                int maxEnergy = 0;
                int counter = 0;
                for (Animal animal : map.animals.get(grassPosition)) {
                    if (animal.getEnergy() == maxEnergy) {
                        counter += 1;
                    } else if (animal.getEnergy() > maxEnergy) {
                        maxEnergy = animal.getEnergy();
                        counter = 1;
                    }
                }
                for (Animal animal : map.animals.get(grassPosition)) {
                    if (animal.getEnergy() == maxEnergy) {
                        animal.eat(counter);
                    }
                }
            }
        }
        graveyard.forEach(grass -> {map.grassEaten(grass);});
    }

    protected void copulate() {
        int minimalEnergy = (int) Math.ceil(startEnergy/2);
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            animalsList.getValue().sort(Animal::compareTo);
            if(animalsList.getValue().size() > 1 && animalsList.getValue().get(1).getEnergy() >= minimalEnergy
                    && animalsList.getValue().get(0).getEnergy() >= minimalEnergy) {
                Animal parent1 = animalsList.getValue().get(0);
                Animal parent2 = animalsList.getValue().get(1);
                Animal child = new Animal((int) Math.floorDiv(startEnergy, 2), parent1.getPosition(), map, Copulation.copulate(parent1, parent2));
                map.addAnimal(child);
            }
        }
    }

    protected void generateGrass() {
        this.map.generateGrass();
    }
}
