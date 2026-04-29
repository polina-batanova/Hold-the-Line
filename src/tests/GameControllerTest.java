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
        p1 = new Player("Player 1", 100, 200);
        p2 = new Player("Player 2", 100, 200);
        gm = new GameManager(p1, p2);
        gm.startGame();
    }


    // ====== QUEUE TESTS ======

    // tests that queueMob deduct money and add mob to queue
    @Test
    void testMoneyDeduction() {
        Mob mob = new Mob("Goblin", 4, 0, 30, 1, 5, 3, 20, 1, shortPath);
        int before = p1.getMoney();
        boolean ok = gm.queueMob(p1, mob, mob.getCost());
        assertTrue(ok);
        assertEquals(before - 20, p1.getMoney());
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
        Mob mob = new Mob("Goblin", 4, 0, 30, 1, 5, 3, 20, 1, shortPath);
        mob.move(); mob.move(); mob.move();
        assertTrue(mob.hasReachedEnd());
        p2.takeDamage(mob.getDamage());
        assertEquals(95, p2.getHealth());
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
        Mob mob = new Mob("Goblin", 4, 5, 30, 1, 5, 3, 20, 1, shortPath);
        Tower tower = new Tower("Archer", 4, 5, 3, 50, 80);
        assertTrue(tower.isInRange(mob));
        mob.takeDamage(tower.getDamage());
        assertTrue(mob.isDead());
        int before = p2.getMoney();
        p2.addMoney(mob.getBounty());
        assertEquals(before + 3, p2.getMoney());
    }


    // ====== INCOME ======

    // tests that income increases each round
    @Test
    void testIncome() {
        int income1 = gm.getBaseIncome();
        assertEquals(100, income1);
        gm.nextTurn();
        gm.nextTurn();
        gm.nextTurn();
        assertEquals(115, gm.getBaseIncome());
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


    // ====== SINGLE-TARGET TESTS ======

    // tests that tower locks onto one mob and doesn't hit others
    @Test
    void testSingleTarget() {
        Tower tower = new Tower("Archer", 4, 5, 3, 10, 80);
        Mob mob1 = new Mob("Goblin", 4, 5, 50, 1, 10, 5, 30, 1, shortPath);
        Mob mob2 = new Mob("Goblin", 4, 6, 50, 1, 10, 5, 30, 1, shortPath);
        // tower locks mob1
        tower.setCurrentTarget(mob1);
        assertTrue(tower.hasValidTarget());
        // only mob1 takes damage
        mob1.takeDamage(tower.getDamage());
        assertEquals(40, mob1.getHp());
        assertEquals(50, mob2.getHp()); // untouched
    }


    // ====== TOWER COOLDOWN TESTS ======

    // tests tower cannot fire while on cooldown
    @Test
    void testTowerCooldownBlocksFire() {
        Tower tower = new Tower("Archer", 4, 5, 3, 5, 80);
        tower.resetCooldown();
        assertFalse(tower.canFire(), "Tower should not fire right after reset");
    }

    // tests tower can fire after cooldown expires
    @Test
    void testTowerCooldownExpires() {
        Tower tower = new Tower("Archer", 4, 5, 3, 5, 80);
        tower.resetCooldown(); // cooldown = 3
        tower.tickCooldown();  // 2
        tower.tickCooldown();  // 1
        tower.tickCooldown();  // 0
        assertTrue(tower.canFire(), "Tower should fire after cooldown expires");
    }

    // tests that Lv2 tower has faster cooldown
    @Test
    void testUpgradedCooldown() {
        Tower tower = new Tower("Archer", 4, 5, 3, 5, 80);
        assertEquals(2, tower.getAttackCooldown());
        tower.upgrade();
        assertEquals(1, tower.getAttackCooldown(), "Lv2 should have cooldown 1");
    }

    // ====== MOB INCOME BONUS TESTS ======

    // tests that buying mobs increases income bonus
    @Test
    void testMobSpendBonus() {
        assertEquals(0, p1.getMobSpendBonus());
        p1.addMobSpendBonus(20);
        assertEquals(3, p1.getMobSpendBonus());
        p1.addMobSpendBonus(80);
        assertEquals(15, p1.getMobSpendBonus());
    }

    // tests that bonus resets
    @Test
    void testMobBonusReset() {
        p1.addMobSpendBonus(100);
        p1.resetMobSpendBonus();
        assertEquals(0, p1.getMobSpendBonus());
    }


    // ====== FACTORY METHOD TESTS ======

    // tests goblin factory creates correct stats
    @Test
    void testGoblinFactory() {
        Mob g = Mob.createGoblin(1, shortPath);
        assertEquals("Goblin", g.getName());
        assertEquals(30, g.getHp());
        assertEquals(1, g.getSpeed());
        assertEquals(5, g.getDamage());
        assertEquals(3, g.getBounty());
        assertEquals(40, g.getCost());
    }

    // tests wolf factory creates correct stats
    @Test
    void testWolfFactory() {
        Mob w = Mob.createWolf(1, shortPath);
        assertEquals("Wolf", w.getName());
        assertEquals(35, w.getHp());
        assertEquals(2, w.getSpeed());
        assertEquals(8, w.getDamage());
        assertEquals(5, w.getBounty());
        assertEquals(75, w.getCost());
    }

    // tests slime factory creates correct stats
    @Test
    void testSlimeFactory() {
        Mob s = Mob.createSlime(1, shortPath);
        assertEquals("Slime", s.getName());
        assertEquals(100, s.getHp());
        assertEquals(1, s.getSpeed());
        assertEquals(15, s.getDamage());
        assertEquals(8, s.getBounty());
        assertEquals(120, s.getCost());
    }


    // ====== TOWER SELL TESTS ======

    // tests tower sell gives 50% refund
    @Test
    void testTowerSellRefund() {
        Tower t = new Tower("Archer", 4, 5, 3, 5, 80);
        assertEquals(40, t.getSellRefund());
    }

    // tests upgraded tower sell includes upgrade investment
    @Test
    void testUpgradedTowerSellRefund() {
        Tower t = new Tower("Archer", 4, 5, 3, 5, 80);
        t.upgrade(); // +75 invested → total 155
        assertEquals(77, t.getSellRefund()); // 155/2 = 77
    }


    // ====== DEATH ANIMATION TESTS ======

    // tests death animation lifecycle
    @Test
    void testDeathAnimation() {
        Mob mob = new Mob("Goblin", 4, 0, 10, 1, 5, 3, 20, 1, shortPath);
        assertFalse(mob.isDying());
        mob.takeDamage(10);
        assertTrue(mob.isDead());
        mob.startDeathAnimation();
        assertTrue(mob.isDying());
        for (int i = 0; i < 6; i++) mob.updateDeathAnimation();
        assertTrue(mob.shouldRemoveAfterDeath());
    }


    // ====== SINGLE TARGET SWITCH TEST ======

    // tests tower switches target when current dies
    @Test
    void testTargetSwitchOnDeath() {
        Tower tower = new Tower("Archer", 4, 5, 3, 50, 80);
        Mob mob1 = new Mob("Goblin", 4, 5, 10, 1, 5, 3, 20, 1, shortPath);
        tower.setCurrentTarget(mob1);
        mob1.takeDamage(50);
        assertTrue(mob1.isDead());
        assertFalse(tower.hasValidTarget());
    }
}
