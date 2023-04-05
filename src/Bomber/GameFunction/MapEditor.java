package Bomber.GameFunction;

import Bomber.Entity.Bomb;
import Bomber.Game;

public class MapEditor {
    Map map;
    Game game;
    private int selectedTile = 0;
    private boolean enable = false;
    private final Texture selectedTexture;
    private int toX = 0;
    private int toY = 0;
    private double bombRate = 0.1;
    TimeCounter bombCounter = new TimeCounter();

    public MapEditor() {
        selectedTexture = new Texture(Game.textureFolderPath + "map\\selected.png");
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int coordinateDown(double axis) {
        return (int) (axis / Texture.IMAGE_SIZE);
    }

    public void update() {
        if (game.input.contains("F12")) {
            enable = true;
            System.out.println("Hack: On");
        }

        if (game.input.contains("F11")) {
            enable = false;
            System.out.println("Hack: Off");
        }

        if (enable) {
            map.tileId[selectedTile].render(game.getGraphicsContext(), toX, toY);
            game.getGraphicsContext().drawImage(selectedTexture.getImage(), toX * Texture.IMAGE_SIZE,
                    toY * Texture.IMAGE_SIZE);
            toX = coordinateDown(game.mouseX);
            toY = coordinateDown(game.mouseY);

            if (toX < 0) {
                toX = 0;
            }

            if (toY < 0) {
                toY = 0;
            }

            if (toX >= Map.MAP_WIDTH) {
                toX = Map.MAP_WIDTH - 1;
            }

            if (toY >= Map.MAP_HEIGHT) {
                toY = Map.MAP_HEIGHT - 1;
            }

            for (int i = 0; i < Map.TILE_TYPE_LIMIT; i++) {
                if (game.input.contains("DIGIT" + i)) {
                    selectedTile = i;
                }
            }

            int x = toX;
            int y = toY;

            if (game.input.contains("PRIMARY")) {
                if (map.getTile(y, x).getId() != selectedTile) {
                    Sound.playSound("placeCasual");
                }
                map.changeTile(x, y, selectedTile);
            }

            if (game.input.contains("Z")) {
                if (bombCounter.getTime() > bombRate) {
                    game.addEntity(new Bomb(x, y, 50, 3, map, 5, 50,
                            "ExplosionBomb", "placeGentle"));
                    bombCounter.resetCounter();
                }
            }
        }
    }
}
