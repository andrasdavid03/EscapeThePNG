package view;

import model.Game;
import model.LineInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawingPanel extends JPanel {
    private int timer = 0;
    private LineInfo line;

    private final Game game;

    private SpriteManager spriteManager;
    private BufferedImage buffer;

    private Graphics2D g2d;

    public void setTimer(int timer) { this.timer = timer; }

    public DrawingPanel(Game game) {
        this.game = game;
        spriteManager = new SpriteManager();
        buffer = new BufferedImage(MainWindow.SCREEN_WIDTH, MainWindow.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        Graphics2D bufferG2d = buffer.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        setBgColor(bufferG2d);

        printWall(bufferG2d);
        printTime(bufferG2d);

        g2d.drawImage(buffer, 0, 0, null);
        bufferG2d.dispose();
    }

    public void renderGame() {
        repaint();
    }

    private void printTime(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawString("TIME: " + timer, 20, 20);
    }

    private void printWall(Graphics2D g2d) {
        BufferedImage currentSprite;

        for (int x = 0; x < MainWindow.SCREEN_WIDTH; x += 8) {
            line = game.lineToDraw(x);

            if (line.getColorCode() == -1) {
                g2d.setColor(new Color(245, 253, 0));
                for (int j = 0; j < 8; j++)
                    g2d.drawLine(x + j, line.getLineStart(), x + j, line.getLineEnd());
                continue;
            }

            currentSprite = getCorrectSprite(line.getColorCode());
            printLine(g2d, x, currentSprite, line.getHitSide());
        }
    }

    private void printLine(Graphics2D g2d, int x, BufferedImage sprite, int side) {
        int ceiling = line.getLineStart();
        int floor = line.getLineEnd();

        if (sprite == null) return;

        double textureX = line.getTextureX();

        // WALL CALCULATION
        int wallSpriteWidth = sprite.getWidth();
        int wallSpriteHeight = sprite.getHeight();

        int wallPixelX = (int) (textureX * wallSpriteWidth);
        if (wallPixelX < 0) wallPixelX = 0;
        if (wallPixelX >= wallSpriteWidth) wallPixelX = wallSpriteWidth - 1;

        float wallStep = (float)wallSpriteHeight / (floor - ceiling);
        double wallTextureY = 0;

        int yIncrease;
        if (wallStep > 4) {
            yIncrease = 1;
        }
        else if (wallStep > 2) {
            yIncrease = 4;
        } else if (wallStep > 0.5) {
            yIncrease = 8;
        } else if (wallStep > 0.2){
            yIncrease = 10;
        } else {
            yIncrease = 14;
        }

        for (int y = ceiling; y < floor; y+= yIncrease) {
            int wallPixelY = (int) wallTextureY;
            wallPixelY = Math.max(0, Math.min(wallPixelY, wallSpriteHeight - 1));

            int color = sprite.getRGB(wallPixelX, wallPixelY);
            Color wallColor = side == 0 ? new Color(color) : new Color(color).darker();
            g2d.setColor(wallColor);
            g2d.fillRect(x, y, 8, yIncrease);

            wallTextureY += wallStep * yIncrease;
        }
    }

    private void setBgColor(Graphics2D g2d) {
        int[] bgColors = game.getLevelBgColors();
        g2d.setBackground(new Color(bgColors[0]));
        g2d.clearRect(0, 0, getWidth(), getHeight());

        g2d.setBackground(new Color(bgColors[1]));
        g2d.clearRect(0, MainWindow.SCREEN_HEIGHT / 2, MainWindow.SCREEN_WIDTH, MainWindow.SCREEN_HEIGHT / 2);
    }

    private BufferedImage getCorrectSprite(int code) {
        return switch (code) {
            case 1 -> spriteManager.get(game.getLevelSprite());
            case 2 -> spriteManager.get("laci");
            case 3 -> spriteManager.get("johnPork");
            case 4 -> spriteManager.get("fishRider");
            case 5 -> spriteManager.get("DevourOfGods");
            case 6 -> spriteManager.get("legDemon");
            case 7 -> spriteManager.get("theBug");
            case 8 -> spriteManager.get("holyCat");
            case 9 -> spriteManager.get(game.getLevelSprite());
            default -> null;
        };
    }
}
