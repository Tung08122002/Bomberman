package Bomber.Entity;

import Bomber.GameFunction.Map;
import Bomber.GameFunction.Texture;

public abstract class Pawn extends Entity {
    protected String label;
    protected Map map;
    protected double toX = x;
    protected double toY = y;
    protected double speed = 2 / (double) Texture.IMAGE_SIZE;
    protected double tempSpeedBoost = 0;
    protected int[][] mapInfo;
    protected int mapUpdateRate = 5;
    protected int mapUpdateCounter = 0;

    public void setSpeed(double speed) {
        this.speed = (speed + tempSpeedBoost) / (double) Texture.IMAGE_SIZE;
    }

    public double getTempSpeedBoost() {
        return tempSpeedBoost / (double) Texture.IMAGE_SIZE;
    }

    public double getSpeed() {
        return speed + getTempSpeedBoost();
    }

    public void setMap(Map map) {
        this.map = map;
        mapInfo = map.createNavigationMap();
    }

    public abstract void start();

    public boolean checkIfTileIsEmpty(double x, double y) {
        return mapInfo[(int) y][(int) x] == 0;
    }

    public Pawn(String northTexture, String eastTexture, String southTexture) {
        super(northTexture, eastTexture, southTexture);
    }

    public double moveDirection(Double currentCoordinate, Double targetCoordinate) {
        double distanceToChange = getSpeed();
        //Smooth distance to change to not pass over
        if (Math.abs(currentCoordinate - targetCoordinate) < distanceToChange) {
            distanceToChange = Math.abs(currentCoordinate - targetCoordinate);
        }

        if (currentCoordinate > targetCoordinate) {
            distanceToChange *= -1;
        }
        currentCoordinate += distanceToChange;
        return currentCoordinate;
    }

    public boolean isMoving() {
        if (checkIfTileIsEmpty(toX, toY)) {
            if (x != toX) {
                return false;
            } else return y == toY;
        } else {
            toX = x;
            toY = y;
            return true;
        }
    }

    public void move() {
        if (checkIfTileIsEmpty(toX, toY) && active) {
            x = moveDirection(x, toX);
            double i = Double.compare(x, toX);
            if (i > 0) {
                setDirectionFacing(facingDirection.WEST);
            } else if (i < 0) {
                setDirectionFacing(facingDirection.EAST);
            } else {
                y = moveDirection(y, toY);
                i = Double.compare(y, toY);
                if (i > 0) {
                    setDirectionFacing(facingDirection.NORTH);
                } else if (i < 0) {
                    setDirectionFacing(facingDirection.SOUTH);
                }
            }
        }
    }

    public void updateMapInfo() {
        mapUpdateCounter++;
        if (mapUpdateCounter > mapUpdateRate) {
            mapInfo = map.createNavigationMap();
            mapUpdateCounter = 0;
        }
    }
}
