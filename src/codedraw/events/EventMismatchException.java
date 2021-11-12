package codedraw.events;

public class EventMismatchException extends RuntimeException{

	public EventMismatchException(EventType expected, EventType actual) {
		super("Expected " + expected + " but next event is " + actual + ". Add check with EventScanner.hasNext" + expected + "() before calling EventScanner.next" + expected + "().");
	}

	protected EventMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
