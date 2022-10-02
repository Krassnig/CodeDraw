package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * The EventScanner can be used to handle events that are produced in CodeDraw.
 * There is two ways to consume events from the EventScanner, either through the {@link Iterable<Event>} interface or
 * through the {@link java.util.Scanner} like structure by calling <i>has</i>- and <i>next</i>-methods.
 * <br><br>
 * The example below displays the current mouse position and the amount the mouse has been pressed.
 * Before each frame is drawn all events that happened between the previous frame and now are processed
 * by a foreach loop. Each event inside that foreach loop is then processed based on the type of the event.
 * If it is a MouseMoveEvent then the position gets saved. if it is a MouseClickEvent then the clickCount is
 * increased. Otherwise, the Event is discarded in the default branch.
 * <pre>{@code
 * CodeDraw cd = new CodeDraw();
 *
 * int mouseX = 0;
 * int mouseY = 0;
 * int clickCount = 0;
 *
 * while (!es.isClosed()) {
 *     for (var e : cd.getEventScanner()) {
 *         switch (e) {
 *             case MouseMoveEvent a -> {
 *                 mouseX = a.getX();
 *                 mouseY = a.getY();
 *             }
 *             case MouseClickEvent a -> clickCount++;
 *             default -> { }
 *         }
 *     }
 *
 *     cd.clear();
 *     cd.drawText(100, 100, "Position: " + x + " " + y + "\nClick: " + clickCount);
 *     cd.show();
 * }
 * }</pre>
 * <br><br>
 * If you use an older version of Java you can utilize the {@link java.util.Scanner} like properties of the EventScanner.
 * The inner while loop in the example below processes all currently available events.
 * In each iteration a new event will be at the head of the queue and the inner while loop will only stop once all
 * currently available events are consumed.
 * Inside the inner loop depending on which type of event is at the head of the queue one branch of the if/else will be
 * selected.
 * If the head of the queue is a MouseMoveEvent event the hasMouseMoveEvent() method will return true and then the
 * nextMouseMoveEvent() method will be called which returns the MouseMoveEvent from the head of the queue.
 * All other events inside the queue will then shift forward and there will be a new event at the head of the queue.
 * Do not forget to call the next method because otherwise the program will enter an endless loop because
 * the same event will always be at the head of the queue.
 * After the MouseMoveEvent has been returned from the nextMouseMoveEvent() method the event can be used to update
 * the state of the program. In this case it just updates the current mouse position.
 * After all currently available events are processed the changes are displayed by clearing the canvas and
 * then calling the drawText method.
 * <pre>{@code
 * CodeDraw cd = new CodeDraw();
 * EventScanner es = cd.getEventScanner();
 * int mouseX = 0;
 * int mouseY = 0;
 * int clickCount = 0;
 *
 * while (!es.isClosed()) {
 *     while (es.hasEventNow()) {
 *         if (es.hasMouseMoveEvent()) {
 *             MouseMoveEvent a = es.nextMouseMoveEvent();
 *             mouseX = a.getX();
 *             mouseY = a.getY();
 *         }
 *         else if (es.hasMouseClickEvent()) {
 *             es.nextEvent();
 *             clickCount++;
 *         }
 *         else {
 *             es.nextEvent();
 *         }
 *     }
 *
 *     cd.clear();
 *     cd.drawText(100, 100, "Position: " + mouseX + " " + mouseY + "\nClick: " + clickCount);
 *     cd.show();
 * }
 * }</pre>
 * <br><br>
 * Common mistakes when using the EventScanner:
 * <ul>
 *     <li>The next method is not called within an if branch.</li>
 *     <li>When consuming more than two events ifs are used instead of elseif.</li>
 *     <li>The last else branch is forgotten and the remaining events are not discarded.</li>
 * </ul>
 * When a CodeDraw window is closed all remaining events can still be consumed from the EventScanner
 * but no new events will appear.
 */
