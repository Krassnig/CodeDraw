package codedraw.events;

import java.awt.event.MouseWheelEvent;

public class MouseWheelEventArgs {
	public MouseWheelEventArgs(MouseWheelEvent e) {
		this.e = e;
	}

	private MouseWheelEvent e;

	public double getWheelRotation() {
		return e.getPreciseWheelRotation();
	}
}
