package codedraw.events;

/**
 * This argument is given each time the mouse wheel is turned.
 */
public class MouseWheelEvent {
	MouseWheelEvent(java.awt.event.MouseWheelEvent e) {
		this.e = e;
	}

	private final java.awt.event.MouseWheelEvent e;

	/**
	 * Returns the amount of wheel rotation. One rotation is one click on the mouse.
	 * For high resolution scrolling there are fractional rotations.
	 * Positive values are returned for scrolling towards the user,
	 * negative values when scrolling away from the user.
	 * @return the wheel rotation.
	 */
	public double getWheelRotation() {
		return e.getPreciseWheelRotation();
	}
}
