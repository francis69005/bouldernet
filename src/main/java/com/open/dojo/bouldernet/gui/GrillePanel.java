package com.open.dojo.bouldernet.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.open.dojo.bouldernet.BoulderCell;
import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.DirectionEnum;
import com.open.dojo.bouldernet.gui.themes.ThemeManager;

public class GrillePanel extends JPanel implements MapListener {

	private static final long serialVersionUID = 1L;
	
	private static final int CELL_WIDTH = 32;
	private static final int CELL_HEIGHT = 32;
	private static final int HEADER_HEIGHT = 64;

	private BoulderMapProxy grille;
	
	private ClientMapModel map = null;
	
	private int diamondCounter = 0;
	
	private int deathCounter = 0;

	private ThemeManager themeManager;
	
	private Map<Integer, Image[]> playerImageCache = new HashMap<Integer, Image[]>();
	
	public GrillePanel(BoulderMapProxy grille, ThemeManager themeManager) throws IOException {
		this.themeManager = themeManager;
		this.grille = grille;
		this.grille.addMapListener(this);
		setOpaque(true);
		setBackground(Color.BLACK);
		
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				final DirectionEnum direction;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					direction = DirectionEnum.LEFT;
					break;
					
				case KeyEvent.VK_RIGHT:
					direction = DirectionEnum.RIGHT;
					break;
					
				case KeyEvent.VK_DOWN:
					direction = DirectionEnum.DOWN;
					break;
					
				case KeyEvent.VK_UP:
					direction = DirectionEnum.UP;
					break;
				default:
					return;
				}
				GrillePanel.this.grille.move(direction);
				GrillePanel.this.repaint();
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Rectangle rect = new Rectangle(getWidth() - 50-4, 16-4, 32+4*2, 32+4*2);
				if (rect.contains(e.getX(), e.getY())) {
					JFrame frame = (JFrame) SwingUtilities.getRoot(GrillePanel.this);
					OptionDialog dialog = new OptionDialog(frame, GrillePanel.this.themeManager);
					dialog.setLocationRelativeTo(frame);
					dialog.setVisible(true);
					playerImageCache.clear();
				}
			}
		});
		setFocusable(true);
	}
	
	@Override
	public Dimension getPreferredSize() {
		if (this.map == null) {
			return new Dimension(300, 300);
		}
		return new Dimension(
				CELL_WIDTH * map.getWidth(),
				HEADER_HEIGHT + CELL_HEIGHT * map.getHeight());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (map == null) {
			g.setColor(Color.WHITE);
			g.drawString("Not Connected",
					(int)getBounds().getCenterX(), (int)getBounds().getCenterY());
			return;
		}
		drawHeader(g);
		for (int y = 0; y < map.getHeight(); ++y) {
			for (int x = 0; x < map.getWidth(); ++x) {
				BoulderCellEnum cell = map.getCell(x, y);
				
				if (cell.isPerson()) {
					
					int playerId = map.getPlayerId(new BoulderCell(x, y));
					Image coloredImage = getCachedPlayerImage(playerId, cell);
				    g.drawImage(coloredImage, x*CELL_WIDTH, HEADER_HEIGHT + y*CELL_HEIGHT, this);
				} else {
					g.drawImage(themeManager.getIcon(cell), x*CELL_WIDTH, HEADER_HEIGHT + y*CELL_HEIGHT, this);
				}
			}
		}
	}

	private Image getCachedPlayerImage(int playerId, BoulderCellEnum cell) {
		Image[] cachedImages = playerImageCache.get(playerId);
		if (cachedImages == null) {
			cachedImages = new Image[2];
		    cachedImages[0] = createImage(BoulderCellEnum.P, playerId);
		    cachedImages[1] = createImage(BoulderCellEnum.Q, playerId);
		    playerImageCache.put(playerId, cachedImages);
		}
		return cachedImages[cell.ordinal() - BoulderCellEnum.P.ordinal()];
	}

	private Image createImage(BoulderCellEnum cell, int playerId) {
		Image playerImage = themeManager.getIcon(cell);
		ImageFilter colorfilter = new PlayerColorFilter(playerId);
		return createImage(new FilteredImageSource(playerImage.getSource(),colorfilter));
	}

	private void drawHeader(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(2.0f));
		g2d.setFont(new Font("Arial", Font.BOLD, 25));
		
		int x = 16;
		g2d.drawRoundRect(x-4, 16-4, 32+4*2, 32+4*2, 8, 8);
		g2d.drawImage(themeManager.getIcon(BoulderCellEnum.D), x, 16, this);
		g2d.drawString(String.valueOf(diamondCounter), 16 + CELL_WIDTH + 20, 16 + CELL_HEIGHT - 6);
		
		x = 300;
		g2d.drawRoundRect(x-4, 16-4, 32+4*2, 32+4*2, 8, 8);
		g2d.drawImage(themeManager.getIcon(BoulderCellEnum.C), x, 16, this);
		g2d.drawString(String.valueOf(deathCounter), x + CELL_WIDTH + 20, 16 + CELL_HEIGHT - 6);
		
		x = getWidth() - 50;
		g2d.drawRoundRect(x-4, 16-4, 32+4*2, 32+4*2, 8, 8);
		g2d.fillPolygon(new int[] {x+8, x+32-8, x+16}, new int[] {16+8, 16+8, 48-8}, 3);
		
	}

	@Override
	public void sendChange(int diamonds, int deaths, ClientMapModel map) {
		this.diamondCounter = diamonds;
		this.deathCounter = deaths;
		this.map = map;
		invalidate();
		repaint();
	}

}
