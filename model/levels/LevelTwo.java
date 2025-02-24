package model.levels;

import model.enemies.EnemyType;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelTwo extends LevelInfo {
    public LevelTwo() {
        super(37, 37, "redWood", 3278848, 5899520, 2, "levelTwoMusic.wav");
    }

    @Override
    public ArrayList<EnemyType> getAvailableEnemies() {
        return new ArrayList<>(Arrays.asList(EnemyType.DEVOUR_OF_GODS, EnemyType.LEG_DEMON, EnemyType.THE_BUG));
    }
}
