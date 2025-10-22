package util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utilitário para carregar recursos (imagens) tanto em desenvolvimento quanto no JAR.
 * Durante desenvolvimento, tenta carregar de src/assets/, no JAR carrega de assets/.
 */
public class ResourceLoader {

    /**
     * Carrega uma imagem do classpath (assets/).
     * Funciona tanto em desenvolvimento quanto quando executado do JAR.
     */
    public static Image loadImage(String filename) {
        ImageIcon icon = loadImageIcon(filename);
        return icon != null ? icon.getImage() : null;
    }

    /**
     * Carrega um ImageIcon do classpath (assets/).
     * Funciona tanto em desenvolvimento quanto quando executado do JAR.
     */
    public static ImageIcon loadImageIcon(String filename) {
        String path = "/assets/" + filename;
        InputStream is = ResourceLoader.class.getResourceAsStream(path);
        if (is != null) {
            try {
                is.close();
                return new ImageIcon(ResourceLoader.class.getResource(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Fallback para desenvolvimento
        String devPath = "src/assets/" + filename;
        return new ImageIcon(devPath);
    }

    /**
     * Carrega um InputStream para um recurso.
     * Sempre usa o classpath (/assets/).
     */
    public static InputStream loadResourceAsStream(String filename) {
        String path = "/assets/" + filename;
        return ResourceLoader.class.getResourceAsStream(path);
    }
}
