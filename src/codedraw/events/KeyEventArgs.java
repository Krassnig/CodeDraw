package codedraw.events;

import java.awt.event.KeyEvent;

public class KeyEventArgs {
	public KeyEventArgs(KeyEvent e) {
		this.e = e;
	}

	private KeyEvent e;

	public int getExtendedKeyCode() {
		return e.getExtendedKeyCode();
	}

	public boolean isControlDown() {
		return e.isControlDown();
	}

	public int getKeyCode() {
		return e.getKeyCode();
	}
}
