package CodeDraw.TextFormat;

import java.awt.*;

public class TextFormat {
	private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
	private VerticalAlign verticalAlign = VerticalAlign.TOP;


	public HorizontalAlign getHorizontalAlign() {
		return horizontalAlign;
	}

	public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
		this.horizontalAlign = horizontalAlign;
	}

	public VerticalAlign getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(VerticalAlign verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	public void renderText(Graphics2D graphics2D, double x, double y, String text) {

		x -= getHorizontalOffset(graphics2D, text);
		y -= getVerticalOffset(graphics2D.getFont());

		graphics2D.drawString(text, (float) x, (float) y);
	}

	private double getVerticalOffset(Font font) {
		switch (verticalAlign) {
			case TOP:
				return 0;
			case BOTTOM:
				return font.getSize();
			case MIDDLE:
				return font.getSize() / 2.0;
			default:
				throw new RuntimeException("Unknown vertical alignment option");
		}
	}

	private double getHorizontalOffset(Graphics2D graphics, String text) {
		FontMetrics fm = graphics.getFontMetrics(graphics.getFont());
		switch (horizontalAlign) {
			case LEFT:
				return 0;
			case RIGHT:
				return fm.stringWidth(text);
			case CENTER:
				return fm.stringWidth(text) / 2.0;
			default:
				throw new RuntimeException("Unknown horizontal alignment option");
		}
	}
}
