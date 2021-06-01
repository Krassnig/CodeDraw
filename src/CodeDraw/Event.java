package CodeDraw;

import java.util.ArrayList;

class Event<TSender, TArgs> {
	public Event(TSender sender) {
		this.sender = sender;
	}

	private TSender sender;
	private ArrayList<EventHandler<TSender, TArgs>> subscribers = new ArrayList<>();

	public void invoke(TArgs args) {
		for (EventHandler<TSender, TArgs> subscriber : new ArrayList<>(subscribers)) {
			subscriber.handle(sender, args);
		}
	}

	public Unsubscribe onInvoke(EventHandler<TSender, TArgs> handler) {
		this.subscribers.add(handler);
		return () -> this.subscribers.remove(handler);
	}
}
