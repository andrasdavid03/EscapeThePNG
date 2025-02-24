package view;

import model.Direction;
import model.Game;
import persistence.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import static java.awt.event.KeyEvent.*;

public class MainWindow extends JFrame {
    public static final int SCREEN_HEIGHT = 1080;
    public static final int SCREEN_WIDTH = 1980;

    private  Game game;
    private final MainMenu menu;
    private final PauseMenu pauseMenu;
    private LevelSelectMenu levelSelectMenu;
    private LevelWonMenu levelWonMenu;
    private LeaderboardSelectMenu leaderboardSelectMenu;
    private final LeaderboardMenu leaderboardMenu;
    private  DrawingPanel gameScreen;

    private boolean isRunning = false;
    private boolean isPaused = false;
    private final Set<Integer> pressedKeys = new HashSet<>();

    private double totalTimeElapsed;
    private final Database database;

    public MainWindow() {
        setTitle("TestMaze");

        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        GraphicsDevice gdevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gdevice.isFullScreenSupported())
            gdevice.setFullScreenWindow(this);
        else
            System.err.println("Fullscreen isn't supported on here");

        addKeyboardListeners();

        menu = new MainMenu(openLevelSelectMenuAction, openLevelLeaderboardSelectMenu, exitGameAction);
        pauseMenu = new PauseMenu(returnMenuAction, continueGameAction);
        database = new Database(5);
        leaderboardMenu = new LeaderboardMenu(returnLeaderboardSelectMenuAction, database);

