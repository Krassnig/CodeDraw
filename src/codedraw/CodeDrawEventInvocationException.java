package codedraw;

public class CodeDrawEventInvocationException extends RuntimeException {
	CodeDrawEventInvocationException() {
		super("CodeDraw methods should not be called inside of events!");
	}
}
