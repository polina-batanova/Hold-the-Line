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

    // Mobs currently on the road
    private final List<Mob> activeMobs = new ArrayList<>();

    // Mobs bought but waiting for turn end
    private final List<Mob> queuedMobs = new ArrayList<>();

    // Spawn coordinates for mobs
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

    // Method for listening mouse clicks
    private void setupControls() {
        gameMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                // Logic for the 'End Turn' button
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

                        // Tile ID 9 represents a 'Place for Tower' spot
                        if (gameMap.getGrid()[row][col] == 9 && isPlayerTurn()) {
                            attemptTowerPlacement(row, col);
                        }
                    }
                }
            }
        });
    }
    // Cycles through game states
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

    // Handles movement based on path tiles, tower range checks
    private void processExecutionPhase() {
        // If all mobs are gone, return to Player 1's turn
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

            // Tiles 1, 3, 5: Move Right
            if (tileType == 1 || tileType == 3 || tileType == 5) {
                mob.setCol(c + 1);
            }
            // Tiles 2, 4, 6: Move Down
            else if (tileType == 2 || tileType == 4 || tileType == 6) {
                mob.setRow(r + 1);
            }
            else {
                mob.setCol(c + 1);
            }

            // Combat
            for (Tower t : placedTowers) {
                if (t.isInRange(mob)) {
                    mob.takeDamage(t.getDamage());
                }
            }

            // Check health
            if (mob.isDead()) {
                gameManager.getPlayer1().addMoney(mob.getBounty());
                it.remove();
            } else if (mob.getCol() >= 20 || mob.getRow() >= 15) {
                gameManager.getPlayer1().takeDamage(mob.getDamage());
                it.remove();
            }
        }
    }

    // Tries to place a tower
    private void attemptTowerPlacement(int row, int col) {

        // Check if a tower is already at this spot
        for (Tower t : placedTowers) {
            if (t.getRow() == row && t.getCol() == col) {
                JOptionPane.showMessageDialog(gameMap, "A tower is already here!");
                return;
            }
        }

        int cost = 100;
        Player current = gameManager.getCurrentPlayer();

        // Ask the player for confirmation
        int choice = JOptionPane.showConfirmDialog(
                gameMap,
                "Place a tower here?\nCost: " + cost + " gold\n\nYour gold: "
                        + current.getMoney(),
                "Tower Placement",
                JOptionPane.YES_NO_OPTION
        );

        // If the player confirms, place the tower
        if (choice == JOptionPane.YES_OPTION) {
            if (current.spendMoney(cost)) {
                placedTowers.add(new Tower("Archer", row, col, 3, 10, cost));
                System.out.println("Tower placed at (" + row + "," + col + ")");
            } else {
                JOptionPane.showMessageDialog(gameMap, "Not enough gold!");
            }
        }
    }

    // Tries to purchase a mob
    private void purchaseMob(String tier) {
        Player current = gameManager.getCurrentPlayer();
        if (current.spendMoney(50)) {
            queuedMobs.add(new Mob(tier, startRow, startCol, 50, 1, 10, 10, 50));
            System.out.println("Mob added to queue!");
        }
    }

    // Checks if it's a player's turn
    private boolean isPlayerTurn() {
        GameState s = gameManager.getState();
        return s == GameState.PLAYER1_TURN || s == GameState.PLAYER2_TURN;
    }
}