        add(menu);
        setVisible(true);
    }

    /**
     * The main loop, where the game runs, on a different thread
     */
    private void mainLoop() {
        double time = System.nanoTime();
        double oldTime;

        while(isRunning) {
            if (!isPaused) {
                oldTime = time;
                time = System.nanoTime();
                double deltaTime = (time - oldTime) / 1_000_000_000d;
                totalTimeElapsed += deltaTime;

                updateGame(deltaTime);

                gameScreen.setTimer((int) (totalTimeElapsed));
                gameScreen.renderGame();
            }

            try {
                Thread.sleep(4);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Updates the game object, each frame
     */
    private void updateGame(double deltaTime) {
        if (game.foundExit()) {
            //game.nextLevel();
            levelWon();
            return;
        }
        // TODO DO THE JUMPSCARE HERE
        if (game.isPlayerCaught()) {
            jumpScare();
            game.reset();
            return;
        }

        // Update the player pos
        if (pressedKeys.contains(KeyEvent.VK_W))
            game.movePos(Direction.UP, deltaTime);
        if (pressedKeys.contains(KeyEvent.VK_S))
            game.movePos(Direction.DOWN, deltaTime);
        if (pressedKeys.contains(KeyEvent.VK_A))
            game.movePos(Direction.LEFT, deltaTime);
        if (pressedKeys.contains(KeyEvent.VK_D))
            game.movePos(Direction.RIGHT, deltaTime);

        if(pressedKeys.contains(VK_LEFT))
            game.rotatePlayer(1, deltaTime);
        if(pressedKeys.contains(VK_RIGHT))
            game.rotatePlayer(-1, deltaTime);

        game.updateEnemies(deltaTime);
    }

    private void startGame(int level) {
        remove(menu);
        pressedKeys.clear();

        game = new Game(level, SCREEN_HEIGHT, SCREEN_WIDTH);
        gameScreen = new DrawingPanel(game);
        add(gameScreen);

        repaint();
        revalidate();
        requestFocusInWindow();
        isPaused = false;
        totalTimeElapsed = 0;

        if (!isRunning) {
            isRunning = true;
            new Thread(this::mainLoop).start();
        }
    }

    private void levelWon() {
        SoundManager.stopAllSounds();

        if (gameScreen != null)
            remove(gameScreen);
        if (game != null)
            isRunning = false;

        levelWonMenu = new LevelWonMenu(returnMenuAction, (int) totalTimeElapsed, game.getLevel(), database);
        add(levelWonMenu);
        repaint();
        revalidate();
    }

    private void pauseGame() {
        isPaused = true;
        remove(gameScreen);
        SoundManager.pauseAllSounds();

        pauseMenu.setVisible(true);
        add(pauseMenu);
        pauseMenu.revalidate();
        pauseMenu.repaint();
    }

    private void continueGame() {
        isPaused = false;
        add(gameScreen);

        pauseMenu.setVisible(false);
        remove(pauseMenu);
        requestFocusInWindow();
        repaint();
        revalidate();
    }

    private void jumpScare() {
        String jumpscareSound = game.caughtEnemy().getJumpScareSound();
        SoundManager.playJumpscareSound(jumpscareSound);
    }

    private void addKeyboardListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                switch (e.getKeyCode()) {
                    case VK_W:
                    case VK_S:
                        if (!isPaused) {
                            SoundManager.resetContinueSound("walkingSound.wav");
                        }
                        break;
                    case VK_ESCAPE:
                        if (isPaused) {
                            continueGame();
                        } else {
                            pauseGame();
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                switch (e.getKeyCode()) {
                    case VK_W:
                    case VK_S:
                        if (!isPaused)
                            SoundManager.pauseSound("walkingSound.wav");
                        break;
                }
            }
        });
    }

    private void showLevelSelectMenu() {
        remove(menu);

        AbstractAction[] startLevelActions = new AbstractAction[4];
        for (int i = 0; i < startLevelActions.length; ++i) {
            int level = i;
            startLevelActions[level] = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(levelSelectMenu);
                    startGame(level + 1);
                }
            };
        }

        levelSelectMenu = new LevelSelectMenu(startLevelActions, returnMenuAction);
        add(levelSelectMenu);
        repaint();
        revalidate();
    }

    private void showLeaderboardLevelSelectMenu() {
        remove(menu);

        AbstractAction[] showLeaderboardLevelActions = new AbstractAction[4];
        for (int i = 0; i < showLeaderboardLevelActions.length; ++i) {
            int level = i;
            showLeaderboardLevelActions[level] = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    remove(leaderboardSelectMenu);
                    // TODO HERE DO THE LEADERBOARD SWITCHING
                    leaderboardMenu.setLevel(level + 1);
                    leaderboardMenu.showLeaderboard();
                    add(leaderboardMenu);
                    repaint();
                    revalidate();
                }
            };
        }

        leaderboardSelectMenu = new LeaderboardSelectMenu(showLeaderboardLevelActions, returnMenuAction);
        add(leaderboardSelectMenu);
        repaint();
        revalidate();
    }

    // <editor-fold desc="Abstract actions">
    private final AbstractAction openLevelSelectMenuAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showLevelSelectMenu();
        }
    };

    private final AbstractAction returnMenuAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (levelSelectMenu != null)
                remove(levelSelectMenu);
            if (leaderboardSelectMenu != null)
                remove(leaderboardSelectMenu);
            if (levelWonMenu != null)
                remove(levelWonMenu);
            if (pauseMenu != null)
                remove(pauseMenu);
            if (gameScreen != null)
                remove(gameScreen);
            if (game != null)
                isRunning = false;

            add(menu);
            repaint();
            revalidate();
        }
    };

    private final AbstractAction returnLeaderboardSelectMenuAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (leaderboardMenu != null)
                remove(leaderboardMenu);

            add(leaderboardSelectMenu);
            repaint();
            revalidate();
        }
    };

    private final AbstractAction continueGameAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            continueGame();
        }
    };

    private final AbstractAction openLevelLeaderboardSelectMenu = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showLeaderboardLevelSelectMenu();
        }
    };

    private final AbstractAction exitGameAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                System.out.println("Exit action triggered. Closing database...");
                database.stopEmbeddedPostgres();
                System.out.println("Database closed. Exiting application...");
            } catch (Exception ex) {
                System.err.println("Error during exit action: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                System.exit(0);
            }
        }
    };
    // </editor-fold>
}