package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class EventScanner implements AutoCloseable {
	private static final Object EndOfEvent = new Object();

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
		return queue.canPop() && hasNextEvent();
	}

	public boolean hasNextEvent() {
		return !hasEndOfEvent();
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
	private boolean hasEndOfEvent() { return peek().equals(EndOfEvent); }

	public Object nextEvent() { return pop(Object.class); }
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
		return queue.peek();
	}
	
	private <T> T pop(Class<T> type) {
		try {
			if (hasEndOfEvent()) throw new NoSuchElementException();

			T result = type.cast(queue.pop());
			if (result instanceof WindowCloseEventArgs) {
				queue.push(EndOfEvent);
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
		queue.push(EndOfEvent);
	}
}
