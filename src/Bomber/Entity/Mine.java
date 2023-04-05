package Bomber.Entity;

import Bomber.Entity.Enemy.Enemy;
import Bomber.GameFunction.Map;

import java.util.List;

public class Mine extends Bomb {
    List<Entity> entityList;

    public Mine(double x, double y, int range, Map map, String bombSound, String placementSound) {
        super(x, y, range, 99, map, 4, 1, bombSound, placementSound);
        entityList = map.getEntityList();
        this.canBePassed = true;
    }

    @Override
    public void update() {
        entityList.forEach(e -> {
            if (e instanceof Enemy) {
                if (this.distanceTo(e) <= 1) {
                    explode();
                }
            }
        });
    }
}
