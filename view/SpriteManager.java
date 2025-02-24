package view;

import persistence.FileHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SpriteManager {
    private static HashMap<String, BufferedImage> sprites = new HashMap<>();

    public SpriteManager() {
        addSprite("brickWall.jpg");
        addSprite("floor.jpg");
        addSprite("laci.jpg");
        addSprite("redWood.jpg");
        addSprite("johnPork.jpg");
        addSprite("waterWall.png");
        addSprite("fishRider.png");
        addSprite("DevourOfGods.png");
        addSprite("legDemon.png");
        addSprite("theBug.png");
        addSprite("holyCat.png");
        addSprite("backroomsWall.png");
    }

    public BufferedImage get(String name) {
        return sprites.get(name);
    }


    /**
     * Adds sprite to the sprites HashMap
     * @param fileName
     */
    private void addSprite(String fileName) {
        BufferedImage img = FileHandler.getSprite(fileName);
        sprites.put(fileName.substring(0, fileName.lastIndexOf('.')), img);
    }
}
