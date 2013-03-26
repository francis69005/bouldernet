package com.open.dojo.bouldernet.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import com.open.dojo.bouldernet.BoulderCellEnum;

public class ServerConnection implements Runnable{

	private Socket socket;
	private PrintStream printStream; 
	private MapListener listener;
	
	public ServerConnection(Socket socket, MapListener listener) {
		this.socket = socket;	
		this.listener = listener;
		Thread thread = new Thread(this, "Server Connection");
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			while (true) {
				String serializedMap = bufferedReader.readLine();
				if (serializedMap != null) {
					listener.sendChange(deSerializeMap(serializedMap));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private BoulderCellEnum[][] deSerializeMap(String serializedMap) {
		String serializedMapLines[] = serializedMap.split("&");
		String serializedMapCells[] = serializedMapLines[0].split(",");
		int verticalSize = (serializedMap.split("&")).length;
		int horizontalSize = (serializedMapLines[0].split(",")).length;
		BoulderCellEnum[][] map = new BoulderCellEnum[verticalSize][horizontalSize];
		for (int y=0; y < verticalSize; y++) {
			serializedMapCells = serializedMapLines[y].split(",");
			for (int x=0; x < horizontalSize; x++) {
				map[y][x] = BoulderCellEnum.getByName(serializedMapCells[x]);
			}
		}
		return map;
	}
}
