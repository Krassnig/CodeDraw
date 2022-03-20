package codedraw.drawing;

import codedraw.textformat.*;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.HashMap;

class TextRendering {
	private TextRendering() { }

	public static void drawText(Graphics2D graphics, double x, double y, String text, TextFormat textFormat) {
		FontMetrics fontMetrics = graphics.getFontMetrics();
		String[] lines = text.split("(\r\n)|\r|\n");

		y += calculateVerticalOffset(textFormat.getVerticalAlign(), fontMetrics, lines.length);

		for (int i = 0; i < lines.length; i++) {
			double xi = x + calculateHorizontalOffset(textFormat.getHorizontalAlign(), fontMetrics, lines[i]);
			graphics.drawString(lines[i], (float) xi, (float)(y + i * fontMetrics.getHeight()));
		}
	}

	private static double calculateVerticalOffset(VerticalAlign verticalAlign, FontMetrics fontMetrics, int lineCount) {
		double capHeight = fontMetrics.getAscent() - fontMetrics.getDescent();
		double leadings = fontMetrics.getHeight() * (lineCount - 1);
		switch (verticalAlign) {
			case TOP:
				return capHeight;
			case MIDDLE:
				return (capHeight - leadings) / 2;
			case BOTTOM:
				return -leadings;
			default:
				throw new RuntimeException("Unknown vertical alignment option.");
		}
	}

	private static double calculateHorizontalOffset(HorizontalAlign horizontalAlign, FontMetrics fontMetrics, String text) {
		switch (horizontalAlign) {
			case LEFT:
				return 0;
			case CENTER:
				return -fontMetrics.stringWidth(text) / 2D;
			case RIGHT:
				return -fontMetrics.stringWidth(text);
			default:
				throw new RuntimeException("Unknown horizontal alignment option.");
		}
	}

	public static Font createFont(TextFormat format) {
		return Font.getFont(new HashMap<TextAttribute, Object>() {{
				put(TextAttribute.FAMILY, format.getFontName());
				put(TextAttribute.SIZE, format.getFontSize());
				put(TextAttribute.POSTURE, format.isItalic() ? 0.2f : 0);
				put(TextAttribute.UNDERLINE, underlineEnumToTextAttributeUnderlineNumber(format.getUnderline()));
				put(TextAttribute.WEIGHT, format.isBold() ? 2.0f : 1.0f);
				put(TextAttribute.KERNING, TextAttribute.KERNING_ON); //Kerning is always on, 0 == KERNING_OFF
				put(TextAttribute.STRIKETHROUGH, format.isStrikethrough());
		}});
	}

	private static int underlineEnumToTextAttributeUnderlineNumber(Underline underline) {
		switch (underline) {
			case NONE: return -1;
			case SOLID: return TextAttribute.UNDERLINE_ON;
			case DASHED: return TextAttribute.UNDERLINE_LOW_DASHED;
			case DOTTED: return TextAttribute.UNDERLINE_LOW_DOTTED;
			case WAVY: return TextAttribute.UNDERLINE_LOW_GRAY;
			default: throw new RuntimeException("Unknown underline type");
		}
	}
}
