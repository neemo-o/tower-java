package game;

import java.awt.*;

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

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void render(Graphics2D g) {
        // Desenhar ponto da casa
        int size = 20;
        int y = position.y - size / 2;
        

        // Barra de vida acima
        int barX = position.x - barWidth / 2;
        int barY = y - 125;
        float ratio = Math.max(0f, Math.min(1f, health / (float) maxHealth));
        g.setColor(barBackColor);
        g.fillRect(barX, barY, barWidth, barHeight);
        g.setColor(barFillColor);
        g.fillRect(barX, barY, Math.round(barWidth * ratio), barHeight);
        g.setColor(barBorderColor);
        g.drawRect(barX, barY, barWidth, barHeight);
    }
}


