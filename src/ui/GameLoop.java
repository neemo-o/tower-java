package ui;
// classe principal do jogo, gerencia o loop principal, atualiza entidades e renderiza a tela
// jamais houve na historia, tanto import e private junto numa clase só tal qual nessa KKKK
import main.Mediator;
import util.Sound;
import util.ResourceLoader;

import javax.swing.*;

import entities.*;
import entities.enemy.Enemy;
import entities.indicators.CoinIndicator;
import entities.indicators.DamageIndicator;
import entities.tower.LightningEffect;
import entities.tower.Tower;
import entities.tower.TowerPreview;
import entities.tower.TowerProjectile;
import entities.wave.DefaultWaves;
import entities.wave.WaveController;

import java.awt.event.*;
import java.awt.*;
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
	private boolean transitioning = false;
	private float transitionTimer = 0f;
	private float transitionDuration = 2.0f;
	private int nextWaveNumber = 1;
	private Font waveIndicatorFont = new Font("Arial", Font.BOLD, 18);
	private Color waveIndicatorColor = Color.WHITE;
	private Font waveTransitionFont = new Font("Arial", Font.BOLD, 36);
	private Color waveTransitionColor = Color.YELLOW;
	private int money = 100;
	private Font moneyFont = new Font("Arial", Font.BOLD, 16);
	private Color moneyTextColor = Color.WHITE;
	private Color moneyShadowColor = Color.BLACK;
	private final java.util.List<Tower> towers = new ArrayList<>();
	private final java.util.List<TowerProjectile> projectiles = new ArrayList<>();
	private final java.util.List<LightningEffect> lightningEffects = new ArrayList<>();
	private Rectangle placementArea = new Rectangle(120, 250, 200, 150);
	private Tower.Tipo selectedTowerKind = null;
	private Point mousePos = new Point(0, 0);
	private Tower hoveredTower = null;
	private final Rectangle btnNormal = new Rectangle(10, 100, 90, 85);
	private final Rectangle btnAir = new Rectangle(110, 100, 90, 85);
	private final Rectangle btnFast = new Rectangle(210, 100, 90, 85);
	private TowerPreview currentPreview;
	private Rectangle selectedTowerBtn = null;
	private final DamageIndicator.Manager damageIndicators = new DamageIndicator.Manager();
	private final CoinIndicator.Manager coinIndicators = new CoinIndicator.Manager();
	Sound sound = new Sound();

	public GameLoop(Mediator mediator) {
		this.mediator = mediator;
	}
	// desenha o botao de interface para selecionar torres
	private void drawUIButton(Graphics2D g2, Rectangle r, Tower.Tipo tipo, boolean selected) {
		Color old = g2.getColor();
		Color btnColor = selected ? new Color(60, 120, 255) : new Color(40, 40, 40, 180);
		g2.setColor(btnColor);
		g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
		g2.setColor(Color.WHITE);
		g2.drawRoundRect(r.x, r.y, r.width, r.height, 8, 8);

		Tower temp = new Tower(tipo, new Point(0, 0));
		String label = temp.displayName;
		int cost = temp.cost;

		Font oldFont = g2.getFont();
		g2.setFont(new Font("Arial", Font.BOLD, 11));
		FontMetrics fm = g2.getFontMetrics();

		int tx = r.x + (r.width - fm.stringWidth(label)) / 2;
		int ty = r.y + 18;
		g2.setColor(Color.WHITE);
		g2.drawString(label, tx, ty);

		if (temp.image != null) {
			int iconSize = 24;
			int ix = r.x + (r.width - iconSize) / 2;
			int iy = r.y + 28;
			g2.drawImage(temp.image, ix, iy, iconSize, iconSize, null);
		}

		String costText = cost + " $";
		g2.setFont(new Font("Arial", Font.BOLD, 14));
		fm = g2.getFontMetrics();
		tx = r.x + (r.width - fm.stringWidth(costText)) / 2;
		ty = r.y + r.height - 15;

		Color costColor = money >= cost ? new Color(100, 255, 100) : new Color(255, 100, 100);
		g2.setColor(costColor);
		g2.drawString(costText, tx, ty);

		g2.setFont(oldFont);
		g2.setColor(old);
	}
	// verifica se a torre pode ser colocada na posicao especificada
	private boolean canPlaceTowerAt(Point p) {
		if (!placementArea.contains(p))
			return false;
		for (Tower t : towers) {
			double dist = p.distance(t.position);
			if (dist < 32)
				return false;
		}
		return true;
	}
	// inicia o loop principal do jogo
	public void start() {
		frame = new JFrame("InsecTD");
		frame.setIconImage(ResourceLoader.loadImage("logo.png"));
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
				if (e.getKeyCode() == KeyEvent.VK_1)
					selectedTowerKind = Tower.Tipo.NORMAL;
				else if (e.getKeyCode() == KeyEvent.VK_2)
					selectedTowerKind = Tower.Tipo.AIR;
				else if (e.getKeyCode() == KeyEvent.VK_3)
					selectedTowerKind = Tower.Tipo.FAST;
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					selectedTowerKind = null;
					selectedTowerBtn = null;
					currentPreview = null;
					if (gamePanel != null)
						gamePanel.repaint();
				}
			}
		});
		// gerencia o movimento do mouse para selecionar torres e mostrar preview. mas acho que isso é obvio né
		gamePanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos = e.getPoint();
				hoveredTower = null;
				for (Tower t : towers) {
					Rectangle r = new Rectangle(t.position.x - t.width / 2, t.position.y - t.height / 2, t.width,
							t.height);
					if (r.contains(mousePos)) {
						hoveredTower = t;
						break;
					}
				}

				if (selectedTowerKind != null) {
					currentPreview = new TowerPreview(selectedTowerKind, mousePos, canPlaceTowerAt(mousePos));
					gamePanel.repaint();
				} else {
					if (currentPreview != null) {
						currentPreview = null;
						gamePanel.repaint();
					}
				}
			}
		});
		// gerencia os cliques do mouse para colocar torres
		gamePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point p = e.getPoint();

				if (SwingUtilities.isRightMouseButton(e)) {
					selectedTowerKind = null;
					selectedTowerBtn = null;
					currentPreview = null;
					gamePanel.repaint();
					return;
				}

				if (!SwingUtilities.isLeftMouseButton(e))
					return;

				if (!btnNormal.contains(p) && !btnAir.contains(p) && !btnFast.contains(p)) {
					selectedTowerBtn = null;
				}

				if (btnNormal.contains(p)) {
					selectedTowerKind = Tower.Tipo.NORMAL;
					selectedTowerBtn = btnNormal;
					return;
				}
				if (btnAir.contains(p)) {
					selectedTowerKind = Tower.Tipo.AIR;
					selectedTowerBtn = btnAir;
					return;
				}
				if (btnFast.contains(p)) {
					selectedTowerKind = Tower.Tipo.FAST;
					selectedTowerBtn = btnFast;
					return;
				}

				if (selectedTowerKind != null && canPlaceTowerAt(p)) {
					int cost = Tower.getCost(selectedTowerKind);
					if (money >= cost) {
						money -= cost;
						sound.play("colocar_torre.wav", 0.6f);
						towers.add(new Tower(selectedTowerKind, p));
						selectedTowerKind = null;
						currentPreview = null;
						selectedTowerBtn = null;
					}
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
	// atualiza o estado do jogo a cada frame. tentando rodar ao maximo fps que der pra os pc nao morrer
	private void update() {
		long nowNs = System.nanoTime();
		float delta = lastFrameTimeNs == 0L ? 0f : (nowNs - lastFrameTimeNs) / 1_000_000_000f;
		lastFrameTimeNs = nowNs;

		if (waveController.isAllWavesFinished()) {
			timer.stop();
			sound.play("victory.wav", 0.7f);
			JOptionPane.showMessageDialog(frame, "Você venceu! Todas as ondas foram derrotadas.");
			mediator.notify(this, "MainMenu");
			frame.dispose();
		}

		if (!transitioning) {
			waveController.update(delta);
		}

		if (!transitioning) {
			for (Tower t : towers) {
				t.update(delta, waveController.getActiveEnemies(), projectiles, lightningEffects);
			}

			Iterator<TowerProjectile> it = projectiles.iterator();
			while (it.hasNext()) {
				TowerProjectile p = it.next();
				p.update(delta, waveController.getActiveEnemies());
				if (p.isExpired()) {
					Enemy hitEnemy = p.getHitEnemy();
					if (hitEnemy != null) {
						damageIndicators.add(hitEnemy.getX(), hitEnemy.getY(), p.getDamage(), new Color(255, 100, 100));
					}
					it.remove();
				}
			}

			Iterator<LightningEffect> lightningIt = lightningEffects.iterator();
			while (lightningIt.hasNext()) {
				LightningEffect lightning = lightningIt.next();
				lightning.update(delta);
				if (lightning.isExpired()) {
					lightningIt.remove();
				}
			}
		}

		damageIndicators.update(delta);
		coinIndicators.update(delta);

		int reached = waveController.consumeReachedCount();
		if (reached > 0) {
			house.damage(reached);
			damageIndicators.add(house.position.x, house.position.y - 40, reached, new Color(255, 50, 50));
			if (house.isDestroyed()) {
				sound.play("perder.wav", 0.7f);
				timer.stop();
				JOptionPane.showMessageDialog(frame, "Você perdeu! A casa foi destruída.");
				mediator.notify(this, "MainMenu");
				frame.dispose();
			}
		}

		int killed = waveController.consumeKilledCount();
		int reward = waveController.giveReward();
		if (killed > 0) {
			int totalReward = killed * reward;
			money += totalReward;
			sound.play("point-collect.wav", 0.5f);

			if (!waveController.getActiveEnemies().isEmpty()) {
				Enemy lastEnemy = waveController.getActiveEnemies().get(waveController.getActiveEnemies().size() - 1);
				coinIndicators.add(lastEnemy.getX(), lastEnemy.getY() - 5, totalReward);
			}

		}

		if (transitioning) {
			transitionTimer += delta;
			if (transitionTimer >= transitionDuration) {
				transitioning = false;
				waveController.startNextWave();
				if (waveController.getCurrentWaveNumber() >= 2) {
					money += 50; // ramon: bonus por iniciar nova wave TODO

					coinIndicators.add(frame.getHeight() / 2, frame.getWidth() / 2, 50);
				}

			}
		} else {
			if (waveController.isReadyForNextWave()) {
				transitioning = true;
				transitionTimer = 0f;
				nextWaveNumber = waveController.getCurrentWaveNumber() + 1;
			}
		}
	}
	// painel de jogo que lida com a renderizacao de todos os elementos visuais
	private class GamePanel extends JPanel {
		private final Image background;
		private final Image coinImage;
		private final WaveController waveControllerRef;
		private final House houseRef;
		private final GameLoop loopRef;

		public GamePanel(WaveController wc, House house, GameLoop loop) {
			setDoubleBuffered(true);
			background = ResourceLoader.loadImage("GameCenario.png");
			Image tmpCoin = null;
			try {
				Image img = ResourceLoader.loadImage("coin.png");
				tmpCoin = img;
			} catch (Exception ex) {
				tmpCoin = null;
			}
			this.coinImage = tmpCoin;
			this.waveControllerRef = wc;
			this.houseRef = house;
			this.loopRef = loop;
		}
		// renderiza todos os elementos do jogo na tela
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

			for (Tower t : loopRef.towers) {
				boolean showRange = (loopRef.hoveredTower == t);
				t.render(g2, showRange);
			}
			for (TowerProjectile p : loopRef.projectiles) {
				p.render(g2);
			}
			for (LightningEffect lightning : loopRef.lightningEffects) {
				lightning.render(g2);
			}

			loopRef.damageIndicators.render(g2);
			loopRef.coinIndicators.render(g2);

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
				g2.fillRect(loopRef.placementArea.x, loopRef.placementArea.y, loopRef.placementArea.width,
						loopRef.placementArea.height);
				g2.setColor(new Color(0, 150, 0));
				g2.drawRect(loopRef.placementArea.x, loopRef.placementArea.y, loopRef.placementArea.width,
						loopRef.placementArea.height);
				g2.setColor(oldC2);
			}

			Color oldUi = g2.getColor();
			Font oldFont = g2.getFont();
			drawUIButton(g2, btnNormal, Tower.Tipo.NORMAL, btnNormal == selectedTowerBtn);
			drawUIButton(g2, btnAir, Tower.Tipo.AIR, btnAir == selectedTowerBtn);
			drawUIButton(g2, btnFast, Tower.Tipo.FAST, btnFast == selectedTowerBtn);
			g2.setFont(oldFont);
			g2.setColor(oldUi);

			if (currentPreview != null) {
				currentPreview.render(g2);
			}

			if (loopRef.hoveredTower != null) {
				Tower t = loopRef.hoveredTower;
				String[] lines = buildTowerInfoLines(t);
				int pad = 6;
				FontMetrics fm = g2.getFontMetrics();
				int w = 0;
				for (String s : lines)
					w = Math.max(w, fm.stringWidth(s));
				int h = fm.getHeight() * lines.length + pad * 2;
				int x = loopRef.mousePos.x + 12;
				int y = loopRef.mousePos.y + 12;
				g2.setColor(new Color(0, 0, 0, 170));
				g2.fillRoundRect(x, y, w + pad * 2, h, 8, 8);
				g2.setColor(Color.WHITE);
				int ty = y + pad + fm.getAscent();
				for (String s : lines) {
					g2.drawString(s, x + pad, ty);
					ty += fm.getHeight();
				}
			}
		}
		// desenha o botao de interface para selecionar torres. o informativo necessario ae oh
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
