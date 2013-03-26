package com.open.dojo.bouldernet;


import static com.open.dojo.bouldernet.BoulderCellEnum.D;
import static com.open.dojo.bouldernet.BoulderCellEnum.R;
import static com.open.dojo.bouldernet.BoulderCellEnum.T;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.open.dojo.bouldernet.server.BoulderMapServer;

public class NetworkTest {
	private BoulderMapServer server;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		BoulderCellEnum [][] grilleDeDepart =
		{{T, T, R, T, T, T, T, T , T, T},
		{T, T, T, T, T, T, T, T , T, T},
		{T, T, R, T, T, T, T, R , T, T},
		{T, T, T, T, T, T, T, T , T, T},
		{T, T, T, R, T, T, T, R , T, T},
		{T, T, T, T, R, T, T, T , T, T},
		{D, T, T, T, T, T, T, T , T, T},
		{T, R, T, T, T, T, R, D , T, T},
		{T, R, R, T, T, T, T, T , T, T},
		{T, T, T, T, T, T, T, T , T, T}};
		
		server = new BoulderMapServer(new BoulderMap(grilleDeDepart));
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
		Thread.sleep(800);
	}

	@Test
	public void testClientConnection() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 9292);
		socket.setTcpNoDelay(true);
		
		OutputStream outputStream = socket.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printStream.println("Hello");
		
		printStream.close();
		socket.close();
	}
	
	@Test
	public void testClientConnectionResponse() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 9292);
		socket.setTcpNoDelay(true);
		
		InputStream inputStream = socket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		try{
			String serverResponse = bufferedReader.readLine();
			Assert.assertEquals("Hello 0", serverResponse);
		}finally{
			socket.close();
		}
	}
	
	@Test
	public void testGetMap() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 9292);
		socket.setTcpNoDelay(true);
		
		InputStream inputStream = socket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		try{
			String serverResponse = bufferedReader.readLine();
			Assert.assertEquals("Hello 0", serverResponse);
			serverResponse = bufferedReader.readLine();
			Assert.assertNotNull(serverResponse);
			Assert.assertEquals(  "P,T,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse);
	
		}finally{
			socket.close();
		}
	}
	
	@Test
	public void testMove() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 9292);
		socket.setTcpNoDelay(true);
		
		InputStream inputStream = socket.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		OutputStream outputStream = socket.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		
		try{
			String serverResponse = bufferedReader.readLine();
			Assert.assertEquals("Hello 0", serverResponse);
			serverResponse = bufferedReader.readLine();
			Assert.assertNotNull(serverResponse);
			Assert.assertEquals(  "P,T,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse);
			
			printStream.println(DirectionEnum.DOWN.name() + " 0");
			
			serverResponse = bufferedReader.readLine();
			Assert.assertNotNull(serverResponse);
			Assert.assertEquals(  "V,T,R,T,T,T,T,T,T,T&"
								+ "P,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse);
		}finally{
			socket.close();
		}
	}
	
	@Test
	public void testMove2Players() throws UnknownHostException, IOException {
		Socket socket1 = new Socket("localhost", 9292);
		socket1.setTcpNoDelay(true);
		InputStream inputStream1 = socket1.getInputStream();
		BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream1));
		OutputStream outputStream1 = socket1.getOutputStream();
		PrintStream printStream1 = new PrintStream(outputStream1);
		
		Socket socket2 = null;
		
		try{
			String serverResponse1 = bufferedReader1.readLine();
			Assert.assertEquals("Hello 0", serverResponse1);
			serverResponse1 = bufferedReader1.readLine();
			Assert.assertNotNull(serverResponse1);
			Assert.assertEquals(  "P,T,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse1);
			
			printStream1.println(DirectionEnum.DOWN.name() + " 0");
			
			serverResponse1 = bufferedReader1.readLine();
			Assert.assertNotNull(serverResponse1);
			Assert.assertEquals(  "V,T,R,T,T,T,T,T,T,T&"
								+ "P,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse1);

			socket2 = new Socket("localhost", 9292);
			socket2.setTcpNoDelay(true);
			InputStream inputStream2 = socket2.getInputStream();
			BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream2));
			OutputStream outputStream2 = socket2.getOutputStream();
			PrintStream printStream2 = new PrintStream(outputStream2);
			
			String serverResponse2 = bufferedReader2.readLine();
			Assert.assertEquals("Hello 1", serverResponse2);
			serverResponse2 = bufferedReader2.readLine();
			Assert.assertNotNull(serverResponse2);
			Assert.assertEquals(  "P,T,R,T,T,T,T,T,T,T&"
								+ "P,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse2);

			printStream2.println(DirectionEnum.RIGHT.name() + " 1");
			
			serverResponse2 = bufferedReader2.readLine();
			Assert.assertNotNull(serverResponse2);
			Assert.assertEquals(  "V,P,R,T,T,T,T,T,T,T&"
								+ "P,T,T,T,T,T,T,T,T,T&"
								+ "T,T,R,T,T,T,T,R,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&"
								+ "T,T,T,R,T,T,T,R,T,T&"
								+ "T,T,T,T,R,T,T,T,T,T&"
								+ "D,T,T,T,T,T,T,T,T,T&"
								+ "T,R,T,T,T,T,R,D,T,T&"
								+ "T,R,R,T,T,T,T,T,T,T&"
								+ "T,T,T,T,T,T,T,T,T,T&" ,serverResponse2);

			
		}finally{
			if (socket1 != null) {
				socket1.close();
			}
			if (socket2 != null) {
				socket2.close();
			}
		}
	}
	
}
