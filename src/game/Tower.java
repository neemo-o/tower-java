package game;

import java.awt.*;
import java.util.List;

import javax.swing.ImageIcon;

public class Tower {
    public enum Tipo { NORMAL, AIR, FAST }

	public final Tipo tipo;
	public final Point position;
	public float rangeRadius;
	public int damage;
	public float fireRatePerSecond;
	public Image image;
    public int width = 32;
    public int height = 32;
    public String displayName;
    public int cost; // custo da torre

	private float fireCooldown;


	// TODO: ramon fazer a rotação do personagem ao atirar
    public Tower(Tipo tipo, Point position) {
		this.tipo = tipo;
		this.position = position;
		this.image = new ImageIcon("src/assets/tower_test.png").getImage();
		switch (tipo) {
			case NORMAL:
				this.rangeRadius = 140f;
				this.damage = 4;
				this.fireRatePerSecond = 1.1f;
                this.displayName = "Torre Normal";
                this.cost = 60;
				break;
			case AIR:
				this.rangeRadius = 130f;
				this.damage = 5;
				this.fireRatePerSecond = 0.8f;
                this.displayName = "Torre Aérea";
                this.cost = 100;
				break;
			case FAST:
				this.rangeRadius = 100f;
				this.damage = 2;
				this.fireRatePerSecond = 2.2f;
                this.displayName = "Torre Rápida";
                this.cost = 80;
				break;
		}
		this.fireCooldown = 0f;
	}

    
    public static int getCost(Tipo tipo) {
        if (tipo == null) return 0;
        switch (tipo) {
            case NORMAL: return 60;
            case AIR:    return 100;
            case FAST:   return 80;
            default:     return 0;
        }
    }

	public void update(float deltaSeconds, List<Enemy> enemies, List<TowerProjectile> outProjectiles) {
		fireCooldown -= deltaSeconds;
		if (fireCooldown > 0f) return;

		Enemy target = pickTarget(enemies);
		if (target != null) {
			float dirX = target.getX() - position.x;
			float dirY = target.getY() - position.y;
			float dist = (float)Math.sqrt(dirX*dirX + dirY*dirY);
			if (dist > 0f) {
				float vx = (dirX / dist) * 300f;
				float vy = (dirY / dist) * 300f;
				outProjectiles.add(new TowerProjectile(position.x, position.y, vx, vy, damage));
				fireCooldown = 1f / fireRatePerSecond;
			}
		}
	}

	private Enemy pickTarget(List<Enemy> enemies) {
		Enemy best = null;
		float bestDist = Float.MAX_VALUE;
		for (Enemy e : enemies) {
			if (!canTarget(e)) continue;
			float dx = e.getX() - position.x;
			float dy = e.getY() - position.y;
			float d2 = dx*dx + dy*dy;
			if (d2 <= rangeRadius * rangeRadius && d2 < bestDist) {
				best = e;
				bestDist = d2;
			}
		}
		return best;
	}

	private boolean canTarget(Enemy e) {
		if (tipo == Tipo.AIR) return true; // Torres aéreas podem atacar qualquer inimigo
		return e.getType().category == EnemyCategory.GROUND; // Torres normais e rápidas só atacam terrestres
	}

    public void render(Graphics2D g, boolean showRangeOutline) {
		int drawX = position.x - width/2;
		int drawY = position.y - height/2;
		if (image != null) {
			g.drawImage(image, drawX, drawY, width, height, null);
		} else {
            g.setColor(new Color(30, 144, 255));
            g.fillRect(drawX, drawY, width, height);
            g.setColor(Color.WHITE);
            g.drawRect(drawX, drawY, width, height);
		}
        if (showRangeOutline) {
            g.setColor(new Color(0, 200, 255));
            g.drawOval(Math.round(position.x - rangeRadius), Math.round(position.y - rangeRadius), Math.round(rangeRadius*2), Math.round(rangeRadius*2));
        }
	}

}


