package CodeDraw;

import java.util.ArrayList;
import java.util.function.BiConsumer;

class InternalEvent<TSender, TArgs> implements Event<TSender, TArgs> {
	public InternalEvent(TSender sender) {
		this.sender = sender;
	}

	private TSender sender;
	private ArrayList<BiConsumer<TSender, TArgs>> subscribers = new ArrayList<>();

	public void invoke(TArgs args) {
		for (var subscriber : subscribers) {
			subscriber.accept(sender, args);
		}
	}

	@Override
	public void subscribe(BiConsumer<TSender, TArgs> subscriber) {
		if (subscriber == null) throw new NullPointerException("Subscriber must not be null.");
		this.subscribers.add(subscriber);
	}

	@Override
	public void unsubscribe(BiConsumer<TSender, TArgs> subscriber) {
		if (subscriber == null) throw new NullPointerException("Subscriber must not be null.");
		this.subscribers.remove(subscriber);
	}
}
