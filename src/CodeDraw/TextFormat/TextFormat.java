package CodeDraw.TextFormat;

import java.awt.*;

public class TextFormat {
	private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;
	private VerticalAlignment verticalAlignment = VerticalAlignment.BOTTOM;


	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}

	public VerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}

	public void renderText(Graphics2D graphics2D, double x, double y, String text) {

		x -= getHorizontalOffset(graphics2D, text);
		y -= getVerticalOffset(graphics2D.getFont());

		graphics2D.drawString(text, (float) x, (float) y);
	}

	private double getVerticalOffset(Font font) {
		switch (verticalAlignment) {
			case BOTTOM:
				return 0;
			case TOP:
				return font.getSize();
			case CENTER:
				return font.getSize() / 2.0;
			default:
				throw new RuntimeException("Unknown vertical alignment option");
		}
	}

	private double getHorizontalOffset(Graphics2D graphics, String text) {
		FontMetrics fm = graphics.getFontMetrics(graphics.getFont());
		switch (horizontalAlignment) {
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
