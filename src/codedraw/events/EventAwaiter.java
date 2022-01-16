package codedraw.events;

import java.util.function.Function;

public class EventAwaiter<T> implements AutoCloseable {
	public static <T> T waitFor(Function<EventHandler<T>, Subscription> eventMethod) {
		try (EventAwaiter<T> eventAwaiter = new EventAwaiter<>(eventMethod)) {
			return eventAwaiter.waitForNextEvent();
		}
	}

	public EventAwaiter(Function<EventHandler<T>, Subscription> eventMethod) {
		queue = new ConcurrentQueue<>();
		subscription = eventMethod.apply(queue::push);
	}

	private ConcurrentQueue<T> queue;
	private Subscription subscription;

	public boolean hasEventNow() {
		return queue.canPop();
	}

	public T waitForNextEvent() {
		return queue.pop();
	}

	@Override
	public void close() {
		subscription.unsubscribe();
	}
}
