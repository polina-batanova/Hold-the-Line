package tests;

import model.GameManager;
import model.GameState;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

    private Player p1;
    private Player p2;
    private GameManager gm;

    @BeforeEach
    public void setup() {
        // simple setup
        p1 = new Player("P1", 100, 200);
        p2 = new Player("P2", 100, 200);

        gm = new GameManager(p1, p2);
    }

    @Test
    public void testInitState() {
        // game not started yet
        assertEquals(GameState.NOT_STARTED, gm.getState());
        assertEquals(0, gm.getCurrentRound());
    }

    @Test
    public void testStartGame() {
        gm.startGame();

        // should go to P1 turn and round = 1
        assertEquals(GameState.PLAYER1_TURN, gm.getState());
        assertEquals(1, gm.getCurrentRound());
    }


    @Test
    public void testP1toP2() {
        gm.startGame();

        gm.nextTurn(); // P1 -> P2

        assertEquals(GameState.PLAYER2_TURN,  gm.getState());
    }


    @Test
    public void testP2toExec() {
        gm.startGame();

        gm.nextTurn();
        gm.nextTurn(); // P2 -> execution

        assertEquals(GameState.ROUND_EXECUTION, gm.getState());
    }


    @Test
    public void testRoundIncrease() {
        gm.startGame();

        gm.nextTurn();
        gm.nextTurn();

        gm.nextTurn();  // new round

        assertEquals(GameState.PLAYER1_TURN, gm.getState());
        assertEquals(2, gm.getCurrentRound());
     }

     @Test
     public void testStartRoundWrongState() {
         // calling startRound in wrong state should fail
        assertThrows(IllegalStateException.class, () -> {
            gm.startRound();
        });
    }


    @Test
    public void testStartRoundCorrectState() {
        gm.startGame();
        gm.nextTurn(); // now PLAYER2_TURN

        gm.startRound();

        assertEquals(GameState.ROUND_EXECUTION, gm.getState());
    }

    @Test
    public void testBaseIncomeStartValue() {
        assertEquals(50, gm.getBaseIncome());
    }


    @Test
    public void testIncomeAddedOnStart() {
        gm.startGame();


        assertEquals(250, p1.getMoney());
        assertEquals(250, p2.getMoney());
    }



    @Test
    public void testIncomeIncreasesNextRound() {
        gm.startGame();
        gm.nextTurn();
        gm.nextTurn();
        gm.nextTurn();

        assertEquals(60, gm.getBaseIncome());
    }
}