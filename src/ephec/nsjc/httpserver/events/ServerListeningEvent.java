package ephec.nsjc.httpserver.events;

import java.net.InetAddress;

import ephec.nsjc.httpserver.HTTPServer;

public class ServerListeningEvent extends Event {
	
	private int port;
	private InetAddress address;
	
	/**
	 * Constructeur avec paramètre
	 * @param port le port du serveur
	 * @param address l'adresse du serveur
	 */
	public ServerListeningEvent(int port, InetAddress address) {
		super("ServerListeningEvent");
		this.port = port;
		this.address = address;
	}
	/**
	 * 
	 */
	public ServerListeningEvent() {
		super("ServerListeningEvent");
		this.port = HTTPServer.getServer().getPort();
		this.address = HTTPServer.getServer().getAddress();
	}
	/**
	 * 
	 * @return le port du serveur concerné par l'évenement
	 */
	public int getPort() {
		return port;
	}
	/**
	 * 
	 * @return l'addresse du serveur
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	
	
	
	
	
}
