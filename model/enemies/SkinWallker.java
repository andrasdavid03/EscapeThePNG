package model.enemies;

import model.Map;

public class SkinWallker extends Enemy {
    public SkinWallker(int code, Map map, int mapX, int mapY, String soundName, String jumpScareSound, int soundRadius, double enemySpeed) {
        super(code, map, mapX, mapY, soundName, jumpScareSound, soundRadius, enemySpeed);
    }
}
