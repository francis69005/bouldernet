package com.open.dojo.bouldernet;

public class Player {

	private int id;
	private BoulderCell boulderCell;
	private int nbDiamond = 0;
	private int nbDeath = 0;
	private DirectionEnum direction = DirectionEnum.LEFT;
	
	
	public Player(int id, BoulderCell boulderCell) {
		this.id = id;
		this.boulderCell = boulderCell;
	}
	
	public void setBoulderCell(BoulderCell boulderCell) {
		this.boulderCell = boulderCell;
	}

	public BoulderCell getBoulderCell() {
		return boulderCell;
	}

	public void setNbDiamond(int nbDiamond) {
		this.nbDiamond = nbDiamond;
	}
	
	public void addDiamond() {
		nbDiamond++;
	}
	
	public int getNbDiamond() {
		return nbDiamond;
	}
	
	public void addDeath() {
		nbDeath++;
	}
	
	public int getNbDeath() {
		return nbDeath;
	}

	public void setDirection(DirectionEnum direction) {
		if (direction == DirectionEnum.LEFT || direction == DirectionEnum.RIGHT) {
			this.direction = direction;
		}
	}
	
	public DirectionEnum getDirection() {
		return this.direction;
	}
	
	public int getId() {
		return id;
	}
}
