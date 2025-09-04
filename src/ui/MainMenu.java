package ui;

import main.Mediator;
import javax.swing.*;
import java.awt.*;


public class MainMenu {
    private Mediator mediator;

    public MainMenu(Mediator mediator) {
        this.mediator = mediator;
    }

    public void show() {
        System.out.println("Iniciando menu");

        ImageIcon icon = new ImageIcon("src/assets/logo.png");
        JFrame frame = new JFrame("InsecTD");
        frame.setIconImage(icon.getImage());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/assets/background.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);

                ImageIcon logo = new ImageIcon("src/assets/logo2.png"); // Replace with your logo file path
                int logoWidth = logo.getIconWidth() / 2;
                int logoHeight = logo.getIconHeight() / 2;
                int x = (getWidth() - logoWidth) / 2; 
                int y = (getHeight() - logoHeight) / 5; 
                g.drawImage(logo.getImage(), x, y, logoWidth, logoHeight, this);
            }
        };


        panel.setLayout(null);
        frame.add(panel);
        frame.setVisible(true);
    }

}
