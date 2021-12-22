package simulator.animal;

import simulator.helper.RNG;

import java.util.ArrayList;
import java.util.List;

public class Genome {
    private final List<Gene> genes = new ArrayList<>();

    public Genome(List<Gene> left, List<Gene> right) {
        genes.addAll(left);
        genes.addAll(right);
        genes.sort(Gene::compareTo);
    }

    public Genome() {
        for(int i = 0; i < 32; i++) {
            genes.add(new Gene(RNG.rng(0, 7)));
        }
        genes.sort(Gene::compareTo);
    }

    public List<Gene> getGenes() {
        return genes;
    }
}