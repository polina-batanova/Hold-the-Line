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
        // todo: implement validation and assignment
    }

    public int getRange() {
        // todo: implement
        return 0;
    }

    public int getDamage() {
        // todo: implement
        return 0;
    }

    public int getCost() {
        // todo: implement
        return 0;
    }

    // checks if mob in a tower's attack range
    public boolean isInRange(Mob mob) {
        // todo: implement
        return false;
    }
}
