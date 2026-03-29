package view;

import org.junit.jupiter.api.Test;

import javax.swing.*;

// Create main game window
public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Hold The Line");
        // Close the program when pressing X
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Prevent window from being resized
        setResizable(false);

        GameMap gameMap = new GameMap();
        // Adds game panel into the window
        add(gameMap);

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
