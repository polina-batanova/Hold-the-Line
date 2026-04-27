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
        this.speed = 2.3;
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

    public void update() {
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= speed) {
            x = targetX;
            y = targetY;
            active = false;
            return;
        }

        x += (dx / dist) * speed;
        y += (dy / dist) * speed;
    }
    public double getAngle() {
        double dx = targetX - x;
        double dy = targetY - y;
        return Math.atan2(dy, dx);
    }
}