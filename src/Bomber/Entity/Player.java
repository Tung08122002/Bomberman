package Bomber.Entity;

import Bomber.Game;
import Bomber.GameFunction.Texture;
import Bomber.GameFunction.TimeCounter;

import java.util.ArrayList;

public class Player extends Pawn {
    ArrayList<String> input;
    private static final int numberOfBombType = 4;
    public int bombRangeBonus = 4;
    private int selectedBomb = 0;
    public TimeCounter[] bombCoolDownCounter;
    public int[] bombCoolDown;
    public boolean moved = true;
    TimeCounter invulnerableCounter = new TimeCounter();
    public double invulnerableDuration = 1;

    public int getSelectedBomb() {
        return selectedBomb;
    }

    public void setInput(ArrayList<String> input) {
        this.input = input;
    }

    public void healthUp() {
        health++;
    }

    public void setBombRangeBonus(int bombRangeBonus) {
        this.bombRangeBonus = bombRangeBonus;
    }

    public void setBombCoolDown(double bombCoolDown1) {
        for (int i = 0; i < bombCoolDown.length; i++) {
            if (bombCoolDown[i] - bombCoolDown1 >= 0) {
                bombCoolDown[i] -= bombCoolDown1;
            }
        }
    }

    @Override
    public void setXY(double x, double y) {
        super.setXY(x, y);
        toX = x;
        toY = y;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void deactivate() {
        super.deactivate();
    }

    @Override
    public void start() {

    }

    @Override
    public void onTakeDamage() {
        invulnerableCounter.resetCounter();
    }

    public Player() {
        super(Game.textureFolderPath + "player" + "North.png",
                Game.textureFolderPath + "player" + "East.png",
                Game.textureFolderPath + "player" + "South.png");
        this.label = "player";
        canBePassed = true;
        this.setSpeed(2);
        bombCoolDownCounter = new TimeCounter[numberOfBombType];
        for (int i = 0; i < numberOfBombType; i++) {
            bombCoolDownCounter[i] = new TimeCounter();
        }
        bombCoolDown = new int[]{1, 3, 10, 15};
    }

    public void isLive() {
        if (this.health <= 0) {
            active = false;
        }
    }

    public boolean isThisBombReady(int index) {
        boolean b = bombCoolDownCounter[index].getTime() > bombCoolDown[index];
        if (b) {
            bombCoolDownCounter[index].resetCounter();
        }
        return b;
    }

    public void placeBomb() {
        if (isThisBombReady(0)) {
            map.getGame().addEntity(new Bomb(x, y, 1 + bombRangeBonus,
                    1, map, 1, 1, "explosionBomb",
                    "placeGentle"));
            moved = false;
        }
    }

    public void placeDynamite() {
        if (isThisBombReady(1)) {
            map.getGame().addEntity(new Bomb(x, y, 2 + 2 * bombRangeBonus,
                    3, map, 2, 2 + bombRangeBonus, "explosionBig",
                    "placeGentle"));
            moved = false;
        }
    }

    public void placeGasolineBarrel() {
        if (isThisBombReady(2)) {
            map.getGame().addEntity(new GasolineBarrel(x, y, 5 + bombRangeBonus,
                    map, "explosionFlame", "placeGentle"));
            moved = false;
        }
    }

    public void placeMine() {
        if (isThisBombReady(3)) {
            map.getGame().addEntity(new Mine(x, y, 1, map,
                    "explosionFlame", "placeGentle"));
            moved = false;
        }
    }

    public void handleInput() {
        if (isMoving()) {
            if (input.contains("LEFT") || input.contains("A")) {
                toX--;
            } else if (input.contains("RIGHT") || input.contains("D")) {
                toX++;
            } else if (input.contains("UP") || input.contains("W")) {
                toY--;
            } else if (input.contains("DOWN") || input.contains("S")) {
                toY++;
            } else if (input.contains("V")) {
                System.out.println("HP: " + health + " ,Bomb radius: " + bombRangeBonus
                        + " ,Speed: " + speed * (double) Texture.IMAGE_SIZE + " ,Bomb cooldown: " + bombCoolDown[selectedBomb]);
            } else if (input.contains("SPACE")){
                if (checkIfTileIsEmpty(x, y)) {
                    switch (selectedBomb) {
                        case 0 -> placeBomb();
                        case 1 -> placeDynamite();
                        case 2 -> placeGasolineBarrel();
                        case 3 -> placeMine();
                    }
                }
            }
        } else moved = true;

        for (int i = 0; i < numberOfBombType; i++) {
            if (input.contains("DIGIT" + (i + 1))) {
                selectedBomb = i;
            }
        }
    }

    @Override
    public void update() {
        isLive();
        if (invulnerableCounter.getTime() < invulnerableDuration) {
            tempSpeedBoost = 1;
            destructible = false;
        } else {
            tempSpeedBoost = 0;
            destructible = true;
        }
        updateMapInfo();
        handleInput();
        move();
    }
}
