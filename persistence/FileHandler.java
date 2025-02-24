package persistence;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileHandler {
    private static final String soundPath = "/persistence/sounds/";
    private static final String spritePath = "/persistence/sprites/";
    private static final String bgPath = "/persistence/menuBg/";

    public static InputStream getMusicFile(String fileName) {
        InputStream inputStream = FileHandler.class.getResourceAsStream(soundPath + fileName);

        if (inputStream == null) {
            System.out.println("The file does not exists:");
            throw new IllegalArgumentException();
        }

        return inputStream;
    }

    public static BufferedImage getSprite(String fileName) {
        try (InputStream inputStream = FileHandler.class.getResourceAsStream(spritePath + fileName)) {
            if (inputStream == null) {
                System.out.println("The sprite cant be found");
                throw new RuntimeException();
            }
            return ImageIO.read(inputStream);

        } catch (IOException e) {
            System.out.println("The sprite cant be found");
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getMenuBg(String fileName) {
        try (InputStream inputStream = FileHandler.class.getResourceAsStream(bgPath + fileName)){
            if (inputStream == null) {
                System.out.println("The background image can't be found");
                throw new IllegalArgumentException();
            }

            return ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("The sprite cant be found");
            throw new RuntimeException(e);
        }
    }
}
