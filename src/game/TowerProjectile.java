package game;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

public class TowerProjectile {
    private float x;
    private float y;
    private final float vx;
    private final float vy;
    private final int damage;
    private final Image image;
    private final int size = 24;
    private boolean expired = false;

    //angulo em radianos.
    private float rotationAngle = 0f; 

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
        this.image = tmp;
    }

    public void update(float delta, List<Enemy> enemies) {
        if (expired) return;
        x += vx * delta;
        y += vy * delta;

        // pronto nemo, tua sandalia tá rodando agora igual piao
        rotationAngle += 30f * delta; // ajustes pra ficar rapidin. quanto maior F, mais rapido a rotaçao

        for (Enemy e : enemies) {
            int ex = Math.round(e.getX()) - e.getType().width / 2;
            int ey = Math.round(e.getY()) - e.getType().height / 2;
            int ew = e.getType().width;
            int eh = e.getType().height;
            if (x >= ex && x <= ex + ew && y >= ey && y <= ey + eh) {
                e.damage(damage);
                expired = true;
                break;
            }
        }
    }

    public boolean isExpired() {
        return expired;
    }

    public void render(Graphics2D g) {
        int drawX = Math.round(x);
        int drawY = Math.round(y);
        int halfSize = size / 2;

        if (image != null) {
            // so salvando o estado original pra caso de bug
            AffineTransform originalTransform = g.getTransform();

            // isso deu trabalho, mas faz rotacionar a imagem
            g.rotate(rotationAngle, drawX, drawY);
            g.drawImage(image, drawX - halfSize, drawY - halfSize, size, size, null);

            // traz de volta o estado origininal pra num dar ruim
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
