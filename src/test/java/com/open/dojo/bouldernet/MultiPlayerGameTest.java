package com.open.dojo.bouldernet;

import static com.open.dojo.bouldernet.BoulderCellEnum.P;
import static com.open.dojo.bouldernet.BoulderCellEnum.Q;
import static com.open.dojo.bouldernet.BoulderCellEnum.T;
import static com.open.dojo.bouldernet.BoulderCellEnum.V;

import org.junit.Assert;
import org.junit.Test;

public class MultiPlayerGameTest {
	
	@Test
	public void shouldAddPlayerIncrements() {
		BoulderCellEnum[][] grille = 
		{{T, T, T},
		{T, T, T},
		{T, T, T}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(0, 0));
		
		BoulderCellEnum[][] expected1player = 
		{{P, T, T},
		{T, T, T},
		{T, T, T}};
		testMap(expected1player, boulderMap.getMap());
		
		boulderMap.addPlayer(new BoulderCell(2, 2));
		BoulderCellEnum[][] expected2players = 
		{{P, T, T},
		{T, T, T},
		{T, T, P}};
		testMap(expected2players, boulderMap.getMap());
	}
	
	private void testMap(BoulderCellEnum[][] expected, BoulderCellEnum[][] actual) {
		Assert.assertEquals(expected.length, actual.length);
		Assert.assertEquals(expected[0].length, actual[0].length);
		for (int y = 0; y < expected.length; y++) {
			for (int x = 0; x < expected[0].length; x++) {
				Assert.assertEquals("y=" + y + ", x=" + x,expected[y][x], actual[y][x]);
			}
			
		}
	}
	
	@Test
	public void movePlayer1() {
		BoulderCellEnum[][] grille = 
		{{T, T, T},
		{T, T, T},
		{T, T, T}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		int player1 = boulderMap.addPlayer(new BoulderCell(0, 0));
		boulderMap.addPlayer(new BoulderCell(2, 2));
		
		boulderMap.move(player1, DirectionEnum.RIGHT);
		
		BoulderCellEnum[][] expected2players = 
		{{V, Q, T},
		{T, T, T},
		{T, T, P}};
		testMap(expected2players, boulderMap.getMap());
	}

	@Test	
	public void move2Players() {
		BoulderCellEnum[][] grille = 
		{{T, T, T},
		{T, T, T},
		{T, T, T}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		int player1 = boulderMap.addPlayer(new BoulderCell(0, 0));
		int player2 = boulderMap.addPlayer(new BoulderCell(2, 2));
		
		boulderMap.move(player1, DirectionEnum.RIGHT);
		boulderMap.move(player2, DirectionEnum.LEFT);
		
		BoulderCellEnum[][] expected2players = 
		{{V, Q, T},
		{T, T, T},
		{T, P, V}};
		testMap(expected2players, boulderMap.getMap());

		boulderMap.move(player1, DirectionEnum.RIGHT);
		boulderMap.move(player2, DirectionEnum.LEFT);
		
		BoulderCellEnum[][] expected2players2 = 
		{{V, V, Q},
		{T, T, T},
		{P, V, V}};
		testMap(expected2players2, boulderMap.getMap());
	}


}
