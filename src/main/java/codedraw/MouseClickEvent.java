package codedraw;

import java.awt.event.MouseEvent;

/**
 * This argument is given once every time a mouse button is pressed down and quickly released again.
 */
public class MouseClickEvent extends Event {
	MouseClickEvent(MouseEvent e) {
		if (e.getButton() == MouseEvent.NOBUTTON) {
			throw new RuntimeException("MouseClickEvent was created from awt.event.MouseEvent even though there no button was pressed.");
		}
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
		// must be e.getButton() - 1 because e.getButton() == 0 is for mouse events where no button was pressed
		return MouseButton.values()[e.getButton() - 1];
	}

	@Override
	public String toString() {
		return "[Click: " + getMouseButton() + " at (" + getX() + ", " + getY() + ")]";
	}
}
