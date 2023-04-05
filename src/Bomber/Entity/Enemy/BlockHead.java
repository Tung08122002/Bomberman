package Bomber.Entity.Enemy;

public class BlockHead extends Enemy{
    public BlockHead() {
        super("blockHead");
        strengthPoint = 20;
    }

    public void start() {
        this.health = 999;
        this.setSpeed(0);
        canBePassed = true;
    }

    @Override
    public void update() {
        updateCounter++;
        updateMapInfo();
        checkPlayer();
        move();
        enemyIdleNoise();
        if (updateCounter > updateRate) {
            updateCounter = 0;
        }
    }
}
