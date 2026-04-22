package tests;

import entities.Mob;
import entities.Tower;
import org.junit.jupiter.api.Test;
import view.GameMap;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {
    // ====== ENTITY / POSITION TESTS ======

    // tests that Entity correctly stores row/col
    @Test
    public void testPosition() {
        Mob mob = new Mob("Goblin", 3, 5, 50, 1, 10, 5, 30);

        assertEquals(3, mob.getRow());
        assertEquals(5, mob.getCol());
    }

    // tests that mob cannot be created with a negative row coordinate
    @Test
    public void testNegRowPos() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mob("Goblin", -3, 5, 50, 1, 10, 5, 30)
        );
    }

    // tests that mob cannot be created with a negative col coordinate
    @Test
    public void testNegColPos() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mob("Goblin", 3, -5, 50, 1, 10, 5, 30)
        );
    }

    // tests that entity cannot be created with null/blank name
    @Test
    public void testWrongName() {
        // blank
        assertThrows(IllegalArgumentException.class, () ->
                new Mob("", 3, 5, 50, 1, 10, 5, 30)
        );
        // null
        assertThrows(IllegalArgumentException.class, () ->
                new Mob(null, 3, 5, 50, 1, 10, 5, 30)
        );
    }


    // ====== MOB TESTS ======

    // tests that mob init with correct stats
    @Test
    public void testMobInitStats() {
        Mob mob = new Mob("Goblin", 3, 5, 50, 1, 10, 5, 30);
        assertEquals("Goblin", mob.getName());
        assertEquals(50, mob.getHp());
        assertEquals(1, mob.getSpeed());
        assertEquals(10, mob.getDamage());
        assertEquals(5, mob.getBounty());
        assertEquals(30, mob.getCost());
    }

    // tests that takeDamage() reduces hp correctly
    @Test
    public void testTakenDMG() {
        Mob mob = new Mob("Goblin", 3, 5, 50, 1, 10, 5, 30);
        mob.takeDamage(20);
        assertEquals(30, mob.getHp());
    }

    // tests that hp never drops < 0
    @Test
    public void testDeadlyDMG() {
        Mob mob = new Mob("Goblin", 3, 5, 50, 1, 10, 5, 30);
        mob.takeDamage(999);
        assertEquals(0, mob.getHp());
    }

    // tests that isDead() returns true when hp = 0
    @Test
    public void testDeadReturn() {
        Mob mob = new Mob("Goblin", 3, 5, 50, 1, 10, 5, 30);
        mob.takeDamage(50);
        assertTrue(mob.isDead());
    }

    // tests that isDead() returns false when hp > 0
    @Test
    public void testAliveReturn() {
        Mob mob = new Mob("Goblin", 3, 5, 50, 1, 10, 5, 30);
        mob.takeDamage(10);
        assertFalse(mob.isDead());
    }

    // tests that mob rejects <= 0 hp value
    @Test
    public void testZeroHP() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mob("Goblin", 3, 5, 0, 1, 10, 5, 30)
        );
    }

    // tests that mob rejects <= 0 speed value
    @Test
    public void testZeroSpeed() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mob("Goblin", 3, 5, 50, 0, 10, 5, 30)
        );
    }


    // ====== TOWER TESTS ======

    // tests that tower init with correct stats
    @Test
    public void testTowerInitStats() {
        Tower tower = new Tower("Archer", 5, 5, 3, 15, 100);
        assertEquals("Archer", tower.getName());
        assertEquals(3, tower.getRange());
        assertEquals(15, tower.getDamage());
        assertEquals(100, tower.getCost());
        assertEquals(5, tower.getRow());
        assertEquals(5, tower.getCol());
    }

    // tests that isInRange() returns true when mob with in tower attack range
    @Test
    public void testWithInRange() {
        Tower tower = new Tower("Archer", 5, 5, 3, 15, 100);
        Mob mob = new Mob("Goblin", 5, 8, 50, 1, 10, 5, 30);
        assertTrue(tower.isInRange(mob));
    }

    // tests that isInRange() returns false when mob out of tower attack range
    @Test
    public void testOutOfRange() {
        Tower tower = new Tower("Archer", 5, 5, 3, 15, 100);
        Mob mob = new Mob("Goblin", 5, 9, 50, 1, 10, 5, 30);
        assertFalse(tower.isInRange(mob));
    }

    // tests that isInRange() works for mobs on diagonal
    @Test
    public void testOutOfRangeDiagonally() {
        Tower tower = new Tower("Archer", 5, 5, 3, 15, 100);
        Mob mob = new Mob("Goblin", 7, 7, 50, 1, 10, 5, 30);
        assertTrue(tower.isInRange(mob));
    }

    // tests that tower rejects <= range value
    @Test
    public void testZeroRange() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tower("Archer", 5, 5, 0, 15, 100)
        );
    }

    // tests that tower rejects <= damage value
    @Test
    public void testZeroDMG() {
        assertThrows(IllegalArgumentException.class, () ->
                new Tower("Archer", 5, 5, 3, 0, 100)
        );
    }


    // ====== MOVEMENT / PATH TESTS ======

    private final int[][] testPath = {
            {4, 0}, {4, 1}, {4, 2}, {4, 3}, {4, 4}
    };

    // tests that mob starts at the first tile
    @Test
    public void testSpawnTile() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 1, 10, 5, 30, 1, testPath);
        assertEquals(4, mob.getRow());
        assertEquals(0, mob.getCol());
        assertEquals(1, mob.getPlayerNumber());
    }

    // tests that mob at third tile
    @Test
    public void test3Steps() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 1, 10, 5, 30, 1, testPath);
        mob.move();
        mob.move();
        assertEquals(4, mob.getRow());
        assertEquals(2, mob.getCol());
    }

    // tests mob with speed 2
    @Test
    public void testSpeed2() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 2, 10, 5, 30, 1, testPath);
        mob.move(); // should jump from index 0 to index 2
        assertEquals(4, mob.getRow());
        assertEquals(2, mob.getCol());
    }

    // test that mob reaches end after walking whole path
    @Test
    public void testWholePath() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 1, 10, 5, 30, 1, testPath);
        // 4 moves to reach end
        mob.move();
        mob.move();
        mob.move();
        mob.move();
        mob.move();
        assertTrue(mob.hasReachedEnd());
    }

    // tests that move() does nothing after mob has reached the end
    @Test
    public void testMoveWholePath() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 1, 10, 5, 30, 1, testPath);
        for (int i = 0; i < 10; i++) {
            mob.move();
        }
        assertEquals(4, mob.getRow());
        assertEquals(4, mob.getCol());
        assertTrue(mob.hasReachedEnd());
    }

    // tests that mob with spawnDelay doesn't move
    @Test
    public void testDelay() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 1, 10, 5, 30, 1, testPath);
        mob.setSpawnDelay(2);
        mob.move();
        assertEquals(0, mob.getCol());
        mob.move();
        assertEquals(0, mob.getCol());
        mob.move();
        assertEquals(1, mob.getCol());
    }

    // test that mob with no delay moves
    @Test
    public void testNoDelay() {
        Mob mob = new Mob("Goblin", testPath[0][0], testPath[0][1],
                50, 1, 10, 10, 50, 1, testPath);
        mob.move();
        assertEquals(1, mob.getCol());
    }

    @Test
    public void testTowerLevel() {
        Tower t = new Tower("Archer", 0, 0, 3, 5, 100);
        assertEquals(1, t.getLevel());
        assertFalse(t.isMaxLevel());
    }

    @Test
    public void testUpgradeCost() {
        Tower t = new Tower("Archer", 0, 0, 3, 5, 100);

        // level 1 -> 2 should cost 75 (level * 75)
        assertEquals(75, t.getUpgradeCost());
        t.upgrade();

        // level 2 -> 3 should cost 150
        assertEquals(2, t.getLevel());
        assertEquals(150, t.getUpgradeCost());
    }
    @Test
    public void testTowerMaxLevel() {
        Tower t = new Tower("Archer", 0, 0, 3, 5, 100);
        t.upgrade();
        t.upgrade();
        assertTrue(t.isMaxLevel());
    }
    @Test
    public void testHpBarColor() {
        assertEquals(new Color(60, 200, 60), GameMap.hpBarColor(0.51)); // Green
        assertEquals(new Color(230, 200, 50), GameMap.hpBarColor(0.50)); // Yellow (boundary)
        assertEquals(new Color(230, 200, 50), GameMap.hpBarColor(0.25)); // Yellow (boundary)
        assertEquals(new Color(220, 50, 50), GameMap.hpBarColor(0.24)); // Red
    }
}