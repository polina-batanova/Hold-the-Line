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
}