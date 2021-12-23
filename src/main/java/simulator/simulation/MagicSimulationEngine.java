package simulator.simulation;

import simulator.animal.Animal;
import simulator.helper.RNG;
import simulator.map_elements.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MagicSimulationEngine extends AbstractSimulationEngine{
    private int magicAvailable = 3;
    private final List<IMagicObserver> magicObserverList = new ArrayList<>();

    public MagicSimulationEngine(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRation, int numberOfAnimals, int eraTime, boolean isFlat) {
        super(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation, numberOfAnimals, eraTime, isFlat);
    }

    @Override
    public void addMagicObserver(IMagicObserver observer) {
        magicObserverList.add(observer);
    }

    @Override
    public void run() {
        day = 0;
        while (!(map.animals.isEmpty())) {
            day += 1;
            dieAndMove();
            if(this.getAnimalsNumber() == 5 && magicAvailable > 0) {
                this.cloneAnimals();
                magicAvailable--;
                magicObserverList.forEach(IMagicObserver::magicHappened);
            }
            eat();
            copulate();
            generateGrass();
            observerList.forEach(ITurnEndObserver::turnEnded);

            try{
                Thread.sleep(eraTime);
            }
            catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
    }

    private void cloneAnimals() {
        ArrayList<Animal> animalsToAdd = new ArrayList<>();
        for (Map.Entry<Vector2D, ArrayList<Animal>> animalsList : map.animals.entrySet()) {
            for (Animal animal : animalsList.getValue()) {
                while (true) {
                    Vector2D position = new Vector2D(RNG.rng(0, this.map.getWidth() - 1), RNG.rng(0, this.map.getHeight() - 1));
                    if (!(this.map.animals.containsKey(position))) {
                        Animal clone = new Animal(startEnergy, position, this.map, animal.getGenome());
                        animalsToAdd.add(clone);
                        break;
                    }
                }
            }
        }
        animalsToAdd.forEach(this.map::addAnimal);
    }
}
