package codedraw.events;

import java.awt.*;

/**
 * This argument is given every time the CodeDraw window is moved.
 */
public class WindowMoveEventArgs {
	public WindowMoveEventArgs(Point canvasPosition, Point windowPosition) {
		this.canvasPosition = canvasPosition.getLocation();
		this.windowPosition = windowPosition.getLocation();
	}

	private final Point canvasPosition;
	private final Point windowPosition;

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the left side of the main screen to the left of the CodeDraw canvas.
	 */
	public int getCanvasPositionX() {
		return canvasPosition.x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the top side of the main screen to the top of the CodeDraw canvas.
	 */
	public int getCanvasPositionY() {
		return canvasPosition.y;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the left side of the main screen to the left of the CodeDraw window.
	 */
	public int getWindowPositionX() {
		return windowPosition.x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the top side of the main screen to the top of the CodeDraw window.
	 */
	public int getWindowPositionY() {
		return windowPosition.y;
	}
}
