import codedraw.*;
import codedraw.events.*;
import codedraw.textformat.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CodeDrawConfirmation implements AutoCloseable {
	public CodeDrawConfirmation() {
		cd = new CodeDraw(600, 200);
		cd.setWindowPositionX(0);
		cd.setWindowPositionY(10);
		render();
	}

	private String confirmationDialogue;
	private CodeDraw cd;

	private State state = State.START;
	private void setState(State state) {
		this.state = state;
		render();
	}

	public void placeCodeDrawTestingInstance(CodeDraw testingInstance) {
		testingInstance.setWindowPositionX(0);
		testingInstance.setWindowPositionY(250);
	}

	public void setConfirmationDialogue(String text) {
		confirmationDialogue = text;
		setState(state);
	}

	public void assertConfirmation() {
		setState(State.CAN_CONFIRM);

		EventScanner eventScanner = new EventScanner(cd);

		while (eventScanner.hasNextEvent()) {
			if (eventScanner.hasMouseDownEvent()) {
				MouseDownEventArgs eventArgs = eventScanner.nextMouseDownEvent();
				if (eventArgs.getY() > cd.getHeight() * 0.5) {
					boolean isConfirmed = eventArgs.getX() > cd.getWidth() / 2;
					setState(isConfirmed ? State.IS_CONFIRMED : State.IS_REJECTED);
					assertTrue(isConfirmed);
					eventScanner.close();
					return;
				}
			}
			else {
				eventScanner.nextEvent();
			}
		}

		System.err.println("Test was interrupted and neither rejected nor confirmed.");
		System.exit(0);
	}

	public void close() {
		cd.close();
	}

	private void render() {
		TextFormat tf = cd.getTextFormat();
		tf.setHorizontalAlign(HorizontalAlign.CENTER);
		tf.setVerticalAlign(VerticalAlign.MIDDLE);

		cd.clear(Palette.LIGHT_GRAY);

		if (confirmationDialogue != null) {
			cd.setColor(Palette.BLACK);
			cd.drawText(cd.getWidth() * 0.5, cd.getHeight() * 0.25, confirmationDialogue);
		}

		if (state == State.START) {
			cd.setColor(Palette.fromGrayscale(0x1A));
			cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth(), cd.getHeight() * 0.5);
			cd.setColor(Palette.WHITE);
			cd.drawText(cd.getWidth() * 0.5, cd.getHeight() * 0.75, "Please wait...");
		}
		else if (state == State.CAN_CONFIRM) {
			cd.setColor(Palette.RED);
			cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth() * 0.5, cd.getHeight() * 0.5);
			cd.setColor(Palette.GREEN);
			cd.fillRectangle(cd.getWidth() * 0.5, cd.getHeight() * 0.5, cd.getWidth() * 0.5, cd.getHeight() * 0.5);
			cd.setColor(Palette.BLACK);
			cd.drawText(cd.getWidth() * 0.25, cd.getHeight() * 0.75, "Reject");
			cd.drawText(cd.getWidth() * 0.75, cd.getHeight() * 0.75, "Confirm");
		}
		else if (state == State.IS_CONFIRMED || state == State.IS_REJECTED) {
			boolean isConfirmed = state == State.IS_CONFIRMED;

			cd.setColor(isConfirmed ? Palette.GREEN : Palette.RED);
			cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth(), cd.getHeight() * 0.5);
			cd.setColor(Palette.BLACK);
			cd.drawText(cd.getWidth() * 0.5, cd.getHeight() * 0.75, isConfirmed ? "Confirmed" : "Rejected");
		}

		cd.show();
	}

	private enum State {
		START,
		CAN_CONFIRM,
		IS_REJECTED,
		IS_CONFIRMED
	}
}
