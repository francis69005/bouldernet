package com.open.dojo.bouldernet;

public class Player {

	private BoulderCell boulderCell;
	private int nbDiamond = 0;
	private int nbDeath = 0;
	
	public Player(BoulderCell boulderCell) {
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
}
