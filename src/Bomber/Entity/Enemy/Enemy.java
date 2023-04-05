package Bomber.Entity.Enemy;

import Bomber.Entity.Pawn;
import Bomber.Entity.Player;
import Bomber.Game;
import Bomber.GameFunction.Sound;
import Bomber.GameFunction.TimeCounter;

public abstract class Enemy extends Pawn {
    protected int updateRate = 8;
    protected int updateCounter = 0;
    protected double noiseRate = 8;
    protected double randomNoiseRate = Game.randomDouble(noiseRate * 0.75, noiseRate * 1.5f);
    public int strengthPoint = 10;
    TimeCounter noiseIdleCounter = new TimeCounter();
    Player player;

    protected EnemyAI enemyAI = new EnemyAI(player, this);

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Enemy(String label) {
        super(Game.textureFolderPath + "enemyPawn\\" + label + "North.png",
                Game.textureFolderPath + "enemyPawn\\" + label + "East.png",
                Game.textureFolderPath + "enemyPawn\\" + label + "South.png");
        this.label = label;
        destroyOnDeath = true;
    }

    protected void checkPlayer() {
        if (player != null) {
            if (this.isCollidedWith(player)) {
                this.dealDamage(1, player);
            }
        }
    }

    public void enemyIdleNoise() {
        if (noiseIdleCounter.getTime() > randomNoiseRate) {
            Sound.playSound(label + "Idle");
            randomNoiseRate = Game.randomDouble(noiseRate * 0.75, noiseRate * 1.5f);
            noiseIdleCounter.resetCounter();
        }
    }
}
