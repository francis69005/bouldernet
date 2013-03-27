package com.open.dojo.bouldernet.gui.themes;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.open.dojo.bouldernet.BoulderCellEnum;

public abstract class AbstractTheme implements Theme {
	
	private static final int CELL_WIDTH = 32;
	private static final int CELL_HEIGHT = 32;

	private BufferedImage icons;

	public AbstractTheme() {
		
	}
	
	@Override
	public Image getIcon(BoulderCellEnum cell) {
		if (icons == null) {
			initialize();
		}
		return icons.getSubimage(cell.ordinal() * CELL_WIDTH, 0, CELL_WIDTH, CELL_HEIGHT);
	}

	private void initialize() {
		try {
			icons = ImageIO.read(getClass().getResourceAsStream(getIconPath())); // ));
		} catch (IOException e) {
		}
	}

	protected abstract String getIconPath();
}
