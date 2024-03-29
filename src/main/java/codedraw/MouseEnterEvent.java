package codedraw;

import java.awt.event.MouseEvent;

/**
 * This argument is given every time the mouse enters the canvas.
 */
public class MouseEnterEvent extends Event {
	MouseEnterEvent(MouseEvent e) {
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

	@Override
	public String toString() {
		return "[Enter: (" + getX() + ", " + getY() + ")]";
	}
}
