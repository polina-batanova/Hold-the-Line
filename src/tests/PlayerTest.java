package tests;

import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void  playerShouldInitializeCorrectly() {
        Player player = new Player("Player 1", 100, 200);

        assertEquals("Player 1", player.getName());
        assertEquals(100, player.getHealth());
        assertEquals(200,  player.getMoney());
    }

    @Test
    public void takeDamageShouldReduceHealth() {
        Player player = new Player("Player 1", 100, 200);

        player.takeDamage(30);

        assertEquals(70, player.getHealth());
    }

    @Test
    public void takeDamageShouldNotGoBelowZero() {
        Player player = new Player("Player 1", 50, 200);

        player.takeDamage(100);

        assertEquals(0, player.getHealth());
    }

    @Test
    public void addMoneyShouldIncreaseMoney() {
        Player player = new Player("Player 1", 100, 200);


        player.addMoney(50);

        assertEquals(250, player.getMoney());
    }

    @Test
    public void spendMoneyShouldWorkIfEnoughFunds() {
        Player player = new Player("Player 1", 100, 200);

        boolean result = player.spendMoney(80);


        assertTrue(result);
        assertEquals(120, player.getMoney());
    }

    @Test
    public void spendMoneyShouldFailIfNotEnoughFunds() {
        Player player = new Player("Player 1", 100, 50);


        boolean result = player.spendMoney(80);

        assertFalse(result);
        assertEquals(50, player.getMoney());
    }
}