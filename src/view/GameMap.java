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
    private int[][] grid = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,1,1,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,2,0},
            {0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,0,0,0,2,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };
    public int[][] getGrid() {
        return grid;
    }

}
