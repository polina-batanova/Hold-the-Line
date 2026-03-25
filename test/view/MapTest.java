package view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MapTest {

    @Test
    public void testGridDimensions() {
        GameMap gameMap = new GameMap();
        int[][] grid = gameMap.getGrid();

        assertNotNull(grid, "Grid array should be initialized");
        assertEquals(15, grid.length, "Grid should have 15 rows");
        assertEquals(20, grid[0].length, "Grid should have 20 columns");
    }

    @Test
    public void testMapWidth() {
        GameMap map = new GameMap();
        assertEquals(800, map.getPreferredSize().width, "Map width should be 800 pixels");
    }
    @Test
    public void testMapHeight() {
        GameMap map = new GameMap();
        assertEquals(600, map.getPreferredSize().height, "Height should be 600px");
    }

    @Test
    public void testIsRoadLogic() {
        GameMap map = new GameMap();
        assertTrue(map.isRoad(1, 0), "Row 1, Col 0 should be recognized as a road");
        assertFalse(map.isRoad(0, 0), "Row 0, Col 0 should be recognized as grass");
    }

}
