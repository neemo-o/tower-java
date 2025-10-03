package ui;

import main.Mediator;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import game.*;

public class GameLoop {
    private Mediator mediator;


    private JFrame frame;
    private GamePanel gamePanel;
    private Timer timer;
	private final WaveController waveController = new WaveController();
	private final House house = new House(new Point(365, 400), 350);
	private long lastFrameTimeNs = 0L;
	// Wave UI e transição
	private boolean transitioning = false;
	private float transitionTimer = 0f;
	private float transitionDuration = 2.0f; // segundos
	private int nextWaveNumber = 1;
	private Font waveIndicatorFont = new Font("Arial", Font.BOLD, 18);
	private Color waveIndicatorColor = Color.WHITE;
	private Font waveTransitionFont = new Font("Arial", Font.BOLD, 36);
	private Color waveTransitionColor = Color.YELLOW;

    public GameLoop(Mediator mediator) {
        this.mediator = mediator;
    }

    public void start() {
        frame = new JFrame("InsecTD");
        frame.setIconImage(new ImageIcon("src/assets/logo.png").getImage());
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

		waveController.setSpawnPoint(0, new Point(40, 560));
		waveController.setSpawnPoint(1, new Point(10, 400));
		waveController.setTargetPoint(house.position);


		waveController.setWaves(DefaultWaves.create());
		waveController.setAutoAdvance(false);

		transitioning = true;
		nextWaveNumber = 1;
		transitionTimer = 0f;

		gamePanel = new GamePanel(waveController, house, this);
        frame.add(gamePanel);
        frame.setVisible(true);

		int delayMs = 1000 / 60;
        timer = new Timer(delayMs, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				update();
                gamePanel.repaint();
            }
        });
        timer.start();
		lastFrameTimeNs = System.nanoTime();
    }

    private void update() {
		long nowNs = System.nanoTime();
		float delta = lastFrameTimeNs == 0L ? 0f : (nowNs - lastFrameTimeNs) / 1_000_000_000f;
		lastFrameTimeNs = nowNs;

		if (!transitioning) {
			waveController.update(delta);
		}
		int reached = waveController.consumeReachedCount();
		if (reached > 0) {
			house.damage(reached);
			if (house.isDestroyed()) {
				timer.stop();
				JOptionPane.showMessageDialog(frame, "Game Over.");
                mediator.notify(this, "gameOver");
			}
		}

		if (transitioning) {
			transitionTimer += delta;
			if (transitionTimer >= transitionDuration) {
				transitioning = false;
				waveController.startNextWave();
			}
		} else {
			if (waveController.isReadyForNextWave()) {
				transitioning = true;
				transitionTimer = 0f;
				nextWaveNumber = waveController.getCurrentWaveNumber() + 1;
			}
		}
	}

	private static class GamePanel extends JPanel {
		private final Image background;
		private final WaveController waveControllerRef;
		private final House houseRef;
		private final GameLoop loopRef;

		public GamePanel(WaveController wc, House house, GameLoop loop) {
			setDoubleBuffered(true);
			background = new ImageIcon("src/assets/GameCenario.png").getImage();
			this.waveControllerRef = wc;
			this.houseRef = house;
			this.loopRef = loop;
		}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
			if (background != null) {
				g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
			}

			Graphics2D g2 = (Graphics2D) g;
			if (houseRef != null) {
				houseRef.render(g2);
			}
			if (waveControllerRef != null) {
				for (Enemy e : waveControllerRef.getActiveEnemies()) {
					e.render(g2);
				}
			}

			// animacao para waves
			if (loopRef != null && waveControllerRef != null) {
				String indicator = "Wave " + Math.max(1, waveControllerRef.getCurrentWaveNumber());
				Font old = g2.getFont();
				g2.setFont(loopRef.waveIndicatorFont);
				FontMetrics fm = g2.getFontMetrics();
				int tx = (getWidth() - fm.stringWidth(indicator)) / 2;
				int ty = 30;
				g2.setColor(Color.BLACK);
				g2.drawString(indicator, tx + 1, ty + 1);
				g2.setColor(loopRef.waveIndicatorColor);
				g2.drawString(indicator, tx, ty);
				g2.setFont(old);
			}

			
			if (loopRef != null && loopRef.transitioning) {
				float t = Math.min(1f, loopRef.transitionTimer / loopRef.transitionDuration);
				float alpha = 1f - Math.abs(2 * t - 1f); 
				Composite oldC = g2.getComposite();
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, Math.min(1f, alpha))));
				String title = "Wave " + loopRef.nextWaveNumber;
				Font old = g2.getFont();
				g2.setFont(loopRef.waveTransitionFont);
				FontMetrics fm = g2.getFontMetrics();
				int tx = (getWidth() - fm.stringWidth(title)) / 2;
				int ty = getHeight() / 2 - fm.getHeight();
				g2.setColor(Color.BLACK);
				g2.drawString(title, tx + 2, ty + 2);
				g2.setColor(loopRef.waveTransitionColor);
				g2.drawString(title, tx, ty);
				g2.setFont(old);
				g2.setComposite(oldC);
			}
        }
    }
}
