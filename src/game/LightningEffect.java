package game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightningEffect {
    private final Point start;
    private final Point end;
    private final int damage;
    private float lifetime = 0.3f; // Duração do efeito em segundos
    private float age = 0f;
    private final Color color = new Color(100, 200, 255, 200);

    public LightningEffect(Point start, Enemy target, int damage) {
        this.start = new Point(start);
        this.end = new Point((int)target.getX(), (int)target.getY());
        this.damage = damage;
    }

    public void update(float deltaSeconds) {
        age += deltaSeconds;
    }

    public boolean isExpired() {
        return age >= lifetime;
    }

    public void render(Graphics2D g) {
        if (isExpired()) return;

        float alpha = 1.0f - (age / lifetime);
        Color currentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                                     (int)(color.getAlpha() * alpha));

        g.setColor(currentColor);
        g.setStroke(new BasicStroke(3.0f));

        List<Point> lightningPoints = generateLightningPath(start, end, 8);

        for (int i = 0; i < lightningPoints.size() - 1; i++) {
            Point p1 = lightningPoints.get(i);
            Point p2 = lightningPoints.get(i + 1);

            g.setStroke(new BasicStroke(4.0f));
            g.setColor(new Color(255, 255, 255, (int)(150 * alpha)));
            g.drawLine(p1.x, p1.y, p2.x, p2.y);

            g.setStroke(new BasicStroke(2.0f));
            g.setColor(currentColor);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        drawLightningNode(g, start, alpha);
        drawLightningNode(g, end, alpha);
    }

    private List<Point> generateLightningPath(Point start, Point end, int segments) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(start));

        float dx = (end.x - start.x) / (float) segments;
        float dy = (end.y - start.y) / (float) segments;

        for (int i = 1; i < segments; i++) {
            float x = start.x + dx * i;
            float y = start.y + dy * i;

            float offsetX = (float) (Math.sin(i * 0.5) * 15 * Math.random());
            float offsetY = (float) (Math.cos(i * 0.3) * 15 * Math.random());

            points.add(new Point((int)(x + offsetX), (int)(y + offsetY)));
        }

        points.add(new Point(end));
        return points;
    }

    private void drawLightningNode(Graphics2D g, Point center, float alpha) {
        int radius = 8;
        Color nodeColor = new Color(255, 255, 255, (int)(255 * alpha));

        g.setColor(nodeColor);
        g.fillOval(center.x - radius/2, center.y - radius/2, radius, radius);

        g.setColor(new Color(100, 200, 255, (int)(200 * alpha)));
        g.drawOval(center.x - radius/2, center.y - radius/2, radius, radius);
    }

    public int getDamage() {
        return damage;
    }
}
