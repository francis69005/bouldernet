package com.open.dojo.bouldernet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class BoulderMap {

	private BoulderCellEnum[][] map;
	private Map<Integer, Player> playerById = new HashMap<Integer, Player>();
	private Set<BoulderCell> movingRocks = new HashSet<BoulderCell>();
	
	public BoulderMap(BoulderCellEnum[][] grille) {
		map = new BoulderCellEnum[grille.length][grille[0].length];
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length; x++) {
				map[y][x] = grille[y][x];
			}
		}
	}

	public int getHorizontalSize() {
		return map[0].length;
	}

	public int getVerticalSize() {
		return map.length;
	}

	public void move(int playerId, DirectionEnum direction) {
		Player player = playerById.get(playerId);
		BoulderCell persoCell = player.getBoulderCell();
		final BoulderCell wantedPersoCell = persoCell.getNextCell(direction);
		if (isMoveAllowed(wantedPersoCell, direction)) {
			player.setDirection(direction);
			if (map[wantedPersoCell.getY()][wantedPersoCell.getX()] == BoulderCellEnum.R) {
				BoulderCell behindCell = wantedPersoCell.getNextCell(direction);
				map[behindCell.getY()][behindCell.getX()] = BoulderCellEnum.R;
			} else if (map[wantedPersoCell.getY()][wantedPersoCell.getX()] == BoulderCellEnum.D) {
				player.addDiamond();
			}
			map[persoCell.getY()][persoCell.getX()] = BoulderCellEnum.V;
			map[wantedPersoCell.getY()][wantedPersoCell.getX()] =
					player.getDirection() == DirectionEnum.LEFT ?
							BoulderCellEnum.P : BoulderCellEnum.Q;
			player.setBoulderCell(wantedPersoCell);
		}
	}
	
	public void moveObjects(){
		for (int y = map.length - 2; y >= 0; y--) {
			for (int x = 0; x < map[0].length; x++) {
				BoulderCell cell = new BoulderCell(x, y);
				if (map[y][x] == BoulderCellEnum.R) {
					tryMoveRock(cell);
				}
			}
		}
		// rocks at the bottom can't move anymore
		for (int x = 0; x < map[0].length; x++) {
			BoulderCell cell = new BoulderCell(x, map.length-1);
			movingRocks.remove(cell);
		}
	}
	
	private void tryMoveRock(BoulderCell cell) {
		BoulderCell underCell = cell.getNextCell(DirectionEnum.DOWN);
		BoulderCellEnum underCellType = getCell(underCell);
		if (movingRocks.contains(cell) && underCellType.isPerson()) {
			moveRock(cell, underCell);
			Player player = getPlayer(underCell);
			player.addDeath();
			player.setBoulderCell(getRandomPlayerSpawn());
		} else if (underCellType == BoulderCellEnum.V) {
			moveRock(cell, underCell);
		} else {
			movingRocks.remove(cell);
		}
	}

	private void moveRock(BoulderCell from, BoulderCell to) {
		setCell(to, BoulderCellEnum.R);
		setCell(from, BoulderCellEnum.V);
		movingRocks.remove(from);
		movingRocks.add(to);
	}
	
	private BoulderCellEnum getCell(BoulderCell cell) {
		return map[cell.getY()][cell.getX()];
	}
	
	private void setCell(BoulderCell cell, BoulderCellEnum value) {
		map[cell.getY()][cell.getX()] = value;
	}

	private boolean isMoveAllowed(BoulderCell wantedPersoCell, DirectionEnum direction) {
		if (! isInMap(wantedPersoCell)) {
			return false;
		}
		final int x = wantedPersoCell.getX();
		final int y = wantedPersoCell.getY();
		if (map[y][x] == BoulderCellEnum.R) {
			BoulderCell behindCell = wantedPersoCell.getNextCell(direction);
			if (!isInMap(behindCell)
					|| map[behindCell.getY()][behindCell.getX()] != BoulderCellEnum.V) {
				return false;
			}
			return true;
		}
		if (map[y][x] == BoulderCellEnum.D) {
			return true;
		}
		return map[y][x] == BoulderCellEnum.V || map[y][x] == BoulderCellEnum.T;
	}
	
	private boolean isInMap(BoulderCell cell) {
		final int x = cell.getX();
		final int y = cell.getY();
		if (x < 0 || x >= getHorizontalSize()) {
			return false;
		}
		if (y < 0 || y >= getVerticalSize()) {
			return false;
		}
		return true;
	}

	public int addPlayer(BoulderCell boulderCell) {
		int newId = playerById.size();
		Player player = null;
		if (boulderCell == null) {
			player = new Player(newId, getRandomPlayerSpawn());
		} else {
			map[boulderCell.getY()][boulderCell.getX()] = BoulderCellEnum.P;
			player = new Player(newId, boulderCell);
		}
		playerById.put(newId, player);
		return newId;
	}
	
	public void removePlayer(int playerId) {
		Player player = playerById.remove(playerId);
		if (player != null) {
			BoulderCell boulderCell = player.getBoulderCell();
			map[boulderCell.getY()][boulderCell.getX()] = BoulderCellEnum.V;
		}
	}
	
	public BoulderCell getPersonneCell(int playerId) {
		return playerById.get(playerId).getBoulderCell();
	}

	public BoulderCellEnum[][] getMap() {
		return map;
	}

	public int getNbDiamond(int player) {
		return playerById.get(player).getNbDiamond();
	}
	
	public int getNbDeaths(int playerId) {
		return playerById.get(playerId).getNbDeath();
	}
	
	private BoulderCell getRandomPlayerSpawn() {
		while (true) {
			int y = (int)(Math.random()*map.length);
			int x = (int)(Math.random()*map[0].length);
			if (map[y][x] == BoulderCellEnum.T || map[y][x] == BoulderCellEnum.V) {
				map[y][x] = BoulderCellEnum.P;
				return new BoulderCell(x, y);
			}
		}
	}

	public Player getPlayer(BoulderCell boulderCell) {
		for (Player player : playerById.values()) {
			if (player.getBoulderCell().equals(boulderCell)) {
				return player;
			}
		}
		return null;
	}
	
}
