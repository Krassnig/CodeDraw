import codedraw.*;
import codedraw.events.*;
import codedraw.textformat.*;

import static org.junit.Assert.assertTrue;

public class CodeDrawConfirmation implements AutoCloseable {
	public CodeDrawConfirmation() {
		cd = new CodeDraw(600, 200);
		cd.setWindowPositionX(0);
		cd.setWindowPositionY(10);
		render();
	}

	private String confirmationDialogue;
	private CodeDraw cd;

	public void placeCodeDrawTestingInstance(CodeDraw testingInstance) {
		testingInstance.setWindowPositionX(0);
		testingInstance.setWindowPositionY(250);
	}

	public void setConfirmationDialogue(String text) {
		confirmationDialogue = text;
		render();
	}

	public void assertConfirmation() {
		try (EventAwaiter<MouseDownEventArgs> awaiter = new EventAwaiter<>(cd::onMouseDown)) {
			MouseDownEventArgs eventArgs = awaiter.waitForNextEvent();

			while (eventArgs.getY() < cd.getHeight() * 0.5) {
				eventArgs = awaiter.waitForNextEvent();
			}

			assertTrue(eventArgs.getX() > cd.getWidth() / 2);
		}
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

		cd.setColor(Palette.RED);
		cd.fillRectangle(0, cd.getHeight() * 0.5, cd.getWidth() * 0.5, cd.getHeight() * 0.5);
		cd.setColor(Palette.GREEN);
		cd.fillRectangle(cd.getWidth() * 0.5, cd.getHeight() * 0.5, cd.getWidth() * 0.5, cd.getHeight() * 0.5);
		cd.setColor(Palette.BLACK);
		cd.drawText(cd.getWidth() * 0.25, cd.getHeight() * 0.75, "Reject");
		cd.drawText(cd.getWidth() * 0.75, cd.getHeight() * 0.75, "Confirm");

		cd.show();
	}
}
