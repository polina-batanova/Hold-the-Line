package tests;

import entities.Projectile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {


    @Test
    public void testProjectileStartsActive() {
        Projectile p = new Projectile(0, 0, 5, 0);

        assertTrue(p.isActive());
    }
    @Test
    public void testProjectileMovesForward() {
        Projectile p = new Projectile(0, 0, 5, 0);

        p.update();

        assertTrue(p.getX() > 0);
    }

    @Test
    public void testProjectileBecomesInactiveAtTarget() {
        Projectile p = new Projectile(0, 0, 0.1, 0);

        p.update();

        assertFalse(p.isActive());
    }
}