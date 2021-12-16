package simulator;


public class World {
    public static void main(String[] args) {
        Genome genome = new Genome();
        genome.getGenes().forEach(gene -> System.out.print(gene.number + " "));
    }
}
