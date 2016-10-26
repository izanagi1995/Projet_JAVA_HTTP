package ephec.nsjc.httpserver.events;

import org.apache.log4j.Logger;

import ephec.nsjc.httpserver.plugin.EventListener;

/**
 * Classe de base des évenements
 * @author Nicolas
 *
 */
public abstract class Event {
		
	private boolean cancel = false;

	private String eventName;
	
	public Event(String eventName){
		this.eventName = eventName;
	}
	
	public boolean isCancelled(){
		return cancel;
	}
	
	public void setCancelled(boolean cancel){
		this.cancel = cancel;
	}
}
