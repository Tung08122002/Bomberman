package Bomber.Item;

import Bomber.Entity.Entity;
import Bomber.Entity.Player;
import Bomber.StillObject.Tile;

public abstract class Item extends Tile {
    protected boolean isCollected = false;

    public Item(String Image, int x, int y) {
        super(Image);
        this.x = x;
        this.y = y;
        this.canBePassed = true;
        this.destructible = false;
    }

    public abstract void doThisWhenCollided(Player player);

    public boolean collided(Entity other) {
        if (other instanceof Player) {
            if (!isCollected && this.isCollidedWith(other)) {
                doThisWhenCollided((Player) other);
                destroy();
                isCollected = true;
            }
        }
        return false;
    }
}
