package tests;

import model.Player;
import org.junit.jupiter.api.Test;
import model.Mob;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testInit() {
        Player p = new Player("P1", 100, 200);

        // check basic values
        assertEquals("P1", p.getName());
        assertEquals(100, p.getHealth());
        assertEquals(200, p.getMoney());
    }

    @Test
    public void testDamage() {
        Player p = new Player("P1", 100, 200);

        p.takeDamage(30);
        assertEquals(70, p.getHealth());
    }

    @Test
    public void  testDamageBelowZero() {
        Player p = new Player("P1", 50, 200);

        p.takeDamage(100);

        // should not go below 0
        assertEquals(0, p.getHealth());
    }

    @Test
    public void testAddMoney() {
        Player p = new Player("P1", 100, 200);

        p.addMoney(50);

        assertEquals(250,  p.getMoney());
    }

    @Test
    public void testSpendMoneyOk() {
        Player p = new Player("P1", 100, 200);

        boolean  ok = p.spendMoney(80);

        assertTrue(ok);
        assertEquals(120, p.getMoney());
    }

    @Test
    public void testSpendMoneyFail() {
        Player p = new Player("P1", 100, 50);

        boolean ok =  p.spendMoney(80);

        assertFalse(ok);
        assertEquals(50, p.getMoney());
    }

    @Test
    public void testNegativeDamage() {
        Player p = new Player("P1",  100, 200);

        assertThrows(IllegalArgumentException.class, () -> {
            p.takeDamage(-10);
        });
    }

    @Test
    public void testNegativeMoneyAdd() {
        Player p = new Player("P1", 100, 200);

         assertThrows(IllegalArgumentException.class, () -> {
            p.addMoney(-5);
        });
    }

    @Test
    public void testQueueMob() {
        Player p = new Player("P1", 100, 200);
        Mob m = new Mob();

        p.addMobToQueue(m);

        assertEquals(1, p.getQueuedMobs().size());
    }
}