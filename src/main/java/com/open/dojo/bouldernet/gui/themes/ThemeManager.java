package com.open.dojo.bouldernet.gui.themes;

import java.awt.Image;
import java.util.List;

import com.open.dojo.bouldernet.BoulderCellEnum;

public interface ThemeManager {

	Image getIcon(BoulderCellEnum cell);

	List<Theme> getThemes();

	Theme getCurrentTheme();

	void setTheme(Theme theme);

}
