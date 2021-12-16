package simulator;

public class Animal {

    private int energy;
    private Vector2D position;
    private Direction direction;
    private final Genome genome;

    public Animal(int startEnergy, Vector2D position) {
        this.energy = startEnergy;
        this.position = position;
        this.direction = Direction.values()[RNG.rng(0, 7)];
        this.genome = new Genome();
    }

    public Animal(int startEnergy, Vector2D position, Genome genome) {
        this.energy = startEnergy;
        this.position = position;
        this.direction = Direction.values()[RNG.rng(0, 7)];
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
        this.energy -= 1;
    }
}
