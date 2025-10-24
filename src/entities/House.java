package entities;

import java.awt.*;

import entities.indicators.DamageIndicator;
// representa a casa que o jogador deve proteger. tem os atributo basicos do local que os inimigos tentam chegar.
public class House {
    public Point position;
    public int maxHealth;
    public int health;
    public int barWidth = 200;
    public int barHeight = 10;
    public Color barBackColor = new Color(40, 40, 40, 200);
    public Color barFillColor = new Color(20, 200, 20, 220);
    public Color barBorderColor = Color.BLACK;

    public House(Point position, int maxHealth) {
        this.position = position;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public void damage(int amount) {
        health = Math.max(0, health - amount);
    }

    public DamageIndicator createDamageIndicator(int damage) {
        return new DamageIndicator(position.x, position.y - 40, damage, new Color(255, 50, 50));
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
    // desenha a casa e sua barra de vida. visual puro
    public void render(Graphics2D g) {
        int size = 20;
        int y = position.y - size / 2;

        int barX = position.x - barWidth / 4;
        int barY = y - 225;
        float ratio = Math.max(0f, Math.min(1f, health / (float) maxHealth));
        g.setColor(barBackColor);
        g.fillRect(barX, barY, barWidth, barHeight);
        g.setColor(barFillColor);
        g.fillRect(barX, barY, Math.round(barWidth * ratio), barHeight);
        g.setColor(barBorderColor);
        g.drawRect(barX, barY, barWidth, barHeight);
        String hpText = health + "/" + maxHealth + " HP";
        Font old = g.getFont();
        Font f = old.deriveFont(Font.BOLD, 12f);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int tx = barX + (barWidth - fm.stringWidth(hpText)) / 2;
        int ty = barY - 4;
        g.setColor(Color.BLACK);
        g.drawString(hpText, tx, ty);
        g.setFont(old);
    }
}