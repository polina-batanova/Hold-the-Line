package entities;

// represents unit that moves towards the enemy base
// extends Entity for shared position and name
public class Mob extends Entity {

    private int hp;
    private int speed;
    private int damage; // amount of base hp deducted
    private int bounty; // reward on mob death
    private int cost; // cost to queue mob

    // constructs a mob with all stats
    public Mob(String name, int row, int col,
               int hp, int speed, int damage, int bounty, int cost) {
        super(name, row, col);
        // todo: implement validation and assignment
    }

    public int getHp() {
        // todo: implement
        return 0;
    }

    public int getSpeed() {
        // todo: implement
        return 0;
    }

    public int getDamage() {
        // todo: implement
        return 0;
    }

    public int getBounty() {
        // todo: implement
        return 0;
    }

    public int getCost() {
        // todo: implement
        return 0;
    }

    // reduces mob's hp by the given amount
    public void takeDamage(int amount) {
        // todo: implement
    }

    // true if mob's hp <=0
    public boolean isDead() {
        // todo: implement
        return false;
    }
}
