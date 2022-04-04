package codedraw;

import java.awt.*;

class PositionExtension {
	public PositionExtension(Frame frame) {
		this.frame = frame;

		updateWindowAndCanvasPosition();
	}

	private final Frame frame;
	private final Semaphore positionLock = new Semaphore(1);

	private Point windowPosition;
	private Point distanceFromWindowToCanvas;

	public Point getWindowPosition() {
		positionLock.acquire();
		Point result = windowPosition.getLocation();
		positionLock.release();
		return result;
	}

	public void setWindowPosition(Point newWindowPosition) {
		positionLock.acquire();
		setWindowPositionInternal(newWindowPosition);
		positionLock.release();
	}

	public Point getCanvasPosition() {
		positionLock.acquire();
		Point result = plus(windowPosition, distanceFromWindowToCanvas);
		positionLock.release();
		return result;
	}

	public void setCanvasPosition(Point newCanvasPosition) {
		positionLock.acquire();
		setWindowPositionInternal(minus(newCanvasPosition, distanceFromWindowToCanvas));
		positionLock.release();
	}

	public void updateWindowAndCanvasPosition() {
		positionLock.acquire();
		this.windowPosition = frame.getLocationOnScreen().getLocation();
		distanceFromWindowToCanvas = minus(frame.getPanel().getLocationOnScreen(), windowPosition);
		positionLock.release();
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
