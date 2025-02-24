package view;

import javax.swing.*;
import java.awt.*;

public class PauseMenu extends JPanel {
    public PauseMenu(AbstractAction returnMenuAction, AbstractAction continueGameAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton continueButton = new JButton("Continue Game");
        continueButton.setAlignmentX(CENTER_ALIGNMENT);
        continueButton.addActionListener(continueGameAction);

        JButton returnToMenu = new JButton("Return to Menu");
        returnToMenu.setAlignmentX(CENTER_ALIGNMENT);
        returnToMenu.addActionListener(returnMenuAction);


        add(Box.createVerticalStrut(MainWindow.SCREEN_HEIGHT/2 - 80));
        add(continueButton);
        add(Box.createVerticalStrut(20));
        add(returnToMenu);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(0, 0, 0, 100));
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
