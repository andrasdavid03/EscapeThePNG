package view;

import persistence.FileHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LeaderboardSelectMenu extends JPanel {
    private BufferedImage bgImage;

    public LeaderboardSelectMenu(Action[] showLeaderboardLevelActions, AbstractAction returnAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalStrut(MainWindow.SCREEN_HEIGHT/2 - 120));
        for (int i = 0; i < showLeaderboardLevelActions.length; ++i) {
            JButton levelButton = new JButton("Level" + (i + 1));
            levelButton.addActionListener(showLeaderboardLevelActions[i]);
            levelButton.setAlignmentX(CENTER_ALIGNMENT);
            add(levelButton);
            add(Box.createVerticalStrut(10));
        }

        JButton returnButton = new JButton("Return to menu");
        returnButton.setAlignmentX(CENTER_ALIGNMENT);
        returnButton.addActionListener(returnAction);
        add(returnButton);

        bgImage = FileHandler.getMenuBg("ESCAPE_THE_PNG_1.png");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}
