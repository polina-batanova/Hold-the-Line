package view;

import org.junit.jupiter.api.Test;

import javax.swing.*;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Hold The Line");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GameMap gameMap = new GameMap();
        add(gameMap);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        new GameWindow();
    }


}
