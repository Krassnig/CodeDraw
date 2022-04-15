package codedraw.drawing;

import java.awt.*;

class TextRendering {
	private TextRendering() { }

	public static void drawText(Graphics2D graphics, double x, double y, String text, TextFormat textFormat) {
		FontMetrics fontMetrics = graphics.getFontMetrics();
		String[] lines = text.split("(\r\n)|\r|\n", -1);

		y += calculateVerticalOffset(textFormat.getTextOrigin(), fontMetrics, lines.length);

		for (int i = 0; i < lines.length; i++) {
			double xi = x + calculateHorizontalOffset(textFormat.getTextOrigin(), fontMetrics, lines[i]);
			graphics.drawString(lines[i], (float) xi, (float)(y + i * fontMetrics.getHeight()));
		}
	}

	private static double calculateVerticalOffset(TextOrigin verticalAlign, FontMetrics fontMetrics, int lineCount) {
		double capHeight = fontMetrics.getAscent() - fontMetrics.getDescent();
		double leadings = fontMetrics.getHeight() * (lineCount - 1);

		switch (verticalAlign) {
			case TOP_LEFT:
			case TOP_MIDDLE:
			case TOP_RIGHT:
				return capHeight;
			case CENTER_LEFT:
			case CENTER:
			case CENTER_RIGHT:
				return (capHeight - leadings) / 2;
			case BOTTOM_LEFT:
			case BOTTOM_MIDDLE:
			case BOTTOM_RIGHT:
				return -leadings;
			default:
				throw new RuntimeException("Unknown vertical alignment option.");
		}
	}

	private static double calculateHorizontalOffset(TextOrigin horizontalAlign, FontMetrics fontMetrics, String text) {
		switch (horizontalAlign) {
			case TOP_LEFT:
			case CENTER_LEFT:
			case BOTTOM_LEFT:
				return 0;
			case TOP_MIDDLE:
			case CENTER:
			case BOTTOM_MIDDLE:
				return -fontMetrics.stringWidth(text) / 2D;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				return -fontMetrics.stringWidth(text);
			default:
				throw new RuntimeException("Unknown horizontal alignment option.");
		}
	}
}
