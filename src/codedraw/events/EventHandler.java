package codedraw.events;

/**
 * A function that specifies what should happen when a specific event occurs.
 * @param <TSender> The object that triggers the event.
 * @param <TArgs> Argument of the event.
 */
public interface EventHandler<TSender, TArgs> {
	/**
	 * This function will generally be called by internal events and not by the user of this library.
	 * @param sender The object that triggers the event.
	 * @param args Argument of the event.
	 */
	void handle(TSender sender, TArgs args);
}
