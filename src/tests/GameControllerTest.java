package tests;

import entities.Mob;
import model.GameManager;
import model.GameState;
import entities.Tower;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameController;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private Player p1;
    private Player p2;
    private GameManager gm;

    private final int[][] shortPath = {{4, 0}, {4, 1}, {4, 2}};
    @BeforeEach
    void setUp() {
        p1 = new Player("Player 1", 100, 500);
        p2 = new Player("Player 2", 100, 500);
        gm = new GameManager(p1, p2);
        gm.startGame();
    }


    // ====== QUEUE TESTS ======

    // tests that queueMob deduct money and add mob to queue
    @Test
    void testMoneyDeduction() {
        Mob mob = new Mob("Goblin", 4, 0, 50, 1, 10, 5, 30, 1, shortPath);
        int before = p1.getMoney();
        boolean ok = gm.queueMob(p1, mob, mob.getCost());
        assertTrue(ok);
        assertEquals(before - 30, p1.getMoney());
        assertEquals(1, p1.getQueuedMobs().size());
    }

    // tests that queueMob fail if player has no money
    @Test
    void testNoMoney() {
        Player broke = new Player("Broke", 100, 10);
        Mob mob = new Mob("Goblin", 4, 0, 50, 1, 10, 5, 30, 1, shortPath);
        boolean ok = gm.queueMob(broke, mob, mob.getCost());
        assertFalse(ok);
        assertTrue(broke.getQueuedMobs().isEmpty());
    }


    // ====== TWO-PLAYER COMBAT TESTS ======

    // tests that P1 mob reaching end damages P2
    @Test
    void testP1DmgP2() {
        Mob mob = new Mob("Goblin", 4, 0, 50, 1, 10, 5, 30, 1, shortPath);
        mob.move();
        mob.move();
        mob.move();
        assertTrue(mob.hasReachedEnd());
        p2.takeDamage(mob.getDamage());
        assertEquals(90, p2.getHealth());
    }

    // tests that P2 mob reaching end damages P1
    @Test
    void testP2DmgP1() {
        int[][] bottomPath = {{12, 19}, {12, 18}, {12, 17}};
        Mob mob = new Mob("Orc", 12, 19, 50, 1, 10, 5, 30, 2, bottomPath);
        mob.move();
        mob.move();
        mob.move();
        assertTrue(mob.hasReachedEnd());
        p1.takeDamage(mob.getDamage());
        assertEquals(90, p1.getHealth());
    }

    // tests that tower killing a P1 mob gives bounty to P2
    @Test
    void testP1BountyP2() {
        Mob mob = new Mob("Goblin", 4, 5, 10, 1, 10, 5, 50, 1, shortPath);
        Tower tower = new Tower("Archer", 4, 5, 3, 50, 100);
        assertTrue(tower.isInRange(mob));
        mob.takeDamage(tower.getDamage());
        assertTrue(mob.isDead());
        int before = p2.getMoney();
        p2.addMoney(mob.getBounty());
        assertEquals(before + 5, p2.getMoney());
    }


    // ====== INCOME ======

    // tests that income increases each round
    @Test
    void testIncome() {
        int income1 = gm.getBaseIncome();
        gm.nextTurn();
        gm.nextTurn();
        gm.nextTurn();
        assertTrue(gm.getBaseIncome() > income1);
    }


    // ====== PATH DATA TESTS ======

    // test that P1 top road start on the right side (col 19)
    @Test
    public void testTopPathStart() {
        int[] start = GameController.PATH_TOP[0];
        assertEquals(4, start[0], "Top path should start at row 4");
        assertEquals(19, start[1], "Top path should start at col 19 (right edge)");
    }

    // tests that P1 top road end on the left side (col 0)
    @Test
    public void TestTopPathEnd() {
        int[] end = GameController.PATH_TOP[GameController.PATH_TOP.length - 1];
        assertEquals(4, end[0], "Top path should end at row 4");
        assertEquals(0, end[1], "Top path should end at col 0 (left edge)");
    }

    // tests that P2 bottom road start on the left side (col 0)
    @Test
    public void TestBotPathStart() {
        int[] start = GameController.PATH_BOTTOM[0];
        assertEquals(12, start[0], "Bottom path should start at row 12");
        assertEquals(0, start[1], "Bottom path should start at col 0 (left edge)");
    }

    // tests that P2 bottom road end on the right side (col 19)
    @Test
    public void testBotPathEnd() {
        int[] end = GameController.PATH_BOTTOM[GameController.PATH_BOTTOM.length - 1];
        assertEquals(12, end[0], "Bottom path should end at row 12");
        assertEquals(19, end[1], "Bottom path should end at col 19 (right edge)");
    }

    // tests that both paths have the same length
    @Test
    public void testPathLength() {
        assertEquals(GameController.PATH_TOP.length,
                GameController.PATH_BOTTOM.length,
                "Both paths should have equal length");
    }


    // ====== SPAWN DELAY TESTS ======

    // tests that second mob in queue have a delay, first not
    @Test
    public void testDelay() {
        int[][] path = {{4, 19}, {4, 18}, {4, 17}};
        Mob mob1 = new Mob("Goblin", 4, 19, 50, 1, 10, 5, 30, 1, path);
        Mob mob2 = new Mob("Goblin", 4, 19, 50, 1, 10, 5, 30, 1, path);

        mob1.setSpawnDelay(0);
        mob2.setSpawnDelay(3);

        // mob1 should move immediately
        mob1.move();
        assertEquals(18, mob1.getCol());

        // mob2 should wait 3 ticks
        mob2.move();
        assertEquals(19, mob2.getCol(), "Mob2 should not move yet (delay=2)");
        mob2.move();
        assertEquals(19, mob2.getCol(), "Mob2 should not move yet (delay=1)");
        mob2.move();
        assertEquals(19, mob2.getCol(), "Mob2 should not move yet (delay=0)");
        mob2.move();
        assertEquals(18, mob2.getCol(), "Mob2 should move now");
    }
}
