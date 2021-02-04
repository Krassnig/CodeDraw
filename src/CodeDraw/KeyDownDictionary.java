package CodeDraw;

import java.awt.event.KeyEvent;
import java.util.HashMap;

class KeyDownDictionary<TSender> {
	public KeyDownDictionary(InternalEvent<TSender, KeyEvent> keyDownEvent) {
		this.event = keyDownEvent;
	}

	private InternalEvent<TSender, KeyEvent> event;
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
