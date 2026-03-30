package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Game map class
public class GameMap extends JPanel {
    // Size of each tile
    private final int TILE_SIZE = 40;
    // Number of rows and columns
    private final int ROWS = 15;
    private final int COLS = 20;
    // Asset loader for loading images
    private final AssetLoader assetLoader;

    public GameMap() {
        this.assetLoader = new AssetLoader();
        // Set preferred size of the panel
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
    }
    // 2D array representing the game map
    private int[][] grid = {
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1,1,1,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0},
            {0,0,0,0,1,0,0,0,0,0,0,0,0,0,2,0,0,0,2,0},
            {0,0,0,0,1,1,1,1,1,1,1,1,1,1,2,0,0,0,2,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,2,2,2,2,0},
            {0,0,0,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,3,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
    };
    public int[][] getGrid() {
        return grid;
    }

    // Paint the game map
    @Override
    protected void paintComponent(Graphics g) {
        // Clear screen before drawing
        super.paintComponent(g);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                BufferedImage tileImg;

                // Check of tile is road or grass
                if (grid[r][c] == 1 || grid[r][c] == 2) {
                    tileImg = assetLoader.getSprite("road");
                } else {
                    tileImg = assetLoader.getSprite("grass");
                }

                // Draw the base tile
                if (tileImg != null) {
                    g.drawImage(tileImg, c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }

                // If it is a tower, draw overlay
                if (grid[r][c] == 3) {
                    BufferedImage spot = assetLoader.getSprite("tower_spot");
                    if (spot != null) {
                        g.drawImage(spot, c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
    }
    // Check if it is a road
    public boolean isRoad ( int r, int c){
        return isValidCoordinate(r, c) && grid[r][c] > 0 && grid[r][c] < 3;

    }
    // Check if it is a valid coordinate
    public boolean isValidCoordinate ( int r, int c){
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
        }

    }
