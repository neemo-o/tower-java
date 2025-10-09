package ui;

import main.Mediator;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import game.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GameLoop {
    private Mediator mediator;


    private JFrame frame;
    private GamePanel gamePanel;
    private Timer timer;
	private final WaveController waveController = new WaveController();
	private final House house = new House(new Point(365, 400), 100);
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
	

	// moeda do jogo
	private int money = 100; // ramon moeda inicial (caso precise balancear depois)
	private Font moneyFont = new Font("Arial", Font.BOLD, 16);
	private Color moneyTextColor = Color.WHITE;
	private Color moneyShadowColor = Color.BLACK;

	// Torres
	private final java.util.List<Tower> towers = new ArrayList<>();
	private final java.util.List<TowerProjectile> projectiles = new ArrayList<>();
	private Rectangle placementArea = new Rectangle(120, 250, 200, 150);
	private Tower.Tipo selectedTowerKind = null;
	private Point mousePos = new Point(0,0);
	private Tower hoveredTower = null;
	// UI botões
	private final Rectangle btnNormal = new Rectangle(10, 60, 90, 30);
	private final Rectangle btnAir = new Rectangle(110, 60, 90, 30);
	private final Rectangle btnFast = new Rectangle(210, 60, 90, 30);

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
		waveController.setTargetPoint(house.position);


		waveController.setWaves(DefaultWaves.create());
		waveController.setAutoAdvance(false);

		transitioning = true;
		nextWaveNumber = 1;
		transitionTimer = 0f;

		gamePanel = new GamePanel(waveController, house, this);
        frame.add(gamePanel);
        frame.setVisible(true);
		frame.setResizable(false);

		frame.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_1) selectedTowerKind = Tower.Tipo.NORMAL;
				else if (e.getKeyCode() == KeyEvent.VK_2) selectedTowerKind = Tower.Tipo.AIR;
				else if (e.getKeyCode() == KeyEvent.VK_3) selectedTowerKind = Tower.Tipo.FAST;
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) selectedTowerKind = null;
			}
		});
		gamePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();
				// clique em botões
				if (btnNormal.contains(p)) { selectedTowerKind = Tower.Tipo.NORMAL; return; }
				if (btnAir.contains(p)) { selectedTowerKind = Tower.Tipo.AIR; return; }
				if (btnFast.contains(p)) { selectedTowerKind = Tower.Tipo.FAST; return; }
				if (placementArea.contains(p)) {
					int cost = selectedTowerKind == Tower.Tipo.FAST ? 80 : (selectedTowerKind == Tower.Tipo.AIR ? 100 : 60);
					if (selectedTowerKind != null && money >= cost) {
						money -= cost;
						towers.add(new Tower(selectedTowerKind, p));
						selectedTowerKind = null; 
					}
				}
			}
		});
		gamePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos = e.getPoint();
				// detectar hover em torre
				hoveredTower = null;
				for (Tower t : towers) {
					Rectangle r = new Rectangle(t.position.x - t.width/2, t.position.y - t.height/2, t.width, t.height);
					if (r.contains(mousePos)) { hoveredTower = t; break; }
				}
			}
		});

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

		if (waveController.isAllWavesFinished()) {
			timer.stop();
			JOptionPane.showMessageDialog(frame, "sabe muito");
            mediator.notify(this, "MainMenu");
			frame.dispose();
		}


		if (!transitioning) {
			waveController.update(delta);
		}
		// atualizar torres e projéteis quando jogo rolando
		if (!transitioning) {
			for (Tower t : towers) {
				t.update(delta, waveController.getActiveEnemies(), projectiles);
			}
			Iterator<TowerProjectile> it = projectiles.iterator();
			while (it.hasNext()) {
				TowerProjectile p = it.next();
				p.update(delta, waveController.getActiveEnemies());
				if (p.isExpired()) it.remove();
			}
		}
		int reached = waveController.consumeReachedCount();
		if (reached > 0) {
			house.damage(reached);
			if (house.isDestroyed()) {
				timer.stop();
				JOptionPane.showMessageDialog(frame, "morreu. perdemo");
                mediator.notify(this, "MainMenu");
				frame.dispose();
			}
		}
		int killed = waveController.consumeKilledCount();
		int reward = waveController.giveReward();
		if (killed > 0) {
			money += killed * reward; 
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

	private class GamePanel extends JPanel {
		private final Image background;
		private final Image coinImage;
		private final WaveController waveControllerRef;
		private final House houseRef;
		private final GameLoop loopRef;

		public GamePanel(WaveController wc, House house, GameLoop loop) {
			setDoubleBuffered(true);
			background = new ImageIcon("src/assets/GameCenario.png").getImage();
			Image tmpCoin = null;
			try {
				Image img = new ImageIcon("src/assets/coin.png").getImage();
				tmpCoin = img;
			} catch (Exception ex) {
				tmpCoin = null;
			}
			this.coinImage = tmpCoin;
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

			// desenhar torres e projéteis
			for (Tower t : loopRef.towers) {
				boolean showRange = (loopRef.hoveredTower == t);
				t.render(g2, showRange);
			}
			for (TowerProjectile p : loopRef.projectiles) {
				p.render(g2);
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

			if (loopRef != null) {
				Font old = g2.getFont();
				g2.setFont(loopRef.moneyFont);
				FontMetrics fm = g2.getFontMetrics();
				String moneyStr = String.valueOf(loopRef.money);
				int padding = 10;
				int iconSize = 20;
				int gap = 6;
				int right = getWidth() - padding;
				int textW = fm.stringWidth(moneyStr);
				int iconX = right - textW - gap - iconSize;
				int iconY = padding;
				int textX = right - textW;
				int textY = padding + fm.getAscent();

				
				Color oldColor = g2.getColor();
				if (coinImage != null) {
					g2.drawImage(coinImage, iconX, iconY, iconSize, iconSize, this);
				}
				// texto com sombra leve
				g2.setColor(loopRef.moneyShadowColor);
				g2.drawString(moneyStr, textX + 1, textY + 1);
				g2.setColor(loopRef.moneyTextColor);
				g2.drawString(moneyStr, textX, textY);
				g2.setColor(oldColor);
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

			if (loopRef != null && loopRef.placementArea != null && loopRef.selectedTowerKind != null) {
				Color oldC2 = g2.getColor();
				g2.setColor(new Color(0, 255, 0, 60));
				g2.fillRect(loopRef.placementArea.x, loopRef.placementArea.y, loopRef.placementArea.width, loopRef.placementArea.height);
				g2.setColor(new Color(0, 150, 0));
				g2.drawRect(loopRef.placementArea.x, loopRef.placementArea.y, loopRef.placementArea.width, loopRef.placementArea.height);
				g2.setColor(oldC2);
			}

			Color oldUi = g2.getColor();
			Font oldFont = g2.getFont();
			g2.setFont(new Font("Arial", Font.BOLD, 12));
			drawUIButton(g2, btnNormal, "Normal", loopRef.selectedTowerKind == Tower.Tipo.NORMAL);
			drawUIButton(g2, btnAir, "Aérea", loopRef.selectedTowerKind == Tower.Tipo.AIR);
			drawUIButton(g2, btnFast, "Rápida", loopRef.selectedTowerKind == Tower.Tipo.FAST);
			g2.setFont(oldFont);
			g2.setColor(oldUi);

			// Tooltip da torre ao hover
			if (loopRef.hoveredTower != null) {
				Tower t = loopRef.hoveredTower;
				String[] lines = buildTowerInfoLines(t);
				int pad = 6;
				FontMetrics fm = g2.getFontMetrics();
				int w = 0; for (String s: lines) w = Math.max(w, fm.stringWidth(s));
				int h = fm.getHeight() * lines.length + pad * 2;
				int x = loopRef.mousePos.x + 12;
				int y = loopRef.mousePos.y + 12;
				g2.setColor(new Color(0,0,0,170));
				g2.fillRoundRect(x, y, w + pad*2, h, 8, 8);
				g2.setColor(Color.WHITE);
				int ty = y + pad + fm.getAscent();
				for (String s : lines) {
					g2.drawString(s, x + pad, ty);
					ty += fm.getHeight();
				}
			}
        }

		private void drawUIButton(Graphics2D g2, Rectangle r, String label, boolean selected) {
			Color old = g2.getColor();
			g2.setColor(selected ? new Color(60, 120, 255) : new Color(40, 40, 40, 180));
			g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
			g2.setColor(Color.WHITE);
			g2.drawRoundRect(r.x, r.y, r.width, r.height, 8, 8);
			FontMetrics fm = g2.getFontMetrics();
			int tx = r.x + (r.width - fm.stringWidth(label)) / 2;
			int ty = r.y + (r.height + fm.getAscent()) / 2 - 3;
			g2.drawString(label, tx, ty);
			g2.setColor(old);
		}

		private String[] buildTowerInfoLines(Tower t) {
			return new String[] {
				(t.displayName != null ? t.displayName : "Torre"),
				"Dano: " + t.damage,
				"Velocidade de Ataque: " + String.format("%.2f/s", t.fireRatePerSecond),
				"Alcance: " + Math.round(t.rangeRadius)
			};
		}
	}
}
