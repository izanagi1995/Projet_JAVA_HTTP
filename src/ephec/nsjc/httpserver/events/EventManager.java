package ephec.nsjc.httpserver.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ephec.nsjc.httpserver.plugin.EventListener;

public class EventManager {
	
	private Map<Class<? extends Event>, ArrayList<EventListener>> listeners;
	
	private static EventManager INSTANCE = new EventManager();
	
	private EventManager(){
		this.listeners = new HashMap<Class<? extends Event>, ArrayList<EventListener>>();
	}
	
	/**
	 * Retourne l'instance courante du gestionnaire d'évenement.
	 * @return
	 */
	public static EventManager getInstance(){
		return INSTANCE;
	}
	
	/**
	 * Enregistre un listener d'evenement
	 * @param clz Le type d'évènement à écouter.
	 * @param l Le listener.
	 */
	public void register(Class<? extends Event> clz, EventListener l){
		ArrayList<EventListener> listeners = this.listeners.get(clz);
		if(listeners == null){
			listeners = new ArrayList<EventListener>();
		}
		listeners.add(l);
		this.listeners.put(clz, listeners);
	}
	
	/**
	 * Distribue un event aux listeners
	 * @param event l'évenement
	 * @throws IllegalArgumentException Si un listener de la liste n'a pas de méthode pour la reception de l'event
	 * @throws IllegalAccessException Erreur provenant de la reflexion API, est jetée si un accès à une propriété ou une méthode non accessible.
	 * @throws InvocationTargetException Erreur provenant de la reflexion API, est jetée si une exception a lieu lors de l'exécution de la méthode 
	 */
	public void callEvent(Event event) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		ArrayList<EventListener> eventListenerList = this.listeners.get(event.getClass());
		if(eventListenerList == null){
			return;
		}
		for(EventListener evtList : eventListenerList){
			Method[] meths = evtList.getClass().getMethods();
			Method callMethod = null;
			for(Method m : meths){
				if(m.getParameterCount() == 1 && m.getParameterTypes()[0] == event.getClass()){
					callMethod = m;
					break;
				}
			}
			if(callMethod == null){
				throw new IllegalArgumentException("Event Listener "+evtList.getClass().getName() + "has no method for event type "+event.getClass().getName());
			}else{
				callMethod.invoke(evtList, event);
			}
		}
	}
}
