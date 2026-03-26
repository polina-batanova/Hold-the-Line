package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameMap extends JPanel {
    private final int TILE_SIZE = 40;
    private final int ROWS = 15;
    private final int COLS = 20;
    private final AssetLoader assetLoader;

    public GameMap() {
        this.assetLoader = new AssetLoader();
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
    }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                BufferedImage tileImg;

                if (grid[r][c] == 1 || grid[r][c] == 2) {
                    tileImg = assetLoader.getSprite("road");
                } else {
                    tileImg = assetLoader.getSprite("grass");
                }

                if (tileImg != null) {
                    g.drawImage(tileImg, c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }


                if (grid[r][c] == 3) {
                    BufferedImage spot = assetLoader.getSprite("tower_spot");
                    if (spot != null) {
                        g.drawImage(spot, c * TILE_SIZE, r * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }
        }
    }
        public boolean isRoad ( int r, int c){
            return isValidCoordinate(r, c) && grid[r][c] > 0 && grid[r][c] < 3;

        }
        public boolean isValidCoordinate ( int r, int c){
            return r >= 0 && r < ROWS && c >= 0 && c < COLS;
        }

    }
