package model.enemies;

import model.Direction;
import model.Map;

import java.util.ArrayList;
import java.util.Random;

public abstract class Enemy {
    protected int code;
    protected String soundName;
    protected int soundRadius;
    protected Direction currentDir;
    protected String jumpScareSound;

    protected Random rand = new Random(System.nanoTime());
    protected final double enemySpeed;
    protected final double canMove = 1;
    protected double timeToMove = 0;

    protected int mapX;
    protected int mapY;

    protected Map map;

    public Enemy(int code, Map map, int mapX, int mapY, String soundName, String jumpScareSound, int soundRadius, double enemySpeed) {
        this.code = code;
        this.map = map;
        this.soundName = soundName;
        this.mapX = mapX;
        this.mapY = mapY;
        this.soundRadius = soundRadius;
        this.enemySpeed = enemySpeed;
        this.jumpScareSound = jumpScareSound;
        currentDir = Direction.UP;
    }

    public void roam(double deltaTime) {
        if (deltaTime > 2) {
            return;
        }

        timeToMove += enemySpeed * deltaTime;
        if (timeToMove < canMove) return;

        if (canTurnSideways())
            currentDir = getValidDir();
        if (!isValidMove()) {
            currentDir = turnBackDir();
        }

        int oldMapX = mapX;
        int oldMapY = mapY;
        mapX += currentDir.x;
        mapY += currentDir.y;

        map.moveEnemy(oldMapX, oldMapY, mapX, mapY, code);

        timeToMove = 0;
    }

    private Direction turnBackDir() {
        return switch (currentDir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            default -> null;
        };
    }

    protected Direction getValidDir() {
        ArrayList<Direction> validDirections = new ArrayList<>();

        switch (currentDir) {
            case UP:
                if (isValidDirection(Direction.UP)) validDirections.add(Direction.UP);
                if (isValidDirection(Direction.RIGHT)) validDirections.add(Direction.RIGHT);
                if (isValidDirection(Direction.LEFT)) validDirections.add(Direction.LEFT);
                break;
            case DOWN:
                if (isValidDirection(Direction.DOWN)) validDirections.add(Direction.DOWN);
                if (isValidDirection(Direction.RIGHT)) validDirections.add(Direction.RIGHT);
                if (isValidDirection(Direction.LEFT)) validDirections.add(Direction.LEFT);
                break;
            case RIGHT:
                if (isValidDirection(Direction.RIGHT)) validDirections.add(Direction.RIGHT);
                if (isValidDirection(Direction.DOWN)) validDirections.add(Direction.DOWN);
                if (isValidDirection(Direction.UP)) validDirections.add(Direction.UP);
                break;
            case LEFT:
                if (isValidDirection(Direction.LEFT)) validDirections.add(Direction.LEFT);
                if (isValidDirection(Direction.DOWN)) validDirections.add(Direction.DOWN);
                if (isValidDirection(Direction.UP)) validDirections.add(Direction.UP);
                break;
        }

        int dirSize = validDirections.size();

        return dirSize == 0 ? Direction.NOWHERE : validDirections.get(rand.nextInt(dirSize));
    }

    protected boolean canTurnSideways() {
        switch (currentDir) {
            case UP:
            case DOWN:
                if (isValidDirection(Direction.LEFT)) return true;
                if (isValidDirection(Direction.RIGHT)) return true;
                break;
            case LEFT:
            case RIGHT:
                if (isValidDirection(Direction.UP)) return true;
                if (isValidDirection(Direction.DOWN)) return true;
                break;
        }
        return false;
    }

    protected boolean isValidDirection(Direction dir) {
        int newX = mapX + dir.x, newY = mapY + dir.y;
        return newX >= 0 && newY >= 0 && newX < map.mapWidth && newY < map.mapHeight && !map.isWall(newX, newY) && !map.isExit(newX, newY);
    }

    protected boolean isValidMove() {
        int newMapX = mapX + currentDir.x;
        int newMapY = mapY + currentDir.y;

        return newMapX >= 0 && newMapY >= 0 && newMapX < map.mapWidth && newMapY < map.mapHeight && !map.isWall(newMapX, newMapY) && !map.isExit(newMapX, newMapY);
    }

    public boolean caughtPlayer(int x, int y) {
        return mapX == x && mapY == y;
    }

    public int getCode() { return code; }
    public int getMapX() { return mapX; }
    public int getMapY() { return mapY; }
    public String getSoundName() { return soundName; }
    public int getSoundRadius() { return soundRadius; }
    public String getJumpScareSound() { return jumpScareSound; }
}
