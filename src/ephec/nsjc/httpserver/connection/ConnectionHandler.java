package ephec.nsjc.httpserver.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

import ephec.nsjc.httpserver.HTTPServer;
import ephec.nsjc.httpserver.events.ServerListeningEvent;
import ephec.nsjc.httpserver.exceptions.ParserException;
import ephec.nsjc.httpserver.plugin.EventListener;
import ephec.nsjc.httpserver.protocol.Request;
import ephec.nsjc.httpserver.protocol.RequestParser;
import ephec.nsjc.httpserver.protocol.Response;

public class ConnectionHandler implements Runnable{
	
	private static final Logger log = Logger.getLogger(ConnectionHandler.class);
	
	private HTTPServer server;
	private ServerSocket ss;
	private Socket s;
	private volatile boolean running = true;
	
	/**
	 * Gestionnaire de connexions du serveur HTTP. Ecoute et traite les requêtes.
	 * 
	 * @param port le port sur lequel écouter
	 * @param address l'adresse sur laquelle écouter
	 * @throws IOException Venant de {@link java.net.ServerSocket#ServerSocket(int, int, InetAddress)}
	 */
	public ConnectionHandler(HTTPServer server, int port, InetAddress address) throws IOException {
		this.server = server;
		this.ss = new ServerSocket(port, 1024, address);
	}

	@Override
	public void run() {
		ServerListeningEvent sle = new ServerListeningEvent();
		
		try {
			HTTPServer.getEventManager().callEvent(sle);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try{
			this.server.setReady(true);
			while(running){
				s = ss.accept();
				RequestParser parser = new RequestParser(new BufferedReader(new InputStreamReader(s.getInputStream())));
				Request req = parser.parse();
				RequestHandler reqHandler = new RequestHandler(req);
				Response response = reqHandler.handleRequest();
				response.send(s);
				s.close();
			}
			
		}catch(IOException e){
			if(!(e instanceof SocketException)){
				e.printStackTrace();
			}else{
				System.out.println("Got normal exception");
				e.printStackTrace();
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("Server stopped");
			this.server.setReady(false);
		}
		
		
	}

	public void setRunning(boolean b) {
		System.out.println("Server state = "+b);
		this.running = b;
	}

	public ServerSocket getServerSocket() {
		return this.ss;
	}
	
}
