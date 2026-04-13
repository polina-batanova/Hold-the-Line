package entities;

// represents unit that moves towards the enemy base
// extends Entity for shared position and name
public class Mob extends Entity {

    private int hp;
    private int speed;
    private int damage; // amount of base hp deducted
    private int bounty; // reward on mob's death
    private int cost; // cost to queue

    // which player sent mob
    private int playerNumber;
    // list of waypoints mob follows
    private int[][] path;
    // current pos
    private int pathIndex;
    // delay before move
    private int spawnDelay;

    // constructor compatible with milestone 1 tests
    public Mob(String name, int row, int col,
               int hp, int speed, int damage, int bounty, int cost) {
        this(name, row, col, hp, speed, damage, bounty, cost, 0, null);
    }

    // full constructor
    public Mob(String name, int row, int col,
               int hp, int speed, int damage, int bounty, int cost, int playerNumber, int[][] path) {
        super(name, row, col);
        if (hp <= 0) {
            throw new IllegalArgumentException("Mob HP must be greater than 0.");
        }
        if (speed <= 0) {
            throw new IllegalArgumentException("Mob speed must be greater than 0.");
        }
        if (damage < 0) {
            throw new IllegalArgumentException("Mob damage cannot be negative.");
        }
        if (bounty < 0) {
            throw new IllegalArgumentException("Mob bounty cannot be negative.");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Mob cost cannot be negative.");
        }
        this.hp           = hp;
        this.speed        = speed;
        this.damage       = damage;
        this.bounty       = bounty;
        this.cost         = cost;
        this.playerNumber = playerNumber;
        this.path         = path;
        this.pathIndex    = 0;
        this.spawnDelay   = 0;
    }

    // movement
    public void move() {
        if (path == null || hasReachedEnd()) return;
        // wait until delay expires
        if (spawnDelay > 0) {
            spawnDelay--;
            return;
        }
        pathIndex += speed;
        // if on the path update position
        if (pathIndex < path.length) {
            this.row = path[pathIndex][0];
            this.col = path[pathIndex][1];
        } else {
            this.row = path[path.length - 1][0];
            this.col = path[path.length - 1][1];
        }
    }

    // returns true if mob reaches base
    public boolean hasReachedEnd() {
        return path != null && pathIndex >= path.length;
    }

    public int getHp()           { return hp; }
    public int getSpeed()        { return speed; }
    public int getDamage()       { return damage; }
    public int getBounty()       { return bounty; }
    public int getCost()         { return cost; }
    public int getPlayerNumber() { return playerNumber; }

    public void setSpawnDelay(int delay) { this.spawnDelay = delay; }

    // reduces mob's hp by the given amount
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage amount cannot be negative.");
        }
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    // true if mob's hp <= 0
    public boolean isDead() {
        return hp <= 0;
    }
}
