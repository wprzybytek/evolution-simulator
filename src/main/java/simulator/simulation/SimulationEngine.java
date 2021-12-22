package simulator.simulation;

import simulator.helper.RNG;
import simulator.animal.Animal;
import simulator.animal.Copulation;
import simulator.map.AbstractWorldMap;
import simulator.map.FlatMap;
import simulator.map.RoundMap;
import simulator.map_elements.Grass;
import simulator.map_elements.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimulationEngine implements Runnable{
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    private int day;
    private int animalsNumber;
    private List<ITurnEndObserver> observerList = new ArrayList<>();
    private AbstractWorldMap map;

    //constructor
    public SimulationEngine(int width, int height, int startEnergy, int moveEnergy, int plantEnergy,
                            float jungleRation, int numberOfAnimals, boolean isFlat, boolean magicEvolution) {

        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;

        if (isFlat) {
            map = new FlatMap(height, width, jungleRation, moveEnergy, plantEnergy, startEnergy);
        } else {
            map = new RoundMap(height, width, jungleRation, moveEnergy, plantEnergy, startEnergy);
        }

        int animalCounter = 0;

        while (animalCounter <= numberOfAnimals) {
            Vector2D position = new Vector2D(RNG.rng(0, width - 1), RNG.rng(0, height - 1));
            if (!(map.animals.containsKey(position))) {
                Animal animal = new Animal(startEnergy, position, this.map);
                map.addAnimal(animal);
                animalCounter++;
            }
        }
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
            if(animalsList.getValue().size() == 0) {
                System.out.println("cos sie zjebalo");
            }
        }
        this.animalsNumber = size;
        return size;
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

    //setters
    public void addObserver(ITurnEndObserver observer) {
        observerList.add(observer);
    }

    //methods
    @Override
    public void run() {
        day = 0;
        while (!(map.animals.isEmpty())) {
            day += 1;
            dieAndMove();
            eat();
            copulate();
            generateGrass();
            observerList.forEach(ITurnEndObserver::turnEnded);

            try{
                Thread.sleep(300);
            }
            catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }

    private void dieAndMove() {
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
        graveyard.forEach(animal -> {map.removeAnimal(animal, animal.getPosition());});
        for(int i = 0; i < animalsToMoveList.size(); i++) {
            map.removeAnimal(animalsToMoveList.get(i), oldPositionsOfAnimals.get(i));
            map.addAnimal(animalsToMoveList.get(i));
        }
    }

    private void eat() {
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

    private void copulate() {
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

    private void generateGrass() {
        this.map.generateGrassJungle();
        this.map.generateGrassSavannah();
    }
}
