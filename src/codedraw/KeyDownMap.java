package codedraw;

import java.awt.event.KeyEvent;
import java.util.HashMap;

class KeyDownMap {
	public KeyDownMap(Event<CodeDraw, KeyEvent> keyDownEvent) {
		this.event = keyDownEvent;
	}

	private Event<CodeDraw, KeyEvent> event;
	private HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEvent keyEvent) {
		Integer keyCode = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(keyCode)) {
			map.put(keyCode, true);
			event.invoke(keyEvent);
		}
	}

	public void keyRelease(KeyEvent keyEvent) {
		map.put(keyEvent.getExtendedKeyCode(), false);
	}

	private boolean isKeyAlreadyPressed(Integer extendedKeyCode) {
		return map.getOrDefault(extendedKeyCode, false);
	}
}
