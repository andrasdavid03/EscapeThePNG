package view;

import persistence.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenu extends JPanel {
    private final BufferedImage bgImage;

    public MainMenu(AbstractAction levelSelectAction, AbstractAction openLevelLeaderboardSelectMenu, AbstractAction exitGameAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton levelSelectButton = new JButton("Start Game");
        levelSelectButton.setAlignmentX(CENTER_ALIGNMENT);
        levelSelectButton.addActionListener(levelSelectAction);

        JButton leaderboard = new JButton("Leaderboard");
        leaderboard.setAlignmentX(CENTER_ALIGNMENT);
        leaderboard.addActionListener(openLevelLeaderboardSelectMenu);

        JButton exitButton = new JButton("Exit Game");
        exitButton.setAlignmentX(CENTER_ALIGNMENT);
        exitButton.addActionListener(exitGameAction);

        add(Box.createVerticalStrut(MainWindow.SCREEN_HEIGHT/2 - 80));
        add(levelSelectButton);
        add(Box.createVerticalStrut(20));
        add(leaderboard);
        add(Box.createVerticalStrut(20));
        add(exitButton);

        bgImage = FileHandler.getMenuBg("ESCAPE_THE_PNG_1.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}
