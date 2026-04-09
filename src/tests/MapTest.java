package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameMap;
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
    void testAlignment() {
        int[][] grid = gameMap.getGrid();

        assertEquals(60, grid[1][1]);
        assertEquals(60, grid[1][2]);
        assertEquals(60, grid[2][1]);
        assertEquals(60, grid[2][2]);

        assertEquals(50, grid[12][16]);
        assertEquals(50, grid[12][17]);
        assertEquals(50, grid[13][16]);
        assertEquals(50, grid[13][17]);
    }
    @Test
    void testRoadIntegrity() {
        int[][] grid = gameMap.getGrid();

        assertEquals(11, grid[3][0]);
        assertEquals(10, grid[9][0]);
    }
    @Test
    void testMapIntegrity() {
        int[][] grid = gameMap.getGrid();
        assertEquals(7, grid[7][0]);
        assertEquals(9, grid[1][9]);
        assertEquals(60, grid[1][1]);
    }

}



