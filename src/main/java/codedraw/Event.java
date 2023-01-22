package codedraw;

import java.time.OffsetDateTime;

/**
 * Represents an abstract super type of the event classes in CodeDraw.
 */
public abstract class Event {
	Event() { }

	private final OffsetDateTime timeCreated = OffsetDateTime.now();

	/**
	 * Gets the point in time when this event was created.
	 * @return the time this event was created.
	 */
	public OffsetDateTime getTimeCreated() {
		return timeCreated;
	}
}
