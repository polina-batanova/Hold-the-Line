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
    private final int[][] grid = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // Row 0
            {0,60,0,0,3,1,1,4,0,9,0,3,1,1,4,0,9,0,0,0}, // Row 1 (Top path starts)
            {0,0,0,0,2,0,0,2,0,0,0,2,0,0,2,0,0,0,0,0}, // Row 2
            {1,1,1,1,6,0,0,5,1,1,1,6,0,0,5,1,1,1,1,1}, // Row 3
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // Row 4
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // Row 5
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // Row 6
            {7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7}, // Row 7 (THE FENCE)
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // Row 8
            {0,9,0,0,3,1,1,4,0,9,0,3,1,1,4,0,0,0,0,0}, // Row 9
            {1,1,1,1,6,0,0,5,1,1,1,6,0,0,5,1,1,1,1,1}, // Row 10 (Bottom path)
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,50,0}, // Row 11
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,1,1,1,1,1}, // Row 12
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}, // Row 13
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}  // Row 14
    };

    public GameMap() {
        this.assetLoader = new AssetLoader();
        // Set panel size
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;

                draw(g, "tiles/FieldsTile_38", x, y);

                renderPath(g, grid[r][c], x, y);


                if (grid[r][c] == 9) {
                    draw(g, "PlaceForTower1", x, y);
                }

                if (grid[r][c] == 7) {
                    draw(g, "fence/1", x, y);
                }

                if (grid[r][c] == 60 || grid[r][c] == 50) {
                    draw(g, "camp/1", x, y); // Tent
                    draw(g, "decor/Lamp1", x + 15, y - 5); // Banner
                    draw(g, "camp/5", x + 25, y + 20); // Fire
                }
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