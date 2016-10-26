package ephec.nsjc.httpserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.Policy;

import org.apache.log4j.Logger;

import ephec.nsjc.httpserver.assets.ServerConfiguration;
import ephec.nsjc.httpserver.connection.ConnectionHandler;
import ephec.nsjc.httpserver.events.EventManager;
import ephec.nsjc.httpserver.plugin.PluginSandboxingSecurity;
import ephec.nsjc.httpserver.resources.Configuration;

public class HTTPServer {
	
	public static final String httpVersion = "HTTP/1.0";
	private static final Logger log = Logger.getLogger(HTTPServer.class);
	
	private static EventManager evtManager;
	private static HTTPServer INSTANCE;
	
	private ServerConfiguration serverConfig;
	private int port;
	private InetAddress address;
	private ConnectionHandler handler;
	private Thread handlerThread;
	private boolean ready;
	private String pwd;
	
	/**
	 * Initialise le {@link EventManager}, le serveur HTTP et le met à l'écoute sur l'interface locale par défaut.
	 * 
	 * @param port Le port sur lequel le serveur va écouter
	 * @throws IOException Venant de {@link java.net.ServerSocket#ServerSocket(int, int, InetAddress)}
	 */
	public HTTPServer(int port) throws IOException {
		this(port, InetAddress.getLocalHost());
	}
	
	/**
	 * Initialise le {@link EventManager}, le serveur HTTP et le met à l'écoute sur l'interface locale par défaut.
	 * 
	 * @param port Le port sur lequel le serveur va écouter != 0
	 * @param address L'adresse de l'interface du serveur != null
	 * @throws IOException Venant de {@link java.net.ServerSocket#ServerSocket(int, int, InetAddress)}
	 */
	public HTTPServer(int port, InetAddress address) throws IOException {
		
		CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
		File jarFile = null;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			
		}
		this.pwd = jarFile.getParentFile().getPath();
		
		doDirectories();
		loadConfig();
		
		Policy.setPolicy(new PluginSandboxingSecurity());
	    System.setSecurityManager(new SecurityManager());
	    
	    
		
		INSTANCE = this;
		this.port = port;
		this.address = address;
		evtManager = EventManager.getInstance();
		this.handler = new ConnectionHandler(this, port, address);
		this.handlerThread = new Thread(this.handler);
		this.handlerThread.start();
		this.ready = false;
	}

	
	private void loadConfig() throws FileNotFoundException {
		File configFile = new File(this.getWorkDir()+"/config.yml");
		Configuration<ServerConfiguration> loader = new Configuration<ServerConfiguration>(ServerConfiguration.class);
		this.serverConfig = loader.load(configFile);
	}

	public boolean isReady(){
		return ready;
	}
	
	public void setReady(boolean r){
		this.ready = r;
	}
	/**
	 * 
	 * @return le port du serveur
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @return l'adresse du serveur
	 */
	public InetAddress getAddress() {
		return address;
	}
	/**
	 * 
	 * @return l'instance courante du serveur
	 */
	public static HTTPServer getServer(){
		return INSTANCE;
	}
	
	/**
	 * 
	 * @return le gestionnaire d'évenement.
	 */
	public static EventManager getEventManager(){
		return evtManager;
	}
	
	/**
	 * Stop le serveur
	 * @throws IOException Erreur lors de la fermeture du socket
	 */
	public void stop() throws IOException{
		System.out.println("Stopping server");
		this.handler.setRunning(false);
		try {
			this.handler.getServerSocket().close();
		} catch (IOException e) {
			throw e;
		}
	}
	
	public String getWorkDir(){
		return this.pwd;
	}
	
	public void doDirectories(){
		File pluginDir = new File(this.getWorkDir()+"/plugins");
		File configFile = new File(this.getWorkDir()+"/config.yml");
		
		if(!pluginDir.exists() || !pluginDir.isDirectory()){
			pluginDir.mkdirs();
		}
		if(!configFile.exists() || !configFile.isFile()){
			InputStream fStream = HTTPServer.class.getResourceAsStream("/ephec/nsjc/httpserver/assets/default-config.yml");
			
			try 
			{
				FileOutputStream fos = new FileOutputStream(configFile);
			    byte[] buf = new byte[2048];
			    int r;
			    while(-1 != (r = fStream.read(buf))) {
			        fos.write(buf, 0, r);
			    }
			    fos.close();
			}catch (IOException e) {
				// TODO: handle exception
			}
		}
		
	}
	
	public ServerConfiguration getConfiguration(){
		return this.serverConfig;
	}
	

	
	
	
	
}
