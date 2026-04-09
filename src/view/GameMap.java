package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class GameMap extends JPanel {
    private final int TILE_SIZE = 40;
    private final int ROWS = 15;
    private final int COLS = 20;

    private final AssetLoader assetLoader;
    private int animationTick = 0;
    private final Timer animationTimer;


    private final int[][] grid = {
            {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,15,15},
            {10,63,63,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15},
            {10,63,62,0,3,1,1,1,4,0,9,0,3,1,1,1,4,0,9,0},
            {10,0,0,61,2,0,0,0,2,0,0,0,2,0,0,0,2,0,0,0},
            {1,1,1,1,6,0,9,0,5,1,1,1,6,0,9,0,5,1,1,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11},
            {7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7},
            {11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,9,0,3,1,1,1,4,0,9,0,3,1,1,1,4,0,0,60,60},
            {0,0,0,2,0,0,0,2,0,0,0,2,0,0,0,2,61,62,0,60},
            {1,1,1,6,0,9,0,5,1,1,1,6,0,9,0,5,1,1,1,1},
            {10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,15},
            {10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,15,15,15},
    };

    public GameMap() {
        this.assetLoader = new AssetLoader();
        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));

        animationTimer = new Timer(120, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationTick++;
                repaint();
            }
        });
        animationTimer.start();
    }

    public Timer getAnimationTimer() {
        return animationTimer;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;


            }

        }
    }
}