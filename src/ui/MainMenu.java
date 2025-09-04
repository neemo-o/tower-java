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

        JFrame frame = new JFrame("InsecTD");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/assets/background.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(null);
        frame.add(panel);
        frame.setVisible(true);
     }

        
}
