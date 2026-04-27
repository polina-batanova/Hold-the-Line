package entities;

// represents placed defensive structure
// extends Entity for shared position and name
public class Tower extends Entity {

    public static final int MAX_LEVEL = 3;

    private int range; // attack range
    private int damage; // dmg per hit
    private int cost; // cost to place
    private int level; // upgrade level, starts at 1
    private int totalInvested;
    private int attackCooldown;  // ticks between shots
    private int cooldownTimer;

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
        this.totalInvested = cost;
        this.level  = 1;
        this.attackCooldown = 2;
        this.cooldownTimer = 0;
    }
    public int getTotalInvested() {
        return totalInvested;
    }

    public int getSellRefund() {
        return totalInvested / 2;
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

    public int getLevel() {
        return level;
    }

    // locked mob
    private Mob currentTarget;
    // returns focused mob
    public Mob getCurrentTarget() {
        return currentTarget;
    }
    // sets new target
    public void setCurrentTarget(Mob target) {
        this.currentTarget = target;
    }
    // true if target is alive and in range
    public boolean hasValidTarget() {
        return currentTarget != null
                && !currentTarget.isDead()
                && isInRange(currentTarget);
    }

    // true when tower has reached the final upgrade level
    public boolean isMaxLevel() {
        return level >= MAX_LEVEL;
    }

    // to reach the next level player needs gold
    public int getUpgradeCost() {
        if (isMaxLevel()) {
            throw new IllegalStateException("Tower is already at max level.");
        }
        return level * 75;
    }

    public boolean canFire() {
        return cooldownTimer <= 0;
    }
    public void resetCooldown() {
        cooldownTimer = attackCooldown;
    }
    public void tickCooldown() {
        if (cooldownTimer > 0) cooldownTimer--;
    }
    public int getAttackCooldown() {
        return attackCooldown;
    }


    public void upgrade() {
        if (isMaxLevel()) {
            throw new IllegalStateException("Tower is already at max level.");
        }

        int upgradeCost = getUpgradeCost();
        totalInvested += upgradeCost;

        level++;

        if (level == 2) {
            this.damage += 3;
            this.attackCooldown = 1;
        } else if (level == 3) {
            this.damage += 4;
            this.range += 1;
            this.attackCooldown = 0;
        }
    }

    // checks if mob in a tower's attack range
    public boolean isInRange(Mob mob) {
        double distance = Math.hypot(this.row - mob.getRow(), this.col - mob.getCol());
        return distance <= range;
    }
}
