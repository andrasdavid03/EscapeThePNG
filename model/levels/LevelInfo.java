package model.levels;

import model.enemies.EnemyType;

import java.awt.*;
import java.util.ArrayList;

public abstract class LevelInfo {
    private int height, width;
    private String spriteName;
    private int skyColor, floorColor;
    private String bgMusicName;
    private int numberOfExits;

    public LevelInfo(int height, int width, String spriteName, int skyColor, int floorColor, int numberOfExits) {
        this.height = height;
        this.width = width;
        this.spriteName = spriteName;
        this.skyColor = skyColor;
        this.floorColor = floorColor;
        this.bgMusicName = null;
        this.numberOfExits = numberOfExits;
    }

    public LevelInfo(int height, int width, String spriteName, int skyColor, int floorColor, int numberOfExits, String bgMusicName) {
        this.height = height;
        this.width = width;
        this.spriteName = spriteName;
        this.skyColor = skyColor;
        this.floorColor = floorColor;
        this.numberOfExits = numberOfExits;
        this.bgMusicName = bgMusicName;
    }

    public int getHeight() { return height; }
    public int getWidth() { return width; }
    public String getSpriteName() { return spriteName; }
    public String getBgMusicName() { return bgMusicName; }
    public int getNumberOfExits() { return numberOfExits; }

    public abstract ArrayList<EnemyType> getAvailableEnemies();

    public ArrayList<EnemyType> getInitiallySpawnedEnemies() {
        return null;
    }

    public int[] getLevelBgColors() {
        return new int[] { skyColor, floorColor };
    }
}
