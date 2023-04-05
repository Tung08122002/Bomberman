package Bomber.Item;

import Bomber.Entity.Player;
import Bomber.Game;
import Bomber.GameFunction.Sound;

public class ItemAddBomb extends Item {
    public ItemAddBomb(int x, int y) {
        super(Game.textureFolderPath + "itemAddBomb.png", x, y);
    }

    @Override
    public void doThisWhenCollided(Player player) {
        Sound.getItem();
        Game.addBomb(1);
    }
}
