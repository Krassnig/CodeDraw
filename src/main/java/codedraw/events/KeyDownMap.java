package codedraw.events;

import java.awt.event.KeyEvent;
import java.util.HashMap;

class KeyDownMap {
	public KeyDownMap(ConcurrentQueue<Object> queue, CanvasPanel panel) {
		this.queue = queue;
		this.panel = panel;
	}

	private final CanvasPanel panel;
	private final ConcurrentQueue<Object> queue;
	private final HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEvent keyEvent) {
		Integer keyCode = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(keyCode)) {
			map.put(keyCode, true);
			KeyDownEvent a = new KeyDownEvent(keyEvent);
			checkForCopyCanvas(a);
			queue.push(a);
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
