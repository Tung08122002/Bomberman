package Bomber.Entity;

import Bomber.Game;
import Bomber.GameFunction.Map;
import Bomber.GameFunction.TimeCounter;

public class ExplosionBlast extends Entity {
    Map mapRef;
    int range = 2;
    int penetration = 1;
    public double delayDuration = 0;
    public boolean delaying = true;
    public double lingerDuration = 0.1;
    public double delayOffset = 0.02;
    private TimeCounter delayCounter;
    private TimeCounter lingerDurationCounter;

    public ExplosionBlast(double x, double y, int range, Map map, facingDirection _facingDirection,
                          double explosionDelay, int penetration) {
        super(Game.textureFolderPath + "flameNorth.png",
                Game.textureFolderPath + "flameEast.png",
                Game.textureFolderPath + "flameSouth.png",
                Game.textureFolderPath + "flameAll.png");
        lingerDurationCounter = new TimeCounter();
        delayCounter = new TimeCounter();
        this.x = x;
        this.y = y;
        this.range = range;
        this.mapRef = map;
        this.penetration = penetration;
        delayDuration = explosionDelay;
        lingerDuration += this.delayDuration * this.range * 0.25;
        this.directionFacing = _facingDirection;
        this.destructible = false;
        this.setCanBePassed(true);

        if (mapRef.dealDamageTile(x, y, 1)) {
            this.penetration--;
        }

        if (this.penetration < 1) {
            this.range = 0;
        }

        for (Entity e : mapRef.getEntityList()) {
            if (e instanceof Player) {
                if (e.isCollidedWith(this)) {
                    Game.setHpPlayer(e.health - 1);
                    e.takeDamage(1);
                }
            } else if (e.isCollidedWith(this)) {
                e.takeDamage(1);

            }
            if (e instanceof Bomb && e.isCollidedWith(this)) {
                ((Bomb) e).explode();
            }
        }
        mapRef.getGame().addEntity(this);
    }

    public void traverse(facingDirection fd) {
        switch (fd) {
            case ALL -> {
                traverse(facingDirection.NORTH);
                traverse(facingDirection.SOUTH);
                traverse(facingDirection.EAST);
                traverse(facingDirection.WEST);
            }

            case SOUTH -> {
                newFlameAt(0, 1, fd);
            }

            case NORTH -> {
                newFlameAt(0, -1, fd);
            }

            case WEST -> {
                newFlameAt(-1, 0, fd);
            }

            case EAST -> {
                newFlameAt(1, 0, fd);
            }

            case STATIONARY -> {

            }
        }
    }

    public void newFlameAt(double xOffset, double yOffset, facingDirection fd) {
        if (mapRef.isTileDestructible(x + xOffset, y + yOffset)
                || mapRef.isTileEmpty(x + xOffset, y + yOffset)) {
            mapRef.getGame().addEntity(
                    new ExplosionBlast(x + xOffset, y + yOffset,
                            range - 1, mapRef, fd,
                            delayDuration + delayOffset, penetration));
        }
    }

    @Override
    public void update() {
        if (range > 0) {
            if (delayCounter.getTime() > delayDuration && delaying) {
                traverse(directionFacing);
                delaying = false;
            }
        }

        if (lingerDurationCounter.getTime() > lingerDuration) {
            destroy();
        }
    }
}
