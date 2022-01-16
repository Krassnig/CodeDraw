package codedraw;

import codedraw.events.EventHandler;
import codedraw.events.Subscription;

import java.util.ArrayList;

class Event<TArgs> {
	private static EventLoop eventLoop = new EventLoop();

	public Event() { }

	private ArrayList<EventHandler<TArgs>> subscribers = new ArrayList<>();
	private Semaphore subscriberLock = new Semaphore(1);

	public void invoke(TArgs args) {
		eventLoop.queue(() -> invokeAll(args));
	}

	private void invokeAll(TArgs args) {
		for (EventHandler<TArgs> subscriber : getSubscribers()) {
			subscriber.handle(args);
		}
	}

	public Subscription onInvoke(EventHandler<TArgs> handler) {
		subscribe(handler);
		return () -> unsubscribe(handler);
	}

	private void subscribe(EventHandler<TArgs> handler) {
		subscriberLock.acquire();
		subscribers.add(handler);
		subscriberLock.release();
	}

	private void unsubscribe(EventHandler<TArgs> handler) {
		subscriberLock.acquire();
		subscribers.remove(handler);
		subscriberLock.release();
	}

	private ArrayList<EventHandler<TArgs>> getSubscribers() {
		subscriberLock.acquire();
		ArrayList<EventHandler<TArgs>> result = new ArrayList<>(subscribers);
		subscriberLock.release();
		return result;
	}
}
