import CodeDraw.CodeDraw;
import CodeDraw.TextFormat.*;

public class TextFormatTest {
	public static void main(String[] args) {
		//textFormatPostureTest();
		textFormatWeightTest();
		//textFormatKerningTest();
		//textFormatStrikeThroughTest();
		//textFormatUnderlineTest();
		//textFormatTest();
		//textAlignmentTest();
		//centerTextTest();
	}

	private static void textFormatPostureTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setFontSize(20);
		for (float f = -1.0f; f <= 1.0; f += 0.25) {
			format.setPosture(f);
			testDraw(cd, "Posture " + f);
		}
		cd.show();
	}

	private static void textFormatWeightTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getFormat();
		format.setFontSize(20);
		for (float f = 0.5f; f <= 5.0; f += 0.25) {
			format.setWeight(f);
			testDraw(cd, "Weight " + f);
		}
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

	private static int num = 1;

	private static void testDraw(CodeDraw cd, String text) {
		cd.drawText(50, 20 * num++, text);
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
				cd.drawText(baseLineX, y, text, option);
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
		cd.drawText(300, 300, "CENTER Test", option);
		cd.show();
	}
}
