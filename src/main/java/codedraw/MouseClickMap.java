package codedraw;

import codedraw.events.MouseClickEventArgs;

import java.awt.event.MouseEvent;

class MouseClickMap {
	public MouseClickMap(Event<MouseClickEventArgs> mouseClickEvent) {
		this.mouseClickEvent = mouseClickEvent;
	}

	private final Event<MouseClickEventArgs> mouseClickEvent;
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
				mouseClickEvent.invoke(new MouseClickEventArgs(mouseEvent));
			}
		}
	}

	private static int calculateDelta(MouseEvent a, MouseEvent b) {
		int deltaX = Math.abs(a.getX() - b.getX());
		int deltaY = Math.abs(a.getY() - b.getY());
		return deltaX * deltaX + deltaY * deltaY;
	}
}
