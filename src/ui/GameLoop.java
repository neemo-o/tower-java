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

        JFrame frame = new JFrame("INSECTD *-* ");
        frame.setIconImage(new ImageIcon("src\\assets\\background.png").getImage());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src\\assets\\BaseScreenGamming.jpeg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
                
            }  
        };
        panel.setLayout(new GridBagLayout());

        JButton exitScreen = createGameButton(
                normalPath:"src\\assets\\ExitButton.jpg",
                
                _ -> {
                    mediator.notify(this, "exitScreen");
                    frame.dispose();
                });
        frame.add(panel);
        frame.setVisible(true);

        private JButton createGameButton(String normalPath, String hoverPath, ActionListener event) {
        ImageIcon normalIcon = scaleIcon(loadIcon(normalPath), 150, 60);
        ImageIcon hoverIcon = scaleIcon(loadIcon(hoverPath), 150, 60);

        JButton button = new JButton(normalIcon);
        button.setRolloverIcon(hoverIcon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.addActionListener(event);
        return button;
    }

     private void addButtonsToPanel(JPanel panel, JButton exitScreen){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 1;
        panel.add(exitScreen, gbc);

    }

        // dai um temporizador que eu garimpei pra testar esse troço
       /*  new Timer(5000, e -> {
            mediator.notify(this, "endGame");
            frame.dispose();
        }).start(); */

        //funciona mas nao sei como fazer isso sair sem o temporizador. calma ae
        
                
          }
    }
