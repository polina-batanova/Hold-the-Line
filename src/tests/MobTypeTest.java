package tests;

import entities.Mob;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MobTypeTest {

    private final int[][] path = {
            {0, 0},
            {0, 1},
            {0, 2}
    };

    @Test
    public void testGoblinStats() {
        Mob m = Mob.createGoblin(1, path);

        assertEquals("Goblin", m.getName());
        assertEquals(30, m.getHp());
        assertEquals(5, m.getDamage());
        assertEquals(3, m.getBounty());
        assertEquals(20, m.getCost());
        assertEquals(1, m.getPlayerNumber());
    }

    @Test
    public void testWolfStats() {
        Mob m = Mob.createWolf(2, path);

        assertEquals("Wolf", m.getName());
        assertEquals(60, m.getHp());
        assertEquals(10, m.getDamage());
        assertEquals(6, m.getBounty());
        assertEquals(50, m.getCost());
        assertEquals(2, m.getPlayerNumber());
    }

    @Test
    public void testSlimeStats() {
        Mob m = Mob.createSlime(1, path);

        assertEquals("Slime", m.getName());
        assertEquals(120, m.getHp());
        assertEquals(20, m.getDamage());
        assertEquals(12, m.getBounty());
        assertEquals(100, m.getCost());
        assertEquals(1, m.getPlayerNumber());
    }
}