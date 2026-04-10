package tests;

import entities.Mob;
import entities.Tower;
import model.GameManager;
import model.Player;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

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
}
