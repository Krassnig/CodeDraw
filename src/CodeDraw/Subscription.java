package CodeDraw;

/**
 * A Subscription can be used to stop the triggering of an EventHandler at a later time.
 */
public interface Subscription {
	/**
	 * Stops the triggering of the EventHandler passed on to onEventName (e.g. onMouseClick).
	 */
	void unsubscribe();
}
