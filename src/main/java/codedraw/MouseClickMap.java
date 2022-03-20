package codedraw;

import codedraw.events.MouseClickEvent;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

class MouseClickMap {
	public MouseClickMap(Consumer<Object> queue) {
		this.queue = queue;
	}

	private final Consumer<Object> queue;
	private MouseEvent lastMouseDown;

	public void mousePressed(MouseEvent mouseEvent) {
		lastMouseDown = mouseEvent;
	}

	public void mouseMoved(MouseEvent mouseEvent) {
		if (lastMouseDown != null && calculateDelta(lastMouseDown, mouseEvent) > 38 * 38) {
			lastMouseDown = null;
		}
	}

	public void mouseReleased(MouseEvent mouseEvent) {
		if (lastMouseDown != null) {
			if (calculateDelta(lastMouseDown, mouseEvent) < 38 * 38) {
				queue.accept(new MouseClickEvent(mouseEvent));
			}
		}
	}

	private static int calculateDelta(MouseEvent a, MouseEvent b) {
		int deltaX = Math.abs(a.getX() - b.getX());
		int deltaY = Math.abs(a.getY() - b.getY());
		return deltaX * deltaX + deltaY * deltaY;
	}
}
