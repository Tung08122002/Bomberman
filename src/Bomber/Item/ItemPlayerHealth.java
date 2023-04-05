package Bomber.Item;

import Bomber.Entity.Player;
import Bomber.Game;
import Bomber.GameFunction.Sound;

public class ItemPlayerHealth extends Item {
    public ItemPlayerHealth(int x, int y) {
        super(Game.textureFolderPath + "itemHealth.png", x, y);
    }

    @Override
    public void doThisWhenCollided(Player player) {
        player.healthUp();
        Sound.getItem();
        Game.HpUp(1);
    }
}
