package Bomber.GameFunction;

import Bomber.Entity.Enemy.Balloon;
import Bomber.Entity.Entity;
import Bomber.Entity.ExplosionBlast;
import Bomber.Game;
import Bomber.Item.ItemAddBomb;
import Bomber.Item.ItemBombRange;
import Bomber.Item.ItemPlayerHealth;
import Bomber.Item.ItemPlayerSpeed;
import Bomber.StillObject.Tile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {
    public static final int MAP_HEIGHT = 15;
    public static final int MAP_WIDTH = 28;
    public static final int TILE_TYPE_LIMIT = 10;
    private Game game;
    private List<Entity> entityList;
    public Tile[][] mapTile = new Tile[MAP_HEIGHT][MAP_WIDTH];
    public Tile[] tileId = new Tile[10];
    public int[][] mapInfo = new int[MAP_HEIGHT][MAP_WIDTH];

    public List<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<Entity> entityList) {
        this.entityList = entityList;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Tile getTile(int i, int j) {
        return mapTile[i][j];
    }

    public List<Entity> mapTileArrayToList() {
        List<Entity> n = new ArrayList<>();
        for (int i = 0; i < MAP_HEIGHT; i++) {
            n.addAll(Arrays.asList(mapTile[i]).subList(0, MAP_WIDTH));
        }
        return n;
    }

    public void changeTile(int x, int y, int id) {
        mapTile[y][x] = new Tile(tileId[id]);
        mapTile[y][x].setXY(x, y);
    }

    public int[][] createNavigationMap() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                mapInfo[i][j] = mapTile[i][j].getCanBePassed() ? 0 : 1;
            }
        }

        for (Entity entity : entityList) {
            if (!(entity instanceof Balloon) && !(entity instanceof ExplosionBlast)) {
                mapInfo[(int) entity.getY()][(int) entity.getX()] = entity.getCanBePassed() ? 0 : 1;
            }
        }

        return mapInfo;
    }

    public boolean validTile(int x, int y) {
        return x >= 0 && y >= 0 && x < Map.MAP_WIDTH - 1 && y < Map.MAP_HEIGHT - 1;
    }

    public boolean isTileEmpty(int x, int y) {
        if (validTile(x, y)) {
            return mapTile[y][x].getCanBePassed();
        } else {
            return false;
        }
    }

    public boolean isTileEmpty(double x, double y) {
        return isTileEmpty((int) x, (int) y);
    }

    public boolean isTileDestructible(int x, int y) {
        if (validTile(x, y)) {
            return mapTile[y][x].isDestructible();
        }
        return false;
    }

    public boolean isTileDestructible(double x, double y) {
        return isTileDestructible((int) x, (int) y);
    }

    public void destroyTile(int x, int y) {
        if (validTile(x, y)) {
            Tile tile = mapTile[y][x];
            if (tile.isDestructible()) {
                if (tile.getFloorTile() != null) {
                    changeTile(x, y, tile.getFloorTile().getId());
                    spawnRandomLoot(x, y);
                }
            }
        }
    }

    public boolean dealDamageTile(int x, int y, int damage) {
        if (validTile(x, y)) {
            Tile tile = mapTile[y][x];
            if (tile.takeDamage(damage)) {
                if (tile.getHealth() <= 0) {
                    destroyTile(x, y);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean dealDamageTile(double x, double y, int damage) {
        return dealDamageTile((int) x, (int) y, damage);
    }

    private void spawnRandomLoot(int x, int y) {
        int random = Game.randomInt(0, 50);
        switch (random) {
            case 0 -> getGame().addEntity(new ItemAddBomb(x, y));
            case 1 -> getGame().addEntity(new ItemPlayerHealth(x, y));
            case 2 -> getGame().addEntity(new ItemPlayerSpeed(x, y));
            case 3 -> getGame().addEntity(new ItemBombRange(x, y));
            default -> {

            }
        }
    }

    public void loadTile() {
        for (int i = 0; i < TILE_TYPE_LIMIT; i++) {
            String s = String.format(Game.textureFolderPath + "map\\%d.png", i);
            tileId[i] = new Tile(s);
            tileId[i].setId(i);
        }

        //Plasteel floor
        tileId[0].setCanBePassed(true);
        tileId[0].setDestructible(false);

        //Plasteel wall, indestructible
        tileId[1].setCanBePassed(false);
        tileId[1].setDestructible(false);
        tileId[1].setFloorTile(new Tile(tileId[0], false));

        //Sandstone floor
        tileId[2].setCanBePassed(true);
        tileId[2].setDestructible(false);

        //Natural sandstone wall, destroy after one explosion, return floor
        tileId[3].setCanBePassed(false);
        tileId[3].setDestructible(true);
        tileId[3].setFloorTile(new Tile(tileId[2], false));

        //Cracked sandstone wall, destroy after two explosion, return floor
        tileId[4].setCanBePassed(false);
        tileId[4].setDestructible(true);
        tileId[4].setHealth(2);
        tileId[4].setFloorTile(new Tile(tileId[2], false));

        //Sandstone brick wall, destroy after three explosion, return floor
        tileId[5].setCanBePassed(false);
        tileId[5].setDestructible(true);
        tileId[5].setHealth(3);
        tileId[5].setFloorTile(new Tile(tileId[2], false));

        //Gate floor
        tileId[6].setCanBePassed(true);
        tileId[6].setDestructible(false);

        //Gate
        tileId[7].setCanBePassed(false);
        tileId[7].setDestructible(true);
        tileId[7].setHealth(5);
        tileId[7].setFloorTile(new Tile(tileId[6], false));

        //Gas vent
        tileId[8].setCanBePassed(false);
        tileId[8].setDestructible(true);
        tileId[8].setHealth(10);
        tileId[8].setFloorTile(new Tile(tileId[6], false));

        //Plasteel embrasure
        tileId[9].setCanBePassed(false);
        tileId[9].setDestructible(false);
    }

    public void loadMap(String path) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
            String bufferString;
            for (int i = 0; i < MAP_HEIGHT; i++) {
                bufferString = bufferedReader.readLine();
                for (int j = 0; j < MAP_WIDTH; j++) {
                    changeTile(j, i, bufferString.charAt(j) - 48);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

