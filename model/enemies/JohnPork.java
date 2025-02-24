package model.enemies;

import model.Map;

public class JohnPork extends Enemy {
    public JohnPork(int code, Map map, int mapX, int mapY, String soundName, String jumpScareSound, int soundRadius, int enemySpeed) {
        super(code, map, mapX, mapY, soundName, jumpScareSound, soundRadius, enemySpeed);
    }
}
