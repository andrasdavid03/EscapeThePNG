package model.levels;

import model.enemies.EnemyType;
import model.enemies.FishRider;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelThree extends LevelInfo {
    public LevelThree() {
        super(53, 53, "waterWall", -16773545, -16075596, 2, "levelThreeMusic.wav");
    }

    @Override
    public ArrayList<EnemyType> getAvailableEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.FISH_RIDER));
    }

    @Override
    public ArrayList<EnemyType> getInitiallySpawnedEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.FISH_RIDER));
    }
}
