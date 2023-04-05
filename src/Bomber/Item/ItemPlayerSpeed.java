package Bomber.Item;

import Bomber.Entity.Player;
import Bomber.Game;
import Bomber.GameFunction.Sound;

public class ItemPlayerSpeed extends Item {
    public ItemPlayerSpeed(int x, int y) {
        super(Game.textureFolderPath + "itemSpeedUp.png", x, y);
    }

    @Override
    public void doThisWhenCollided(Player player) {
        Sound.getItem();
        Game.speedUp(1);
    }
}
