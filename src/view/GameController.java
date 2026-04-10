package view;

import entities.Mob;
import entities.Tower;
import model.GameManager;
import model.GameState;
import model.Player;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameController {
    private final GameMap gameMap;
    private final GameManager gameManager;
    private final List<Tower> placedTowers = new ArrayList<>();
    private final List<Mob> activeMobs = new ArrayList<>();

    private Timer gameLoop;

    public GameController(GameMap map, GameManager manager) {
        this.gameMap = map;
        this.gameManager = manager;
        setupControls();
        startGameLoop();
    }

    private void setupControls() {
        gameMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col = e.getX() / gameMap.getTileSize();
                int row = e.getY() / gameMap.getTileSize();

                if (gameMap.getGrid()[row][col] == 9 && isPlayerTurn()) {
                    attemptTowerPlacement(row, col);
                }
            }
        });
    }

    private void attemptTowerPlacement(int row, int col) {
        Player current = (gameManager.getState() == GameState.PLAYER1_TURN)
                ? gameManager.getPlayer1() : gameManager.getPlayer2();

        int cost = 100;
        if (current.spendMoney(cost)) {
            placedTowers.add(new Tower("Basic Tower", row, col, 3, 10, cost));
            gameMap.repaint();
        }
    }
    private void startGameLoop() {
        gameLoop = new Timer(100, e -> {
            if (gameManager.getState() == GameState.ROUND_EXECUTION) {
                processExecutionPhase();
            }
            gameMap.updateData(placedTowers, activeMobs);
        });
        gameLoop.start();
    }
    private void processExecutionPhase() {
        Iterator<Mob> it = activeMobs.iterator();
        while (it.hasNext()) {
            Mob mob = it.next();

            for (Tower t : placedTowers) {
                if (t.isInRange(mob)) mob.takeDamage(t.getDamage());
            }

            if (mob.isDead()) {
                gameManager.getPlayer1().addMoney(mob.getBounty());
                it.remove();
            }

            else if (mob.getCol() >= 19) {
                gameManager.getPlayer1().takeDamage(mob.getDamage());
                it.remove();
                checkGameOver();
            }
        }
        if (activeMobs.isEmpty()) {
            gameManager.nextTurn();
        }
    }
    private void checkGameOver() {
        if (gameManager.getPlayer1().getHealth() <= 0) {
        }
    }

    private boolean isPlayerTurn() {
        GameState s = gameManager.getState();
        return s == GameState.PLAYER1_TURN || s == GameState.PLAYER2_TURN;
    }
}
