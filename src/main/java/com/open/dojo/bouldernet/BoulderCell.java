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
	
	@Override
	public int hashCode() {
		return x ^ (y << 3);
	}
	
	@Override
	public boolean equals(Object o) {
		if (! (o instanceof BoulderCell)) {
			return false;
		}
		return ((BoulderCell)o).x == x && ((BoulderCell)o).y == y;
	}

	public BoulderCell getNextCell(DirectionEnum direction) {
		return new BoulderCell(
				x + direction.getX(),
				y + direction.getY());
	}
	
}
