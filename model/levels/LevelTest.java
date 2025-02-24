package model.levels;

import model.enemies.EnemyType;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelTest extends LevelInfo {

    public LevelTest() {
        super(13, 13, "brickWall", 0, 3778560, 3, "levelThreeMusic.wav");
    }

    @Override
    public ArrayList<EnemyType> getAvailableEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.GASPAR_LACI, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER));
    }

    @Override
    public ArrayList<EnemyType> getInitiallySpawnedEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER));
    }
}
