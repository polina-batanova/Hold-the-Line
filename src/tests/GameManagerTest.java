package tests;

import model.GameManager;
import model.GameState;
import model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

    private Player player1;
    private Player player2;
    private GameManager gameManager;


    @BeforeEach
    public void setUp() {
        player1 = new Player("Player 1",  100, 200);
        player2 = new Player("Player 2", 100, 200);
        gameManager = new GameManager(player1, player2);
    }

    @Test
    public void gameShouldStartInNotStartedState() {
        assertEquals(GameState.NOT_STARTED,  gameManager.getState());
        assertEquals(0, gameManager.getCurrentRound());
    }

    @Test
    public void startGameShouldSetPlayer1Turn() {
        gameManager.startGame();

        assertEquals(GameState.PLAYER1_TURN, gameManager.getState());
        assertEquals(1,  gameManager.getCurrentRound());
    }

    @Test
    public void nextTurnShouldMoveFromPlayer1ToPlayer2() {
          gameManager.startGame();

        gameManager.nextTurn();

        assertEquals(GameState.PLAYER2_TURN, gameManager.getState());
    }

    @Test
    public void nextTurnShouldMoveFromPlayer2ToRoundExecution() {
        gameManager.startGame();

        gameManager.nextTurn();


        gameManager.nextTurn();

        assertEquals(GameState.ROUND_EXECUTION, gameManager.getState());
    }

    @Test
    public void nextTurnShouldIncreaseRoundAfterExecution() {
        gameManager.startGame();
        gameManager.nextTurn();
        gameManager.nextTurn();

        gameManager.nextTurn();

        assertEquals(GameState.PLAYER1_TURN, gameManager.getState());
        assertEquals(2, gameManager.getCurrentRound());
    }
}