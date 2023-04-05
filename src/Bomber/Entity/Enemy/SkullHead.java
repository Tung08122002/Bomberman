package Bomber.Entity.Enemy;

public class SkullHead extends Enemy {
    public SkullHead() {
        super("skullHead");
        strengthPoint = 15;
    }

    @Override
    public void start() {
        this.health = 1;
        this.setSpeed(3);
        canBePassed = true;
    }

    public void movement() {
        enemyAI = new EnemyAI(player, this);
        if (updateCounter > updateRate && isMoving()) {
            switch (enemyAI.getDirection()) {
                case WEST -> toX--;
                case EAST -> toX++;
                case SOUTH -> toY++;
                case NORTH -> toY--;
                default -> {
                }
            }
        }
    }

    @Override
    public void update() {
        updateCounter++;
        updateMapInfo();
        checkPlayer();
        movement();
        move();
        enemyIdleNoise();
        if (updateCounter > updateRate) {
            updateCounter = 0;
        }
    }
}
