package com.open.dojo.bouldernet.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.open.dojo.bouldernet.BoulderCell;
import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.BoulderMap;
import com.open.dojo.bouldernet.DirectionEnum;
import com.open.dojo.bouldernet.Player;

public class BoulderMapServer {

	private BoulderMap boulderMap;
	private Thread monThread;
	private Thread threadClient;
	private List<ClientConnection> clientConnections = new ArrayList<ClientConnection>();
	private Map<Integer, DirectionEnum> directionByPlayer = new HashMap<Integer, DirectionEnum>();
	private ServerSocket serverSocket;
	private boolean running = true;
	private boolean autoSpawn;

	public BoulderMapServer(BoulderMap boulderMap) throws IOException {
		this(boulderMap, false);
	}
	
	public BoulderMapServer() throws IOException {
		this(null, true);
	}
	
	public BoulderMapServer(BoulderMap boulderMap, boolean autoSpawn) throws IOException {
		this.autoSpawn = autoSpawn;
		if (boulderMap == null) {
			this.boulderMap = new BoulderMap(getRandomMap(40, 25, 25, 75));
		} else {
			this.boulderMap = boulderMap;
		}
		
		// ouverture du socket de rendez-vous
		serverSocket = new ServerSocket(9292);
				
		monThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (running) {
					try {
						synchronized (directionByPlayer) {
							for (Integer playerId : directionByPlayer.keySet()) {
								BoulderMapServer.this.boulderMap.move(playerId, directionByPlayer.get(playerId));
							}
							directionByPlayer.clear();
						}
						BoulderMapServer.this.boulderMap.moveObjects();
						
						// Envoi vers les sockets des clients
						for (ClientConnection clientConnection : clientConnections) {
							clientConnection.sendChange();
						}
						
						// Détection de la fin de partie et génération nouvelle map
						int remainingDiamonds = BoulderMapServer.this.boulderMap.getRemainingDiamonds();
						if (remainingDiamonds == 0) {
							// Sauvegarde des infos des joueurs
							Map<Integer, Player> playerById = BoulderMapServer.this.boulderMap.getPlayers();
							BoulderMapServer.this.boulderMap = new BoulderMap(getRandomMap(40, 25, 25, 75));
							for (Player player : playerById.values()) {
								BoulderMapServer.this.boulderMap.addPlayer(player);
							}
						}
						
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, "mainLoop");
		monThread.setDaemon(true);
		monThread.start();
		
		threadClient = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Socket socket;
				while (running) {
					try {
						socket = serverSocket.accept();
						socket.setTcpNoDelay(true);
						
						ClientConnection clientConnection = new ClientConnection(socket, BoulderMapServer.this);
						connect(clientConnection);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, "Thread Client");
		threadClient.setDaemon(true);
		threadClient.start();
	}
	
	public BoulderMap getBoulderMap() {
		return boulderMap;
	}

	public void move(int playerId, DirectionEnum direction) {
		synchronized (directionByPlayer) {
			directionByPlayer.put(playerId, direction);
		}
	}
	
	public int addPlayer() {
		return boulderMap.addPlayer(autoSpawn ? null : new BoulderCell(0, 0));
	}
	
	public void removePlayer(ClientConnection clientConnection) {
		boulderMap.removePlayer(clientConnection.getPlayerId());
		clientConnections.remove(clientConnection);
	}

	public void connect(ClientConnection clientConnection) {
		this.clientConnections.add(clientConnection);
	}

	public void stop() {
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
	
	private static BoulderCellEnum [][] getRandomMap(int horizontalSize, int verticalSize, int diamondNb, int rockNb) {
		BoulderCellEnum [][] grille = new BoulderCellEnum[verticalSize][horizontalSize];
		// Remplissage Terre
		for (int y = 0; y < verticalSize; y++) {
			for (int x = 0; x < horizontalSize; x++) {
				grille[y][x] = BoulderCellEnum.T;
			}
		}

		// Remplissage Diamants
		for (int count = 0; count < diamondNb; count++) {
			setRandomMapCell(grille, BoulderCellEnum.D);
		}
		
		// Remplissage Rochers
		for (int count = 0; count < rockNb; count++) {
			setRandomMapCell(grille, BoulderCellEnum.R);
		}
		
		return grille;
	}
	
	private static void setRandomMapCell(BoulderCellEnum [][] grille, BoulderCellEnum cellType) {
		while (true) {
			int y = (int)(Math.random()*grille.length);
			int x = (int)(Math.random()*grille[0].length);
			if (grille[y][x] == BoulderCellEnum.T) {
				grille[y][x] = cellType;
				break;
			}
		}
	}
}
