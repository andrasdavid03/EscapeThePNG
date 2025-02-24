package model.enemies;

import model.Direction;
import model.Map;

import java.util.ArrayList;

public class FishRider extends Enemy {
    public FishRider(int code, Map map, int mapX, int mapY, String soundName, String jumpScareSound, int soundRadius, int enemySpeed) {
        super(code, map, mapX, mapY, soundName, jumpScareSound, soundRadius, enemySpeed);
    }


    @Override
    public void roam(double deltaTime) {
        if (deltaTime > 2) {
            return;
        }

        timeToMove += enemySpeed * deltaTime;
        if (timeToMove < canMove) return;

        if (!isValidMove()) {
            currentDir = getValidDir();
        }

        int oldMapX = mapX;
        int oldMapY = mapY;
        mapX += currentDir.x;
        mapY += currentDir.y;

        map.moveEnemy(oldMapX, oldMapY, mapX, mapY, code);

        timeToMove = 0;
        map.showMaze();
    }

    @Override
    protected Direction getValidDir() {
        ArrayList<Direction> validDirections = new ArrayList<>();

        switch (currentDir) {
            case UP:
                if (isValidDirection(Direction.DOWN)) validDirections.add(Direction.DOWN);
                if (isValidDirection(Direction.RIGHT)) validDirections.add(Direction.RIGHT);
                if (isValidDirection(Direction.LEFT)) validDirections.add(Direction.LEFT);
                break;
            case DOWN:
                if (isValidDirection(Direction.UP)) validDirections.add(Direction.UP);
                if (isValidDirection(Direction.RIGHT)) validDirections.add(Direction.RIGHT);
                if (isValidDirection(Direction.LEFT)) validDirections.add(Direction.LEFT);
                break;
            case RIGHT:
                if (isValidDirection(Direction.LEFT)) validDirections.add(Direction.LEFT);
                if (isValidDirection(Direction.DOWN)) validDirections.add(Direction.DOWN);
                if (isValidDirection(Direction.UP)) validDirections.add(Direction.UP);
                break;
            case LEFT:
                if (isValidDirection(Direction.RIGHT)) validDirections.add(Direction.RIGHT);
                if (isValidDirection(Direction.DOWN)) validDirections.add(Direction.DOWN);
                if (isValidDirection(Direction.UP)) validDirections.add(Direction.UP);
                break;
        }

        int dirSize = validDirections.size();

        return dirSize == 0 ? Direction.NOWHERE : validDirections.get(rand.nextInt(dirSize));
    }

    @Override
    protected boolean isValidDirection(Direction dir) {
        int newMapX = mapX + dir.x;
        int newMapY = mapY + dir.y;
        return !map.isExit(newMapX, newMapY) && 0 < newMapX && newMapX < map.mapWidth - 1 && 0 < newMapY && newMapY < map.mapHeight - 1;
    }
}
