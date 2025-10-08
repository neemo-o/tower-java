package game;

import javax.swing.*;
import java.awt.*;

public class TowerProjectile {
	private float x;
	private float y;
	private final float vx;
	private final float vy;
	private final int damage;
	private final Image image;
    private final int size = 8;
	private boolean expired = false;

	public TowerProjectile(float x, float y, float vx, float vy, int damage) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.damage = damage;
		Image tmp;
		try {
			tmp = new ImageIcon("src/assets/projectile.png").getImage();
		} catch (Exception e) {
			tmp = null;
		}
		this.image = null;
	}

	public void update(float delta, java.util.List<Enemy> enemies) {
		if (expired) return;
		x += vx * delta;
		y += vy * delta;
		for (Enemy e : enemies) {
			int ex = Math.round(e.getX()) - e.getType().width/2;
			int ey = Math.round(e.getY()) - e.getType().height/2;
			int ew = e.getType().width;
			int eh = e.getType().height;
			if (x >= ex && x <= ex + ew && y >= ey && y <= ey + eh) {
				e.damage(damage);
				expired = true;
				break;
			}
		}
	}

	public boolean isExpired() { return expired; }

	public void render(Graphics2D g) {
		int drawX = Math.round(x) - size/2;
		int drawY = Math.round(y) - size/2;
        if (image != null) {
            g.drawImage(image, drawX, drawY, size, size, null);
        } else {
            g.setColor(Color.YELLOW);
            int dot = 10;
            g.fillOval(Math.round(x) - dot/2, Math.round(y) - dot/2, dot, dot);
            g.setColor(Color.ORANGE);
            g.drawOval(Math.round(x) - dot/2, Math.round(y) - dot/2, dot, dot);
        }
	}
}


