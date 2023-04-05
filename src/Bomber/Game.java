package Bomber;

import Bomber.Entity.Enemy.*;
import Bomber.Entity.Entity;
import Bomber.Entity.Player;
import Bomber.GameFunction.InGameUI;
import Bomber.GameFunction.Map;
import Bomber.GameFunction.MapEditor;
import Bomber.GameFunction.Texture;
import Bomber.Item.Item;
import Bomber.StillObject.Gate;
import Bomber.StillObject.Tile;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Game extends Canvas {
    public static final String textureFolderPath = System.getProperty("user.dir") + "\\src\\texture\\";
    public static final int WIDTH = 25;
    public static final int HEIGHT = 12;
    public double mouseX = 0;
    public double mouseY = 0;
    public int numberOfEnemy = 0;
    private boolean gatePassed = false;
    private boolean isShown = false;

    public int playerSpawnX = 1;
    public int playerSpawnY = 1;
    private static int playerSpeed = 2;
    private static int bombLevel = 0;
    private static int hpPlayer = 2;
    private static int bombCoolDown = 0;
    Player player;
    MainMenu mainMenu;

    public static void setHpPlayer(int hpPlayer1) {
        hpPlayer = hpPlayer1;
    }

    public Game(double width, double height) {
        super(width, height);
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    Scene scene;
    GraphicsContext graphicsContext;
    InGameUI userInterface;

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    public ArrayList<String> input = new ArrayList<>();

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static double randomDouble(double min, double max) {
        return Math.random() * (max - min + 1) + min;
    }

    public Map map;
    MapEditor mapEditor;
    Texture crackStage1;
    Texture crackStage2;
    Texture crackStage3;

    Stack<Entity> addStack = new Stack<>();

    public void addEntity(Entity e) {
        addStack.push(e);
    }

    Stack<Entity> removeStack = new Stack<>();

    public void removeEntity(Entity e) {
        removeStack.push(e);
    }

    public int addEnemy(Enemy enemy, int x, int y) {
        enemy.setMap(map);
        enemy.setXY(x, y);
        enemy.setPlayer(player);
        enemy.start();
        addEntity(enemy);
        return enemy.strengthPoint;
    }

    public void spawnEnemy(int strengthPoint) {
        while (strengthPoint > 0) {
            int tempX = randomInt(0, Map.MAP_WIDTH - 1);
            int tempY = randomInt(0, Map.MAP_HEIGHT - 1);
            if (map.createNavigationMap()[tempY][tempX] == 0
                    && Entity.distanceTo(tempX, tempY, playerSpawnX, playerSpawnY) > 10) {
                int random = randomInt(20, 100);
                if (random > 80 && random <= 100) {
                    strengthPoint -= addEnemy(new Needle(), tempX, tempY);
                } else if(random > 60) {
                    strengthPoint -= addEnemy(new SkullHead(), tempX, tempY);
                } else if (random > 40) {
                    strengthPoint -= addEnemy(new Balloon(), tempX, tempY);
                } else if(random > 20) {
                    strengthPoint -= addEnemy(new BlockHead(), tempX, tempY);
                }
            }
        }
    }

    public boolean isPlayAgain() {
        for (Entity e: entities) {
            if (e instanceof Player) {
                if (e.getActive()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setGatePassed(boolean gatePassed) {
        this.gatePassed = gatePassed;
    }

    public void getInput() {
        scene.setOnMousePressed(mouseEvent -> {
            String code = mouseEvent.getButton().toString();
            if (!input.contains(code)) {
                input.add(code);
            }
        });

        scene.setOnMouseReleased(mouseEvent -> {
            String code = mouseEvent.getButton().toString();
            input.remove(code);
        });

        scene.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        });

        scene.setOnMouseDragged(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        });

        scene.setOnKeyPressed(keyEvent -> {
            String code = keyEvent.getCode().toString();
            if (!input.contains(code)) {
                input.add(code);
            }
        });

        scene.setOnKeyReleased(keyEvent -> {
            String code = keyEvent.getCode().toString();
            input.remove(code);
        });
    }

    public GraphicsContext getGraphicsContext() {
        return graphicsContext;
    }

    public void render() {
        graphicsContext.clearRect(0, 0, this.getWidth(), this.getHeight());
        stillObjects.forEach(e -> {
            e.render(graphicsContext);
            ((Tile) e).renderState(graphicsContext, crackStage1, crackStage2, crackStage3);
        });
        entities.forEach(e -> e.render(graphicsContext));
        userInterface.render(graphicsContext);
    }

    public void updateMap() {
        stillObjects = map.mapTileArrayToList();
    }

    public void newGame(boolean resetStat) {
        graphicsContext.clearRect(0, 0, this.getWidth(), this.getHeight());
        if (resetStat) {
            player.setHealth(hpPlayer);
            playerSpeed = 2;
            bombLevel = 0;
            hpPlayer = 2;
            bombCoolDown = 0;
            numberOfEnemy = 0;
        }
        player.setXY(playerSpawnX, playerSpawnY);
    }

    public void newLevel(int level) {
        entities.forEach(e -> {
            if (e instanceof Enemy) {
                e.setToDelete(true);
            }
        });

        map.loadMap(System.getProperty("user.dir") + "\\src\\level\\level" + level + ".txt");
        updateMap();

        player.setXY(playerSpawnX, playerSpawnY);
        player.setHealth(hpPlayer);
        player.setActive(true);
        spawnEnemy(175);
        isShown = false;
    }

    public static void speedUp(int speed) {
        System.out.print(playerSpeed);
        playerSpeed += speed;
        System.out.println(" -> Speed up successful -> " + playerSpeed);
    }

    public static void bombLevelUp(int bomblevel) {
        System.out.print(bombLevel);
        bombLevel += bomblevel;
        System.out.println(" -> Bomb level up successful -> " + bombLevel);

    }

    public static void HpUp(int Hp) {
        System.out.print(hpPlayer);
        hpPlayer += Hp;
        System.out.println(" -> Hp up successful -> " + hpPlayer);
    }

    public static void addBomb(double bombCoolDown1) {
        System.out.print(bombCoolDown);
        bombCoolDown += bombCoolDown1;
        System.out.println(" -> Bomb number up successful -> " + bombCoolDown);
    }

    public void update() {
        getInput();
        if (player.getActive() && !isShown) {
            mainMenu.drawPlayAgain();
            isShown = true;
        }

        player.setSpeed(playerSpeed);
        player.setBombRangeBonus(bombLevel);
        player.setBombCoolDown(bombCoolDown);
        for (Entity e: entities) {
            if (e.isToDelete()) {
                removeEntity(e);
            } else if (e instanceof Item) {
                if (((Item) e).collided(player)) {
                    e.destroy();
                }
            } else {
                e.update();
            }
        }

        if (gatePassed) {
            mainMenu.nextLevel();
            newGame(false);
            gatePassed = false;

        }

        while (!addStack.isEmpty()) {
            entities.add(addStack.pop());
        }

        while (!removeStack.isEmpty()) {
            entities.remove(removeStack.pop());
        }

        mapEditor.update();
        updateMap();
    }

    public void start(int level) {
        graphicsContext = this.getGraphicsContext2D();
        crackStage1 = new Texture(textureFolderPath + "crack1.png");
        crackStage2 = new Texture(textureFolderPath + "crack2.png");
        crackStage3 = new Texture(textureFolderPath + "crack3.png");
        map = new Map();
        map.setGame(this);

        map.loadTile();
        map.setEntityList(entities);
        map.loadMap(System.getProperty("user.dir") + "\\src\\level\\level" + level + ".txt");
        updateMap();

        Gate gate = new Gate(this);
        gate.setXY(25, 5);
        entities.add(gate);
        mapEditor = new MapEditor();
        mapEditor.setMap(map);
        mapEditor.setGame(this);

        player = new Player();
        player.setMap(map);
        player.setInput(input);

        entities.add(player);
        newLevel(level);

        userInterface = new InGameUI();
        userInterface.setUp(player, mapEditor, this);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
            }
        };
        timer.start();
    }
}
