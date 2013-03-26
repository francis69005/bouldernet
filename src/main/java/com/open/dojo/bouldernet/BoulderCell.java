package com.open.dojo.bouldernet;

public class BoulderCell {
	private final int x;
	private final int y;
	
	public BoulderCell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public BoulderCell getNextCell(DirectionEnum direction) {
		return new BoulderCell(
				x + direction.getX(),
				y + direction.getY());
	}
	
}
