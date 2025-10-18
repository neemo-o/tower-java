package game;

import java.awt.*;

public class TowerPreview {
    private final Point position;
    private final float range;
    private final boolean canPlace;
    private final Image towerImage;
    private final int width;
    private final int height;
    
    public TowerPreview(Tower.Tipo type, Point position, boolean canPlace) {
        this.towerType = type;
        this.position = position;
        this.canPlace = canPlace;
        
        Tower temp = new Tower(type, new Point(position));
        this.range = temp.rangeRadius;
        this.towerImage = temp.image;
        this.width = temp.width;
        this.height = temp.height;
    }
    
    public void render(Graphics2D g) {
        Color rangeColor = canPlace ? new Color(0, 200, 0, 60) : new Color(200, 0, 0, 60);
        Composite oldComp = g.getComposite();
        Color oldColor = g.getColor();
        g.setColor(rangeColor);
        g.fillOval(
            Math.round(position.x - range),
            Math.round(position.y - range),
            Math.round(range * 2),
            Math.round(range * 2)
        );

        float alpha = 0.85f;
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        int drawX = position.x - width/2;
        int drawY = position.y - height/2;
        if (towerImage != null) {
            g.drawImage(towerImage, drawX, drawY, width, height, null);
        } else {
            g.setColor(new Color(220,220,220,200));
            g.fillRect(drawX, drawY, width, height);
            g.setColor(Color.BLACK);
            g.drawRect(drawX, drawY, width, height);
        }

        if (!canPlace) {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
            g.setColor(new Color(200, 0, 0, 180));
            g.fillRect(drawX, drawY, width, height);
        }

        // restaurar estado gráfico
        g.setComposite(oldComp);
        g.setColor(oldColor);
    }
}
