package CodeDraw;

import java.util.function.BiConsumer;

public interface Event<TSender, TArgs> {
	void subscribe(BiConsumer<TSender, TArgs> subscriber);
	void unsubscribe(BiConsumer<TSender, TArgs> subscriber);
}
