package codedraw.events;

/**
 * A function that specifies what should happen when a specific event occurs.
 * @param <TArgs> Argument of the event.
 */
public interface EventHandler<TArgs> {
	/**
	 * This function will generally be called by internal events and not by the user of this library.
	 * @param args Argument of the event.
	 */
	void handle(TArgs args);
}
