package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.function.Function;

public class EventScanner implements AutoCloseable {
	public EventScanner(CodeDraw codeDraw) {
		this.codeDraw = codeDraw;
		subscriptions = new ArrayList<>(12);
		multiQueue = ConcurrentQueue.createMultiQueue();

		mouseClickQueue = bindEvent(codeDraw::onMouseClick);
		mouseMoveQueue = bindEvent(codeDraw::onMouseMove);
		mouseDownQueue = bindEvent(codeDraw::onMouseDown);
		mouseUpQueue = bindEvent(codeDraw::onMouseUp);
		mouseEnterQueue = bindEvent(codeDraw::onMouseEnter);
		mouseLeaveQueue = bindEvent(codeDraw::onMouseLeave);
		mouseWheelQueue = bindEvent(codeDraw::onMouseWheel);
		keyDownQueue = bindEvent(codeDraw::onKeyDown);
		keyUpQueue = bindEvent(codeDraw::onKeyUp);
		keyPressQueue = bindEvent(codeDraw::onKeyPress);
		windowMoveQueue = bindEvent(codeDraw::onWindowMove);
		windowCloseQueue = bindEvent(codeDraw::onWindowClose);
	}

	private <T> ConcurrentQueue<T> bindEvent(Function<EventHandler<CodeDraw, T>, Subscription> onEvent) {
		ConcurrentQueue<T> queue = multiQueue.newQueue();
		subscriptions.add(onEvent.apply((c, a) -> queue.push(a)));
		return queue;
	}

	private final CodeDraw codeDraw;
	private final ArrayList<Subscription> subscriptions;
	private final ConcurrentQueue.MultiQueue multiQueue;

	private final ConcurrentQueue<MouseClickEventArgs> mouseClickQueue;
	private final ConcurrentQueue<MouseMoveEventArgs> mouseMoveQueue;
	private final ConcurrentQueue<MouseClickEventArgs> mouseDownQueue;
	private final ConcurrentQueue<MouseClickEventArgs> mouseUpQueue;
	private final ConcurrentQueue<MouseMoveEventArgs> mouseEnterQueue;
	private final ConcurrentQueue<MouseMoveEventArgs> mouseLeaveQueue;
	private final ConcurrentQueue<MouseWheelEventArgs> mouseWheelQueue;
	private final ConcurrentQueue<KeyEventArgs> keyDownQueue;
	private final ConcurrentQueue<KeyEventArgs> keyUpQueue;
	private final ConcurrentQueue<KeyEventArgs> keyPressQueue;
	private final ConcurrentQueue<WindowMoveEventArgs> windowMoveQueue;
	private final ConcurrentQueue<Void> windowCloseQueue;

	public boolean hasEvent() {
		return multiQueue.canAcquire();
	}

	public void waitForNextEvent() {
		multiQueue.waitForNext();
	}

	public void close() {
		subscriptions.forEach(Subscription::unsubscribe);
	}

	public boolean hasMouseClickEvent() { return mouseClickQueue.canPop(); }
	public boolean hasMouseMoveEvent() { return mouseMoveQueue.canPop(); }
	public boolean hasMouseDownEvent() { return mouseDownQueue.canPop(); }
	public boolean hasMouseUpEvent() { return mouseUpQueue.canPop(); }
	public boolean hasMouseEnterEvent() { return mouseEnterQueue.canPop(); }
	public boolean hasMouseLeaveEvent() { return mouseLeaveQueue.canPop(); }
	public boolean hasMouseWheelEvent() { return mouseWheelQueue.canPop(); }
	public boolean hasKeyDownEvent() { return keyDownQueue.canPop(); }
	public boolean hasKeyUpEvent() { return keyUpQueue.canPop(); }
	public boolean hasKeyPressEvent() { return keyPressQueue.canPop(); }
	public boolean hasWindowMoveEvent() { return windowMoveQueue.canPop(); }
	public boolean hasWindowCloseEvent() { return windowCloseQueue.canPop(); }

	public MouseClickEventArgs waitForMouseClickEvent() { return mouseClickQueue.pop(); }
	public MouseMoveEventArgs waitForMouseMoveEvent() { return mouseMoveQueue.pop(); }
	public MouseClickEventArgs waitForMouseDownEvent() { return mouseClickQueue.pop(); }
	public MouseClickEventArgs waitForMouseUpEvent() { return mouseUpQueue.pop(); }
	public MouseMoveEventArgs waitForMouseEnterEvent() { return mouseEnterQueue.pop(); }
	public MouseMoveEventArgs waitForMouseLeaveEvent() { return mouseLeaveQueue.pop(); }
	public MouseWheelEventArgs waitForMouseWheelEvent() { return mouseWheelQueue.pop(); }
	public KeyEventArgs waitForKeyDownEvent() { return keyDownQueue.pop(); }
	public KeyEventArgs waitForKeyUpEvent() { return keyUpQueue.pop(); }
	public KeyEventArgs waitForKeyPressEvent() { return keyPressQueue.pop(); }
	public WindowMoveEventArgs waitForWindowMoveEvent() { return windowMoveQueue.pop(); }
	public void waitForWindowCloseEvent() { windowCloseQueue.pop(); }
}
