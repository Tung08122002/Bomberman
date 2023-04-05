package Bomber.Entity.Enemy;

import Bomber.Game;

public class Needle extends Enemy {
    public Needle() {
        super("needle");
        strengthPoint = 10;
    }

    @Override
    public void start() {
        this.health = 1;
        this.setSpeed(2);
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
