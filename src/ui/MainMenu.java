package ui;

import main.Mediator;
import javax.swing.*;
import java.awt.event.*;
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

                ImageIcon logo = new ImageIcon("src/assets/logo2.png"); 
                int logoWidth = logo.getIconWidth() / 2;
                int logoHeight = logo.getIconHeight() / 2;
                int x = (getWidth() - logoWidth) / 2;
                int y = (getHeight() - logoHeight) / 14;
                g.drawImage(logo.getImage(), x, y, logoWidth, logoHeight, this);
            }
        };
        panel.setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);


        ImageIcon startNormal = new ImageIcon("src/assets/PlayBtn.png");
        ImageIcon startHover = new ImageIcon("src/assets/PlayClick.png"); 
        Image startImageHover = startHover.getImage().getScaledInstance(150, 60, Image.SCALE_SMOOTH); 
        ImageIcon scaledStartNormalHover = new ImageIcon(startImageHover);
        Image startImage = startNormal.getImage().getScaledInstance(150, 60, Image.SCALE_SMOOTH); 
        ImageIcon scaledStartNormal = new ImageIcon(startImage);
        JButton startButton = new JButton(scaledStartNormal);
        startButton.setRolloverIcon(scaledStartNormalHover);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setBounds(500, 300, 100, 40);
        startButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 1; 
        gbc.anchor = GridBagConstraints.CENTER; 
        panel.add(startButton, gbc);


        ImageIcon exitNormal = new ImageIcon("src/assets/ExitBtn.png");
        ImageIcon exitHover = new ImageIcon("src/assets/ExitClick.png"); 
        Image exitImageHover = exitHover.getImage().getScaledInstance(150, 60, Image.SCALE_SMOOTH); 
        ImageIcon scaledExitNormalHover = new ImageIcon(exitImageHover);
        Image exitImage = exitNormal.getImage().getScaledInstance(150, 60, Image.SCALE_SMOOTH); 
        ImageIcon scaledExitNormal = new ImageIcon(exitImage);
        JButton exitButton = new JButton(scaledExitNormal);
        exitButton.setRolloverIcon(scaledExitNormalHover);
        exitButton.setBorderPainted(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        gbc.gridy = 2; 
        panel.add(exitButton, gbc);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediator.startGame();
                frame.dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); 
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }

}
