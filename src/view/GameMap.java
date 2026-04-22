package view;

import entities.Mob;
import entities.Tower;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


public class GameMap extends JPanel {
    private final int TILE_SIZE = 40;
    private final int ROWS = 15;
    private final int COLS = 20;

    private String currentPlayerName = "";
    private int currentGold = 0;
    private int currentRound = 0;
    private int p1Health = 100;
    private int p2Health = 100;
    private boolean isBattlePhase = false;

    // Game-over overlay state
    private boolean isGameOver = false;
    private String winnerName = "";

    // Dynamic lists provided by the GameController
    private List<Tower> currentTowers = new ArrayList<>();
    private List<Mob> currentMobs = new ArrayList<>();

    private final AssetLoader assetLoader;
    // Global tick used to sync all sprite animations
    private int animationTick = 0;
    private final Timer animationTimer;

    /**
     * 0: Grass,
     * 1-6: Path,
     * 9: Tower Slot,
     * 10: Bushes,
     * 11: Trees,
     * 60-63: Camps/Decoration
     */
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

    public void updateData(List<Tower> towers, List<Mob> mobs) {
        this.currentTowers = towers;
        this.currentMobs = mobs;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the Static Grid (Tiles, Paths, Decorations)
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int x = c * TILE_SIZE;
                int y = r * TILE_SIZE;

                // Always draw a grass tile
                draw(g, "tiles/FieldsTile_38", x, y);

                if (shouldDrawShadow(grid[r][c])) {
                    drawSimpleShadow(g2d, x, y);
                }

                // Render path logic (Dirt roads)
                renderPath(g, grid[r][c], x, y);

                // Render specific objects based on ID
                if (grid[r][c] == 9) draw(g, "placeholders/PlaceForTower1", x, y);
                if (grid[r][c] == 7) draw(g, "fence/1", x, y);
                if (grid[r][c] == 10) draw(g, "bushes/" + ((r + c * 7) % 6 + 1), x, y);
                if (grid[r][c] == 15) draw(g, "rocks/" + ((r * 3 + c * 7) % 10 + 1), x + 3, y + 3);
                if (grid[r][c] == 11) draw(g, "trees/Tree1", x, y);
                if (grid[r][c] == 12) draw(g, "trees/Tree2", x, y);
                if (grid[r][c] == 60) draw(g, "camp/1", x, y);
                if (grid[r][c] == 63) drawMirrored(g, "camp/1", x, y);
                if (grid[r][c] == 61) drawFlag(g, x, y);
                if (grid[r][c] == 62) {
                    int offsetX = (r < 5) ? 15 : 20;
                    int offsetY = -10;
                    String fireAsset = (r < 5) ? "camp/2" : "camp/3";
                    drawFireGlow(g2d, x + offsetX, y + offsetY);
                    drawFire(g, fireAsset, x + offsetX, y + offsetY);
                }
            }
        }

        // Draw Active Game Objects
        for (Tower tower : currentTowers) {
            renderTower(g, tower);
        }

        for (Mob mob : currentMobs) {
            renderMob(g, mob);
        }

        // Draw UI Overlay Elements
        drawHUD(g);
    }
    public void updateHUD(String playerName, int gold, int round,
                          int p1Hp, int p2Hp, boolean battlePhase) {
        // HUD stops updating during game-over
        if (isGameOver) {
            return;
        }
        this.currentPlayerName = playerName;
        this.currentGold = gold;
        this.currentRound = round;
        this.p1Health = p1Hp;
        this.p2Health = p2Hp;
        this.isBattlePhase = battlePhase;
    }

    // Marks the game as over and stores the winner's display name.
    public void setGameOver(String winner) {
        if (winner == null || winner.isBlank()) {
            throw new IllegalArgumentException("Winner name cannot be blank or null.");
        }
        this.isGameOver = true;
        this.winnerName = winner;
        repaint();
    }


    private void drawHUD(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int hudY = ROWS * TILE_SIZE - 50; // bottom bar Y position

        // Dark semi-transparent bar across the bottom
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, hudY, COLS * TILE_SIZE, 50);

        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        if (isBattlePhase) {
            g2d.setColor(new Color(255, 80, 80));
            g2d.drawString("⚔ BATTLE PHASE", 10, hudY + 20);
        } else {
            g2d.setColor(new Color(100, 255, 100));
            g2d.drawString(currentPlayerName + "'s Turn", 10, hudY + 20);
        }
        // Gold display
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("Gold: " + currentGold, 10, hudY + 40);

        // Round counter (center)
        g2d.setColor(Color.WHITE);
        g2d.drawString("Round " + currentRound, 370, hudY + 30);

        // HP displays
        g2d.setColor(new Color(100, 200, 255));
        g2d.drawString("P1 HP: " + p1Health, 230, hudY + 20);
        g2d.drawString("P2 HP: " + p2Health, 230, hudY + 40);

        // Button for buying mobs
        if (!isBattlePhase) {
            g2d.setColor(new Color(50, 130, 50));
            g2d.fillRoundRect(500, hudY + 5, 120, 35, 10, 10);
            g2d.setColor(new Color(80, 200, 80));
            g2d.drawRoundRect(500, hudY + 5, 120, 35, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("BUY MOB - 30g", 510, hudY + 28);
        }

        // Button for ending turn
        if (!isBattlePhase) {
            g2d.setColor(new Color(200, 50, 50));
            g2d.fillRoundRect(660, hudY + 5, 120, 35, 10, 10);
            g2d.setColor(new Color(255, 100, 100));
            g2d.drawRoundRect(660, hudY + 5, 120, 35, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("END TURN", 685, hudY + 28);
        }
    }


    // Renders an animated walking mob
    private void renderMob(Graphics g, Mob m) {
        String tier = "tier1";
        BufferedImage sheet = assetLoader.getSprite("mobs/" + tier + "/walk");
        if (sheet != null) {
            int frameCount = 6;
            int frameWidth = sheet.getWidth() / frameCount;
            int frameHeight = sheet.getHeight();

            int currentFrame = (animationTick / 2) % frameCount;
            BufferedImage frame = sheet.getSubimage(currentFrame * frameWidth, 0, frameWidth, frameHeight);

            int x = m.getCol() * TILE_SIZE;
            int y = m.getRow() * TILE_SIZE;

            g.drawImage(frame, x, y, TILE_SIZE, TILE_SIZE, null);
        }
    }

    // Draw a tower
    private void renderTower(Graphics g, Tower t) {
        String spriteKey = spriteKeyForLevel(t.getLevel());
        int frameCount = frameCountForLevel(t.getLevel());

        BufferedImage sheet = assetLoader.getSprite(spriteKey);
        if (sheet == null) {
            return;
        }

        // Compute frame dimensions from the sheet itself so we don't hard-code pixels.
        int sheetFrameWidth  = sheet.getWidth()  / Math.max(1, frameCount);
        int sheetFrameHeight = sheet.getHeight();

        int currentFrame = (animationTick / 2) % Math.max(1, frameCount);
        BufferedImage frame = sheet.getSubimage(
                currentFrame * sheetFrameWidth, 0, sheetFrameWidth, sheetFrameHeight);

        // Rendered tower size on the map
        int drawWidth  = 70;
        int drawHeight = 130;

        // Center the sprite on the tower tile, anchored to the bottom of the tile.
        int x = (t.getCol() * TILE_SIZE) + (TILE_SIZE / 2) - (drawWidth / 2);
        int y = (t.getRow() * TILE_SIZE) + TILE_SIZE - drawHeight;

        g.drawImage(frame, x, y, drawWidth, drawHeight, null);
    }

    public static int frameCountForLevel(int level) {
        if (level <= 1) {
            return 4;
        } else {
            return 6;
        }
    }

    // Returns the sprite for a given tower level
    public static String spriteKeyForLevel(int level) {
        if (level <= 1) {
            return "towers/idle/2";   // small wooden tower
        } else if (level == 2) {
            return "towers/idle/5";   // medium stone tower
        } else {
            return "towers/idle/7";   // full castle
        }
    }

    private void renderPath(Graphics g, int type, int x, int y) {
        switch (type) {
            case 1:
                draw(g, "tiles/FieldsTile_02", x, y);
                draw(g, "tiles/FieldsTile_14", x, y);
                break;
            case 2: draw(g, "tiles/FieldsTile_08", x, y); break;
            case 3: draw(g, "tiles/FieldsTile_05", x, y); break;
            case 4: draw(g, "tiles/FieldsTile_11", x, y); break;
            case 5: draw(g, "tiles/FieldsTile_18", x, y); break;
            case 6: draw(g, "tiles/FieldsTile_23", x, y); break;
        }
    }

    // Helper for drawing animated fire
    private void drawFire(Graphics g, String path, int x, int y) {
        BufferedImage sheet = assetLoader.getSprite(path);
        if (sheet != null) {
            int frames = 6;
            int w = sheet.getWidth() / frames;
            int h = sheet.getHeight();
            int current = (animationTick % frames);
            BufferedImage frame = sheet.getSubimage(current * w, 0, w, h);
            g.drawImage(frame, x, y, 32, 32, null);
        }
    }

    // Logic for camp flags
    private void drawFlag(Graphics g, int x, int y) {
        BufferedImage sheet = assetLoader.getSprite("flag/1");
        if (sheet != null) {
            int frames = 6;
            int w = sheet.getWidth() / frames;
            int h = sheet.getHeight();
            int current = (animationTick % frames);
            BufferedImage frame = sheet.getSubimage(current * w, 0, w, h);
            g.drawImage(frame, x + 15, y - 22, 32, 64, null);
        }
    }

    // Method for creating a glowing orange light effect around campfires */
    private void drawFireGlow(Graphics2D g2d, int x, int y) {
        RadialGradientPaint glow = new RadialGradientPaint(
                x + TILE_SIZE / 2, y + TILE_SIZE / 2, TILE_SIZE,
                new float[]{0f, 1f},
                new Color[]{new Color(255, 150, 0, 80), new Color(255, 100, 0, 0)}
        );
        g2d.setPaint(glow);
        g2d.fillOval(x - TILE_SIZE / 2, y - TILE_SIZE / 2, TILE_SIZE * 2, TILE_SIZE * 2);
    }

    // Method for drawing mirrored camp
    private void drawMirrored(Graphics g, String key, int x, int y) {
        BufferedImage img = assetLoader.getSprite(key);
        if (img != null) {
            g.drawImage(img, x + TILE_SIZE, y, x, y + TILE_SIZE, 0, 0, img.getWidth(), img.getHeight(), null);
        }
    }

    private void drawSimpleShadow(Graphics2D g2d, int x, int y) {
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x + 5, y + TILE_SIZE - 12, TILE_SIZE - 10, 8);
    }

    // Helper to draw a static sprite
    private void draw(Graphics g, String key, int x, int y) {
        BufferedImage img = assetLoader.getSprite(key);
        if (img != null) {
            g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, null);
        }
    }

    public boolean shouldDrawShadow(int tileType) {
        return tileType == 10 || tileType == 11 || tileType == 15 || tileType == 7;
    }
    public Timer getAnimationTimer() {
        return animationTimer;
    }

    public int getTileSize() { return TILE_SIZE; }
    public int[][] getGrid() { return grid; }

    public int calculateFrameX(int tick, int sheetWidth, int totalFrames) {
        int frameWidth = sheetWidth / totalFrames;
        return (tick % totalFrames) * frameWidth;
    }
}