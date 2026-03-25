package view;

import javax.swing.*;
import java.awt.*;

public class GameMap extends JPanel {
    private final int TILE_SIZE = 40;
    private final int ROWS = 15;
    private final int COLS = 20;

    public GameMap() {
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
    }

}
