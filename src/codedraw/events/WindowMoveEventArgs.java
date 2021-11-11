package codedraw.events;

import java.awt.*;

public class WindowMoveEventArgs {
	public WindowMoveEventArgs(Point canvasPosition, Point windowPosition) {
		this.canvasPosition = canvasPosition;
		this.windowPosition = windowPosition;
	}

	private Point canvasPosition;
	private Point windowPosition;

	public int getCanvasPositionX() {
		return canvasPosition.x;
	}

	public int getCanvasPositionY() {
		return canvasPosition.y;
	}

	public int getWindowPositionX() {
		return windowPosition.x;
	}

	public int getWindowPositionY() {
		return windowPosition.y;
	}
}
