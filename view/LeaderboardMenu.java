package view;

import persistence.Database;
import persistence.FileHandler;
import persistence.HighScore;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LeaderboardMenu extends JPanel {
    private BufferedImage bgImage;

    private int level;
    private Database database;
    private AbstractAction returnAction;

    private ArrayList<HighScore> highScores;

    public LeaderboardMenu(AbstractAction returnAction, Database database) {
        this.database = database;
        this.returnAction = returnAction;

        bgImage = FileHandler.getMenuBg("ESCAPE_THE_PNG_1.png");
    }

    public void setLevel(int level) {  this.level = level; }

    public void showLeaderboard() {
        highScores = database.getHighScoresSorted(level);

        removeAll();

        JPanel leaderBoard = new JPanel();
        leaderBoard.setLayout(new BoxLayout(leaderBoard, BoxLayout.Y_AXIS));
        leaderBoard.setAlignmentX(CENTER_ALIGNMENT);

        Dimension size = new Dimension(800, 250);
        leaderBoard.setPreferredSize(size);
        leaderBoard.setMinimumSize(size);
        leaderBoard.setMaximumSize(size);
        leaderBoard.setBorder(new LineBorder(Color.BLACK, 5));

        JLabel title = new JLabel("Level " + level);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setAlignmentX(CENTER_ALIGNMENT);
        leaderBoard.add(Box.createVerticalStrut(20));
        leaderBoard.add(title);
        leaderBoard.add(Box.createVerticalStrut(20));


        int leaderboardPos = 1;
        for (HighScore highScore : highScores) {
            JPanel entryPanel = new JPanel();
            entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.X_AXIS));
            entryPanel.setAlignmentX(CENTER_ALIGNMENT);

            JLabel timeLabel = new JLabel(leaderboardPos + ". " + highScore.getName() + " | Time: " + highScore.getTime() + "s");
            timeLabel.setFont(new Font("Arial", Font.PLAIN, 20));

            entryPanel.add(timeLabel);
            leaderBoard.add(entryPanel);

            leaderBoard.add(entryPanel);

            if (leaderboardPos < highScores.size()) {
                leaderBoard.add(new JSeparator(SwingConstants.HORIZONTAL));
            }

            leaderboardPos += 1;
        }

        JButton returnToSelectionButton = new JButton("Go back");
        returnToSelectionButton.setAlignmentX(CENTER_ALIGNMENT);
        returnToSelectionButton.addActionListener(returnAction);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(MainWindow.SCREEN_HEIGHT/3));
        add(leaderBoard);
        add(Box.createVerticalStrut(20));
        add(returnToSelectionButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
    }
}
