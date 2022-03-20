package codedraw;

import javax.swing.*;
import java.awt.*;

class GuiExtension {
	public GuiExtension(JFrame frame, CanvasPanel panel) {
		this.frame = frame;
		this.panel = panel;

		updateWindowAndCanvasPosition();
	}

	private final JFrame frame;
	private final CanvasPanel panel;
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
		distanceFromWindowToCanvas = minus(panel.getLocationOnScreen(), windowPosition);
		positionLock.release();
	}

	public void copyCanvasToClipboard() {
		panel.copyCanvasToClipboard();
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
