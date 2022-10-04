package codedraw.events;

import java.awt.event.MouseEvent;

/**
 * This argument is given once every time a mouse button is pressed down and quickly released again.
 */
public class MouseClickEvent extends Event {
	MouseClickEvent(MouseEvent e) {
		this.e = e;
	}

	private final MouseEvent e;

	/**
	 * Gets the distance in pixel from the left side of the canvas to the mouse.
	 * @return the distance in pixel.
	 */
	public int getX() {
		return e.getX();
	}

	/**
	 * Gets the distance in pixel from the top side of the canvas to the mouse.
	 * @return the distance in pixel.
	 */
	public int getY() {
		return e.getY();
	}

	/**
	 * Gets the type of mouse button that was pressed to trigger the event.
	 * @return a mouse button.
	 */
	public MouseButton getMouseButton() {
		return MouseButton.values()[e.getButton() - 1];
	}

	@Override
	public String toString() {
		return "[Click: " + getMouseButton() + " at (" + getX() + ", " + getY() + ")]";
	}
}
