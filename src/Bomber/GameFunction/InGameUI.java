package Bomber.GameFunction;

import Bomber.Entity.Player;
import Bomber.Game;
import javafx.scene.canvas.GraphicsContext;

public class InGameUI {
    Player player;
    Texture bar;
    Game game;
    MapEditor mapEditor;
    int y = Map.MAP_HEIGHT * Texture.IMAGE_SIZE;

    public void setUp(Player player, MapEditor mapEditor, Game game) {
        this.player = player;
        this.mapEditor = mapEditor;
        this.game = game;
    }

    public InGameUI() {
        bar = new Texture(Game.textureFolderPath + "bar.png");
    }

    public void render(GraphicsContext gc) {
        bar.render(gc, 0, y);
        gc.drawImage(new Texture(Game.textureFolderPath + "Bomb" + (player.getSelectedBomb() + 1)
                        + ".png").getImage(), Texture.IMAGE_SIZE, y);
    }
}
