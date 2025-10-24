package entities.enemy;

import javax.swing.*;
import java.awt.*;
import util.ResourceLoader;
// organizaçao dos tipos de inimigos e suas caracteristicas
public class EnemyType {
    public final String name;
    public final int maxHealth;
    public final float velocidade;
    public final Image image;
    public final int width;
    public final int height;
    public final EnemyCategory category;
    public final int reward;
    private boolean flipHorizontal = false;

    public EnemyType(String name, int maxHealth, float velocidade, String imagePath, int width, int height) {
        this(name, maxHealth, velocidade, imagePath, width, height, EnemyCategory.GROUND, 10);
    }

    public EnemyType(String name, int maxHealth, float velocidade, String imagePath, int width, int height, EnemyCategory category) {
        this(name, maxHealth, velocidade, imagePath, width, height, category, 10);
    }

    public EnemyType(String name, int maxHealth, float velocidade, String imagePath, int width, int height, EnemyCategory category, int reward) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.velocidade = velocidade;
        ImageIcon icon = imagePath != null ? ResourceLoader.loadImageIcon(imagePath) : null;
        this.image = icon != null ? icon.getImage() : null;
        this.width = width;
        this.height = height;
        this.category = category;
        this.reward = reward;
    }

    public boolean isFlipHorizontal() {
        return flipHorizontal;
    }

    public EnemyType setFlipHorizontal(boolean flip) {
        this.flipHorizontal = flip;
        return this;
    }
}
