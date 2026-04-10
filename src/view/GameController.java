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

    private final int startRow = 4;
    private final int startCol = 0;
    private Timer gameLoop;

    public GameController(GameMap map, GameManager manager) {
        this.gameMap = map;
        this.gameManager = manager;

        this.activeMobs.clear();
        this.queuedMobs.clear();
        setupControls();
        startGameLoop();
    }

    private void setupControls() {
        gameMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (x >= 710 && x <= 790 && y >= 5 && y <= 45) {
                    handleEndTurn();
                }
                else if (x >= 50 && x <= 90 && y >= 500 && y <= 540) {
                    purchaseMob("tier1");
                }
                else {
                    int col = x / gameMap.getTileSize();
                    int row = y / gameMap.getTileSize();

                    if (row >= 0 && row < gameMap.getGrid().length &&
                            col >= 0 && col < gameMap.getGrid()[0].length) {

                        if (gameMap.getGrid()[row][col] == 9 && isPlayerTurn()) {
                            attemptTowerPlacement(row, col);
                        }
                    }
                }
            }
        });
    }

    private void handleEndTurn() {
        if (gameManager.getState() == GameState.PLAYER1_TURN) {
            gameManager.nextTurn();
            System.out.println("Switching to Player 2");
        } else if (gameManager.getState() == GameState.PLAYER2_TURN) {
            activeMobs.addAll(queuedMobs);
            queuedMobs.clear();

            gameManager.startRound();
            System.out.println("Battle Phase Started!");
        }
    }

    private void startGameLoop() {
        gameLoop = new Timer(400, e -> {
            if (gameManager.getState() == GameState.ROUND_EXECUTION) {
                processExecutionPhase();
            }
            gameMap.updateData(placedTowers, activeMobs);
        });
        gameLoop.start();
    }

    private void processExecutionPhase() {
        if (activeMobs.isEmpty()) {
            gameManager.setState(GameState.PLAYER1_TURN);
            return;
        }

        Iterator<Mob> it = activeMobs.iterator();
        while (it.hasNext()) {
            Mob mob = it.next();
            int r = mob.getRow();
            int c = mob.getCol();

            if (r < 0 || r >= 15 || c < 0 || c >= 20) {
                it.remove();
                continue;
            }

            int tileType = gameMap.getGrid()[r][c];

            if (tileType == 1 || tileType == 3 || tileType == 5) {
                mob.setCol(c + 1);
            }
            else if (tileType == 2 || tileType == 4 || tileType == 6) {
                mob.setRow(r + 1);
            }
            else {
                mob.setCol(c + 1);
            }

            for (Tower t : placedTowers) {
                if (t.isInRange(mob)) {
                    mob.takeDamage(t.getDamage());
                }
            }

            if (mob.isDead()) {
                gameManager.getPlayer1().addMoney(mob.getBounty());
                it.remove();
            } else if (mob.getCol() >= 20 || mob.getRow() >= 15) {
                gameManager.getPlayer1().takeDamage(mob.getDamage());
                it.remove();
            }
        }
    }

    private void attemptTowerPlacement(int row, int col) {
        Player current = gameManager.getCurrentPlayer();
        if (current.spendMoney(100)) {
            placedTowers.add(new Tower("Basic Tower", row, col, 3, 10, 100));
        }
    }

    private void purchaseMob(String tier) {
        Player current = gameManager.getCurrentPlayer();
        if (current.spendMoney(50)) {
            queuedMobs.add(new Mob(tier, startRow, startCol, 50, 1, 10, 10, 50));
            System.out.println("Mob added to queue!");
        }
    }

    private boolean isPlayerTurn() {
        GameState s = gameManager.getState();
        return s == GameState.PLAYER1_TURN || s == GameState.PLAYER2_TURN;
    }
}