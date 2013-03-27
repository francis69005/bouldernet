package com.open.dojo.bouldernet.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.open.dojo.bouldernet.BoulderCell;
import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.DirectionEnum;

public class ClientConnection implements Runnable{

	private Socket socket;
	private BoulderMapServer boulderMapServer;
	private PrintStream printStream;
	private int playerId;
	
	public ClientConnection(Socket socket, BoulderMapServer boulderMapServer) {
		this.socket = socket;	
		this.boulderMapServer = boulderMapServer;
		Thread thread = new Thread(this, "Client Connection");
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			playerId = boulderMapServer.addPlayer();
			
			OutputStream outputStream = socket.getOutputStream();
			printStream = new PrintStream(outputStream);
			printStream.println("Hello " + playerId);
			
			sendChange();
			
			while (boulderMapServer.isRunning()) {
				String moveCommand = bufferedReader.readLine();
				if(moveCommand == null) {
					break;
				}
				String[] directionElements = moveCommand.split(" ");
				boulderMapServer.move(Integer.parseInt(directionElements[1]), DirectionEnum.valueOf(directionElements[0]));
			}
			
		} catch (IOException e) {
		} finally {
			boulderMapServer.removePlayer(this);
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private String serializeMap() {
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < boulderMapServer.getBoulderMap().getVerticalSize(); ++y) {
			for (int x = 0; x < boulderMapServer.getBoulderMap().getHorizontalSize(); ++x) {
				if (x > 0) {
					builder.append(',');
				}
				BoulderCellEnum cell = boulderMapServer.getBoulderMap().getMap()[y][x];
				builder.append(cell.name());
				if (cell.isPerson()) {
					builder.append(boulderMapServer.getBoulderMap().getPlayer(new BoulderCell(x, y)).getId());
				}
			}
			builder.append('&');
		}
		return builder.toString();
	}

	public void sendChange() {
		printStream.print(boulderMapServer.getBoulderMap().getNbDiamond(playerId));
		printStream.print("&");
		printStream.println(boulderMapServer.getBoulderMap().getNbLifes(playerId));
		printStream.println(serializeMap());
	}

	public int getPlayerId() {
		return playerId;
	}

}
