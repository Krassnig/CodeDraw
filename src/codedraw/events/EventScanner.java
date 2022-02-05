package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static codedraw.events.EventType.*;

/**
 * The EventScanner works in the same way as a {@link java.util.Scanner}.
 * The source of the events is specified as an argument to the constructor of the EventScanner.
 * If multi EventScanner listen to the same CodeDraw instance/window,
 * then the event will appear in both EventScanner.
 * <br><br>
 * The following example shows the current position of the mouse and the number of clicks:
 * <pre>{@code
 * CodeDraw cd = new CodeDraw();
 * EventScanner es = new EventScanner(cd);
 *
 * int x = 0;
 * int y = 0;
 * int clickCount = 0;
 *
 * while (!es.isClosed()) {
 *     while (es.hasEventNow()) {
 *         if (es.hasMouseMoveEvent()) {
 *             MouseMoveEventArgs a = es.nextMouseMoveEvent();
 *             x = a.getX();
 *             y = a.getY();
 *         }
 *         if (es.hasMouseClickEvent()) {
 *             clickCount++;
 *             es.nextEvent();
 *         }
 *         else {
 *             es.nextEvent();
 *         }
 *     }
 *
 *     cd.clear();
 *     cd.drawText(100, 100, "Position: " + x + " " + y + "\nClick: " + clickCount);
 *     cd.show();
 * }
 * }</pre>
 * This example first check if there are events available.
 * If there are, they get processed until there are no more events available.
 * Events that are not moves or clicks are ignored.
 * Once the events are processed the x/y position and clickCount are shown.
 * This continues in a loop until the user closes the window.
 * <br><br>
 * The EventScanner automatically closes when the CodeDraw window is closed.
 * The remaining events can still be consumed when the EventScanner is closed,
 * but no new events will appear.
 */
public class EventScanner implements AutoCloseable {
	private static final EventInfo EndOfEvent = new EventInfo(END_OF_EVENT, null);

	/**
	 * Waits until the next time the given event is triggered and
	 * then returns the event arguments as the result of this function.
	 * However, events that happen just before or after calling waitFor are lost.
	 * That means if events happen in between waitFor calls these events will be lost.
	 * To prevent that create an EventScanner object and use that object.
	 * <br><br>
	 * Example:
	 * <pre>{@code
	 * MouseClickEventArgs args = EventScanner.waitFor(codeDraw::onMouseClick);
	 * // use args here
	 * }</pre>
	 * @param codeDrawEvent a CodeDraw Event.
	 * @param <T> a type of event argument.
	 * @return The event argument.
	 */
	public static <T> T waitFor(Function<EventHandler<T>, Subscription> codeDrawEvent) {
		Semaphore semaphore = new Semaphore(0);
		AtomicReference<T> result = new AtomicReference<>(null);
		Subscription subscription = codeDrawEvent.apply(a -> {
			result.set(a);
			semaphore.release();
		});
		semaphore.acquire();
		subscription.unsubscribe();
		return result.get();
	}

