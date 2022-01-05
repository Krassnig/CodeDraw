package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static codedraw.events.EventType.*;

public class EventScanner {

	private final Map<EventType, ConcurrentQueue<Object>> eventQueues;
	private final ConcurrentQueue.MultiQueue multiQueue;
	private final List<Subscription> subscriptions = new ArrayList<>();

	public EventScanner(CodeDraw cd) {
		multiQueue = ConcurrentQueue.createMultiQueue();
		eventQueues = new HashMap<>();
		for (EventType type : EventType.values()) {
			eventQueues.put(type, multiQueue.newQueue());
		}

		bindEvents(cd);
	}

	public boolean hasEvent() {
		return multiQueue.canAcquire();
	}

	public boolean hasMouseMoveEvent() {
		return hasEvent(MOUSE_MOVE);
	}

	public boolean hasMouseUpEvent() {
		return hasEvent(MOUSE_UP);
	}

	public boolean hasMouseDownEvent() {
		return hasEvent(MOUSE_DOWN);
	}

	public boolean hasMouseEnterEvent() {
		return hasEvent(MOUSE_ENTER);
	}

	public boolean hasMouseLeaveEvent() {
		return hasEvent(MOUSE_LEAVE);
	}

	public boolean hasMouseWheelEvent() {
		return hasEvent(MOUSE_WHEEL);
	}

	public boolean hasKeyDownEvent() {
		return hasEvent(KEY_DOWN);
	}

	public boolean hasKeyPressEvent() {
		return hasEvent(KEY_PRESS);
	}

	public boolean hasKeyUpEvent() {
		return hasEvent(KEY_UP);
	}

	public boolean hasWindowMoveEvent() {
		return hasEvent(WINDOW_MOVE);
	}

	public MouseMoveEventArgs nextMouseMoveEvent() {
		return popEventArg(MOUSE_MOVE);
	}

	public MouseClickEventArgs waitForMouseUpEvent() {
		return popEventArg(MOUSE_UP);
	}

	public MouseClickEventArgs waitForMouseDownEvent() {
		return popEventArg(MOUSE_DOWN);
	}

	public MouseMoveEventArgs waitForMouseEnterEvent() {
		return popEventArg(MOUSE_ENTER);
	}

	public MouseMoveEventArgs waitForMouseLeaveEvent() {
		return popEventArg(MOUSE_LEAVE);
	}

	public MouseWheelEventArgs waitForMouseWheelEvent() {
		return popEventArg(MOUSE_WHEEL);
	}

	public KeyEventArgs waitForKeyDownEvent() {
		return popEventArg(KEY_DOWN);
	}

	public KeyEventArgs waitForKeyPressEvent() {
		return popEventArg(KEY_PRESS);
	}

	public KeyEventArgs waitForKeyUpEvent() {
		return popEventArg(KEY_UP);
	}

	public WindowMoveEventArgs waitForWindowMoveEvent() {
		return popEventArg(WINDOW_MOVE);
	}

	public void close() {
		subscriptions.forEach(Subscription::unsubscribe);
	}

	private <T> T popEventArg(EventType expectedEventType) {
		ConcurrentQueue<Object> queue = eventQueues.get(expectedEventType);
		return (T) queue.pop();
	}

	private boolean hasEvent(EventType type) {
		return eventQueues.get(type).canPop();
	}

	private void pushEvent(EventType eventType, Object eventArg) {
		eventQueues.get(eventType).push(eventArg);
	}

	private void bindEvents(CodeDraw cd) {
		subscriptions.add(cd.onMouseClick((c, arg) -> pushEvent(MOUSE_CLICK, arg)));
		subscriptions.add(cd.onMouseMove((c, arg) -> pushEvent(MOUSE_CLICK, arg)));
		subscriptions.add(cd.onMouseUp((c, arg) -> pushEvent(MOUSE_UP, arg)));
		subscriptions.add(cd.onMouseDown((c, arg) -> pushEvent(MOUSE_DOWN, arg)));
		subscriptions.add(cd.onMouseEnter((c, arg) -> pushEvent(MOUSE_ENTER, arg)));
		subscriptions.add(cd.onMouseLeave((c, arg) -> pushEvent(MOUSE_LEAVE, arg)));
		subscriptions.add(cd.onMouseWheel((c, arg) -> pushEvent(MOUSE_WHEEL, arg)));
		subscriptions.add(cd.onKeyDown((c, arg) -> pushEvent(KEY_DOWN, arg)));
		subscriptions.add(cd.onKeyPress((c, arg) -> pushEvent(KEY_PRESS, arg)));
		subscriptions.add(cd.onKeyUp((c, arg) -> pushEvent(KEY_UP, arg)));
		subscriptions.add(cd.onWindowMove((c, arg) -> pushEvent(WINDOW_MOVE, arg)));
		subscriptions.add(cd.onWindowClose((c, arg) -> pushEvent(WINDOW_CLOSE, arg)));
	}


}
