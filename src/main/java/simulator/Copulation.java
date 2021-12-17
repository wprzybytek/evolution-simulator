package simulator;

import java.util.ArrayList;

public class Copulation {
    public static Genome copulate(Animal strongerParent, Animal parent) {
        int sumOfEnergy = strongerParent.getEnergy() + parent.getEnergy();
        int dnaFromStronger = (int) Math.ceil((strongerParent.getEnergy()/sumOfEnergy) * 32);
        ArrayList<Gene> left;
        ArrayList<Gene> right;
        if(RNG.rng(0, 1) == 0) {
            left = (ArrayList<Gene>) strongerParent.getGenome().getGenes().subList(0, dnaFromStronger);
            right = (ArrayList<Gene>) parent.getGenome().getGenes().subList(dnaFromStronger, 32);
        }
        else {
            left = (ArrayList<Gene>) parent.getGenome().getGenes().subList(0, 32 - dnaFromStronger);
            right = (ArrayList<Gene>) strongerParent.getGenome().getGenes().subList(32 - dnaFromStronger, 32);
        }
        parent.removeEnergy();
        strongerParent.removeEnergy();
        return new Genome(left, right);
    }
}
