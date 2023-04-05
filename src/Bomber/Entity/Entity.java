package Bomber.Entity;

import Bomber.GameFunction.Texture;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class Entity {
    protected double x = 0;
    protected double y = 0;
    protected boolean canBePassed = false;
    protected int health = 1;
    protected int maxHealth = 1;
    protected boolean active = true;
    protected boolean destructible = true;
    protected boolean toDelete = false;
    public boolean destroyOnDeath = false;

    public boolean isToDelete() {
        return toDelete;
    }
    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

    public void destroy() {
        toDelete = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isDestructible() {
        return destructible;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public boolean getCanBePassed() {
        return canBePassed;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCanBePassed(boolean canBePassed) {
        this.canBePassed = canBePassed;
    }

    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }

    public void setHealth(int health) {
        maxHealth = health;
        this.health = health;
    }

    public boolean getActive() {
        return !active;
    }

    public abstract void update();

    protected facingDirection directionFacing = facingDirection.NORTH;
    protected Texture northTexture;
    protected Texture eastTexture;
    protected Texture southTexture;
    protected Texture allTexture;

    public Entity() {

    }

    public Entity(String texturePath) {
        northTexture = new Texture(texturePath);
        directionFacing = facingDirection.NORTH;
    }

    public Entity(String northTexture, String eastTexture, String southTexture) {
        this.northTexture = new Texture(northTexture);
        this.eastTexture = new Texture(eastTexture);
        this.southTexture = new Texture(southTexture);
    }

    public Entity(String northTexture, String eastTexture, String southTexture, String allTexture) {
        this.northTexture = new Texture(northTexture);
        this.eastTexture = new Texture(eastTexture);
        this.southTexture = new Texture(southTexture);
        this.allTexture = new Texture(allTexture);
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setDirectionFacing(facingDirection directionFacing) {
        if (this.directionFacing != facingDirection.STATIONARY) {
            this.directionFacing = directionFacing;
        }
    }

    public double distanceTo(Entity other) {
        return Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
    }

    public static double distanceTo(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public boolean isCollidedWith(Entity other) {
        return distanceTo(other) < 0.5;
    }

    public void onTakeDamage() {

    }

    public boolean takeDamage(int damage) {
        if (destructible) {
            health -= damage;
            if (health <= 0) {
                if (destroyOnDeath) {
                    destroy();
                } else {
                    deactivate();
                }
            } else {
                onTakeDamage();
            }
            return true;
        } else {
            return false;
        }
    }

    public void dealDamage(int damage, Entity other) {
        other.takeDamage(damage);
    }

    public void render(GraphicsContext gc, int x, int y) {
        Image img = null;
        int flip = 1;
        int xOffset = 0;
        switch (directionFacing) {
            case ALL -> img = allTexture.getImage();
            case STATIONARY, NORTH -> img = northTexture.getImage();
            case SOUTH -> img = southTexture.getImage();
            case EAST -> img = eastTexture.getImage();
            case WEST -> {
                img = eastTexture.getImage();
                flip = -1;
                xOffset = 1;
            }
        }

        gc.drawImage(img, 0, 0,
                img.getWidth(), img.getHeight(),
                (x + xOffset) * Texture.IMAGE_SIZE, y * Texture.IMAGE_SIZE,
                flip * Texture.IMAGE_SIZE, Texture.IMAGE_SIZE);
    }

    public void render(GraphicsContext gc) {
        if (active) {
            Image img = null;
            int flip = 1;
            int xOffset = 0;
            switch (directionFacing) {
                case ALL -> img = allTexture.getImage();
                case STATIONARY, NORTH -> img = northTexture.getImage();
                case SOUTH -> img = southTexture.getImage();
                case EAST -> img = eastTexture.getImage();
                case WEST -> {
                    img = eastTexture.getImage();
                    flip = -1;
                    xOffset = 1;
                }
            }

            gc.drawImage(img, 0, 0,
                    img.getWidth(), img.getHeight(),
                    (x + xOffset) * Texture.IMAGE_SIZE, y * Texture.IMAGE_SIZE,
                    flip * Texture.IMAGE_SIZE, Texture.IMAGE_SIZE);
        }
    }
}
