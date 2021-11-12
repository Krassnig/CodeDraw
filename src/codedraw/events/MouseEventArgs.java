package codedraw.events;

import java.awt.event.MouseEvent;

public class MouseEventArgs {
	public MouseEventArgs(MouseEvent e) {
		this.e = e;
	}

	private MouseEvent e;

	public int getX() {
		return e.getX();
	}

	public int getY() {
		return e.getY();
	}

	public MouseButton getMouseButtonType() {
		return MouseButton.values()[e.getButton()];
	}
}
