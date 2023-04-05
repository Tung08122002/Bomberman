package Bomber.Entity;

import Bomber.GameFunction.Map;

public class GasolineBarrel extends Bomb {
    public GasolineBarrel(double x, double y, int range, Map map, String bombSound, String placementSound) {
        //Explode when on fire
        super(x, y, range, 999, map, 3, 1, bombSound, placementSound);
    }
}
