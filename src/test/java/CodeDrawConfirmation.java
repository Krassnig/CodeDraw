import codedraw.*;
import codedraw.drawing.TextFormat;
import codedraw.drawing.TextOrigin;
import codedraw.events.*;

import static org.junit.Assert.fail;

public class CodeDrawConfirmation implements AutoCloseable {
	public CodeDrawConfirmation() {
		cd = new CodeDraw(600, 200);
		esc = cd.getEventScanner();
		cd.setWindowPositionX(0);
		cd.setWindowPositionY(10);
		setState(State.LOADING);
	}

	private final CodeDraw cd;
	private final EventScanner esc;

	private String confirmationDialogue;
	private State state;

	private void setState(State state) {
		this.state = state;
		rerender();
	}

	public void placeCodeDrawTestingInstance(CodeDraw testingInstance) {
		testingInstance.setWindowPositionX(0);
		testingInstance.setWindowPositionY(250);
	}

	public void setConfirmationDialogue(String text) {
		confirmationDialogue = text;
		rerender();
	}

	public void assertConfirmation() {
		if (state == State.LOADING) {
			setState(State.CAN_CONFIRM);
		}

		if (state != State.CAN_CONFIRM) {
			return;
		}

		while (esc.hasEvent()) {
			if (esc.hasMouseDownEvent()) {
				if (isConfirmed(esc.nextMouseDownEvent())) {
					return;
				}
			}
			else {
				esc.nextEvent();
			}
		}

		System.err.println("Test was interrupted and neither rejected nor confirmed.");
		System.exit(0);
	}

	public boolean hasConfirmationBeenPressed() {
		if (state == State.LOADING) {
			setState(State.CAN_CONFIRM);
		}

		while (esc.hasEventNow()) {
			if (!esc.hasEvent()) {
				System.err.println("Test was interrupted and neither rejected nor confirmed.");
				System.exit(0);
			}
			else if (esc.hasMouseDownEvent()) {
				if (isConfirmed(esc.nextMouseDownEvent())) {
					return true;
				}
			}
			else {
				esc.nextEvent();
			}
		}

		return false;
	}

	private boolean isConfirmed(MouseDownEvent a) {
		if (a.getY() > cd.getHeight() * 0.5) {
			boolean isConfirmed = a.getX() > cd.getWidth() / 2;
			if (isConfirmed) {
				setState(State.IS_CONFIRMED);
				return true;
			}
			else {
				setState(State.IS_REJECTED);
				fail();
			}
		}

		return false;
	}

	public void close() {
		cd.close();
	}

	private void rerender() {
		TextFormat format = cd.getTextFormat();
		format.setTextOrigin(TextOrigin.CENTER);

		cd.clear(Color.LIGHT_GRAY);

		if (confirmationDialogue != null) {
			cd.setColor(Color.BLACK);
			cd.drawText(cd.getWidth() * 0.5, cd.getHeight() * 0.25, confirmationDialogue);
		}

		if (state == State.LOADING) {
			cd.setColor(Color.fromGrayscale(0x1A));
			cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth(), cd.getHeight() * 0.5);
			cd.setColor(Color.WHITE);
			cd.drawText(cd.getWidth() * 0.5, cd.getHeight() * 0.75, "Please wait...");
		}
		else if (state == State.CAN_CONFIRM) {
			cd.setColor(Color.RED);
			cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth() * 0.5, cd.getHeight() * 0.5);
			cd.setColor(Color.GREEN);
			cd.fillRectangle(cd.getWidth() * 0.5, cd.getHeight() * 0.5, cd.getWidth() * 0.5, cd.getHeight() * 0.5);
			cd.setColor(Color.BLACK);
			cd.drawText(cd.getWidth() * 0.25, cd.getHeight() * 0.75, "Reject");
			cd.drawText(cd.getWidth() * 0.75, cd.getHeight() * 0.75, "Confirm");
		}
		else if (state == State.IS_CONFIRMED || state == State.IS_REJECTED) {
			boolean isConfirmed = state == State.IS_CONFIRMED;

			cd.setColor(isConfirmed ? Color.GREEN : Color.RED);
			cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth(), cd.getHeight() * 0.5);
			cd.setColor(Color.BLACK);
			cd.drawText(cd.getWidth() * 0.5, cd.getHeight() * 0.75, isConfirmed ? "Confirmed" : "Rejected");
		}

		cd.show();
	}

	private enum State {
		LOADING,
		CAN_CONFIRM,
		IS_REJECTED,
		IS_CONFIRMED
	}
}
