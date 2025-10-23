package entities.tower;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

import entities.enemy.Enemy;
import entities.enemy.EnemyCategory;
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
    public Image projectileImage;
    public int width = 45;
    public int height = 45;
    public String displayName;
    public int cost;

    private float fireCooldown;

    public Tower(Tipo tipo, Point position) {
        this.tipo = tipo;
        this.position = position;

        switch (tipo) {
            case NORMAL:
                this.rangeRadius = 150f;
                this.damage = 8;
                this.fireRatePerSecond = 0.9f;
                this.displayName = "Estilingue";
                this.cost = 80;
                this.image = ResourceLoader.loadImage("tower_pedra.png");
                this.projectileImage = ResourceLoader.loadImage("pedra.png");
                break;
            case AIR:
                this.rangeRadius = 130f;
                this.damage = 3;
                this.fireRatePerSecond = 1.4f;
                this.displayName = "Chinelada";
                this.cost = 90;
                this.image = ResourceLoader.loadImage("tower_test.png");
                this.projectileImage = ResourceLoader.loadImage("projectile.png");
                break;
            case FAST:
                this.rangeRadius = 120f;
                this.damage = 5;
                this.fireRatePerSecond = 3.0f;
                this.displayName = "Anti-Inseto";
                this.cost = 120;
                this.image = ResourceLoader.loadImage("tesla.png");
                this.projectileImage = null; // Usará efeito de raio
                break;
        }
        this.fireCooldown = 0f;
    }

    public static int getCost(Tipo tipo) {
        if (tipo == null)
            return 0;
        switch (tipo) {
            case NORMAL:
                return 80;
            case AIR:
                return 90;
            case FAST:
                return 120;
            default:
                return 0;
        }
    }

    public void update(float deltaSeconds, List<Enemy> enemies, List<TowerProjectile> outProjectiles, List<LightningEffect> outLightningEffects) {
        fireCooldown -= deltaSeconds;
        if (fireCooldown > 0f)
            return;

        Enemy target = pickTarget(enemies);
        if (target != null) {
            if (tipo == Tipo.FAST) {
                // Torre Anti-Inseto usa raios
                outLightningEffects.add(new LightningEffect(position, target, damage));
                target.damage(damage);
                fireCooldown = 1f / fireRatePerSecond;

                Sound sound = new Sound();
                sound.play("projetil.wav", 0.6f);
            } else {
                // Torres normais usam projétils
                float dirX = target.getX() - position.x;
                float dirY = target.getY() - position.y;
                float dist = (float) Math.sqrt(dirX * dirX + dirY * dirY);
                if (dist > 0f) {
                    float vx = (dirX / dist) * 300f;
                    float vy = (dirY / dist) * 300f;
                    outProjectiles.add(new TowerProjectile(position.x, position.y, vx, vy, damage, projectileImage));
                    fireCooldown = 1f / fireRatePerSecond;

                    Sound sound = new Sound();
                    sound.play("projetil.wav", 0.45f);
                }
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
