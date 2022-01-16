package codedraw.events;

import codedraw.CodeDraw;

import java.util.function.Function;

public class EventAwaiter<T> implements AutoCloseable {
	public static <T> T waitFor(Function<EventHandler<CodeDraw, T>, Subscription> eventMethod) {
		try (EventAwaiter<T> eventAwaiter = new EventAwaiter<>(eventMethod)) {
			return eventAwaiter.waitForNextEvent();
		}
	}

	public EventAwaiter(Function<EventHandler<CodeDraw, T>, Subscription> eventMethod) {
		queue = new ConcurrentQueue<>();
		subscription = eventMethod.apply((c, a) -> queue.push(a));
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