public class EventScanner implements Iterable<Event> {
	EventScanner() {
		this.queue = new ConcurrentQueue<>(128);
	}

	private final ConcurrentQueue<Event> queue;

	void push(Event event) {
		queue.push(event);
	}

	/**
	 * Compared to {@link #hasEvent} this method does not wait until the next event is available, but instead returns immediately.
	 * If there is currently an event available returns true otherwise false.
	 * @return whether there are currently events available
	 */
	public boolean hasEventNow() {
		return hasNow(Event.class);
	}

	/**
	 * Waits until the next event is available.
	 * Returns false if there are no more events, true otherwise.
	 * @return whether there are more events available
	 */
	public boolean hasEvent() {
		return has(Event.class);
	}

	/**
	 * Waits until the next mouse click event is available.
	 * Returns true if the next event is a mouse click event, otherwise false.
	 * The mouse click event is triggered once when a mouse button is pressed down and quickly released again.
	 * @return whether the next event is a mouse click event.
	 */
	public boolean hasMouseClickEvent() { return has(MouseClickEvent.class); }

	/**
	 * Waits until the next mouse move event is available.
	 * Returns true if the next event is a mouse move event, otherwise false.
	 * The mouse move event is triggered continuously while the mouse is being moved.
	 * @return whether the next event is a mouse move event.
	 */
	public boolean hasMouseMoveEvent() { return has(MouseMoveEvent.class); }

	/**
	 * Waits until the next mouse down event is available.
	 * Returns true if the next event is a mouse down event, otherwise false.
	 * The mouse down event is triggered exactly once when a mouse button is pressed down.
	 * @return whether the next event is a mouse down event.
	 */
	public boolean hasMouseDownEvent() { return has(MouseDownEvent.class); }

	/**
	 * Waits until the next mouse up event is available.
	 * Returns true if the next event is a mouse up event, otherwise false.
	 * The mouse up event is triggered when a mouse button is released.
	 * @return whether the next event is a mouse up event.
	 */
	public boolean hasMouseUpEvent() { return has(MouseUpEvent.class); }

	/**
	 * Waits until the next mouse enter event is available.
	 * Returns true if the next event is a mouse enter event, otherwise false.
	 * The mouse enter event is triggered when the mouse enters the canvas.
	 * @return whether the next event is a mouse enter event.
	 */
	public boolean hasMouseEnterEvent() { return has(MouseEnterEvent.class); }

	/**
	 * Waits until the next mouse leave event is available.
	 * Returns true if the next event is a mouse leave event, otherwise false.
	 * The mouse leave event is triggered when the mouse leaves the canvas.
	 * @return whether the next event is a mouse leave event.
	 */
	public boolean hasMouseLeaveEvent() { return has(MouseLeaveEvent.class); }

	/**
	 * Waits until the next mouse wheel event is available.
	 * Returns true if the next event is a mouse wheel event, otherwise false.
	 * The mouse wheel event is triggered each time the mouse wheel is turned.
	 * @return whether the next event is a mouse wheel event.
	 */
	public boolean hasMouseWheelEvent() { return has(MouseWheelEvent.class); }

	/**
	 * Waits until the next key down event is available.
	 * Returns true if the next event is a key down event, otherwise false.
	 * The key down event is triggered exactly once when a key is pressed down.
	 * @return whether the next event is a key down event.
	 */
	public boolean hasKeyDownEvent() { return has(KeyDownEvent.class); }

	/**
	 * Waits until the next key up event is available.
	 * Returns true if the next event is a key up event, otherwise false.
	 * The key up event is triggered exactly once when a key is released.
	 * @return whether the next event is a key up event.
	 */
	public boolean hasKeyUpEvent() { return has(KeyUpEvent.class); }

