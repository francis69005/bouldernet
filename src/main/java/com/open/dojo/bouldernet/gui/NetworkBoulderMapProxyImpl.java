package com.open.dojo.bouldernet.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import com.open.dojo.bouldernet.BoulderCell;
import com.open.dojo.bouldernet.BoulderCellEnum;
import com.open.dojo.bouldernet.DirectionEnum;

public class NetworkBoulderMapProxyImpl implements BoulderMapProxy, Runnable {

	private Socket socket;
	private PrintStream printStream;
	private BufferedReader bufferedReader;
	private int playerId;
	private MapListener listener;
	
	public NetworkBoulderMapProxyImpl(String serverIp) {
		try {
			socket = new Socket(serverIp, 9292);
			InputStream inputStream = socket.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			playerId = Integer.parseInt(bufferedReader.readLine().split(" ")[1]);
			
			OutputStream outputStream = socket.getOutputStream();
			printStream = new PrintStream(outputStream);
			
			Thread thread = new Thread(this, "Server Connection");
			thread.setDaemon(true);
			thread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void move(DirectionEnum direction) {
		printStream.println(direction.name() + " " + playerId);
	}

	@Override
	public void addMapListener(MapListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				String infoLine = bufferedReader.readLine();
				if (infoLine != null) {
					String[] elements = infoLine.split("&");
					int diamonds = Integer.parseInt(elements[0]);
					int lifes = Integer.parseInt(elements[1]);
					String serializedMap = bufferedReader.readLine();
					if (serializedMap != null && listener != null) {
						listener.sendChange(diamonds, lifes, deSerializeMap(serializedMap));
					}
				} else {
					break; // lost connection
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

	private ClientMapModel deSerializeMap(String serializedMap) {
		String serializedMapLines[] = serializedMap.split("&");
		String serializedMapCells[] = serializedMapLines[0].split(",");
		int verticalSize = (serializedMap.split("&")).length;
		int horizontalSize = (serializedMapLines[0].split(",")).length;
		BoulderCellEnum[][] map = new BoulderCellEnum[verticalSize][horizontalSize];
		Map<BoulderCell, Integer> playerPositions = new HashMap<BoulderCell, Integer>();
		for (int y=0; y < verticalSize; y++) {
			serializedMapCells = serializedMapLines[y].split(",");
			for (int x=0; x < horizontalSize; x++) {
				BoulderCellEnum cell = BoulderCellEnum.getByName(serializedMapCells[x].substring(0, 1));
				map[y][x] = cell;
				if (cell.isPerson()) {
					int playerId = Integer.parseInt(serializedMapCells[x].substring(1));
					playerPositions.put(new BoulderCell(x, y), playerId);
				}
			}
		}
		return new ClientMapModel(map, playerPositions);
	}

}
