package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import util.ResourceLoader;

public class TowerProjectile {
    private float x;
    private float y;
    private final float vx;
    private final float vy;
    private final int damage;
    private final Image image;
    private final int size = 24;
    private boolean expired = false;
    private float rotationAngle = 0f;
    private Enemy hitEnemy = null;

    public TowerProjectile(float x, float y, float vx, float vy, int damage) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
        Image tmp;
        try {
            tmp = ResourceLoader.loadImage("projectile.png");
        } catch (Exception e) {
            tmp = null;
        }
        this.image = tmp;
    }

    public void update(float delta, List<Enemy> enemies) {
        if (expired)
            return;
        x += vx * delta;
        y += vy * delta;
        rotationAngle += 30f * delta;

        for (Enemy e : enemies) {
            int ex = Math.round(e.getX()) - e.getType().width / 2;
            int ey = Math.round(e.getY()) - e.getType().height / 2;
            int ew = e.getType().width;
            int eh = e.getType().height;
            if (x >= ex && x <= ex + ew && y >= ey && y <= ey + eh) {
                e.damage(damage);
                hitEnemy = e;
                expired = true;
                break;
            }
        }
    }

    public boolean isExpired() {
        return expired;
    }

    public Enemy getHitEnemy() {
        return hitEnemy;
    }

    public int getDamage() {
        return damage;
    }

    public void render(Graphics2D g) {
        int drawX = Math.round(x);
        int drawY = Math.round(y);
        int halfSize = size / 2;

        if (image != null) {
            AffineTransform originalTransform = g.getTransform();
            g.rotate(rotationAngle, drawX, drawY);
            g.drawImage(image, drawX - halfSize, drawY - halfSize, size, size, null);
            g.setTransform(originalTransform);
        } else {
            g.setColor(Color.YELLOW);
            int dot = 10;
            g.fillOval(drawX - dot / 2, drawY - dot / 2, dot, dot);
            g.setColor(Color.ORANGE);
            g.drawOval(drawX - dot / 2, drawY - dot / 2, dot, dot);
        }
    }
}
