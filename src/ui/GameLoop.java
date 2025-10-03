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

    private JFrame frame;
    private GamePanel gamePanel;
    private Timer timer;

    public GameLoop(Mediator mediator) {
        this.mediator = mediator;
    }

    public void start() {
        frame = new JFrame("InsecTD - Jogo");
        frame.setIconImage(new ImageIcon("src/assets/logo.png").getImage());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setVisible(true);

        // Loop básico de jogo com Swing Timer (~60 FPS)
        int delayMs = 1000 / 60;
        timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
                gamePanel.repaint();
            }
        });
        timer.start();
    }

    private void update() {
        // Atualizações de lógica do jogo irão aqui (inputs, inimigos, projéteis, etc.)
    }

    private static class GamePanel extends JPanel {
        private final Image background;

        public GamePanel() {
            setDoubleBuffered(true);
            background = new ImageIcon("src/assets/GameCenario.png").getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) {
                g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
            }
            // Futuro: desenhar entidades, HUD, etc.
        }
    }
}
