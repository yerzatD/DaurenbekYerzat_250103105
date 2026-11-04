class Player {
    private MovementStrategy strategy;

    public Player(MovementStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MovementStrategy strategy) {
        this.strategy = strategy;
    }

    public void move() {
        strategy.move();
    }
}

public class Main {
    public static void main(String[] args) {
        Player player = new Player(new Walking());

        player.move();

        player.setStrategy(new Skateboarding());
        player.move();

        player.setStrategy(new Jetpack());
        player.move();
    }
}
