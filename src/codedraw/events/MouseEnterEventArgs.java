package codedraw.events;

import java.awt.event.MouseEvent;

public class MouseEnterEventArgs {
	public MouseEnterEventArgs(MouseEvent e) {
		this.e = e;
	}

	private MouseEvent e;

	public int getX() {
		return e.getX();
	}

	public int getY() {
		return e.getY();
	}
}
