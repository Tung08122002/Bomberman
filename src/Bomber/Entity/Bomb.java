package Bomber.Entity;

import Bomber.Game;
import Bomber.GameFunction.Map;
import Bomber.GameFunction.Sound;
import Bomber.GameFunction.TimeCounter;

public class Bomb extends Entity {
    Map map;
    public boolean exploded = false;
    public String bombSound;
    TimeCounter timeCounter;
    private int penetration = 1;
    int range = 1;
    double fuseTime = 2f;

    public void changeStat() {
        this.destructible = false;
        this.canBePassed = false;
        this.timeCounter = new TimeCounter();
    }

    public Bomb(double x, double y, int range, double fuseTime, Map map,
                int bombType, int penetration, String bombSound, String placementSound) {
        super(Game.textureFolderPath + "Bomb" + bombType + ".png");
        this.x = x;
        this.y = y;
        this.range = range;
        this.fuseTime = fuseTime;
        this.map = map;
        this.penetration = penetration;
        this.bombSound = bombSound;
        Sound.playSound(placementSound);
        changeStat();
    }

    public void explode() {
        if (!exploded) {
            exploded = true;
            Sound.playSound(bombSound);
            map.getGame().addEntity(new ExplosionBlast(x, y, range, map, facingDirection.ALL,
                    0, penetration));
            setToDelete(true);
        }
    }

    @Override
    public void update() {
        if (timeCounter.getTime() > fuseTime) {
            explode();
        }
    }
}
