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

	public boolean hasNextEvent() {
		return queue.peek() != null;
	}

	public Object nextEvent() {
		return pop(Object.class);
	}

	public boolean hasMouseClickEvent() { return queue.peek() instanceof MouseClickEventArgs; }
	public boolean hasMouseMoveEvent() { return queue.peek() instanceof MouseMoveEventArgs; }
	public boolean hasMouseDownEvent() { return queue.peek() instanceof MouseDownEventArgs; }
	public boolean hasMouseUpEvent() { return queue.peek() instanceof MouseUpEventArgs; }
	public boolean hasMouseEnterEvent() { return queue.peek() instanceof MouseEnterEventArgs; }
	public boolean hasMouseLeaveEvent() { return queue.peek() instanceof MouseLeaveEventArgs; }
	public boolean hasMouseWheelEvent() { return queue.peek() instanceof MouseWheelEventArgs; }
	public boolean hasKeyDownEvent() { return queue.peek() instanceof KeyDownEventArgs; }
	public boolean hasKeyUpEvent() { return queue.peek() instanceof KeyUpEventArgs; }
	public boolean hasKeyPressEvent() { return queue.peek() instanceof KeyPressEventArgs; }
	public boolean hasWindowMoveEvent() { return queue.peek() instanceof WindowMoveEventArgs; }
	public boolean hasWindowCloseEvent() { return queue.peek() == null; }

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

	public <T> T pop(Class<T> type) {
		if (subscriptions == null) throw new IllegalStateException();

		try {
			T tmp = type.cast(queue.peek());
			if (tmp == null) throw new NoSuchElementException();
			return type.cast(queue.pop());
		}
		catch (ClassCastException e) {
			throw new InputMismatchException();
		}
	}

	@Override
	public void close() {
		if (subscriptions == null) throw new IllegalStateException();
		subscriptions.forEach(Subscription::unsubscribe);
		subscriptions = null;
	}
}
