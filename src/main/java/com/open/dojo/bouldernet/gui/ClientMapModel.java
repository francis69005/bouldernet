package com.open.dojo.bouldernet.gui;

import java.util.Map;

import com.open.dojo.bouldernet.BoulderCell;
import com.open.dojo.bouldernet.BoulderCellEnum;

public class ClientMapModel {

	private BoulderCellEnum[][] map;
	private Map<BoulderCell, Integer> playerPositions;

	public ClientMapModel(BoulderCellEnum[][] map, Map<BoulderCell, Integer> playerPositions) {
		this.map = map;
		this.playerPositions = playerPositions;
	}

	public int getWidth() {
		return map[0].length;
	}

	public int getHeight() {
		return map.length;
	}

	public BoulderCellEnum getCell(int x, int y) {
		return map[y][x];
	}

	public int getPlayerId(BoulderCell cell) {
		return playerPositions.get(cell);
	}

}
