package Bomber.Entity.Enemy;

import Bomber.Game;

public class Balloon extends Enemy {
    public Balloon() {
        super("balloon");
        strengthPoint = 15;
    }

    public void start() {
        this.health = 1;
        this.setSpeed(1);
        canBePassed = true;
    }

    public void movement() {
        enemyAI = new EnemyAI(player, this);
        if (updateCounter > updateRate && isMoving()) {
            switch (Game.randomInt(1, 4)) {
                case 1 -> toX++;
                case 2 -> toY++;
                case 3 -> toX--;
                case 4 -> toY--;
                default -> {
                }
            }
        }
    }

    @Override
    public boolean checkIfTileIsEmpty(double x, double y) {
        return x <= Game.WIDTH - 1 && y <= Game.HEIGHT - 1 && x > 0 && y > 0;
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