	/**
	 * Creates a new EventScanner. Read the class documentation for a detailed explanation.
	 * @param codeDraw the source of the events.
	 */
	public EventScanner(CodeDraw codeDraw) {
		queue = new ConcurrentQueue<>(128);
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

	private final ConcurrentQueue<EventInfo> queue;
	private final ArrayList<Subscription> subscriptions;
	private boolean isClosed = false;

	/**
	 * Doesn't wait until the next event is available,
	 * but instead returns immediately.
	 * If there is currently an event available returns true
	 * otherwise false.
	 * @return whether there are currently events available
	 */
	public boolean hasEventNow() {
		return !queue.isEmpty() && hasEvent();
	}

	/**
	 * Waits until the next event is available.
	 * Returns false if there are no more events.
	 * @return whether there are more events available
	 */
	public boolean hasEvent() {
		return !hasEndOfEvent();
	}

	/**
	 * Waits until the next mouse click event is available.
	 * Returns true if the next event is a mouse click event, otherwise false.
	 * The mouse click event is triggered once when a mouse button is pressed down and quickly released again.
	 * @return whether the next event is a mouse click event.
	 */
	public boolean hasMouseClickEvent() { return has(MOUSE_CLICK); }

	/**
	 * Waits until the next mouse move event is available.
	 * Returns true if the next event is a mouse move event, otherwise false.
	 * The mouse move event is triggered continuously while the mouse is being moved.
	 * @return whether the next event is a mouse move event.
	 */
	public boolean hasMouseMoveEvent() { return has(MOUSE_MOVE); }

	/**
	 * Waits until the next mouse down event is available.
	 * Returns true if the next event is a mouse down event, otherwise false.
	 * The mouse down event is triggered exactly once when a mouse button is pressed down.
	 * @return whether the next event is a mouse down event.
	 */
	public boolean hasMouseDownEvent() { return has(MOUSE_DOWN); }

	/**
	 * Waits until the next mouse up event is available.
	 * Returns true if the next event is a mouse up event, otherwise false.
	 * The mouse up event is triggered when a mouse button is released.
	 * @return whether the next event is a mouse up event.
	 */
	public boolean hasMouseUpEvent() { return has(MOUSE_UP); }

	/**
	 * Waits until the next mouse enter event is available.
	 * Returns true if the next event is a mouse enter event, otherwise false.
	 * The mouse enter event is triggered when the mouse enters the canvas.
	 * @return whether the next event is a mouse enter event.
	 */
	public boolean hasMouseEnterEvent() { return has(MOUSE_ENTER); }

	/**
	 * Waits until the next mouse leave event is available.
	 * Returns true if the next event is a mouse leave event, otherwise false.
	 * The mouse leave event is triggered when the mouse leaves the canvas.
	 * @return whether the next event is a mouse leave event.
	 */
	public boolean hasMouseLeaveEvent() { return has(MOUSE_LEAVE); }

	/**
	 * Waits until the next mouse wheel event is available.
	 * Returns true if the next event is a mouse wheel event, otherwise false.
	 * The mouse wheel event is triggered each time the mouse wheel is turned.
	 * @return whether the next event is a mouse wheel event.
	 */
	public boolean hasMouseWheelEvent() { return has(MOUSE_WHEEL); }

	/**
	 * Waits until the next key down event is available.
	 * Returns true if the next event is a key down event, otherwise false.
	 * The key down event is triggered exactly once when a key is pressed down.
	 * @return whether the next event is a key down event.
	 */
	public boolean hasKeyDownEvent() { return has(KEY_DOWN); }

	/**
	 * Waits until the next key up event is available.
	 * Returns true if the next event is a key up event, otherwise false.
	 * The key up event is triggered exactly once when a key is released.
	 * @return whether the next event is a key up event.
	 */
	public boolean hasKeyUpEvent() { return has(KEY_UP); }

	/**
	 * Waits until the next key press event is available.
	 * Returns true if the next event is a key press event, otherwise false.
	 * The key press event is triggered continuously while a key is being held down.
	 * @return whether the next event is a key press event.
	 */
	public boolean hasKeyPressEvent() { return has(KEY_PRESS); }

	/**
	 * Waits until the next window move event is available.
	 * Returns true if the next event is a window move event, otherwise false.
	 * The window move event is triggered every time the CodeDraw window is moved.
	 * @return whether the next event is a window move event.
	 */
	public boolean hasWindowMoveEvent() { return has(WINDOW_MOVE); }

	/**
	 * Waits until the next window close event is available.
	 * Returns true if the next event is a window close event, otherwise false.
	 * The window close event is triggered exactly once when the user closes the window or {@link CodeDraw#close()} is called.
	 * @return whether the next event is a window close event.
	 */
	public boolean hasWindowCloseEvent() { return has(WINDOW_CLOSE); }

	private boolean hasEndOfEvent() { return has(END_OF_EVENT); }

	/**
	 * Waits for the next event and then returns it.
	 * Check {@link #hasEvent()} or {@link #hasEventNow()} before calling this function.
	 * <br><br>
	 * In the newer java version 17+ you can use switch pattern matching to handle events.
	 * <pre>{@code
	 * EventScanner es = new EventScanner(codeDraw);
	 * while (es.hasEvent()) {
	 *     switch (es.nextEvent()) {
	 *         case MouseDownEventArgs a:
	 *             System.out.println("The mouse has been pressed at x=" + a.getX() + " y=" + a.getY() + ".");
	 *             break;
	 *         case KeyDownEventArgs a:
	 *             System.out.println("The " + a.getKey() + " has been pressed.");
	 *             break;
	 *         default:
	 *     }
	 * }
	 * }</pre>
	 * @throws NoSuchElementException if there are no more events.
	 * @return The event args as an object.
	 */
	public Object nextEvent() { return next(ANY); }

	/**
	 * Waits for the next mouse click event and then consumes the event.
	 * The mouse click event is triggered once when a mouse button is pressed down and quickly released again.
	 * Check {@link #hasMouseClickEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse click event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse click event.
	 */
	public MouseClickEventArgs nextMouseClickEvent() { return next(MOUSE_CLICK); }

	/**
	 * Waits for the next mouse move event and then consumes the event.
	 * The mouse move event is triggered continuously while the mouse is being moved.
	 * Check {@link #hasMouseMoveEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse move event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse move event.
	 */
	public MouseMoveEventArgs nextMouseMoveEvent() { return next(MOUSE_MOVE); }

	/**
	 * Waits for the next mouse down event and then consumes the event.
	 * The mouse down event is triggered exactly once when a mouse button is pressed down.
	 * Check {@link #hasMouseDownEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse down event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse down event.
	 */
	public MouseDownEventArgs nextMouseDownEvent() { return next(MOUSE_DOWN); }

	/**
	 * Waits for the next mouse up event and then consumes the event.
	 * The mouse up event is triggered when a mouse button is released.
	 * Check {@link #hasMouseUpEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse up event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse up event.
	 */
	public MouseUpEventArgs nextMouseUpEvent() { return next(MOUSE_UP); }

	/**
	 * Waits for the next mouse enter event and then consumes the event.
	 * The mouse enter event is triggered when the mouse enters the canvas.
	 * Check {@link #hasMouseEnterEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse enter event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse enter event.
	 */
	public MouseEnterEventArgs nextMouseEnterEvent() { return next(MOUSE_ENTER); }

	/**
	 * Waits for the next mouse leave event and then consumes the event.
	 * The mouse leave event is triggered when the mouse leaves the canvas.
	 * Check {@link #hasMouseLeaveEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse leave event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse leave event.
	 */
	public MouseLeaveEventArgs nextMouseLeaveEvent() { return next(MOUSE_LEAVE); }

	/**
	 * Waits for the next mouse wheel event and then consumes the event.
	 * The mouse wheel event is triggered each time the mouse wheel is turned.
	 * Check {@link #hasMouseWheelEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse wheel event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse wheel event.
	 */
	public MouseWheelEventArgs nextMouseWheelEvent() { return next(MOUSE_WHEEL); }

	/**
	 * Waits for the next key down event and then consumes the event.
	 * The key down event is triggered exactly once when a key is pressed down.
	 * Check {@link #hasKeyDownEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a key down event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a key down event.
	 */
	public KeyDownEventArgs nextKeyDownEvent() { return next(KEY_DOWN); }

	/**
	 * Waits for the next key up event and then consumes the event.
	 * The key up event is triggered exactly once when a key is released.
	 * Check {@link #hasKeyUpEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a key up event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a key up event.
	 */
	public KeyUpEventArgs nextKeyUpEvent() { return next(KEY_UP); }

	/**
	 * Waits for the next key press event and then consumes the event.
	 * The key press event is triggered continuously while a key is being held down.
	 * Check {@link #hasKeyPressEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a key press event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a key press event.
	 */
	public KeyPressEventArgs nextKeyPressEvent() { return next(KEY_PRESS); }

	/**
	 * Waits for the next window move event and then consumes the event.
	 * The window move event is triggered every time the CodeDraw window is moved.
	 * Check {@link #hasWindowMoveEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a window move event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a window move event.
	 */
	public WindowMoveEventArgs nextWindowMoveEvent() { return next(WINDOW_MOVE); }

	/**
	 * Waits for the next window close event and then consumes the event.
	 * The window close event is triggered exactly once when the user closes the window or {@link CodeDraw#close()} is called.
	 * Check {@link #hasWindowCloseEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a window close event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a window close event.
	 */
	public WindowCloseEventArgs nextWindowCloseEvent() { return next(WINDOW_CLOSE); }

	private boolean has(EventType type) {
		return peekType().isEqual(type);
	}

	private EventType peekType() {
		return queue.peek().type;
	}

	private <T> T next(EventType expected) {
		EventType actual = peekType();

		if (actual.isEqual(END_OF_EVENT))
			throw new NoSuchElementException("There are no more events in this EventScanner. Check if there are events available before calling next.");
		if (actual.isEqual(WINDOW_CLOSE))
			close();
		if (!actual.isEqual(expected))
			throw new InputMismatchException("The next event is a " + actual + " but tried to consume " + expected + ". Check whether " + actual + " is next before consuming.");

		return (T)queue.pop().args;
	}

	/**
	 * Closes the EventScanner.
	 * The EventScanner automatically closes when the CodeDraw window is closed.
	 * The remaining events can still be consumed when the EventScanner is closed,
	 * but no new events will appear.
	 */
	@Override
	public void close() {
		if (!isClosed) {
			subscriptions.forEach(Subscription::unsubscribe);
			queue.push(EndOfEvent);
			isClosed = true;
		}
	}

	/**
	 * Checks whether this EventScanner is closed.
	 * The EventScanner automatically closes when the CodeDraw window is closed.
	 * @return whether the EventScanner still received new events.
	 */
	public boolean isClosed() {
		return isClosed;
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
