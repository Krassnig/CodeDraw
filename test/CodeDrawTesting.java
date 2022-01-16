import codedraw.CodeDraw;
import codedraw.Palette;
import codedraw.events.EventAwaiter;
import codedraw.events.MouseDownEventArgs;
import codedraw.textformat.*;

import java.awt.*;

import static org.junit.Assert.assertTrue;

public class CodeDrawTesting {
	public static void configureConfirmationDialouge(CodeDraw cd) {
		cd.setColor(Palette.RED);
		cd.fillRectangle(0, cd.getHeight() * (9D / 10), cd.getWidth() / 2D, cd.getHeight() * (1D / 10));
		cd.setColor(Palette.GREEN);
		cd.fillRectangle(cd.getWidth() / 2D, cd.getHeight() * (9D / 10), cd.getWidth() / 2D, cd.getHeight() * (1D / 10));
		cd.setColor(Palette.BLACK);
		TextFormat tf = cd.getTextFormat();
		tf.setHorizontalAlign(HorizontalAlign.CENTER);
		tf.setVerticalAlign(VerticalAlign.MIDDLE);
		cd.drawText(cd.getWidth() * (1D / 4), cd.getHeight() * (19D / 20), "Reject");
		cd.drawText(cd.getWidth() * (3D / 4), cd.getHeight() * (19D / 20), "Confirm");
		cd.setTextFormat(new TextFormat());
	}

	public static void assertConfirmation(CodeDraw cd) {
		try (EventAwaiter<MouseDownEventArgs> awaiter = new EventAwaiter<>(cd::onMouseDown)) {
			MouseDownEventArgs eventArgs = awaiter.waitForNextEvent();

			while (eventArgs.getY() < cd.getHeight() * (9D / 10)) {
				eventArgs = awaiter.waitForNextEvent();
			}

			assertTrue(eventArgs.getX() > cd.getWidth() / 2);
		}
	}

	public static void setTestDescription(CodeDraw cd, String text) {
		TextFormat ogTextFormat = cd.getTextFormat();
		Color ogColor = cd.getColor();

		cd.setColor(Palette.LIGHT_GRAY);
		cd.fillRectangle(0, cd.getHeight() * (8D / 10), cd.getWidth(), cd.getHeight() * (1D / 10));

		TextFormat format = new TextFormat();
		format.setHorizontalAlign(HorizontalAlign.CENTER);
		format.setVerticalAlign(VerticalAlign.MIDDLE);
		cd.setTextFormat(format);
		cd.setColor(Palette.BLACK);
		cd.drawText(cd.getWidth() / 2D, cd.getHeight() * (17D / 20), text);

		cd.setColor(ogColor);
		cd.setTextFormat(ogTextFormat);
	}
}
