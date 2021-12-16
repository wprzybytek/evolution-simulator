package simulator;

public class Gene implements Comparable<Gene>{
    public final int number;

    public Gene(int i) {
        this.number = i;
    }

    @Override
    public int compareTo(Gene o) {
        if(this.number > o.number) return 1;
        else if(this.number < o.number) return -1;
        return 0;
    }
}
