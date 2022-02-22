package codedraw;

import codedraw.events.KeyDownEvent;

import java.awt.event.KeyEvent;
import java.util.HashMap;

class KeyDownMap {
	public KeyDownMap(Event<KeyDownEvent> keyDownEvent) {
		this.event = keyDownEvent;
	}

	private final Event<KeyDownEvent> event;
	private final HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEvent keyEvent) {
		Integer keyCode = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(keyCode)) {
			map.put(keyCode, true);
			event.invoke(new KeyDownEvent(keyEvent));
		}
	}

	public void keyRelease(KeyEvent keyEvent) {
		map.put(keyEvent.getExtendedKeyCode(), false);
	}

	private boolean isKeyAlreadyPressed(Integer extendedKeyCode) {
		return map.getOrDefault(extendedKeyCode, false);
	}
}
