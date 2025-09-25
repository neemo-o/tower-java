package ui;
import main.Mediator;
import util.Sound;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GameLoop {
    private Mediator mediator;
    private float musicVolume = 0.5f;
    private Sound soundPlayer = new Sound();

    public GameLoop(Mediator mediator) {
        this.mediator = mediator;
    }
    public void show() {
        System.out.println("teste");

        JFrame frame = new JFrame("Ins");
        frame.setIconImage(new ImageIcon("src/assets/logo.png").getImage());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src\\assets\\quadrado-padrao_702545-138_554x600.png");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
    }
}