	/**
	 * Waits until the next key press event is available.
	 * Returns true if the next event is a key press event, otherwise false.
	 * The key press event is triggered continuously while a key is being held down.
	 * @return whether the next event is a key press event.
	 */
	public boolean hasKeyPressEvent() { return has(KeyPressEvent.class); }

	/**
	 * Waits until the next window move event is available.
	 * Returns true if the next event is a window move event, otherwise false.
	 * The window move event is triggered every time the CodeDraw window is moved.
	 * @return whether the next event is a window move event.
	 */
	public boolean hasWindowMoveEvent() { return has(WindowMoveEvent.class); }

	/**
	 * Waits until the next window close event is available.
	 * Returns true if the next event is a window close event, otherwise false.
	 * The window close event is triggered exactly once when the user closes the window or {@link CodeDraw#close()} is called.
	 * @return whether the next event is a window close event.
	 */
	public boolean hasWindowCloseEvent() { return has(WindowCloseEvent.class); }

	/**
	 * Waits for the next event and then returns it.
	 * Check {@link #hasEvent()} or {@link #hasEventNow()} before calling this function.
	 * <br><br>
	 * You can use this method to either discard events or use the switch type matching feature of Java.
	 * <pre>{@code
	 * while (es.hasEventNow()) {
	 *     switch (es.nextEvent()) {
	 *         case MouseDownEventArgs a:
	 *             System.out.println("The mouse has been pressed at x=" + a.getX() + " y=" + a.getY() + ".");
	 *             break;
	 *         case KeyDownEventArgs a:
	 *             System.out.println("The " + a.getKey() + " has been pressed.");
	 *             break;
	 *         default:
	 *             break;
	 *     }
	 * }
	 * }</pre>
	 * @throws NoSuchElementException if there are no more events.
	 * @return The next available event.
	 */
	public Event nextEvent() { return next(Event.class); }

	/**
	 * Waits for the next mouse click event and then consumes the event.
	 * The mouse click event is triggered once when a mouse button is pressed down and quickly released again.
	 * Check {@link #hasMouseClickEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse click event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse click event.
	 */
	public MouseClickEvent nextMouseClickEvent() { return next(MouseClickEvent.class); }

	/**
	 * Waits for the next mouse move event and then consumes the event.
	 * The mouse move event is triggered continuously while the mouse is being moved.
	 * Check {@link #hasMouseMoveEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse move event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse move event.
	 */
	public MouseMoveEvent nextMouseMoveEvent() { return next(MouseMoveEvent.class); }

	/**
	 * Waits for the next mouse down event and then consumes the event.
	 * The mouse down event is triggered exactly once when a mouse button is pressed down.
	 * Check {@link #hasMouseDownEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse down event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse down event.
	 */
	public MouseDownEvent nextMouseDownEvent() { return next(MouseDownEvent.class); }

	/**
	 * Waits for the next mouse up event and then consumes the event.
	 * The mouse up event is triggered when a mouse button is released.
	 * Check {@link #hasMouseUpEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse up event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse up event.
	 */
	public MouseUpEvent nextMouseUpEvent() { return next(MouseUpEvent.class); }

	/**
	 * Waits for the next mouse enter event and then consumes the event.
	 * The mouse enter event is triggered when the mouse enters the canvas.
	 * Check {@link #hasMouseEnterEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse enter event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse enter event.
	 */
	public MouseEnterEvent nextMouseEnterEvent() { return next(MouseEnterEvent.class); }

	/**
	 * Waits for the next mouse leave event and then consumes the event.
	 * The mouse leave event is triggered when the mouse leaves the canvas.
	 * Check {@link #hasMouseLeaveEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse leave event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse leave event.
	 */
	public MouseLeaveEvent nextMouseLeaveEvent() { return next(MouseLeaveEvent.class); }

	/**
	 * Waits for the next mouse wheel event and then consumes the event.
	 * The mouse wheel event is triggered each time the mouse wheel is turned.
	 * Check {@link #hasMouseWheelEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a mouse wheel event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a mouse wheel event.
	 */
	public MouseWheelEvent nextMouseWheelEvent() { return next(MouseWheelEvent.class); }

