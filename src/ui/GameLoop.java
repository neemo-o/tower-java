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
        System.out.println("Início da Gameplay eu acho");

        JFrame frame = new JFrame("inferno");
        frame.setIconImage(new ImageIcon("src\\assets\\quadrado-padrao_702545-138_554x600.png").getImage());
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
        panel.setLayout(new GridBagLayout());
        frame.add(panel);
        frame.setVisible(true);
        // dai um temporizador que eu garimpei pra testar esse troço
       /*  new Timer(5000, e -> {
            mediator.notify(this, "endGame");
            frame.dispose();
        }).start(); */

        //funciona mas nao sei como fazer isso sair sem o temporizador. calma ae
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE) {
                    mediator.notify(GameLoop.this, "endGame");
                    frame.dispose();
                }
            }
        });
        };
    }
