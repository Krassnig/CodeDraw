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
	
	private ConcurrentQueue<Object> queue;
	private ArrayList<Subscription> subscriptions;

	public boolean hasNextEventNow() {
		if (isClosed()) throw createIllegalStateException();
		return queue.canPop();
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
	public boolean hasWindowCloseEvent() { return peek() == null; }

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

	private Object peek() {
		if (isClosed()) throw createIllegalStateException();
		return queue.peek();
	}
	
	private <T> T pop(Class<T> type) {
		if (isClosed()) throw createIllegalStateException();

		try {
			T tmp = type.cast(peek());
			if (tmp == null) throw new NoSuchElementException();
			return type.cast(queue.pop());
		}
		catch (ClassCastException e) {
			throw new InputMismatchException();
		}
	}

	@Override
	public void close() {
		if (isClosed()) throw createIllegalStateException();
		subscriptions.forEach(Subscription::unsubscribe);
		subscriptions = null;
	}

	private boolean isClosed() {
		return subscriptions == null;
	}

	private IllegalStateException createIllegalStateException() {
		return new IllegalStateException();
	}
}
