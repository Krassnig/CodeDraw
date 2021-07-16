package codedraw;

import java.awt.event.KeyEvent;
import java.util.HashMap;

class KeyDownMap<TSender> {
	public KeyDownMap(Event<TSender, KeyEvent> keyDownEvent) {
		this.event = keyDownEvent;
	}

	private Event<TSender, KeyEvent> event;
	private HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEvent keyEvent) {
		Integer i = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(i)) {
			map.put(i, true);
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
