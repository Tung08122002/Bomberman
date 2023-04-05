package Bomber.StillObject;

import Bomber.Entity.Entity;
import Bomber.Entity.Player;
import Bomber.Game;

import java.util.List;

public class Gate extends Tile {
    Game game;
    List<Entity> entityList;
    public Gate(Game game) {
        super(Game.textureFolderPath + "map\\7.png");
        this.game = game;
        this.canBePassed = true;
        entityList = game.map.getEntityList();
    }

    @Override
    public void update() {
        entityList.forEach(e -> {
            if (e instanceof Player) {
                game.setGatePassed(isCollidedWith(e));
            }
        });
    }

    @Override
    public boolean takeDamage(int damage) {
        return super.takeDamage(0);
    }
}
