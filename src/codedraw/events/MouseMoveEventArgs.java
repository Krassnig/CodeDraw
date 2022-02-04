package codedraw.events;

import java.awt.event.MouseEvent;

public class MouseMoveEventArgs {
	public MouseMoveEventArgs(MouseEvent e) {
		this.e = e;
	}

	private final MouseEvent e;

	public int getX() {
		return e.getX();
	}

	public int getY() {
		return e.getY();
	}
}
