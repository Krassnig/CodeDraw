import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextFormatTest {
	private CodeDrawConfirmation confirm;
	private CodeDraw cd;

	@Before
	public void beforeEach() {
		confirm = new CodeDrawConfirmation();
		cd = new CodeDraw();
		confirm.placeCodeDrawTestingInstance(cd);
	}

	@After
	public void afterEach() {
		cd.close();
		confirm.close();
	}

	@Test
	public void defaultAlignmentTest() {
		confirm.setConfirmationDialogue("The default alignment of text should be the top left\n(the red origin point should be to the top left).");

		cd.drawLine(200, 100, 200, 300);
		cd.drawLine(100, 200, 300, 200);

		cd.setColor(Palette.RED);
		cd.setLineWidth(4);
		cd.drawPoint(200, 200);

		cd.setColor(Palette.BLACK);
		cd.drawText(200, 200, "Hello World!");

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void fontSizeTest() {
		confirm.setConfirmationDialogue("The font size should get increasingly bigger with the number.");
		TextFormat format = cd.getTextFormat();

		for (int i = 1; i < 35; i++) {
			format.setFontSize(i);
			cd.drawText(30, 10 + ((i * (i + 1)) / 2D), i + " Bigger!");
		}

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void fontNameTest() {
		confirm.setConfirmationDialogue("Each text should have a different font.");
		TextFormat format = cd.getTextFormat();
		format.setFontSize(16);

		String[] fonts = TextFormat.getAllAvailableFontNames();

		for (int i = 0; i < 60 && i < fonts.length; i++) {
			format.setFontName(fonts[i]);
			if (i < 30) {
				cd.drawText(30, 10 + i * 20, i + "    Hello World!");
			}
			else {
				cd.drawText(330, 10 + (i - 30) * 20, i + "   Hello World!");
			}
		}

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void fontStyleTest() {
		confirm.setConfirmationDialogue("The text should be correctly stylized.");
		TextFormat format = cd.getTextFormat();
		format.setFontSize(20);

		format.setItalic(true);
		cd.drawText(30, 100, "Italic");
		format.setItalic(false);
		cd.drawText(30, 150, "Not Italic");

		format.setBold(true);
		cd.drawText(180, 100, "Bold");
		format.setBold(false);
		cd.drawText(180, 150, "Not Bold");

		format.setStrikethrough(true);
		cd.drawText(330, 100, "Strikethrough");
		format.setStrikethrough(false);
		cd.drawText(330, 150, "No strikethrough");

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void underlineTest() {
		confirm.setConfirmationDialogue("There should be different styles for each underline type.");
		TextFormat format = cd.getTextFormat();

		format.setFontName("Times New Roman");
		format.setFontSize(20);

		int i = 0;
		for (Underline underline : Underline.values()) {
			format.setUnderlined(underline);
			cd.drawText(30, 30 + i++ * 30, underline.name().toLowerCase());
		}

		cd.setAntiAliased(false);
		i = 0;
		for (Underline underline : Underline.values()) {
			format.setUnderlined(underline);
			cd.drawText(200, 30 + i++ * 30, underline.name().toLowerCase());
		}

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void multilineAlignmentTest() {
		confirm.setConfirmationDialogue("The text should be differently aligned for each quadrant.");

		drawAlignment(100, 100, TextOrigin.TOP_LEFT);
		drawAlignment(300, 100, TextOrigin.TOP_MIDDLE);
		drawAlignment(500, 100, TextOrigin.TOP_RIGHT);
		drawAlignment(100, 300, TextOrigin.CENTER_LEFT);
		drawAlignment(300, 300, TextOrigin.CENTER);
		drawAlignment(500, 300, TextOrigin.CENTER_RIGHT);
		drawAlignment(100, 500, TextOrigin.BOTTOM_LEFT);
		drawAlignment(300, 500, TextOrigin.BOTTOM_MIDDLE);
		drawAlignment(500, 500, TextOrigin.BOTTOM_RIGHT);

		cd.show();
		confirm.assertConfirmation();
	}

	private void drawAlignment(double x, double y, TextOrigin textOrigin) {
		TextFormat format = cd.getTextFormat();
		format.setTextOrigin(textOrigin);

		cd.setColor(Palette.RED);
		cd.drawLine(x - 80, y, x + 80, y);
		cd.drawLine(x, y - 80, x, y + 80);
		cd.setColor(Palette.BLACK);
		cd.drawText(x, y, String.join("\n", textOrigin.name().split("_", -1)).toLowerCase() + "\nTest");
	}
}
