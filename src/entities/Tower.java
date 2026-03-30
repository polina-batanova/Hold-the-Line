package entities;

// represents placed defensive structure
// extends Entity for shared position and name
public class Tower extends Entity {

    private int range; // attack range
    private int damage; // dmg per hit
    private int cost; // cost to place

    // constructs a tower with all stats
    public Tower(String name, int row, int col, int range, int damage, int cost) {
        super(name, row, col);
        if (range <= 0) {
            throw new IllegalArgumentException("Tower range must be greater than 0.");
        }
        if (damage <= 0) {
            throw new IllegalArgumentException("Tower damage must be greater than 0.");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Tower cost cannot be negative.");
        }
        this.range  = range;
        this.damage = damage;
        this.cost   = cost;
    }

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public int getCost() {
        return cost;
    }

    // checks if mob in a tower's attack range
    public boolean isInRange(Mob mob) {
        double distance = Math.hypot(this.row - mob.getRow(), this.col - mob.getCol());
        return distance <= range;
    }
}