	/**
	 * Waits for the next key down event and then consumes the event.
	 * The key down event is triggered exactly once when a key is pressed down.
	 * Check {@link #hasKeyDownEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a key down event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a key down event.
	 */
	public KeyDownEvent nextKeyDownEvent() { return next(KeyDownEvent.class); }

	/**
	 * Waits for the next key up event and then consumes the event.
	 * The key up event is triggered exactly once when a key is released.
	 * Check {@link #hasKeyUpEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a key up event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a key up event.
	 */
	public KeyUpEvent nextKeyUpEvent() { return next(KeyUpEvent.class); }

	/**
	 * Waits for the next key press event and then consumes the event.
	 * The key press event is triggered continuously while a key is being held down.
	 * Check {@link #hasKeyPressEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a key press event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a key press event.
	 */
	public KeyPressEvent nextKeyPressEvent() { return next(KeyPressEvent.class); }

	/**
	 * Waits for the next window move event and then consumes the event.
	 * The window move event is triggered every time the CodeDraw window is moved.
	 * Check {@link #hasWindowMoveEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a window move event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a window move event.
	 */
	public WindowMoveEvent nextWindowMoveEvent() { return next(WindowMoveEvent.class); }

	/**
	 * Waits for the next window close event and then consumes the event.
	 * The window close event is triggered exactly once when the user closes the window or {@link CodeDraw#close()} is called.
	 * Check {@link #hasWindowCloseEvent()} before calling this function.
	 * @throws InputMismatchException if the next event is not a window close event.
	 * @throws NoSuchElementException if there are no more events.
	 * @return a window close event.
	 */
	public WindowCloseEvent nextWindowCloseEvent() { return next(WindowCloseEvent.class); }

	/**
	 * Creates an iterator for all the currently available events.
	 * By calling this method all events in the EventScanner are consumed
	 * and the EventScanner will be empty until new events are generated.
	 * @return An event iterator.
	 */
	@Override
	public Iterator<Event> iterator() {
		ArrayList<Event> result = new ArrayList<>();

		while (hasEventNow()) {
			result.add(nextEvent());
		}

		return result.iterator();
	}

	private boolean hasNow(Class<? extends Event> type) {
		return !queue.isEmpty() && has(type);
	}

	private boolean has(Class<? extends Event> expected) {
		Class<? extends Event> actual = peekType();
		return !actual.isAssignableFrom(EndOfEvent.class) && expected.isAssignableFrom(actual);
	}

	private Class<? extends Event> peekType() {
		return queue.peek().getClass();
	}

	private <T extends Event> T next(Class<T> expected) {
		Class<? extends Event> actual = peekType();

		if (EndOfEvent.class.isAssignableFrom(actual))
			throw new NoSuchElementException("There are no more events in this EventScanner. Check if there are events available before calling next.");
		if (WindowCloseEvent.class.isAssignableFrom(actual))
			push(EndOfEvent.INSTANCE);
		if (!expected.isAssignableFrom(actual)) {
			String expectedName = getEventName(expected);
			String actualName = getEventName(actual);
			throw new InputMismatchException(
				"The next event is of type " + actualName + " but method next" + expectedName + "() was called. " +
				"Check whether an event of type " + expectedName + " is available by calling has" + expectedName + "() before calling next" + expectedName + "()."
			);
		}

		return expected.cast(queue.pop());
	}

	private static <T extends Event> String getEventName(Class<T> eventType) {
		if (Event.class.isAssignableFrom(eventType)) {
			return "Event";
		}
		else {
			return eventType.getSimpleName();
		}
	}

	private static class EndOfEvent extends Event {
		private EndOfEvent() { }

		public static final EndOfEvent INSTANCE = new EndOfEvent();
	}
}
