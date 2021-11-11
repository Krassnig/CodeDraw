package codedraw.events;

import java.awt.event.MouseWheelEvent;

public class MouseWheelEventArgs {
	public MouseWheelEventArgs(MouseWheelEvent e) {
		this.e = e;
	}

	private MouseWheelEvent e;

	public int getWheelRotation() {
		return e.getWheelRotation();
	}
}
