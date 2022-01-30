package codedraw;

/**
 * This exception is thrown when you try to use CodeDraw inside the CodeDraw event loop.
 * Calling CodeDraw inside the event loop leads to concurrency issues and blocks the event loop.
 */
public class CodeDrawEventInvocationException extends RuntimeException {
	CodeDrawEventInvocationException() {
		super("CodeDraw methods should not be called inside of events!");
	}
}
