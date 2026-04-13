package view;

import model.GameManager;
import model.Player;

import javax.swing.*;

// Create main game window
public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Hold The Line");
        // Close the program when pressing X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Prevent window from being resized
        setResizable(false);

        Player p1 = new Player("Player 1", 100, 500);
        Player p2 = new Player("Player 2", 100, 500);

        GameManager gameManager = new GameManager(p1, p2);
        gameManager.startGame();
        GameMap gameMap = new GameMap();
        // Adds game panel into the window
        add(gameMap);

        new GameController(gameMap, gameManager);
        // Sizes the window to fit the size of the game panel
        pack();
        // Centers the window on the screen
        setLocationRelativeTo(null);
        // Makes the window visible
        setVisible(true);
    }
    public static void main(String[] args) {
        new GameWindow();
    }
}
