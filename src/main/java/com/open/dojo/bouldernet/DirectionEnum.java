package com.open.dojo.bouldernet;

public enum DirectionEnum {
	UP(0, -1),
	DOWN(0, 1),
	LEFT(-1,0),
	RIGHT(1,0);

	private int x;
	private int y;
	
	private DirectionEnum (int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
