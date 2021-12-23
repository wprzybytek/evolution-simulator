package simulator.simulation;

public class NormalSimulationEngine extends AbstractSimulationEngine{
    public NormalSimulationEngine(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRation, int numberOfAnimals, int eraTime, boolean isFlat) {
        super(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation, numberOfAnimals, eraTime, isFlat);
    }
}
