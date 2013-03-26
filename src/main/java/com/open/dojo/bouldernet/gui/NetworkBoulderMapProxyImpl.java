package com.open.dojo.bouldernet.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.open.dojo.bouldernet.DirectionEnum;

public class NetworkBoulderMapProxyImpl implements BoulderMapProxy {

	private Socket socket;
	private PrintStream printStream;
	private BufferedReader bufferedReader;
	private int playerId;
	
	public NetworkBoulderMapProxyImpl(String serverIp) {
		try {
			socket = new Socket(serverIp, 9292);
			InputStream inputStream = socket.getInputStream();
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			playerId = Integer.parseInt(bufferedReader.readLine().split(" ")[1]);
			OutputStream outputStream = socket.getOutputStream();
			printStream = new PrintStream(outputStream);
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
		ServerConnection serverConnection = new ServerConnection(socket, listener);
	}

}
