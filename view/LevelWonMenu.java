package view;

import persistence.Database;

import javax.swing.*;
import java.awt.*;

public class LevelWonMenu extends JPanel {
    public LevelWonMenu(AbstractAction returnMenuAction, int time, int level, Database database) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel wonLabel = new JLabel("YOU HAVE WON!!!");
        wonLabel.setFont(new Font("Arial", Font.BOLD,48));
        wonLabel.setAlignmentX(CENTER_ALIGNMENT);
        JLabel timeLabel = new JLabel("Your time: " + time + " seconds");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 48));
        timeLabel.setAlignmentX(CENTER_ALIGNMENT);


        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);

        JTextArea nameTextArea = new JTextArea(1, 20);
        nameTextArea.setFont(new Font("Arial", Font.BOLD, 24));
        nameTextArea.setAlignmentX(CENTER_ALIGNMENT);
        nameTextArea.setMaximumSize(nameTextArea.getPreferredSize());

        JButton returnToMenu = new JButton("Return to Menu");
        returnToMenu.setAlignmentX(CENTER_ALIGNMENT);
        returnToMenu.addActionListener(e -> {
            String name = nameTextArea.getText();
            if (name.length() > 15) {
                name = name.substring(0, 15);
            }

            database.putHighScore(level, name, time);
            returnMenuAction.actionPerformed(e);
        });

        add(Box.createVerticalStrut(MainWindow.SCREEN_HEIGHT/2 - 80));
        add(wonLabel);
        add(Box.createVerticalStrut(20));
        add(timeLabel);

        add(Box.createVerticalStrut(30));
        add(nameLabel);

        // here goes the name
        add(Box.createVerticalStrut(30));
        add(nameTextArea);

        add(Box.createVerticalStrut(20));
        add(returnToMenu);
    }
}
