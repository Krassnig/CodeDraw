package codedraw.events;

import java.awt.event.KeyEvent;

/**
 * This argument is given exactly once every time a key is pressed down.
 */
public class KeyDownEvent extends Event {
	KeyDownEvent(KeyEvent e) {
		this.e = e;
		key = Key.getKeyFromKeyCode(e.getKeyCode());
	}

	private final KeyEvent e;
	private final Key key;

	/**
	 * Gets the key that was pressed to trigger the event.
	 * If the key is not found or unknown the key value {@link Key#UNDEFINED} will be used.
	 * @return a key.
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * Gets the character representation of the key that was pressed.
	 * For example pressing the A key + SHIFT will result in an 'A',
	 * but without pressing SHIFT will result in a lowercase 'a'.
	 * @return a char.
	 */
	public char getChar() {
		return e.getKeyChar();
	}

	/**
	 * Returns the key code of the key pressed.
	 * The extended key code can be used to uniquely identify a key on a keyboard.
	 * @return the key code.
	 */
	public int getExtendedKeyCode() {
		return e.getExtendedKeyCode();
	}

	/**
	 * Gets whether control was pressed.
	 * @return true if control was pressed, otherwise false.
	 */
	public boolean isControlDown() {
		return e.isControlDown();
	}

	/**
	 * Gets whether alt was pressed.
	 * @return true if alt was pressed, otherwise false.
	 */
	public boolean isAltDown() {
		return e.isAltDown();
	}

	/**
	 * Gets whether alt graph was pressed.
	 * @return true if alt graph was pressed, otherwise false.
	 */
	public boolean isAltGraphDown() {
		return e.isAltGraphDown();
	}

	/**
	 * Gets whether shift was pressed.
	 * @return true if shift was pressed, otherwise false.
	 */
	public boolean isShiftDown() {
		return e.isShiftDown();
	}

	@Override
	public String toString() {
		return "[Key: " + toControlString() + getKey() + ", Char: " + getChar() + "]";
	}

	private String toControlString() {
		return "" +
			(isControlDown() && this.key != Key.CONTROL ? "CTRL + " : "") +
			(isShiftDown() && this.key != Key.SHIFT ? "SHIFT + " : "") +
			(isAltDown() && this.key != Key.ALT ? "ALT +" : "") +
			(isAltGraphDown() && this.key != Key.ALT_GRAPH ? "ALT GR + " : "");
	}
}
