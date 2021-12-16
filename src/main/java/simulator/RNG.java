package simulator;

public class RNG {
    public static int rng(int min, int max) {
        return (int) ((Math.random() * (max + 1 - min)) + min);
    }
}
