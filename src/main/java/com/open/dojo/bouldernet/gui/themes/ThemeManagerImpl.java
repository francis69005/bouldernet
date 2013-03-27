package com.open.dojo.bouldernet.gui.themes;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.gui.themes.classic.ClassicTheme;

public class ThemeManagerImpl implements ThemeManager {

	private List<Theme> themes = new ArrayList<Theme>();
	private Theme currentTheme = new ClassicTheme();

	public ThemeManagerImpl() {
		Iterator<Theme> serviceIterator = ServiceLoader.load(Theme.class).iterator();
		while (serviceIterator.hasNext()) {
			Theme theme = serviceIterator.next();
			themes.add(theme);
			if (currentTheme == null) {
				currentTheme = theme;
			}
		}
	}

	@Override
	public Image getIcon(BoulderCellEnum cell) {
		return currentTheme.getIcon(cell);
	}

	@Override
	public List<Theme> getThemes() {
		return themes;
	}

	@Override
	public Theme getCurrentTheme() {
		return currentTheme;
	}

	@Override
	public void setTheme(Theme theme) {
		this.currentTheme = theme;
	}
}
