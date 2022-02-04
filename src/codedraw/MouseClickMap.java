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

	public void mouseReleased(MouseEvent mouseEvent) {
		if (lastMouseDown != null) {
			int deltaX = Math.abs(lastMouseDown.getX() - mouseEvent.getX());
			int deltaY = Math.abs(lastMouseDown.getY() - mouseEvent.getY());
			int delta = deltaX * deltaX + deltaY * deltaY;

			if (delta < 38 * 38) {
				mouseClickEvent.invoke(new MouseClickEventArgs(mouseEvent));
			}
		}
	}
}
