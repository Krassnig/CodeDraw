package codedraw.events;

/**
 * This argument is given exactly once when the user closes the window or {@link codedraw.CodeDraw#close()} is called.
 * No information except the fact that it was triggered is supplied,
 * that is why this class is empty.
 */
public class WindowCloseEvent {
	WindowCloseEvent() { }
}
