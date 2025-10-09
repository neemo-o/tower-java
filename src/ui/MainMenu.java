package ui;

import main.Mediator;
import util.Sound;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class MainMenu {
    private Mediator mediator;
    private float musicVolume = 0.5f;
    private Sound soundPlayer = new Sound();

    public MainMenu(Mediator mediator) {
        this.mediator = mediator;
    }

    public void show() {
        System.out.println("TesteMenu");

        JFrame frame = new JFrame("InsecTD");
        frame.setIconImage(new ImageIcon("src/assets/logo.png").getImage());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

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

        JButton startButton = createMenuButton(
                "src/assets/PlayBtn.png",
                "src/assets/PlayClick.png",
                _ -> {
                    mediator.notify(this, "startGame");
                    frame.dispose();
                });

        JButton exitButton = createMenuButton(
                "src/assets/ExitBtn.png",
                "src/assets/ExitClick.png",
                _ -> {
                    System.exit(0);
                });

        addButtonsToPanel(panel, startButton, exitButton);

        frame.add(panel);
        frame.setVisible(true);

        soundPlayer.playLoop("menu-music.wav", musicVolume);
        System.out.println("Menu iniciado");
    }

    private JButton createMenuButton(String normalPath, String hoverPath, ActionListener event) {
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

    private void addButtonsToPanel(JPanel panel, JButton startButton, JButton exitButton) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 10, 2, 10);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 1;
        panel.add(startButton, gbc);

        gbc.gridy = 2;
        panel.add(exitButton, gbc);
    }

    private ImageIcon loadIcon(String path) {
        return new ImageIcon(path);
    }

    private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
}
