package Bomber.Item;

import Bomber.Entity.Player;
import Bomber.Game;
import Bomber.GameFunction.Sound;

public class ItemBombRange extends Item {
    public ItemBombRange(int x, int y) {
        super(Game.textureFolderPath + "itemBombRange.png", x, y);
    }

    @Override
    public void doThisWhenCollided(Player player) {
        Sound.getItem();
        Game.bombLevelUp(1);
    }
}
