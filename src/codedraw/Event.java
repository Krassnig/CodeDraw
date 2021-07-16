package codedraw;

import java.util.ArrayList;

class Event<TSender, TArgs> {
	public Event(TSender sender) {
		this.sender = sender;
	}

	private TSender sender;
	private ArrayList<EventHandler<TSender, TArgs>> subscribers = new ArrayList<>();
	private Semaphore subscriberLock = new Semaphore(1);

	public void invoke(TArgs args) {
		for (EventHandler<TSender, TArgs> subscriber : getSubscribers()) {
			subscriber.handle(sender, args);
		}
	}

	public Subscription onInvoke(EventHandler<TSender, TArgs> handler) {
		subscribe(handler);
		return () -> unsubscribe(handler);
	}

	private void subscribe(EventHandler<TSender, TArgs> handler) {
		subscriberLock.acquire();
		subscribers.add(handler);
		subscriberLock.release();
	}

	private void unsubscribe(EventHandler<TSender, TArgs> handler) {
		subscriberLock.acquire();
		subscribers.remove(handler);
		subscriberLock.release();
	}

	private ArrayList<EventHandler<TSender, TArgs>> getSubscribers() {
		subscriberLock.acquire();
		ArrayList<EventHandler<TSender, TArgs>> result = new ArrayList<>(subscribers);
		subscriberLock.release();
		return result;
	}
}
