package codedraw.events;

import javax.swing.*;
import java.awt.*;

class PositionExtension {
	public PositionExtension(JFrame frame, CanvasPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}

	private final JFrame frame;
	private final CanvasPanel panel;
	private final Semaphore positionLock = new Semaphore(1);

	private Point windowPosition;
	private Point distanceFromWindowToCanvas;

	public Point getWindowPosition() {
		updatePositionIfNull();

		positionLock.acquire();
		Point result = windowPosition.getLocation();
		positionLock.release();
		return result;
	}

	public void setWindowPosition(Point newWindowPosition) {
		updatePositionIfNull();

		positionLock.acquire();
		setWindowPositionInternal(newWindowPosition);
		positionLock.release();
	}

	public Point getCanvasPosition() {
		updatePositionIfNull();

		positionLock.acquire();
		Point result = plus(windowPosition, distanceFromWindowToCanvas);
		positionLock.release();
		return result;
	}

	public void setCanvasPosition(Point newCanvasPosition) {
		updatePositionIfNull();

		positionLock.acquire();
		setWindowPositionInternal(minus(newCanvasPosition, distanceFromWindowToCanvas));
		positionLock.release();
	}

	public void updateWindowAndCanvasPosition() {
		positionLock.acquire();
		this.windowPosition = frame.getLocationOnScreen().getLocation();
		distanceFromWindowToCanvas = minus(panel.getLocationOnScreen(), windowPosition);
		positionLock.release();
	}

	private void updatePositionIfNull() {
		if (windowPosition == null) {
			updateWindowAndCanvasPosition();
		}
	}

	private void setWindowPositionInternal(Point newWindowPosition) {
		windowPosition = newWindowPosition.getLocation();
		frame.setLocation(windowPosition);
	}

	private static Point minus(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	private static Point plus(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
}
