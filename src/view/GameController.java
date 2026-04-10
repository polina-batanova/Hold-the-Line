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

    private final List<Mob> queuedMobs = new ArrayList<>();

    private final int startRow = 7;
    private final int startCol = 0;

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

                if (e.getX() >= 50 && e.getX() <= 90 && e.getY() >= 500 && e.getY() <= 540) {
                    handleMouseClick(e.getX(), e.getY());
                } else {
                    int col = e.getX() / gameMap.getTileSize();
                    int row = e.getY() / gameMap.getTileSize();

                    if (row < gameMap.getGrid().length && col < gameMap.getGrid()[0].length) {
                        if (gameMap.getGrid()[row][col] == 9 && isPlayerTurn()) {
                            attemptTowerPlacement(row, col);
                        }
                    }
                }
            }
        });
    }

    private void attemptTowerPlacement(int row, int col) {
        Player current = gameManager.getCurrentPlayer();

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

    public void startRound() {
        activeMobs.addAll(queuedMobs);
        queuedMobs.clear();
        gameManager.startRound();
    }

    private void processExecutionPhase() {
        Iterator<Mob> it = activeMobs.iterator();
        while (it.hasNext()) {
            Mob mob = it.next();

            mob.setCol(mob.getCol() + 1);

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
            gameLoop.stop();
        }
    }

    private boolean isPlayerTurn() {
        GameState s = gameManager.getState();
        return s == GameState.PLAYER1_TURN || s == GameState.PLAYER2_TURN;
    }

    public void handleMouseClick(int x, int y) {
        if (x >= 50 && x <= 90 && y >= 500 && y <= 540) {
            purchaseMob("tier1");
        }
    }

    private void purchaseMob(String tier) {
        Player current = gameManager.getCurrentPlayer();
        int cost = 50;

        if (current.spendMoney(cost)) {
            Mob m = new Mob(tier, startRow, startCol, 50, 1, 10, 10, cost);
            queuedMobs.add(m);
            System.out.println("Mob added to queue. Money left: " + current.getMoney());
        }
    }
}