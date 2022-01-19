package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class EventScanner implements AutoCloseable {
	public EventScanner(CodeDraw codeDraw) {
		queue = new ConcurrentQueue<>();
		subscriptions = new ArrayList<>(12);
		
		bindEvent(codeDraw::onMouseClick);
		bindEvent(codeDraw::onMouseMove);
		bindEvent(codeDraw::onMouseDown);
		bindEvent(codeDraw::onMouseUp);
		bindEvent(codeDraw::onMouseEnter);
		bindEvent(codeDraw::onMouseLeave);
		bindEvent(codeDraw::onMouseWheel);
		bindEvent(codeDraw::onKeyDown);
		bindEvent(codeDraw::onKeyUp);
		bindEvent(codeDraw::onKeyPress);
		bindEvent(codeDraw::onWindowMove);
		bindEvent(codeDraw::onWindowClose);
	}
	
	private <T> void bindEvent(Function<EventHandler<T>, Subscription> onEvent) {
		subscriptions.add(onEvent.apply(queue::push));
	}

	private boolean isEndOfEvents = false;
	private ConcurrentQueue<Object> queue;
	private ArrayList<Subscription> subscriptions;

	public boolean hasNextEventNow() {
		return !isEndOfEvents && queue.canPop();
	}

	public boolean hasNextEvent() {
		return peek() != null;
	}

	public Object nextEvent() {
		return pop(Object.class);
	}

	public boolean hasMouseClickEvent() { return peek() instanceof MouseClickEventArgs; }
	public boolean hasMouseMoveEvent() { return peek() instanceof MouseMoveEventArgs; }
	public boolean hasMouseDownEvent() { return peek() instanceof MouseDownEventArgs; }
	public boolean hasMouseUpEvent() { return peek() instanceof MouseUpEventArgs; }
	public boolean hasMouseEnterEvent() { return peek() instanceof MouseEnterEventArgs; }
	public boolean hasMouseLeaveEvent() { return peek() instanceof MouseLeaveEventArgs; }
	public boolean hasMouseWheelEvent() { return peek() instanceof MouseWheelEventArgs; }
	public boolean hasKeyDownEvent() { return peek() instanceof KeyDownEventArgs; }
	public boolean hasKeyUpEvent() { return peek() instanceof KeyUpEventArgs; }
	public boolean hasKeyPressEvent() { return peek() instanceof KeyPressEventArgs; }
	public boolean hasWindowMoveEvent() { return peek() instanceof WindowMoveEventArgs; }
	public boolean hasWindowCloseEvent() { return peek() instanceof WindowCloseEventArgs; }

	public MouseClickEventArgs nextMouseClickEvent() { return pop(MouseClickEventArgs.class); }
	public MouseMoveEventArgs nextMouseMoveEvent() { return pop(MouseMoveEventArgs.class); }
	public MouseDownEventArgs nextMouseDownEvent() { return pop(MouseDownEventArgs.class); }
	public MouseUpEventArgs nextMouseUpEvent() { return pop(MouseUpEventArgs.class); }
	public MouseEnterEventArgs nextMouseEnterEvent() { return pop(MouseEnterEventArgs.class); }
	public MouseLeaveEventArgs nextMouseLeaveEvent() { return pop(MouseLeaveEventArgs.class); }
	public MouseWheelEventArgs nextMouseWheelEvent() { return pop(MouseWheelEventArgs.class); }
	public KeyDownEventArgs nextKeyDownEvent() { return pop(KeyDownEventArgs.class); }
	public KeyUpEventArgs nextKeyUpEvent() { return pop(KeyUpEventArgs.class); }
	public KeyPressEventArgs nextKeyPressEvent() { return pop(KeyPressEventArgs.class); }
	public WindowMoveEventArgs nextWindowMoveEvent() { return pop(WindowMoveEventArgs.class); }
	public WindowCloseEventArgs newWindowCloseEvent() { return pop(WindowCloseEventArgs.class); }

	private Object peek() {
		return isEndOfEvents ? null : queue.peek();
	}
	
	private <T> T pop(Class<T> type) {
		if (isEndOfEvents) throw new NoSuchElementException();

		try {
			T result = type.cast(queue.pop());
			if (result instanceof WindowCloseEventArgs) {
				isEndOfEvents = true;
			}
			return result;
		}
		catch (ClassCastException e) {
			throw new InputMismatchException();
		}
	}

	@Override
	public void close() {
		subscriptions.forEach(Subscription::unsubscribe);
	}
}
