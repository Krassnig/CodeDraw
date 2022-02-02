package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.function.Function;

import static codedraw.events.EventType.*;

public class EventScanner implements AutoCloseable {
	private static final EventInfo EndOfEvent = new EventInfo(END_OF_EVENT, null);

	public EventScanner(CodeDraw codeDraw) {
		queue = new ConcurrentQueue<>();
		subscriptions = new ArrayList<>(12);
		
		bindEvent(codeDraw::onMouseClick, MOUSE_CLICK);
		bindEvent(codeDraw::onMouseMove, MOUSE_MOVE);
		bindEvent(codeDraw::onMouseDown, MOUSE_DOWN);
		bindEvent(codeDraw::onMouseUp, MOUSE_UP);
		bindEvent(codeDraw::onMouseEnter, MOUSE_ENTER);
		bindEvent(codeDraw::onMouseLeave, MOUSE_LEAVE);
		bindEvent(codeDraw::onMouseWheel, MOUSE_WHEEL);
		bindEvent(codeDraw::onKeyDown, KEY_DOWN);
		bindEvent(codeDraw::onKeyUp, KEY_UP);
		bindEvent(codeDraw::onKeyPress, KEY_PRESS);
		bindEvent(codeDraw::onWindowMove, WINDOW_MOVE);
		bindEvent(codeDraw::onWindowClose, WINDOW_CLOSE);
	}

	private <T> void bindEvent(Function<EventHandler<T>, Subscription> onEvent, EventType type) {
		subscriptions.add(onEvent.apply(a -> queue.push(new EventInfo(type, a))));
	}

	private ConcurrentQueue<EventInfo> queue;
	private ArrayList<Subscription> subscriptions;

	public boolean hasNextEventNow() {
		return queue.canPop() && hasNextEvent();
	}

	public boolean hasNextEvent() {
		return !hasEndOfEvent();
	}

	public boolean hasMouseClickEvent() { return peek(MOUSE_CLICK); }
	public boolean hasMouseMoveEvent() { return peek(MOUSE_MOVE); }
	public boolean hasMouseDownEvent() { return peek(MOUSE_DOWN); }
	public boolean hasMouseUpEvent() { return peek(MOUSE_UP); }
	public boolean hasMouseEnterEvent() { return peek(MOUSE_ENTER); }
	public boolean hasMouseLeaveEvent() { return peek(MOUSE_LEAVE); }
	public boolean hasMouseWheelEvent() { return peek(MOUSE_WHEEL); }
	public boolean hasKeyDownEvent() { return peek(KEY_DOWN); }
	public boolean hasKeyUpEvent() { return peek(KEY_UP); }
	public boolean hasKeyPressEvent() { return peek(KEY_PRESS); }
	public boolean hasWindowMoveEvent() { return peek(WINDOW_MOVE); }
	public boolean hasWindowCloseEvent() { return peek(WINDOW_CLOSE); }
	private boolean hasEndOfEvent() { return peek(END_OF_EVENT); }

	public Object nextEvent() { return pop(ANY); }
	public MouseClickEventArgs nextMouseClickEvent() { return pop(MOUSE_CLICK); }
	public MouseMoveEventArgs nextMouseMoveEvent() { return pop(MOUSE_MOVE); }
	public MouseDownEventArgs nextMouseDownEvent() { return pop(MOUSE_DOWN); }
	public MouseUpEventArgs nextMouseUpEvent() { return pop(MOUSE_UP); }
	public MouseEnterEventArgs nextMouseEnterEvent() { return pop(MOUSE_ENTER); }
	public MouseLeaveEventArgs nextMouseLeaveEvent() { return pop(MOUSE_LEAVE); }
	public MouseWheelEventArgs nextMouseWheelEvent() { return pop(MOUSE_WHEEL); }
	public KeyDownEventArgs nextKeyDownEvent() { return pop(KEY_DOWN); }
	public KeyUpEventArgs nextKeyUpEvent() { return pop(KEY_UP); }
	public KeyPressEventArgs nextKeyPressEvent() { return pop(KEY_PRESS); }
	public WindowMoveEventArgs nextWindowMoveEvent() { return pop(WINDOW_MOVE); }
	public WindowCloseEventArgs nextWindowCloseEvent() { return pop(WINDOW_CLOSE); }

	private boolean peek(EventType type) {
		return queue.peek().type == type;
	}

	private <T> T pop(EventType type) {
		if (hasEndOfEvent()) throw new NoSuchElementException();

		EventInfo ei = queue.pop();
		if (type != ei.type && type != ANY) throw new InputMismatchException();
		if (ei.type == WINDOW_CLOSE) {
			queue.push(EndOfEvent);
		}

		return (T)ei.args;
	}

	@Override
	public void close() {
		subscriptions.forEach(Subscription::unsubscribe);
		queue.push(EndOfEvent);
	}

	private static class EventInfo {
		public EventInfo(EventType type, Object args) {
			this.type = type;
			this.args = args;
		}

		private final EventType type;
		private final Object args;
	}
}
