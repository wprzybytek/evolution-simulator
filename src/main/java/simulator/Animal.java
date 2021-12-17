package simulator;

public class Animal implements Comparable<Animal>{

    private int energy;
    private Vector2D position;
    private Direction direction;
    private final Genome genome;
    private AbstractWorldMap map;

    public Animal(int startEnergy, Vector2D position, AbstractWorldMap map) {
        this.energy = startEnergy;
        this.position = position;
        this.direction = Direction.values()[RNG.rng(0, 7)];
        this.map = map;
        this.genome = new Genome();
    }

    public Animal(int startEnergy, Vector2D position, AbstractWorldMap map, Genome genome) {
        this.energy = startEnergy;
        this.position = position;
        this.direction = Direction.values()[RNG.rng(0, 7)];
        this.map = map;
        this.genome = genome;
    }

    public int getEnergy() {
        return energy;
    }

    public Vector2D getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Genome getGenome() {
        return genome;
    }

    public void move() {
        Direction moveDirection = Direction.values()[genome.getGenes().get(RNG.rng(0, 31)).number];
        if(moveDirection == Direction.NORTH) {
            this.position = this.position.next(this.direction);
        }
        else if(moveDirection == Direction.SOUTH) {
            this.position = this.position.previous(this.direction);
        }
        else {
            this.direction = Direction.getDirection((this.direction.angle + moveDirection.angle) % 360);
        }
        this.energy -= map.moveEnergy;
    }

    public void eat(Grass grass, int splitWith) {
        this.energy += Math.floorDiv(map.plantEnergy, splitWith);
        if(splitWith == 1) map.grassEaten(grass);
    }

    public void removeEnergy() {
        this.energy -= (int) Math.ceil(map.startEnergy/4);
    }

    @Override
    public int compareTo(Animal o) {
        return Integer.compare(this.getEnergy(), o.getEnergy());
    }
}
