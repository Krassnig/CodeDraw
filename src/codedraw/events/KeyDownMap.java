package codedraw.events;

import codedraw.CodeDraw;

import java.util.HashMap;

public class KeyDownMap {
	public KeyDownMap(Event<CodeDraw, KeyEventArgs> keyDownEvent) {
		this.event = keyDownEvent;
	}

	private Event<CodeDraw, KeyEventArgs> event;
	private HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEventArgs keyEvent) {
		Integer keyCode = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(keyCode)) {
			map.put(keyCode, true);
			event.invoke(keyEvent);
		}
	}

	public void keyRelease(KeyEventArgs keyEvent) {
		map.put(keyEvent.getExtendedKeyCode(), false);
	}

	private boolean isKeyAlreadyPressed(Integer extendedKeyCode) {
		return map.getOrDefault(extendedKeyCode, false);
	}
}
