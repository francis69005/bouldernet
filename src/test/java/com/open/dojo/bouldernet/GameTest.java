package com.open.dojo.bouldernet;

import static com.open.dojo.bouldernet.BoulderCellEnum.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GameTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void initializationTest(){
		BoulderCellEnum[][] grille = 
		{{T, T, T},
		{T, T, T},
		{T, T, T}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		Assert.assertEquals(grille[0].length, boulderMap.getHorizontalSize());
		Assert.assertEquals(grille.length, boulderMap.getVerticalSize());
		Assert.assertEquals(0, boulderMap.getPersonneCount());
		Assert.assertEquals(0, boulderMap.getPersonneCount());
		Assert.assertEquals(9, boulderMap.getTerreCount());
		Assert.assertEquals(0, boulderMap.getDiamantCount());
		Assert.assertEquals(0, boulderMap.getVideCount());
		Assert.assertEquals(0, boulderMap.getSortieCount());
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
	public void testMoveUpAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(1, 1));
		boulderMap.move(0, DirectionEnum.UP);
		
		BoulderCellEnum[][] newGrille = 
		{{V, P, V},
		{V, V, V},
		{V, V, V}};
		
		testMap(newGrille, boulderMap.getMap());
	}
	
	@Test
	public void testRemovePlayer() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		int playerId = boulderMap.addPlayer(new BoulderCell(1, 1));
		
		BoulderCellEnum[][] newGrille = 
		{{V, V, V},
		{V, P, V},
		{V, V, V}};
		
		testMap(newGrille, boulderMap.getMap());
		
		boulderMap.removePlayer(playerId);
		
		testMap(grille, boulderMap.getMap());
	}
	
	@Test
	public void testMoveUpAllowedAndEatTheGoodEarth() {
		BoulderCellEnum[][] grille = 
		{{V, T, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(1, 1));
		boulderMap.move(0, DirectionEnum.UP);
		
		BoulderCellEnum[][] newGrille = 
		{{V, P, V},
		{V, V, V},
		{V, V, V}};
		
		testMap(newGrille, boulderMap.getMap());
	}
	
	@Test
	public void testMoveDownAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(1, 1));
		boulderMap.move(0, DirectionEnum.DOWN);
		
		BoulderCellEnum[][] newGrille = 
		{{V, V, V},
		{V, V, V},
		{V, P, V}};
		
		testMap(newGrille, boulderMap.getMap());
	}
	
	@Test
	public void testMoveMultipleMovesAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(1, 1));
		boulderMap.move(0, DirectionEnum.LEFT);
		boulderMap.move(0, DirectionEnum.UP);
		
		BoulderCellEnum[][] newGrille = 
		{{P, V, V},
		{V, V, V},
		{V, V, V}};
		
		testMap(newGrille, boulderMap.getMap());
	}
	
	@Test
	public void testBorderMoveNotAllowed() {
		BoulderCellEnum[][] grille = 
		{{P, V, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(0, 0));
		boulderMap.move(0, DirectionEnum.UP);
		testMap(grille, boulderMap.getMap());
		boulderMap.move(0, DirectionEnum.LEFT);
		testMap(grille, boulderMap.getMap());

		BoulderCellEnum[][] grille2 = 
		{{V, V, V},
		{V, V, V},
		{V, V, P}};
		
		boulderMap = new BoulderMap(grille2);
		boulderMap.addPlayer(new BoulderCell(2, 2));
		boulderMap.move(0, DirectionEnum.DOWN);
		testMap(grille2, boulderMap.getMap());
		boulderMap.move(0, DirectionEnum.RIGHT);
		testMap(grille2, boulderMap.getMap());
	}

	@Test
	public void testRockMoveNotAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, T, V, V},
		{V, V, R, V, V},
		{R, R, P, R, D},
		{V, V, R, V, V},
		{V, V, T, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(2, 2));
		boulderMap.move(0, DirectionEnum.DOWN);
		testMap(grille, boulderMap.getMap());
		boulderMap.move(0, DirectionEnum.UP);
		testMap(grille, boulderMap.getMap());
		boulderMap.move(0, DirectionEnum.LEFT);
		testMap(grille, boulderMap.getMap());
		boulderMap.move(0, DirectionEnum.RIGHT);
		testMap(grille, boulderMap.getMap());
	}

	@Test
	public void testRockMoveUpAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{V, R, P, R, V},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(2, 2));
		boulderMap.move(0, DirectionEnum.UP);
		
		BoulderCellEnum[][] movedGrille = 
		{{V, V, R, V, V},
		{V, V, P, V, V},
		{V, R, V, R, V},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		testMap(movedGrille, boulderMap.getMap());
	}
	
	@Test
	public void testRockMoveDownAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{V, R, P, R, V},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(2, 2));
		boulderMap.move(0, DirectionEnum.DOWN);
		
		BoulderCellEnum[][] movedGrille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{V, R, V, R, V},
		{V, V, P, V, V},
		{V, V, R, V, V}};
		testMap(movedGrille, boulderMap.getMap());
		
		// XXX Ici le personnage se prend le caillou sur la t�te et meurt dans d'atroces souffrances
	}
	
	@Test
	public void testRockMoveLeftAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{V, R, P, R, V},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(2, 2));
		boulderMap.move(0, DirectionEnum.LEFT);
		
		BoulderCellEnum[][] movedGrille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{R, P, V, R, V},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		testMap(movedGrille, boulderMap.getMap());
	}
	
	@Test
	public void testRockMoveRightAllowed() {
		BoulderCellEnum[][] grille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{V, R, P, R, V},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.addPlayer(new BoulderCell(2, 2));
		boulderMap.move(0, DirectionEnum.RIGHT);
		
		BoulderCellEnum[][] movedGrille = 
		{{V, V, V, V, V},
		{V, V, R, V, V},
		{V, R, V, Q, R},
		{V, V, R, V, V},
		{V, V, V, V, V}};
		testMap(movedGrille, boulderMap.getMap());
	}
	
	@Test
	public void testGetDiamond() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, P, D},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		int player = boulderMap.addPlayer(new BoulderCell(1, 1));
		boulderMap.move(0, DirectionEnum.RIGHT);
		
		Assert.assertEquals(1, boulderMap.getNbDiamond(player));
		BoulderCellEnum[][] movedGrille = 
		{{V, V, V},
		{V, V, Q},
		{V, V, V}};
		testMap(movedGrille, boulderMap.getMap());
	}
	
	@Test
	public void testRockFall() {
		BoulderCellEnum[][] grille = 
		{{V, R, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.moveObjects();
		
		BoulderCellEnum[][] expectedGrille = 
		{{V, V, V},
		{V, R, V},
		{V, V, V}};
		
		testMap(expectedGrille, boulderMap.getMap());
	}

	@Test
	public void testRockDoesntFall() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, R, V},
		{V, T, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.moveObjects();
		
		testMap(grille, boulderMap.getMap());
	}

	@Test
	public void testRockDoesntFallOutside() {
		BoulderCellEnum[][] grille = 
		{{V, V, V},
		{V, V, V},
		{V, R, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.moveObjects();
		
		testMap(grille, boulderMap.getMap());
	}

	@Test
	public void testRockFallFromTopToBottom() {
		BoulderCellEnum[][] grille = 
		{{V, R, V},
		{V, V, V},
		{V, V, V}};
		
		BoulderMap boulderMap = new BoulderMap(grille);
		boulderMap.moveObjects();
		boulderMap.moveObjects();
		
		BoulderCellEnum[][] expectedGrille = 
		{{V, V, V},
		{V, V, V},
		{V, R, V}};
		
		testMap(expectedGrille, boulderMap.getMap());
	}

}
