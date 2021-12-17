package simulator;

import java.util.ArrayList;
import java.util.Map;

public class SimulationEngine {
    private int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private int numberOfAnimals;

    private AbstractWorldMap map;

    public SimulationEngine(int width, int height, int startEnergy, int moveEnergy, int plantEnergy,
                            float jungleRation, int numberOfAnimals, boolean isFlat, boolean magicEvolution) {

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

    public void run() {
        while (true) {
            dieAndMove();
            eat();
            copulate();
        }
    }

    private void dieAndMove() {
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            for (Animal animal : animalsList.getValue()) {
                if (animal.getEnergy() >= moveEnergy) animal.move();
                else map.removeAnimal(animal);
            }
        }
    }

    private void eat() {
        for (Map.Entry<Vector2D, Grass> entry : map.grassMap.entrySet()) {
            Vector2D grassPosition = entry.getKey();
            if (map.animals.containsKey(grassPosition)) {
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
                        animal.eat(entry.getValue(), counter);
                    }
                }
            }
        }
    }

    private void copulate() {
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            animalsList.getValue().sort(Animal::compareTo);
            if(animalsList.getValue().size() > 1 && animalsList.getValue().get(1).getEnergy() >= startEnergy/2) {
                Animal parent1 = animalsList.getValue().get(0);
                Animal parent2 = animalsList.getValue().get(1);
                Animal child = new Animal((int) Math.floorDiv(startEnergy, 2), parent1.getPosition(), map, Copulation.copulate(parent1, parent2));
                map.addAnimal(child);
            }
        }
    }
}
