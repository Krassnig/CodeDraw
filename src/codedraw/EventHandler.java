package codedraw;

/**
 * A function that specifies what should happen when a specific event occurs.
 * @param <TSender> The class that triggers the event.
 * @param <TArgs> Arguments of the event.
 */
public interface EventHandler<TSender, TArgs> {
	void handle(TSender sender, TArgs args);
}
