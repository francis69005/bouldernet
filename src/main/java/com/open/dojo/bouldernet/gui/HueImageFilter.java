package com.open.dojo.bouldernet.gui;

import java.awt.image.RGBImageFilter;

public class HueImageFilter extends RGBImageFilter {

	private int red = 0;
	private int green = 0;
	private int blue = 0;

	public HueImageFilter(int id) {
		canFilterIndexColorModel = true;
		
		for (int i = id-1; i >= 0; --i) {
			if (i % 3 == 0) {
				this.red += 5;
			} else if (i % 3 == 1) {
				this.green += 5;
			} else {
				this.blue += 5;
			}
		}
	}

	public int filterRGB(int x, int y, int rgb) {
		int originalAlpha = (rgb & 0xFF000000) >> 24;
		int originalRed = (rgb & 0x00FF0000) >> 16;
		int originalGreen = (rgb & 0x0000FF00) >> 8;
		int originalBlue = (rgb & 0x000000FF);
		
		// mod colours here
		originalRed += red;
		originalGreen += green;
		originalBlue += blue;

		return (originalAlpha << 24) | (originalRed << 16) | (originalGreen << 8) | originalBlue;
	}
	
}
