import codedraw.*;
import codedraw.textformat.*;

import java.awt.*;

public class TextFormatTest {
	public static void main(String[] args) {
		//newLineTest();
		//fontNameTest();
		//italicTest();
		//boldTest();
		//strikethroughTest();
		//underlineTest();
		//alignmentTest();
		//fontSizeTest();
		//defaultAlignmentTest();
	}

	private static void newLineTest() {
		CodeDraw cd = new CodeDraw();

		cd.drawText(100, 100, "New lines\ndon't work :(");

		cd.show();
	}

	private static void defaultAlignmentTest() {
		CodeDraw cd = new CodeDraw();

		cd.drawLine(200, 100, 200, 300);
		cd.drawLine(100, 200, 300, 200);

		cd.drawText(200, 200, "Hello World!");

		cd.show();
	}

	private static void fontSizeTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getTextFormat();

		for (int i = 1; i < 35; i++) {
			format.setFontSize(i);
			cd.drawText(30, 10 + ((i * (i + 1)) / 2D), i + " Bigger!");
		}

		cd.show();
	}

	private static void fontNameTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getTextFormat();
		format.setFontSize(16);

		String[] fonts = getAvailableFontNames();

		for (int i = 0; i < 60 && i < fonts.length; i++) {
			format.setFontName(fonts[i]);
			if (i < 30) {
				cd.drawText(30, 30 + i * 20, i + "    Hello World!");
			}
			else {
				cd.drawText(330, 30 + (i - 30) * 20, i + "   Hello World!");
			}
		}

		cd.show();
	}

	private static void italicTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getTextFormat();
		format.setFontSize(20);
		cd.drawText(30, 100, "Not Italic");
		format.isItalic(true);
		cd.drawText(30, 150, "Italic");
		cd.show();
	}

	private static void boldTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getTextFormat();
		format.setFontSize(20);
		cd.drawText(30, 100, "Not Bold");
		format.isBold(true);
		cd.drawText(30, 150, "Bold");
		cd.show();
	}

	private static void strikethroughTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getTextFormat();
		format.isStrikethrough(true);
		cd.drawText(30, 100, "Strikethrough");
		format.isStrikethrough(false);
		cd.drawText(30, 150, "No strikethrough");
		cd.show();
	}

	private static void underlineTest() {
		CodeDraw cd = new CodeDraw();
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
	}

	private static void alignmentTest() {
		CodeDraw cd = new CodeDraw();

		drawAlignment(cd, 100, 100, VerticalAlign.TOP   , HorizontalAlign.LEFT);
		drawAlignment(cd, 300, 100, VerticalAlign.TOP   , HorizontalAlign.CENTER);
		drawAlignment(cd, 500, 100, VerticalAlign.TOP   , HorizontalAlign.RIGHT);
		drawAlignment(cd, 100, 300, VerticalAlign.MIDDLE, HorizontalAlign.LEFT);
		drawAlignment(cd, 300, 300, VerticalAlign.MIDDLE, HorizontalAlign.CENTER);
		drawAlignment(cd, 500, 300, VerticalAlign.MIDDLE, HorizontalAlign.RIGHT);
		drawAlignment(cd, 100, 500, VerticalAlign.BOTTOM, HorizontalAlign.LEFT);
		drawAlignment(cd, 300, 500, VerticalAlign.BOTTOM, HorizontalAlign.CENTER);
		drawAlignment(cd, 500, 500, VerticalAlign.BOTTOM, HorizontalAlign.RIGHT);

		cd.show();
	}

	private static void drawAlignment(CodeDraw cd, double x, double y, VerticalAlign va, HorizontalAlign ha) {
		TextFormat format = cd.getTextFormat();
		format.setVerticalAlign(va);
		format.setHorizontalAlign(ha);

		cd.setColor(Palette.RED);
		cd.drawLine(x - 80, y, x + 80, y);
		cd.drawLine(x, y - 80, x, y + 80);
		cd.setColor(Palette.BLACK);
		cd.drawText(x, y, va + " " + (ha + "").toLowerCase());
	}

	private static String[] getAvailableFontNames() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}
}
