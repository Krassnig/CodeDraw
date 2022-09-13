package codedraw.events;

import java.awt.event.KeyEvent;
import java.util.HashMap;

class KeyDownMap {
	public KeyDownMap(EventScanner eventScanner, CanvasPanel panel) {
		this.eventScanner = eventScanner;
		this.panel = panel;
	}

	private final CanvasPanel panel;
	private final EventScanner eventScanner;
	private final HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEvent keyEvent) {
		Integer keyCode = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(keyCode)) {
			map.put(keyCode, true);
			KeyDownEvent a = new KeyDownEvent(keyEvent);
			checkForCopyCanvas(a);
			eventScanner.push(a);
		}
	}

	private void checkForCopyCanvas(KeyDownEvent a) {
		if (a.getKey() == Key.C && a.isControlDown()) {
			panel.copyCanvasToClipboard();
		}
	}

	public void keyRelease(KeyEvent keyEvent) {
		map.put(keyEvent.getExtendedKeyCode(), false);
	}

	private boolean isKeyAlreadyPressed(Integer extendedKeyCode) {
		return map.getOrDefault(extendedKeyCode, false);
	}
}
