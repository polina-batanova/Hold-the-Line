package model;

public class GameManager {
    private Player player1;
    private Player player2;
    private GameState state;
    private int currentRound;
    private int baseIncome;

    public GameManager(Player player1, Player player2) {
        if (player1 == null || player2 == null) {
            throw new IllegalArgumentException("Players cannot be null.");
        }

        this.player1 = player1;
        this.player2 = player2;
        this.state = GameState.NOT_STARTED;
        this.currentRound = 0;
        this.baseIncome = 50;
    }

    public void startGame() {
        public void startGame() {
            currentRound = 1;
            giveRoundIncome();
            state = GameState.PLAYER1_TURN;
        }
    }

    public void nextTurn() {
        switch (state) {
            case PLAYER1_TURN:
                state = GameState.PLAYER2_TURN;
                break;
            case PLAYER2_TURN:
                state = GameState.ROUND_EXECUTION;
                break;
            case ROUND_EXECUTION:
                currentRound++;
                baseIncome += 10;
                giveRoundIncome();
                state = GameState.PLAYER1_TURN;
                break;
            default:
                throw new IllegalStateException("Cannot move to next turn from state: " + state);
        }
    }

    public void startRound() {
        if (state != GameState.PLAYER2_TURN) {
            throw new IllegalStateException("Round can only start after Player 2 turn.");
        }
        state = GameState.ROUND_EXECUTION;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public GameState getState() {
        return state;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Player getCurrentPlayer() {
        return (state == GameState.PLAYER1_TURN) ? player1 : player2;
    }

    public void giveRoundIncome() {
        player1.addMoney(baseIncome);
        player2.addMoney(baseIncome);
    }



    public boolean buyTower(Player p, int cost) {
        if (p == null) {
             throw new  IllegalArgumentException("Player cannot be null.");
        }

        return p.spendMoney(cost);
    }

    public boolean queueMob(Player p, Mob mob,  int cost) {
        if (p == null  || mob == null) {
            throw new IllegalArgumentException("Invalid input.");
        }

         if (p.spendMoney(cost)) {
            p.addMobToQueue(mob);
            return true;
        }

        return false;
    }

     public int getBaseIncome() {
        return baseIncome;
    }
}