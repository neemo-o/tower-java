package game;

import java.awt.*;

public class Enemy {
    private final EnemyType type;
    private float x;
    private float y;
    private int health;
    private boolean reachedTarget;

    public Enemy(EnemyType type, float startX, float startY) {
        this.type = type;
        this.x = startX;
        this.y = startY;
        this.health = type.maxHealth;
        this.reachedTarget = false;
    }

    // um pouco bugado, mas funciona
    public void updateTowards(Point target, float deltaSeconds) {
        if (reachedTarget) return;
        float dx = target.x - x;
        float dy = target.y - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 1f) {
            reachedTarget = true;
            return;
        }
        float vx = (dx / dist) * type.velocidade;
        float vy = (dy / dist) * type.velocidade;
        x += vx * deltaSeconds;
        y += vy * deltaSeconds;
    }

    public void render(Graphics2D g) {
        int drawX = Math.round(x) - type.width / 2;
        int drawY = Math.round(y) - type.height / 2;
        if (type.image != null) {
            g.drawImage(type.image, drawX, drawY, type.width, type.height, null);
        } else {
            g.setColor(Color.RED);
            g.fillOval(drawX, drawY, type.width, type.height);
        }


        // vida
        float healthRatio = Math.max(0f, Math.min(1f, health / (float) type.maxHealth));
        int barW = type.width;
        int barH = 4;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(drawX, drawY - 6, barW, barH);
        g.setColor(Color.GREEN);
        g.fillRect(drawX, drawY - 6, Math.round(barW * healthRatio), barH);
        g.setColor(Color.BLACK);
        g.drawRect(drawX, drawY - 6, barW, barH);
    }

    public boolean hasReachedTarget() { return reachedTarget; }
    public void markReached() { reachedTarget = true; }
    public int getHealth() { return health; }
    public void damage(int amount) { health = Math.max(0, health - amount); }
    public boolean isDead() { return health <= 0; }
    public float getX() { return x; }
    public float getY() { return y; }
}


