package tests;

import entities.Mob;
import org.junit.jupiter.api.Test;

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
        assertEquals(1,  mob.getSpeed());
        assertEquals(10, mob.getDamage());
        assertEquals(5,  mob.getBounty());
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
    // actually no reason to test it now, bc method will always return false
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

    // tests that mob rejects <= 0 speed value.
    @Test
    public void testZeroSpeed() {
        assertThrows(IllegalArgumentException.class, () ->
                new Mob("Goblin", 3, 5, 50, 0, 10, 5, 30)
        );
    }
}