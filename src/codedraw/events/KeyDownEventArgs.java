package codedraw.events;

import java.awt.event.KeyEvent;

public class KeyDownEventArgs {
	public KeyDownEventArgs(KeyEvent e) {
		this.e = e;
		key = Key.getKeyFromKeyCode(e.getKeyCode());
	}

	private KeyEvent e;
	private Key key;

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
