package entities;

public class Projectile {
    private double x;
    private double y;
    private double targetX;
    private double targetY;
    private double speed;
    private boolean active;

    public Projectile(double startX, double startY, double targetX, double targetY) {
        this.x = startX;
        this.y = startY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.speed = 0.25;
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getTargetX() {
        return targetX;
    }

    public double getTargetY() {
        return targetY;
    }

    public double getSpeed() {
        return speed;
    }
}