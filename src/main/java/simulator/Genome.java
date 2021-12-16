package simulator;

import java.util.ArrayList;

public class Genome {
    private final ArrayList<Gene> genes = new ArrayList<>();

    public Genome(ArrayList<Gene> left, ArrayList<Gene> right) {
        genes.addAll(left);
        genes.addAll(right);
        //genes.sort();
    }

    public Genome() {
        for(int i = 0; i < 32; i++) {
            genes.add(new Gene(RNG.rng(0, 7)));
        }
    }
}
