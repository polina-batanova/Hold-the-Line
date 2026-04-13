package tests;

import entities.Mob;
import entities.Tower;
import model.GameManager;
import model.GameState;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameLogicTest {
    private Player player1;
    private Player player2;
    private GameManager manager;
    private List<Tower> placedTowers;
    private List<Mob> activeMobs;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player 1", 100, 200);
        player2 = new Player("Player 2", 100, 200);
        manager = new GameManager(player1, player2);
        placedTowers = new ArrayList<>();
        activeMobs = new ArrayList<>();
    }

    // Verifies that towers only place if the player has enough money
    @Test
    void testTowerPlacement() {
        int towerCost = 150;

        // Test successful purchase
        boolean success = player1.spendMoney(towerCost);
        if (success) {
            placedTowers.add(new Tower("Archer", 5, 5, 3, 10, towerCost));
        }
        assertEquals(1, placedTowers.size(), "Tower should be placed when player has money.");
        assertEquals(50, player1.getMoney(), "Player money should decrease by cost.");

        // Test failed purchase (insufficient funds)
        boolean failure = player1.spendMoney(towerCost);
        if (failure) {
            placedTowers.add(new Tower("Archer", 6, 6, 3, 10, towerCost));
        }

        assertEquals(1, placedTowers.size(), "Second tower should NOT be placed if player is broke.");
    }

    // Checks if a player's HP correctly decreases
    @Test
    void testReducesHP() {
        Mob infiltrator = new Mob("Goblin", 2, 19, 50, 1, 20, 10, 10);
        activeMobs.add(infiltrator);

        if (infiltrator.getCol() >= 19) {
            player1.takeDamage(infiltrator.getDamage());
            activeMobs.remove(infiltrator);
        }

        assertEquals(80, player1.getHealth(), "Base HP should drop by mob damage amount.");
        assertTrue(activeMobs.isEmpty(), "Mob should be removed after damaging base.");
    }

    // Ensures the game correctly cycles from Battle Phase back to the Build Phase
    @Test
    void testRoundTransition() {
        manager.startGame();
        manager.nextTurn();
        manager.startRound();

        assertEquals(GameState.ROUND_EXECUTION, manager.getState());

        Mob target = new Mob("Orc", 1, 1, 10, 1, 10, 5, 5);
        activeMobs.add(target);

        activeMobs.remove(target);

        if (manager.getState() == GameState.ROUND_EXECUTION && activeMobs.isEmpty()) {
            manager.nextTurn();
        }

        assertEquals(GameState.PLAYER1_TURN, manager.getState(),
                "Should return to Player 1 Move Phase after all mobs are cleared.");
    }
    // Tests the shop logic for buying units
    @Test
    void testMobPurchasing() {
        int initialMoney = player1.getMoney();
        int mobCost = 50;

        if (player1.spendMoney(mobCost)) {
            Mob queuedMob = new Mob("tier1", 0, 0, 50, 1, 10, 10, mobCost);
            activeMobs.add(queuedMob);
        }
        assertEquals(initialMoney - 50, player1.getMoney(), "Money should be deducted from player1");
        assertEquals(1, activeMobs.size(), "One mob should be registered");
    }
    // Tests the movement of mobs
    @Test
    void testMobMovement() {
        Mob m = new Mob("tier1", 7, 0, 50, 1, 10, 10, 50);
        int initialCol = m.getCol();
        m.setCol(m.getCol() + 1);
        assertEquals(initialCol + 1, m.getCol(), "Mob should have moved forward one column");
    }
}
