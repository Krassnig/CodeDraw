import codedraw.*;
import codedraw.textformat.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class TextFormatTest {
	private CodeDraw cd;

	@Before
	public void beforeEach() {
		cd = new CodeDraw();
		cd.setWindowPositionX(100);
		cd.setWindowPositionY(100);
		CodeDrawTesting.configureConfirmationDialouge(cd);
	}

	@After
	public void afterEach() {
		cd.close();
	}

	private void assertConfirmation() {
		CodeDrawTesting.assertConfirmation(cd);
	}

	private void setTestDescription(String text) {
		CodeDrawTesting.setTestDescription(cd, text);
	}

	@Test
	public void defaultAlignmentTest() {
		setTestDescription("The default alignment of text should be the bottom right.");

		cd.drawLine(200, 100, 200, 300);
		cd.drawLine(100, 200, 300, 200);

		cd.drawText(200, 200, "Hello World!");

		cd.show();
		assertConfirmation();
	}

	@Test
	public void fontSizeTest() {
		setTestDescription("The font size should get increasingly bigger with the number.");
		TextFormat format = cd.getTextFormat();

		for (int i = 1; i < 30; i++) {
			format.setFontSize(i);
			cd.drawText(30, 10 + ((i * (i + 1)) / 2D), i + " Bigger!");
		}

		cd.show();
		assertConfirmation();
	}

	@Test
	public void fontNameTest() {
		setTestDescription("Each text should have a different font.");
		TextFormat format = cd.getTextFormat();
		format.setFontSize(16);

		String[] fonts = getAvailableFontNames();

		for (int i = 0; i < 40 && i < fonts.length; i++) {
			format.setFontName(fonts[i]);
			if (i < 20) {
				cd.drawText(30, 20 + i * 20, i + "    Hello World!");
			}
			else {
				cd.drawText(330, 20 + (i - 20) * 20, i + "   Hello World!");
			}
		}

		cd.show();
		assertConfirmation();
	}

	@Test
	public void fontStyleTest() {
		setTestDescription("The text should be correctly stylized.");
		TextFormat format = cd.getTextFormat();
		format.setFontSize(20);

		format.isItalic(true);
		cd.drawText(30, 100, "Italic");
		format.isItalic(false);
		cd.drawText(30, 150, "Not Italic");

		format.isBold(true);
		cd.drawText(180, 100, "Bold");
		format.isBold(false);
		cd.drawText(180, 150, "Not Bold");

		format.isStrikethrough(true);
		cd.drawText(330, 100, "Strikethrough");
		format.isStrikethrough(false);
		cd.drawText(330, 150, "No strikethrough");

		cd.show();
		assertConfirmation();
	}

	@Test
	public void underlineTest() {
		setTestDescription("There should be different styles for each underline type.");
		TextFormat format = cd.getTextFormat();

		format.setFontName("Times New Roman");
		format.setFontSize(20);

		int i = 0;
		for (Underline underline : Underline.values()) {
			format.setUnderlined(underline);
			cd.drawText(30, 30 + i++ * 30, underline.name().toLowerCase());
		}

		cd.isAntiAliased(false);
		i = 0;
		for (Underline underline : Underline.values()) {
			format.setUnderlined(underline);
			cd.drawText(200, 30 + i++ * 30, underline.name().toLowerCase());
		}

		cd.show();
		assertConfirmation();
	}

	@Test
	public void multilineAlignmentTest() {
		setTestDescription("The text should be differently aligned for each quadrant.");

		drawAlignment(100, 70, VerticalAlign.TOP   , HorizontalAlign.LEFT);
		drawAlignment(300, 70, VerticalAlign.TOP   , HorizontalAlign.CENTER);
		drawAlignment(500, 70, VerticalAlign.TOP   , HorizontalAlign.RIGHT);
		drawAlignment(100, 220, VerticalAlign.MIDDLE, HorizontalAlign.LEFT);
		drawAlignment(300, 220, VerticalAlign.MIDDLE, HorizontalAlign.CENTER);
		drawAlignment(500, 220, VerticalAlign.MIDDLE, HorizontalAlign.RIGHT);
		drawAlignment(100, 370, VerticalAlign.BOTTOM, HorizontalAlign.LEFT);
		drawAlignment(300, 370, VerticalAlign.BOTTOM, HorizontalAlign.CENTER);
		drawAlignment(500, 370, VerticalAlign.BOTTOM, HorizontalAlign.RIGHT);

		cd.show();
		assertConfirmation();
	}

	private void drawAlignment(double x, double y, VerticalAlign va, HorizontalAlign ha) {
		TextFormat format = cd.getTextFormat();
		format.setVerticalAlign(va);
		format.setHorizontalAlign(ha);

		cd.setColor(Palette.RED);
		cd.drawLine(x - 60, y, x + 60, y);
		cd.drawLine(x, y - 60, x, y + 60);
		cd.setColor(Palette.BLACK);
		cd.drawText(x, y, va + "\n" + (ha + "").toLowerCase() + "\nTEST");
	}

	private static String[] getAvailableFontNames() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}
}
