import CodeDraw.CodeDraw;
import CodeDraw.TextFormat.HorizontalAlign;
import CodeDraw.TextFormat.TextFormat;
import CodeDraw.TextFormat.UnderlineType;
import CodeDraw.TextFormat.VerticalAlign;

import java.awt.*;
import java.util.Arrays;

public class TextFormatTest {
	public static void main(String[] args) {
		//textFontNameTest();
		//textFormatPostureTest();
		textFormatWeightTest();
		//textFormatKerningTest();
		//textFormatStrikeThroughTest();
		//textFormatUnderlineTest();
		//textFormatTest();
		//textAlignmentTest();
		//centerTextTest();
	}

	private static void textFontNameTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setFontSize(16);
		testDraw(cd, "Arial");
		int i = 1;
		for(String font: GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
			format.setFontName(font);
			testDraw(cd, font + i++);
			if(i == 30) break;
		}
		cd.show();
	}

	private static void textFormatPostureTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setFontSize(20);
		testDraw(cd, "Not Italic");
		format.setItalic(true);
		testDraw(cd, "Italic");
		cd.show();
	}

	private static void textFormatWeightTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setFontSize(20);
		testDraw(cd, "Not Bold");
		format.setBold(true);
		testDraw(cd, "Bold");
		cd.show();
	}

	private static void textFormatKerningTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setKerning(true);
		testDraw(cd, "Kerning");
		format.setKerning(false);
		testDraw(cd, "No Kerning");
		cd.show();
	}

	private static void textFormatStrikeThroughTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setStrikethrough(true);
		testDraw(cd, "strikethrough");
		format.setStrikethrough(false);
		testDraw(cd, "No strikethrough");
		cd.show();
	}

	private static void textFormatUnderlineTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		for (UnderlineType underlineType : UnderlineType.values()) {
			format.setUnderlined(underlineType);
			testDraw(cd, underlineType.name());
		}
		cd.show();
	}

	private static void textAlignmentTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat option = new TextFormat();
		double baseLineX = 300;
		double baseLineY = 32;
		cd.drawLine(baseLineX, 0, baseLineX, 600);
		for (int i = 0; i < 3; i++) {
			option.setHorizontalAlign(HorizontalAlign.values()[i]);
			for (int j = 0; j < 3; j++) {

				option.setVerticalAlign(VerticalAlign.values()[j]);
				String text = "Horz: " + option.getHorizontalAlign() + ", Vert: " + option.getVerticalAlign();
				double y = (i * 3 + j + 1) * baseLineY;
				cd.drawText(baseLineX, y, text);
				cd.drawLine(0, y, 600, y);
			}
		}
		cd.show();
	}

	private static void centerTextTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat option = new TextFormat();
		option.setVerticalAlign(VerticalAlign.MIDDLE);
		option.setHorizontalAlign(HorizontalAlign.CENTER);
		cd.setFormat(option);
		cd.drawText(300, 300, "CENTER Test");
		cd.show();
	}



	private static int num = 1;

	private static void testDraw(CodeDraw cd, String text) {
		cd.drawText(50, 20 * num++, text);
	}

}
