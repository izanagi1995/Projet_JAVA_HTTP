package ephec.nsjc.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ephec.nsjc.httpserver.HTTPServer;

public class HTTPServerBasicTest {
	
	static HTTPServer theHTTPServer;
	static Socket theTestSocket;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		theHTTPServer = new HTTPServer(12453);
		theTestSocket = new Socket(theHTTPServer.getAddress(), theHTTPServer.getPort());
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		theTestSocket.close();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicServer() {
		while(!theHTTPServer.isReady()){
			Thread.yield();
		}
		assertEquals(true, theTestSocket.isConnected());
		try {
			theHTTPServer.stop();
		} catch (IOException e1) {
			fail(e1.getMessage());
		}
		while(theHTTPServer.isReady()){
			Thread.yield();
		}
		try {
			theTestSocket = new Socket(theHTTPServer.getAddress(), theHTTPServer.getPort());
			fail("Socket should not be able to connect");
		} catch (IOException e) {
			
		}
	}
	

}
