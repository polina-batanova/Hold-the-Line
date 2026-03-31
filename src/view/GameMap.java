package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class GameMap extends JPanel {
    private final int TILE_SIZE = 40;
    private final int ROWS = 15;
    private final int COLS = 20;

    private final AssetLoader assetLoader;

    //  // 2D grid representing the map layout
    private int[][] grid = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0,60,60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            { 0,60,60, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {11,11,11,11,11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 11,11,11,11,11,11,11,11,11,11, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11,11,11,11,11, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {10,10,10,10,10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 10,10,10,10,10,10,10,10,10,10, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 50,50, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 50,50, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10}
    };

    public GameMap() {
        this.assetLoader = new AssetLoader();
        // Set panel size
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Clears screen before drawing
        super.paintComponent(g);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {

                // Convert grid position to pixel position
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;

                // Draw base floor tile
                BufferedImage floor = assetLoader.getSprite("tiles/FieldsTile_05");
                if (floor != null) {
                    g.drawImage(floor, x, y, TILE_SIZE, TILE_SIZE, null);
                }

                if (grid[r][c] == 10 || grid[r][c] == 11) {
                    BufferedImage road = assetLoader.getSprite("decor/Dirt1");
                    if (road != null) {
                        g.drawImage(road, x, y, TILE_SIZE, TILE_SIZE, null);
                    }
                }

                // Draw special objects (bases)
                renderBases(g, r, c, x, y);
            }
        }
    }

    private void renderBases(Graphics g, int r, int c, int x, int y) {
        int type = grid[r][c];
        if (type == 50 || type == 60) {
            BufferedImage shadow = assetLoader.getSprite("shadow/6");
            BufferedImage tent = assetLoader.getSprite("camp/1");
            BufferedImage fence = assetLoader.getSprite("decor/1");

            // Draw shadow
            if (shadow != null) g.drawImage(shadow, x, y + 15, TILE_SIZE, TILE_SIZE / 2, null);
            // Draw main tent
            if (tent != null) g.drawImage(tent, x, y, TILE_SIZE, TILE_SIZE, null);
            // Draw fence
            if (fence != null) g.drawImage(fence, x, y + 32, TILE_SIZE, 8, null);
        }
    }
    public int[][] getGrid() {
        return grid;
    }
}