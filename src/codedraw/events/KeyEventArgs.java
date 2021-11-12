package codedraw.events;

import java.awt.event.KeyEvent;

public class KeyEventArgs {
	public KeyEventArgs(KeyEvent e) {
		this.e = e;
		for (Key k : Key.values()) {
			if (k.getKeyCode() == e.getKeyCode()) {
				key = k;
			}
		}
	}

	private KeyEvent e;
	private Key key = null;

	public Key getKey() {
		return key;
	}

	public char getChar() {
		return e.getKeyChar();
	}

	public int getExtendedKeyCode() {
		return e.getExtendedKeyCode();
	}

	public boolean isControlDown() {
		return e.isControlDown();
	}

	public boolean isAltDown() {
		return e.isAltDown();
	}

	public boolean isAltGraphDown() {
		return e.isAltGraphDown();
	}

	public boolean isShiftDown() {
		return e.isShiftDown();
	}

	public boolean isMetaDown() {
		return e.isMetaDown();
	}
}
