package model;

public class Player {
    private int health;
    private int money;
    private String name;

    public Player(String name, int health, int money) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (health < 0)  {
            throw new IllegalArgumentException("Health cannot be negative.");
        }

        if (money < 0) {
            throw new IllegalArgumentException("Money cannot be negative.");
        }

        this.name = name;
        this.health = health;
        this.money = money;
    }


    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }


    public int getMoney() {
        return money;
    }

    public void takeDamage(int dmg) {
        if (dmg < 0) {
            throw new IllegalArgumentException("Damage cannot be negative.");
        }



        health -= dmg;
        if (health < 0) {
            health =  0;
        }
    }

    public void addMoney(int amt) {
        if (amt < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        money += amt;
    }

    public boolean spendMoney(int amt) {
        if (amt < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        if (money  >= amt) {
            money -= amt;
            return true;
        }

        return false;
    }
}