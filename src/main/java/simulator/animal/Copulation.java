package simulator.animal;
import simulator.helper.RNG;

import java.util.List;

public class Copulation {
    public static Genome copulate(Animal strongerParent, Animal parent) {
        int sumOfEnergy = strongerParent.getEnergy() + parent.getEnergy();
        int dnaFromStronger = Math.max((int) Math.ceil((strongerParent.getEnergy()/sumOfEnergy) * 32), 31);
        List<Gene> left;
        List<Gene> right;
        if(RNG.rng(0, 1) == 0) {
            left = strongerParent.getGenome().getGenes().subList(0, dnaFromStronger);
            right = parent.getGenome().getGenes().subList(dnaFromStronger, 32);
        }
        else {
            left = parent.getGenome().getGenes().subList(0, 32 - dnaFromStronger);
            right = strongerParent.getGenome().getGenes().subList(32 - dnaFromStronger, 32);
        }
        parent.removeEnergy();
        strongerParent.removeEnergy();
        return new Genome(left, right);
    }
}
