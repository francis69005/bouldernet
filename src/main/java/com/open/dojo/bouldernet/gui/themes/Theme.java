package com.open.dojo.bouldernet.gui.themes;

import java.awt.Image;

import com.open.dojo.bouldernet.BoulderCellEnum;

public interface Theme {
	
	String getName();

	Image getIcon(BoulderCellEnum cell);

}
