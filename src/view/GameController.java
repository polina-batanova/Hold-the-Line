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
import java.util.Queue;

public class GameController {
    private final GameMap gameMap;
    private final GameManager gameManager;
    private final List<Tower> placedTowers = new ArrayList<>();
    private final List<Mob> activeMobs = new ArrayList<>();
    private Timer gameLoop;

    // path definitions
    // player 1 road (top)
    public static final int[][] PATH_TOP = {
            {4,19},{4,18},{4,17},{4,16},
            {3,16},
            {2,16},{2,15},{2,14},{2,13},{2,12},
            {3,12},
            {4,12},{4,11},{4,10},{4,9},{4,8},
            {3,8},
            {2,8},{2,7},{2,6},{2,5},{2,4},
            {3,4},
            {4,4},{4,3},{4,2},{4,1},{4,0}
    };

    // player 2 road (bot)
    public static final int[][] PATH_BOTTOM = {
            {12,0},{12,1},{12,2},{12,3},
            {11,3},
            {10,3},{10,4},{10,5},{10,6},{10,7},
            {11,7},
            {12,7},{12,8},{12,9},{12,10},{12,11},
            {11,11},
            {10,11},{10,12},{10,13},{10,14},{10,15},
            {11,15},
            {12,15},{12,16},{12,17},{12,18},{12,19}
    };

    public GameController(GameMap map, GameManager manager) {
        this.gameMap = map;
        this.gameManager = manager;
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
                int hudY = gameMap.getPreferredSize().height - 50;

                // Logic for the 'End Turn' button
                if (x >= 660 && x <= 780 && y >= hudY + 5 && y <= hudY + 40) {
                    handleEndTurn();
                    return;
                }

                // buy mob button
                if (x >= 500 && x <= 620 && y >= hudY + 5 && y <= hudY + 40) {
                    if (isPlayerTurn()) {
                        purchaseMob();
                    }
                    return;
                }

                // check for tower spots
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
        });
    }

    // Cycles through game states
    private void handleEndTurn() {
        GameState state = gameManager.getState();

        if (state == GameState.PLAYER1_TURN) {
            gameManager.nextTurn();
            System.out.println("Switching to Player 2");
        } else if (state == GameState.PLAYER2_TURN) {
            // drain both mob queues into the battlefield
            drainQueueIntoActive(gameManager.getPlayer1());
            drainQueueIntoActive(gameManager.getPlayer2());

            gameManager.startRound();
            System.out.println("Battle Phase Started!");
        }
    }

    // moves all mobs from purchase queue into the active list
    private void drainQueueIntoActive(Player player) {
        Queue<Mob> queue = player.getQueuedMobs();
        int delay = 0;
        while (!queue.isEmpty()) {
            Mob mob = queue.poll();
            mob.setSpawnDelay(delay);
            activeMobs.add(mob);
            delay += 3; // 3 ticks between each mob
        }
    }

    // purchases
    // buys mob for current player
    private void purchaseMob() {
        Player current = gameManager.getCurrentPlayer();
        int playerNum = (current == gameManager.getPlayer1()) ? 1 : 2;
        int[][] path = (playerNum == 1) ? PATH_TOP : PATH_BOTTOM;

        // create mob at the start of the path
        Mob mob = new Mob("Goblin", path[0][0], path[0][1],
                50, 1, 10, 5, 30, playerNum, path);

        if (gameManager.queueMob(current, mob, mob.getCost())) {
            System.out.println(current.getName() + " queued a mob!");
        } else {
            JOptionPane.showMessageDialog(gameMap, "Not enough gold!");
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

    // game loop
    private void startGameLoop() {
        gameLoop = new Timer(400, e -> {
            if (gameManager.getState() == GameState.ROUND_EXECUTION) {
                processExecutionPhase();
            }
            gameMap.updateData(placedTowers, activeMobs);

            // update hud
            Player current = gameManager.getCurrentPlayer();
            boolean battle = gameManager.getState() == GameState.ROUND_EXECUTION;
            gameMap.updateHUD(
                    current.getName(),
                    current.getMoney(),
                    gameManager.getCurrentRound(),
                    gameManager.getPlayer1().getHealth(),
                    gameManager.getPlayer2().getHealth(),
                    battle
            );
        });
        gameLoop.start();
    }


    // Handles movement based on path tiles, tower range checks
    private void processExecutionPhase() {
        // If all mobs are gone, return to Player 1's turn
        if (activeMobs.isEmpty()) {
            gameManager.nextTurn();
            System.out.println("Round ended. Player 1's turn.");
            return;
        }

        Iterator<Mob> it = activeMobs.iterator();
        while (it.hasNext()) {
            Mob mob = it.next();

            // move
            mob.move();
            // tower combat
            for (Tower t : placedTowers) {
                if (t.isInRange(mob)) {
                    mob.takeDamage(t.getDamage());
                }
            }

            // check if mob died
            if (mob.isDead()) {
                // bounty goes to the DEFENDING player
                getDefender(mob).addMoney(mob.getBounty());
                it.remove();
            }
            // check if mob reached enemy base
            else if (mob.hasReachedEnd()) {
                // damage goes to the DEFENDING player's base
                getDefender(mob).takeDamage(mob.getDamage());
                it.remove();
            }
        }
    }

    // returns player who defends against this mob
    private Player getDefender(Mob mob) {
        if (mob.getPlayerNumber() == 1) {
            return gameManager.getPlayer2(); // P2 defends against P1 mobs
        } else {
            return gameManager.getPlayer1(); // P1 defends against P2 mobs
        }
    }

    private boolean isPlayerTurn() {
        GameState s = gameManager.getState();
        return s == GameState.PLAYER1_TURN || s == GameState.PLAYER2_TURN;
    }
}