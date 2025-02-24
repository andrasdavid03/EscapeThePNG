package model;

import model.enemies.Enemy;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Map {
    public int mapWidth;
    public int mapHeight;
    Random rand = new Random();

    int[][] worldMap;

    public Map(int mapWidth, int mapHeight, int posX, int posY, int numberOfExits) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        rand.setSeed(System.nanoTime());
        worldMap = new int[mapHeight][mapWidth];

        for (int i = 0; i < mapHeight; i++)
            for (int j = 0; j < mapWidth; j++)
                worldMap[i][j] = 1;

        generateMaze(posX,posY);
        createExits(numberOfExits);
        //showMaze();
    }

    public void placeEnemy(Enemy enemy) {
        worldMap[enemy.getMapY()][enemy.getMapX()] = enemy.getCode();
    }

    public boolean isWall(int x, int y) { return worldMap[y][x] > 0; }

    public boolean isExit(int x, int y) { return worldMap[y][x] == -1; }

    private void generateMaze(int x, int y) {
        Direction[] dirs = new Direction[] { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT };
        Collections.shuffle(Arrays.asList(dirs), rand);

        for (Direction direction : dirs) {
            switch (direction) {
                case UP:
                    if (y >= 2 && worldMap[y - 2][x] != 0) {
                        worldMap[y - 1][x] = 0;
                        worldMap[y - 2][x] = 0;
                        generateMaze(x, y - 2);
                    }
                    break;
                case LEFT:
                    if (x >= 2 && worldMap[y][x - 2] != 0) {
                        worldMap[y][x - 1] = 0;
                        worldMap[y][x - 2] = 0;
                        generateMaze(x - 2, y);
                    }
                    break;
                case DOWN:
                    if (y < mapHeight - 2 && worldMap[y + 2][x] != 0) {
                        worldMap[y + 1][x] = 0;
                        worldMap[y + 2][x] = 0;
                        generateMaze(x, y + 2);
                    }
                    break;
                case RIGHT:
                    if (x < mapWidth - 2 && worldMap[y][x + 2] != 0) {
                        worldMap[y][x + 1] = 0;
                        worldMap[y][x + 2] = 0;
                        generateMaze(x + 2, y);
                    }
                    break;
            }
        }
    }

    // TODO THIS IS PROPABLY HAVE TO BE UPGRADED IN THE FUTUREEEEEEEEEEEEE
    // TODO ->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private void createExits(int numberOfExits) {
        if (numberOfExits <= 2) {
            worldMap[1][0] = -1;
            worldMap[mapHeight - 2][mapWidth - 1] = -1;
        } else {
            worldMap[1][0] = -1;
            worldMap[mapHeight - 2][mapWidth - 1] = -1;
            worldMap[0][mapWidth / 2] = -1;
        }
    }

    public void moveEnemy(int x, int y, int newX, int newY, int code) {
        worldMap[y][x] = 0;
        worldMap[newY][newX] = code;
    }

    public void showMaze() {
        for (int i = 0; i < 50; i++) { System.out.println(); }

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                if (worldMap[y][x] == -1)
                    System.out.print("+");
                else if (worldMap[y][x] == 2)
                    System.out.print("G");
                else if (worldMap[y][x] == 3)
                    System.out.print("P");
                else if (worldMap[y][x] == 4)
                    System.out.print("F");
                else if (worldMap[y][x] == 9)
                    System.out.print("W");
                else
                    System.out.print(worldMap[y][x] == 0 ? " " : "#");
            }
            System.out.println();
        }
    }

    public void killEnemy(int mapX, int mapY) {
        worldMap[mapY][mapX] = 0;
    }
}
