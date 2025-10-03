package game;

import javax.swing.*;
import java.awt.*;

public class EnemyType {
    public final String name;
    public final int maxHealth;
    public final float velocidade;
    public final Image image;
    public final int width;
    public final int height;
    private boolean flipHorizontal = false;

    public EnemyType(String name, int maxHealth, float velocidade, String imagePath, int width, int height) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.velocidade = velocidade;
        ImageIcon icon = imagePath != null ? new ImageIcon(imagePath) : null;
        this.image = icon != null ? icon.getImage() : null;
        this.width = width;
        this.height = height;
    }

    public boolean isFlipHorizontal() {
        return flipHorizontal;
    }

    public EnemyType setFlipHorizontal(boolean flip) {
        this.flipHorizontal = flip;
        return this;
    }
}


