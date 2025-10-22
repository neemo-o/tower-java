package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DamageIndicator {
    private float x;
    private float y;
    private final String text;
    private float lifetime;
    private final float maxLifetime = 1.0f;
    private final Color color;

    public DamageIndicator(float x, float y, int damage, Color color) {
        this.x = x;
        this.y = y;
        this.text = "-" + damage;
        this.lifetime = maxLifetime;
        this.color = color;
    }

    public void update(float deltaSeconds) {
        lifetime -= deltaSeconds;
        y -= 30f * deltaSeconds;
    }

    public boolean isExpired() {
        return lifetime <= 0f;
    }

    public void render(Graphics2D g) {
        float alpha = Math.max(0f, Math.min(1f, lifetime / maxLifetime));
        Composite oldComp = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        Font old = g.getFont();
        Font f = old.deriveFont(Font.BOLD, 14f);
        g.setFont(f);
        
        FontMetrics fm = g.getFontMetrics();
        int tx = Math.round(x) - fm.stringWidth(text) / 2;
        int ty = Math.round(y);
        
        g.setColor(Color.BLACK);
        g.drawString(text, tx + 1, ty + 1);
        g.setColor(color);
        g.drawString(text, tx, ty);
        
        g.setFont(old);
        g.setComposite(oldComp);
    }

    public static class Manager {
        private final List<DamageIndicator> indicators = new ArrayList<>();

        public void add(float x, float y, int damage, Color color) {
            indicators.add(new DamageIndicator(x, y, damage, color));
        }

        public void update(float deltaSeconds) {
            Iterator<DamageIndicator> it = indicators.iterator();
            while (it.hasNext()) {
                DamageIndicator ind = it.next();
                ind.update(deltaSeconds);
                if (ind.isExpired()) {
                    it.remove();
                }
            }
        }

        public void render(Graphics2D g) {
            for (DamageIndicator ind : indicators) {
                ind.render(g);
            }
        }
    }
}