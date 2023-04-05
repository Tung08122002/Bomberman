package Bomber.Entity.Enemy;

import Bomber.Entity.Player;
import Bomber.Entity.facingDirection;

import java.util.Random;

public class EnemyAI {
    Player player;
    Enemy enemy;

    public EnemyAI(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    public facingDirection goHorizontal() {
        if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
            return facingDirection.STATIONARY;
        } else if (enemy.getX() > player.getX()) {
            return facingDirection.WEST;
        } else if (enemy.getX() == player.getX()) {
            return goVertical();
        } else {
            return facingDirection.EAST;
        }
    }

    private facingDirection goVertical() {
        if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
            return facingDirection.STATIONARY;
        } else if (enemy.getY() > player.getY()) {
            return facingDirection.NORTH;
        } else if (enemy.getY() == player.getY()) {
            return goHorizontal();
        } else {
            return facingDirection.SOUTH;
        }
    }

    public facingDirection getDirection() {
        Random random = new Random();
        if (random.nextInt(2) == 0) return goHorizontal();
        return goVertical();
    }
}
