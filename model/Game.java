package model;

import model.enemies.Enemy;
import model.enemies.EnemyType;
import model.levels.*;
import view.SoundManager;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private int level;
    private LevelInfo currentLevel;

    private int SCREEN_HEIGHT;
    private int SCREEN_WIDTH;

    private  Map map;
    private  ArrayList<Enemy> livingEnemies;
    private  ArrayList<EnemyType> availableEnemies;
    private ArrayList<EnemyType> initiallySpawnedEnemies;
    private double timerForNextEnemySpawn;

    // Vectors
    private double posX, posY;
    private double dirX, dirY;
    private double planeX, planeY;

    Random rand = new Random(System.nanoTime());

    private static final double PLAYER_SPEED = 2;
    private static final double ROTATION_SPEED = 2.5;
    private static final double COLLISION_THRESHOLD = 0.15;
    private static final int ENEMY_SPAWN_TIME = 25;

    public Game(int level, int SCREEN_HEIGHT, int SCREEN_WIDTH) {
        init(level, SCREEN_HEIGHT, SCREEN_WIDTH);
    }

    /**
     * Resets the level (mainly used if player dies)
     */
    public void reset() {
        SoundManager.stopAllSounds();

        String bgMusic = currentLevel.getBgMusicName();
        if (bgMusic != null) {
            SoundManager.startSound(currentLevel.getBgMusicName());
            SoundManager.setVolume(bgMusic, -10);

        }
        posX = (int)(map.mapWidth/2d) + 1.5; posY = (int)(map.mapHeight/2d) + 1.5;
        dirX = 0; dirY = 1;
        planeX = 0.66; planeY = 0;

        for (Enemy enemy : livingEnemies) {
            map.killEnemy(enemy.getMapX(), enemy.getMapY());
        }

        livingEnemies.clear();
        availableEnemies = currentLevel.getAvailableEnemies();
        timerForNextEnemySpawn = 0;

        initiallySpawnedEnemies = currentLevel.getInitiallySpawnedEnemies();
        if (initiallySpawnedEnemies != null) {
            spawnStartingEnemies();
        }
    }

    public void movePos(Direction dir, double deltaTime) {
        double newPosX = posX, newPosY = posY;

        switch (dir) {
            case UP:
                newPosX += dirX * PLAYER_SPEED * deltaTime;
                newPosY += dirY * PLAYER_SPEED * deltaTime;
                break;
            case DOWN:
                newPosX -= dirX * PLAYER_SPEED * deltaTime;
                newPosY -= dirY * PLAYER_SPEED * deltaTime;
                break;
            case LEFT:
                newPosX -= dirY * PLAYER_SPEED * deltaTime;
                newPosY += dirX * PLAYER_SPEED * deltaTime;
                break;
            case RIGHT:
                newPosX += dirY * PLAYER_SPEED * deltaTime;
                newPosY -= dirX * PLAYER_SPEED * deltaTime;
                break;
        }

        boolean collidedX = false, collidedY = false;

        if (newPosX < COLLISION_THRESHOLD || newPosX >= map.mapWidth - COLLISION_THRESHOLD
                || map.isWall((int) newPosX, (int) posY)
                || map.isWall((int) (newPosX + COLLISION_THRESHOLD), (int) (posY + COLLISION_THRESHOLD))
                || map.isWall((int) (newPosX - COLLISION_THRESHOLD), (int) (posY - COLLISION_THRESHOLD))
                || map.isWall((int) (newPosX + COLLISION_THRESHOLD), (int) (posY - COLLISION_THRESHOLD))
                || map.isWall((int) (newPosX - COLLISION_THRESHOLD), (int) (posY + COLLISION_THRESHOLD))) {
            collidedX = true;
        }

        if (newPosY < COLLISION_THRESHOLD || newPosY >= map.mapHeight - COLLISION_THRESHOLD
                || map.isWall((int) posX, (int) newPosY)
                || map.isWall((int) (posX - COLLISION_THRESHOLD), (int) (newPosY - COLLISION_THRESHOLD))
                || map.isWall((int) (posX + COLLISION_THRESHOLD), (int) (newPosY + COLLISION_THRESHOLD))
                || map.isWall((int) (posX - COLLISION_THRESHOLD), (int) (newPosY + COLLISION_THRESHOLD))
                || map.isWall((int) (posX + COLLISION_THRESHOLD), (int) (newPosY - COLLISION_THRESHOLD))) {
            collidedY = true;
        }

        if (!collidedX && !collidedY) {
            posX = newPosX;
            posY = newPosY;
        }
    }

    public void rotatePlayer(int side, double deltaTime) {
        double rotationSpeed = side * ROTATION_SPEED * deltaTime;

        double oldDirX = dirX;
        dirX = dirX * Math.cos(rotationSpeed) - dirY * Math.sin(rotationSpeed);
        dirY = oldDirX * Math.sin(rotationSpeed) + dirY * Math.cos(rotationSpeed);

        double oldPlaneX = planeX;
        planeX = planeX * Math.cos(rotationSpeed) - planeY * Math.sin(rotationSpeed);
        planeY = oldPlaneX * Math.sin(rotationSpeed) + planeY * Math.cos(rotationSpeed);
    }

    public void spawnEnemy() {
        if (availableEnemies.isEmpty()) return;

        int enemyX, enemyY;
        do {
            enemyY = rand.nextInt(map.mapWidth);
            enemyX = rand.nextInt(map.mapHeight);
        } while (map.isWall(enemyX, enemyY) || map.isExit(enemyX, enemyY));

        int index = rand.nextInt(availableEnemies.size());

        EnemyType enemyT = availableEnemies.get(index);
        Enemy newEnemy = enemyT.CreateEnemy(map, enemyX, enemyY);

        availableEnemies.remove(enemyT);
        map.placeEnemy(newEnemy);
        livingEnemies.add(newEnemy);
        //System.out.println("SOmeone spawned");
        //System.out.println(livingEnemies.size());
    }

    public void updateEnemies(double deltaTime) {
        for (Enemy enemy : livingEnemies) {
            enemy.roam(deltaTime);
            updateEnemySound(enemy);
        }
        tryToSpawnEnemy(deltaTime);
    }

    public boolean enemyExists() { return !livingEnemies.isEmpty(); }

    public boolean foundExit() {
        return map.isExit((int)posX, (int)posY);
    }

    public boolean isPlayerCaught() {
        return caughtEnemy() != null;
    }

    public Enemy caughtEnemy() {
        for (Enemy enemy : livingEnemies) {
            if (enemy.caughtPlayer((int) posX, (int) posY)) {
                return enemy;
            }
        }
        return null;
    }

    /**
     * Creates the correct level, based on the level num
     * @return LevelInfo
     */
    public LevelInfo getCorrectLevel() {
        return switch (level) {
            case 1 -> new LevelOne();
            case 2 -> new LevelTwo();
            case 3 -> new LevelThree();
            case 4 -> new LevelFour();
            case 0 -> new LevelTest();
            default -> null;
        };
    }

    public String getLevelSprite() {
        return currentLevel.getSpriteName();
    }

    public int[] getLevelBgColors() {
        return currentLevel.getLevelBgColors();
    }

    public int getLevel() { return level; }

    /**
     * The main algorithm for displaying everything in 3D.
     * It uses DDA algorithm to check where are the intersections of the rays being cast
     * @param x The point where the ray is being cast
     * @return LineInfo
     */
    public LineInfo lineToDraw(int x) {
        double cameraX = 2 * x / (double)SCREEN_WIDTH - 1;
        double rayDirX = dirX + planeX * cameraX;
        double rayDirY = dirY + planeY * cameraX;

        int mapX = (int)posX;
        int mapY = (int)posY;

        double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY)/(rayDirX * rayDirX));
        double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX)/(rayDirY * rayDirY));

        int stepX; int stepY;
        double sideDistX; double sideDistY;

        if (rayDirX < 0) {
            stepX = -1;
            sideDistX = (posX - (double)mapX) * deltaDistX;
        } else {
            stepX = 1;
            sideDistX = ((double)(mapX + 1) - posX) * deltaDistX;
        }

        if (rayDirY < 0) {
            stepY = -1;
            sideDistY = (posY - (double)mapY) * deltaDistY;
        } else {
            stepY = 1;
            sideDistY = ((double)(mapY + 1) - posY) * deltaDistY;
        }

        // lets start the dda, algorithm
        int side = 0;
        boolean hit = false;
        boolean exit = false;
        while (!hit && !exit) {
            if (sideDistX < sideDistY) {
                mapX += stepX;
                side = 0;
                sideDistX += deltaDistX;
            } else  {
                mapY += stepY;
                side = 1;
                sideDistY += deltaDistY;
            }

            if (mapX < 0 || mapX >= map.mapWidth || mapY < 0 || mapY >= map.mapHeight)
                exit = true;
            else if (map.worldMap[mapY][mapX] != 0)
                hit = true;
        }

        double distFromCamX = side == 0
                ? (mapX - posX + (1d - stepX) / 2) / rayDirX
                : (mapY - posY + (1d - stepY) / 2) / rayDirY;

        int lineHeight = (int)(SCREEN_HEIGHT / distFromCamX);
        int lineStart = SCREEN_HEIGHT / 2 - lineHeight / 2;
        int lineEnd = lineHeight / 2 + SCREEN_HEIGHT / 2;

        // Texture calculations
        // TODO may have to change things
        double textureX;
        // If the x side was hit from <- right side
        if (side == 0)
            textureX = posY + distFromCamX * rayDirY;
        else
            textureX = posX + distFromCamX * rayDirX;
        textureX -= Math.floor(textureX);

        return hit
            ? new LineInfo(lineHeight, lineStart, lineEnd, side, map.worldMap[mapY][mapX], textureX)
            : new LineInfo(lineHeight, lineStart, lineEnd, side, -1, textureX);
    }

    private double distanceFromEnemy(Enemy enemy) {
        int dx = (int)posX - enemy.getMapX();
        int dy = (int)posY - enemy.getMapY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void tryToSpawnEnemy(double deltaTime) {
        timerForNextEnemySpawn += deltaTime;
        if (timerForNextEnemySpawn >= ENEMY_SPAWN_TIME) {
            spawnEnemy();
            timerForNextEnemySpawn = 0;
        }
    }

    private void updateEnemySound(Enemy enemy) {
        double distance = distanceFromEnemy(enemy);
        int radius = enemy.getSoundRadius();
        if (distance < radius) {
            SoundManager.continueSound(enemy.getSoundName());

            float maxVolume = 0.0f;
            float minVolume = -30.0f;
            float volume = (float) ((maxVolume - minVolume) * (radius - distance) / radius + minVolume);

            SoundManager.setVolume(enemy.getSoundName(), volume);
        } else {
            SoundManager.pauseSound(enemy.getSoundName());
        }
    }

    private void spawnStartingEnemies() {
        int enemyX, enemyY;
        Enemy newEnemy;

        for (EnemyType enemyT : initiallySpawnedEnemies) {
            do {
                enemyY = rand.nextInt(map.mapWidth);
                enemyX = rand.nextInt(map.mapHeight);
            } while (map.isWall(enemyX, enemyY) || map.isExit(enemyX, enemyY));

            newEnemy = enemyT.CreateEnemy(map, enemyX, enemyY);
            availableEnemies.remove(enemyT);
            map.placeEnemy(newEnemy);
            livingEnemies.add(newEnemy);
        }
        //System.out.println("Initially spawned enemies: " + livingEnemies.size());
    }

    /**
     * Initialize the game
     */
    private void init(int level, int SCREEN_HEIGHT, int SCREEN_WIDTH) {
        SoundManager.stopAllSounds();

        this.level = level;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
        this.SCREEN_WIDTH = SCREEN_WIDTH;

        currentLevel = getCorrectLevel();
        int width = currentLevel.getWidth();
        int height = currentLevel.getHeight();

        String currentLevelBgMusic = currentLevel.getBgMusicName();
        if (currentLevelBgMusic != null) {
            SoundManager.startSound(currentLevelBgMusic);
            SoundManager.setVolume(currentLevelBgMusic, -10);
        }

        posX = (int)(width/2d) + 1.5; posY = (int)(height/2d) + 1.5;
        dirX = 0; dirY = 1;
        planeX = 0.66; planeY = 0;

        map = new Map(width, height, (int)posX, (int)posY, currentLevel.getNumberOfExits());
        livingEnemies = new ArrayList<>();
        availableEnemies = currentLevel.getAvailableEnemies();
        timerForNextEnemySpawn = 0;


        initiallySpawnedEnemies = currentLevel.getInitiallySpawnedEnemies();
        if (initiallySpawnedEnemies != null) {
            spawnStartingEnemies();
        }
    }
}
