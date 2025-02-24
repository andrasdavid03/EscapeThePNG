package model.levels;

import model.enemies.EnemyType;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelFour extends LevelInfo {
    public LevelFour() {
        super(53, 53, "backroomsWall", -14606321, -9080779, 2, "levelFourMusic.wav");
    }
    @Override
    public ArrayList<EnemyType> getAvailableEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER));
    }

    @Override
    public ArrayList<EnemyType> getInitiallySpawnedEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER, EnemyType.SKIN_WALLKER));
    }
}
