package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoinIndicator {
    private float x;
    private float y;
    private final String text;
    private float lifetime;
    private final float maxLifetime = 1.2f;
    private final Color color;
    private final Color shadowColor;

    public CoinIndicator(float x, float y, int coins) {
        this.x = x;
        this.y = y;
        this.text = "+" + coins + "$";
        this.lifetime = maxLifetime;
        this.color = new Color(255, 215, 0);
        this.shadowColor = new Color(139, 90, 0);
    }

    public void update(float deltaSeconds) {
        lifetime -= deltaSeconds;
        y -= 40f * deltaSeconds;
    }

    public boolean isExpired() {
        return lifetime <= 0f;
    }

    public void render(Graphics2D g) {
        float alpha = Math.max(0f, Math.min(1f, lifetime / maxLifetime));
        Composite oldComp = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        Font old = g.getFont();
        Font f = old.deriveFont(Font.BOLD, 26f);
        g.setFont(f);

        FontMetrics fm = g.getFontMetrics();
        int tx = Math.round(x) - fm.stringWidth(text) / 2;
        int ty = Math.round(y);

        g.setColor(shadowColor);
        g.drawString(text, tx + 2, ty + 2);
        g.setColor(color);
        g.drawString(text, tx, ty);

        g.setFont(old);
        g.setComposite(oldComp);
    }

    public static class Manager {
        private final List<CoinIndicator> indicators = new ArrayList<>();

        public void add(float x, float y, int coins) {
            indicators.add(new CoinIndicator(x, y, coins));
        }

        public void update(float deltaSeconds) {
            Iterator<CoinIndicator> it = indicators.iterator();
            while (it.hasNext()) {
                CoinIndicator ind = it.next();
                ind.update(deltaSeconds);
                if (ind.isExpired()) {
                    it.remove();
                }
            }
        }

        public void render(Graphics2D g) {
            for (CoinIndicator ind : indicators) {
                ind.render(g);
            }
        }
    }
}