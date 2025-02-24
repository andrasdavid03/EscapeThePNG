package model.enemies;

import model.Map;

public enum EnemyType {
    GASPAR_LACI, JOHN_PORK, FISH_RIDER, DEVOUR_OF_GODS, LEG_DEMON, THE_BUG, HOLY_CAT, SKIN_WALLKER;

    public Enemy CreateEnemy(Map map, int x, int y) {
        return switch (this) {
            case GASPAR_LACI -> new Laci(2, map, x, y, "gasparSong.wav", "laciJumpscareSound.wav", 15, 2);
            case JOHN_PORK -> new JohnPork(3, map, x, y, "johnSound.wav", "johnPorkJumpscareSound.wav", 7, 1);
            case FISH_RIDER -> new FishRider(4, map, x, y, "fishRiderSound.wav", "fishRiderJumpscareSound.wav", 20, 20);
            case DEVOUR_OF_GODS -> new DevourOfGods(5, map, x, y, "dogSound.wav", "dogJumpscareSound.wav", 15, 6);
            case LEG_DEMON -> new LegDemon(6, map, x, y, "legDemonSound.wav", "legDemonJumpscareSound.wav", 10, 3);
            case THE_BUG -> new TheBug(7, map, x, y, "theBugSound.wav", "theBugJumpscareSound.wav", 7, 2);
            case HOLY_CAT -> new HolyCat(8, map, x, y, "holyCatSound.wav", "holyCatJumpscareSound.wav", 9, 1);
            case SKIN_WALLKER -> new SkinWallker(9, map, x, y, "skinWallkerJumpscareSound.wav", "skinWallkerJumpscareSound.wav", 0, 0.5);
        };
    }
}
