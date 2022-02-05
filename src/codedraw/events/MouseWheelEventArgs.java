package codedraw.events;

import java.awt.event.MouseWheelEvent;

/**
 * This argument is given each time the mouse wheel is turned.
 */
public class MouseWheelEventArgs {
	public MouseWheelEventArgs(MouseWheelEvent e) {
		this.e = e;
	}

	private final MouseWheelEvent e;

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
