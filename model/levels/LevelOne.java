package model.levels;

import model.enemies.EnemyType;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelOne extends LevelInfo {
    public LevelOne() {
        super(37, 37, "brickWall", 0,3778560, 2);
    }

    @Override
    public ArrayList<EnemyType> getAvailableEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.GASPAR_LACI, EnemyType.JOHN_PORK, EnemyType.HOLY_CAT));
    }

    @Override
    public ArrayList<EnemyType> getInitiallySpawnedEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.GASPAR_LACI));
    }
}
