package com.open.dojo.bouldernet.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import com.open.dojo.bouldernet.DirectionEnum;

public class ClientConnection implements Runnable{

	private Socket socket;
	private BoulderMapServer boulderMapServer;
	private PrintStream printStream;
	private int score;
	private int death;
	
	public ClientConnection(Socket socket, BoulderMapServer boulderMapServer) {
		this.socket = socket;	
		this.boulderMapServer = boulderMapServer;
		score = 0;
		death = 0;
		Thread thread = new Thread(this, "Client Connection");
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			int playerId = boulderMapServer.addPlayer();
			
			OutputStream outputStream = socket.getOutputStream();
			printStream = new PrintStream(outputStream);
			printStream.println("Hello " + playerId);
			
			printStream.println(serializeMap());
			
			while (boulderMapServer.isRunning()) {
				String moveCommand = bufferedReader.readLine();
				if(moveCommand != null){
					String[] directionElements = moveCommand.split(" ");
					boulderMapServer.move(Integer.parseInt(directionElements[1]), DirectionEnum.valueOf(directionElements[0]));
				}
			}
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String serializeMap() {
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < boulderMapServer.getBoulderMap().getVerticalSize(); ++y) {
			for (int x = 0; x < boulderMapServer.getBoulderMap().getHorizontalSize(); ++x) {
				if (x > 0) {
					builder.append(',');
				}
				builder.append(boulderMapServer.getBoulderMap().getMap()[y][x].name());
			}
			builder.append('&');
		}
		return builder.toString();
	}

	public void sendChange() {
		printStream.println(serializeMap());
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getDeath() {
		return death;
	}

	public void setDeath(int death) {
		this.death = death;
	}

}
