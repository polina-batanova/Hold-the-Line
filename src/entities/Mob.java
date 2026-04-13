package entities;

// represents unit that moves towards the enemy base
// extends Entity for shared position and name
public class Mob extends Entity {

    private int hp;
    private int speed;
    private int damage; // amount of base hp deducted
    private int bounty; // reward on mob's death
    private int cost; // cost to queue

    // constructs a mob with all stats
    public Mob(String name, int row, int col,
               int hp, int speed, int damage, int bounty, int cost) {
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
        this.hp     = hp;
        this.speed  = speed;
        this.damage = damage;
        this.bounty = bounty;
        this.cost   = cost;
    }

    public int getHp() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getBounty() {
        return bounty;
    }

    public int getCost() {
        return cost;
    }

    public void setCol(int col) {
        this.col = col;
    }
    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public int getRow() {
        return this.row;
    }

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
