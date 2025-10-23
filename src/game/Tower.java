package game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import util.Sound;
import util.ResourceLoader;

public class Tower {
    public enum Tipo {
        NORMAL, AIR, FAST
    }

    public final Tipo tipo;
    public final Point position;
    public float rangeRadius;
    public int damage;
    public float fireRatePerSecond;
    public Image image;
    public int width = 32;
    public int height = 32;
    public String displayName;
    public int cost;

    private float fireCooldown;
    private float rotationAngle = 0f;

    public Tower(Tipo tipo, Point position) {
        this.tipo = tipo;
        this.position = position;
        this.image = ResourceLoader.loadImage("tower_test.png");
        switch (tipo) {
            case NORMAL:
                this.rangeRadius = 150f;
                this.damage = 8;
                this.fireRatePerSecond = 0.9f;
                this.displayName = "Torre Normal";
                this.cost = 80;
                break;
            case AIR:
                this.rangeRadius = 130f;
                this.damage = 3;
                this.fireRatePerSecond = 1.4f;
                this.displayName = "Torre Aérea";
                this.cost = 90;
                break;
            case FAST:
                this.rangeRadius = 100f;
                this.damage = 2;
                this.fireRatePerSecond = 2.7f;
                this.displayName = "Torre Rápida";
                this.cost = 75;
                break;
        }
        this.fireCooldown = 0f;
    }

    public static int getCost(Tipo tipo) {
        if (tipo == null)
            return 0;
        switch (tipo) {
            case NORMAL:
                return 60;
            case AIR:
                return 100;
            case FAST:
                return 80;
            default:
                return 0;
        }
    }

    public void update(float deltaSeconds, List<Enemy> enemies, List<TowerProjectile> outProjectiles) {
        fireCooldown -= deltaSeconds;
        if (fireCooldown > 0f)
            return;

        Enemy target = pickTarget(enemies);
        if (target != null) {
            float dirX = target.getX() - position.x;
            float dirY = target.getY() - position.y;
            float dist = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            if (dist > 0f) {
                float vx = (dirX / dist) * 300f;
                float vy = (dirY / dist) * 300f;
                rotationAngle = (float) Math.atan2(dirY, dirX);
                outProjectiles.add(new TowerProjectile(position.x, position.y, vx, vy, damage));
                fireCooldown = 1f / fireRatePerSecond;

                Sound sound = new Sound();
                sound.play("projetil.wav", 0.45f);
            }
        }
    }

    private Enemy pickTarget(List<Enemy> enemies) {
        Enemy best = null;
        float bestDist = Float.MAX_VALUE;
        for (Enemy e : enemies) {
            if (!canTarget(e))
                continue;
            float dx = e.getX() - position.x;
            float dy = e.getY() - position.y;
            float d2 = dx * dx + dy * dy;
            if (d2 <= rangeRadius * rangeRadius && d2 < bestDist) {
                best = e;
                bestDist = d2;
            }
        }
        return best;
    }

    private boolean canTarget(Enemy e) {
        if (tipo == Tipo.AIR)
            return true;
        return e.getType().category == EnemyCategory.GROUND;
    }

    public void render(Graphics2D g, boolean showRangeOutline) {
        int drawX = position.x;
        int drawY = position.y;
        int halfW = width / 2;
        int halfH = height / 2;

        if (image != null) {
            AffineTransform original = g.getTransform();
            g.rotate(rotationAngle, drawX, drawY);
            g.drawImage(image, drawX - halfW, drawY - halfH, width, height, null);
            g.setTransform(original);
        } else {
            g.setColor(new Color(30, 144, 255));
            g.fillRect(drawX - halfW, drawY - halfH, width, height);
            g.setColor(Color.WHITE);
            g.drawRect(drawX - halfW, drawY - halfH, width, height);
        }

        if (showRangeOutline) {
            g.setColor(new Color(0, 200, 255));
            g.drawOval(Math.round(position.x - rangeRadius), Math.round(position.y - rangeRadius),
                    Math.round(rangeRadius * 2), Math.round(rangeRadius * 2));
        }
    }
}
