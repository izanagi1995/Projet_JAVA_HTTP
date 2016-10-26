package ephec.nsjc.httpserver;

import java.io.IOException;
import java.net.URISyntaxException;

import ephec.nsjc.httpserver.events.ServerListeningEvent;
import ephec.nsjc.httpserver.plugin.EventListener;

public class Main extends EventListener{
	

	@SuppressWarnings("unused")
	public static void main(String[] args) throws InterruptedException, URISyntaxException {
		Main m = new Main();
		try {
			HTTPServer server = new HTTPServer(8888);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void onListening(ServerListeningEvent e){
		System.out.println("Listening on "+e.getAddress().getHostAddress()+":"+e.getPort());
		
	}
}
