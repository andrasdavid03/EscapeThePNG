package model.enemies;

import model.Map;

public class TheBug extends Enemy {
    public TheBug(int code, Map map, int mapX, int mapY, String soundName, String jumpScareSound, int soundRadius, double enemySpeed) {
        super(code, map, mapX, mapY, soundName, jumpScareSound, soundRadius, enemySpeed);
    }
}
