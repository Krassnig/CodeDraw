package codedraw;

/**
 * This argument is given exactly once after the user closes the window or {@link codedraw.CodeDraw#close()} is called.
 * No information except the fact that it was triggered is supplied,
 * which is why this class is empty.
 */
public class WindowCloseEvent extends Event {
	WindowCloseEvent() { }

	@Override
	public String toString() {
		return "[Closing]";
	}
}
