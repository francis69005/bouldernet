package com.open.dojo.bouldernet.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.open.dojo.bouldernet.BoulderCell;
import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.DirectionEnum;

public class GrillePanel extends JPanel implements MapListener {

	private static final long serialVersionUID = 1L;
	
	private static final int CELL_WIDTH = 32;
	private static final int CELL_HEIGHT = 32;
	private static final int HEADER_HEIGHT = 64;

	private BoulderMapProxy grille;
	
	private ClientMapModel map = null;
	
	private final BufferedImage icons;

	private BoulderGUI gui;
	
	private int diamondCounter = 0;
	
	private int lifeCounter = 0;
	
	public GrillePanel(BoulderGUI gui, BoulderMapProxy grille) throws IOException {
		this.gui = gui;
		this.grille = grille;
		this.grille.addMapListener(this);
		setOpaque(true);
		setBackground(Color.BLACK);
		icons = ImageIO.read(getClass().getResourceAsStream("/images/sprites.png"));
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
				int offset = cell.ordinal() * CELL_WIDTH;
				
				if (cell.isPerson()) {
					BufferedImage playerImage = icons.getSubimage(offset, 0, CELL_WIDTH, CELL_HEIGHT);
					int playerId = map.getPlayerId(new BoulderCell(x, y));
					ImageFilter colorfilter = new HueImageFilter(playerId);
				    Image coloredImage = createImage(new FilteredImageSource(playerImage.getSource(),colorfilter));
					g.drawImage(coloredImage, x*CELL_WIDTH, HEADER_HEIGHT + y*CELL_HEIGHT, this);
				} else {
					g.drawImage(icons, x*CELL_WIDTH, HEADER_HEIGHT + y*CELL_HEIGHT,
							(x+1)*CELL_WIDTH, HEADER_HEIGHT + (y+1)*CELL_HEIGHT,
							offset, 0, offset + CELL_WIDTH, CELL_HEIGHT, this);
				}
			}
		}
	}

	private void drawHeader(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.GRAY);
		g2d.setStroke(new BasicStroke(2.0f));
		g2d.setFont(new Font("Arial", Font.BOLD, 25));
		
		int x = 16;
		int offset = BoulderCellEnum.D.ordinal() * CELL_WIDTH;
		g2d.drawRoundRect(x-4, 16-4, 32+4*2, 32+4*2, 8, 8);
		g2d.drawImage(icons, x, 16, x + CELL_WIDTH, 16 + CELL_HEIGHT,
				offset, 0, offset + CELL_WIDTH, CELL_HEIGHT, this);
		g2d.drawString(String.valueOf(diamondCounter), 16 + CELL_WIDTH + 20, 16 + CELL_HEIGHT - 6);
		
		x = 300;
		offset = BoulderCellEnum.C.ordinal() * CELL_WIDTH;
		g2d.drawRoundRect(x-4, 16-4, 32+4*2, 32+4*2, 8, 8);
		g2d.drawImage(icons, x, 16, x + CELL_WIDTH, 16 + CELL_HEIGHT,
				offset, 0, offset + CELL_WIDTH, CELL_HEIGHT, this);
		g2d.drawString(String.valueOf(lifeCounter), x + CELL_WIDTH + 20, 16 + CELL_HEIGHT - 6);
		
	}

	@Override
	public void sendChange(int diamonds, int lifes, ClientMapModel map) {
		this.diamondCounter = diamonds;
		this.lifeCounter = lifes;
		this.map = map;
		repaint();
		gui.pack();
	}

}
