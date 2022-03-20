package codedraw;

import codedraw.events.Key;
import codedraw.events.KeyDownEvent;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.function.Consumer;

class KeyDownMap {
	public KeyDownMap(Consumer<Object> queue, GuiExtension guiExtension) {
		this.queue = queue;
		this.guiExtension = guiExtension;
	}

	private final GuiExtension guiExtension;
	private final Consumer<Object> queue;
	private final HashMap<Integer, Boolean> map = new HashMap<>();

	public void keyPress(KeyEvent keyEvent) {
		Integer keyCode = keyEvent.getExtendedKeyCode();

		if (!isKeyAlreadyPressed(keyCode)) {
			map.put(keyCode, true);
			KeyDownEvent a = new KeyDownEvent(keyEvent);
			if (a.getKey() == Key.C && a.isShiftDown()) {
				guiExtension.copyCanvasToClipboard();
			}
			queue.accept(a);
		}
	}

	public void keyRelease(KeyEvent keyEvent) {
		map.put(keyEvent.getExtendedKeyCode(), false);
	}

	private boolean isKeyAlreadyPressed(Integer extendedKeyCode) {
		return map.getOrDefault(extendedKeyCode, false);
	}
}
