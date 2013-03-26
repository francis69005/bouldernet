package com.open.dojo.bouldernet.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.DirectionEnum;

public class GrillePanel extends JPanel implements MapListener {

	private static final long serialVersionUID = 1L;

	private BoulderMapProxy grille;
	
	private BoulderCellEnum[][] map = new BoulderCellEnum[0][0];
	
	private final BufferedImage icons;
	
	public GrillePanel(BoulderMapProxy grille) throws IOException {
		this.grille = grille;
		this.grille.addMapListener(this);
		setOpaque(true);
		setBackground(Color.white);
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
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int cellWidth = 32;
		int cellHeight = 32;
		if (map.length == 0) {
			g.drawString("Not Connected",
					(int)getBounds().getCenterX(), (int)getBounds().getCenterY());
			return;
		}
		for (int y = 0; y < map.length; ++y) {
			for (int x = 0; x < map[0].length; ++x) {
				g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
				g.setColor(Color.BLACK);
				g.drawRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
				int offset = map[y][x].ordinal() * cellWidth;
				g.drawImage(icons, x*cellWidth, y*cellHeight,
						(x+1)*cellWidth, (y+1)*cellHeight,
						offset, 0, offset + cellWidth, cellHeight, this);
			}
		}
	}

	@Override
	public void sendChange(BoulderCellEnum[][] map) {
		this.map = map;
		repaint();
	}

}
