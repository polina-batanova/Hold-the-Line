package view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class MapTest {

    @Test
    public void testGridDimensions() {
        GameMap gameMap = new GameMap();
        int[][] grid = gameMap.getGrid();

        assertNotNull(grid, "Grid array should be initialized");
        assertEquals(15, grid.length, "Grid should have 15 rows");
        assertEquals(20, grid[0].length, "Grid should have 20 columns");



    }
}
