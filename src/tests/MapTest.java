package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameMap;

import javax.swing.*;
import java.awt.*;


import static org.junit.jupiter.api.Assertions.*;


public class MapTest {
    private GameMap gameMap;

    @BeforeEach
    void setUp() {
        gameMap = new GameMap();
    }
    @Test
    void testSize() {
        Dimension size = gameMap.getPreferredSize();
        assertEquals(800, size.width, "Map width should be 800");
        assertEquals(600, size.height, "Map height should be 600");
    }

    @Test
    void testTimer() {
        Timer timer = gameMap.getAnimationTimer();
        assertNotNull(timer, "Timer should be initialized in constructor");
        assertTrue(timer.isRunning(), "Timer should be started automatically");
        assertEquals(120, timer.getDelay(), "Timer delay should be exactly 120ms");
    }
    @Test
    void testShadow() {
        assertTrue(gameMap.shouldDrawShadow(10));
        assertTrue(gameMap.shouldDrawShadow(15));
        assertFalse(gameMap.shouldDrawShadow(0));
    }
    @Test
    void testGridObjectPlacement() {
        int[][] grid = gameMap.getGrid();
        assertEquals(7, grid[7][0]);
        assertEquals(9, grid[2][10]);
        assertEquals(11, grid[6][0]);
        assertEquals(61, grid[3][3]);
    }
    @Test
    void testFireGridIntegrity() {
        int[][] grid = gameMap.getGrid();

        assertEquals(62, grid[2][2]);
        assertEquals(62, grid[11][17]);
    }
    @Test
    void testAnimation() {
        int sheetWidth = 192;
        int totalFrames = 6;

        assertEquals(0, gameMap.calculateFrameX(0, sheetWidth, totalFrames));
        assertEquals(32, gameMap.calculateFrameX(1, sheetWidth, totalFrames));
        assertEquals(0, gameMap.calculateFrameX(6, sheetWidth, totalFrames));
    }


}



