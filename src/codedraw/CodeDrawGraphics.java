package codedraw;

import codedraw.textformat.*;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

class CodeDrawGraphics {
	private CodeDrawGraphics(int width, int height, int xScale, int yScale) {
		if (xScale < 1 || yScale < 1) throw new RuntimeException("scale must be greater than 0");
		this.width = width;
		this.height = height;

		image = new BufferedImage(width * xScale, height * yScale, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.scale(xScale, yScale);
		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

		setColor(Palette.BLACK);
		setLineWidth(1);
		isAntiAliased(true);
		isTextAntiAliased(true);
		setCorner(Corner.SHARP);

		clear();
	}

	private BufferedImage image;
	private Graphics2D g;
	private int width;
	private int height;

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public Color getColor() { return g.getColor(); }
	public void setColor(Color color) { g.setColor(color); }

	private double lineWidth = 1;
	public double getLineWidth() { return lineWidth; }
	public void setLineWidth(double lineWidth) {
		this.lineWidth = lineWidth;
		updateBrush();
	}

	private Corner corner = Corner.SHARP;
	public Corner getCorner() { return corner; }
	public void setCorner(Corner corner) {
		this.corner = corner;
		updateBrush();
	}

	private boolean isAntiAliased = true;
	public boolean isAntiAliased() { return isAntiAliased; }
	public void isAntiAliased(boolean isAntiAliased) {
		this.isAntiAliased = isAntiAliased;
		g.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				isAntiAliased ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF
		));
	}

	private boolean isTextAntiAliased = true;
	public boolean isTextAntiAliased() { return isTextAntiAliased; }
	public void isTextAntiAliased(boolean isTextAntiAliased) {
		this.isTextAntiAliased = isTextAntiAliased;
		g.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				isAntiAliased ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
		));
	}

	private void updateBrush() {
		g.setStroke(new BasicStroke((float)lineWidth, getCap(corner), getJoin(corner)));
	}

	private static int getCap(Corner corner) {
		switch (corner) {
			case SHARP: return BasicStroke.CAP_SQUARE;
			case ROUND: return BasicStroke.CAP_ROUND;
			case BEVEL: return BasicStroke.CAP_BUTT;
			default: throw new RuntimeException("Unknown corner type.");
		}
	}

	private static int getJoin(Corner corner) {
		switch (corner) {
			case SHARP: return BasicStroke.JOIN_MITER;
			case ROUND: return BasicStroke.JOIN_ROUND;
			case BEVEL: return BasicStroke.JOIN_BEVEL;
			default: throw new RuntimeException("Unknown corner type.");
		}
	}

	public void drawPixel(double x, double y) {
		g.fill(createSharpRectangle(x, y, 1, 1));
	}

	public void drawPoint(double x, double y) {
		g.fill(createEllipse(x, y, getLineWidth(), getLineWidth()));
	}

	public void drawLine(double startX, double startY, double endX, double endY) {
		g.draw(createLine(startX, startY, endX, endY));
	}

	public void drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		g.draw(createCurve(startX, startY, controlX, controlY, endX, endY));
	}

	public void drawBezier(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		g.draw(createBezierCurve(startX, startY, control1X, control1Y, control2X, control2Y, endX, endY));
	}

	public void drawSquare(double x, double y, double sideLength) {
		g.draw(createDrawRectangle(x, y, sideLength, sideLength));
	}

	public void fillSquare(double x, double y, double sideLength) {
		g.fill(createFillRectangle(x, y, sideLength, sideLength));
	}

	public void drawRectangle(double x, double y, double width, double height) {
		g.draw(createDrawRectangle(x, y, width, height));
	}

	public void fillRectangle(double x, double y, double width, double height) {
		g.fill(createFillRectangle(x, y, width, height));
	}

	public void drawCircle(double x, double y, double radius) {
		g.draw(createEllipse(x, y, radius, radius));
	}

	public void fillCircle(double x, double y, double radius) {
		g.fill(createEllipse(x, y, radius, radius));
	}

	public void drawEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		g.draw(createEllipse(x, y, horizontalRadius, verticalRadius));
	}

	public void fillEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		g.fill(createEllipse(x, y, horizontalRadius, verticalRadius));
	}

	public void drawArc(double x, double y, double radius, double startRadians, double sweepRadians) {
		g.draw(createArc(x, y, radius, radius, startRadians, sweepRadians));
	}

	public void drawArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		g.draw(createArc(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians));
	}

	public void drawPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		g.draw(createPie(x, y, radius, radius, startRadians, sweepRadians));
	}

	public void drawPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		g.draw(createPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians));
	}

	public void fillPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		g.fill(createPie(x, y, radius, radius, startRadians, sweepRadians));
	}

	public void fillPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		g.fill(createPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians));
	}

	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		g.draw(createTriangle(x1, y1, x2, y2, x3, y3));
	}

	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		g.fill(createTriangle(x1, y1, x2, y2, x3, y3));
	}

	public void drawPolygon(double... points) {
		g.draw(createPolygon(points));
	}

	public void fillPolygon(double... points) {
		g.fill(createPolygon(points));
	}

	public void drawImage(double x, double y, Image image) {
		g.drawImage(image, (int)x, (int)y, null);
	}

	public void drawImage(double x, double y, double width, double height, Image image) {
		g.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
	}

	public void drawText(double x, double y, String text, TextFormat textFormat) {
		Font font = createFont(textFormat);
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics(font);

		x -= getHorizontalOffset(textFormat.getHorizontalAlign(), fontMetrics, text);
		y -= getVerticalOffset(textFormat.getVerticalAlign(), fontMetrics, font);

		g.drawString(text, (float) x, (float) y);
	}

	private static Font createFont(TextFormat format) {
		Font font = new Font(format.getFontName(), Font.PLAIN, format.getFontSize());
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>() {
			{
				put(TextAttribute.POSTURE, format.isItalic() ? 0.2f : 0);
				put(TextAttribute.UNDERLINE, getUnderlineTextAttributeValue(format.getUnderline()));
				put(TextAttribute.WEIGHT, format.isBold() ? 2.0f : 1.0f);
				put(TextAttribute.KERNING, TextAttribute.KERNING_ON); //Kerning is always on, 0 == KERNING_OFF
				put(TextAttribute.STRIKETHROUGH, format.isStrikethrough());
			}
		};
		return font.deriveFont(attributes);
	}

	private static double getVerticalOffset(VerticalAlign verticalAlign, FontMetrics fontMetrics, Font font) {
		int d = fontMetrics.getHeight() - fontMetrics.getAscent() - font.getSize();
		switch (verticalAlign) {
			case TOP:
				return d;
			case MIDDLE:
				return d / 2.0;
			case BOTTOM:
				return 0;
			default:
				throw new RuntimeException("Unknown vertical alignment option");
		}
	}

	private static double getHorizontalOffset(HorizontalAlign horizontalAlign, FontMetrics fontMetrics, String text) {
		switch (horizontalAlign) {
			case LEFT:
				return 0;
			case CENTER:
				return fontMetrics.stringWidth(text) / 2.0;
			case RIGHT:
				return fontMetrics.stringWidth(text);
			default:
				throw new RuntimeException("Unknown horizontal alignment option");
		}
	}

	private static int getUnderlineTextAttributeValue(Underline underline) {
		switch (underline) {
			case NONE: return -1;
			case SOLID: return TextAttribute.UNDERLINE_ON;
			case DASHED: return TextAttribute.UNDERLINE_LOW_DASHED;
			case DOTTED: return TextAttribute.UNDERLINE_LOW_DOTTED;
			case WAVY: return TextAttribute.UNDERLINE_LOW_GRAY;
			default: throw new RuntimeException("Unknown underline type");
		}
	}

	public void clear() {
		clear(Palette.WHITE);
	}

	public void clear(Color color) {
		Color c = getColor();
		setColor(color);
		g.fill(createSharpRectangle(0, 0, getWidth(), getHeight()));
		setColor(c);
	}

	public BufferedImage copyAsImage() {
		BufferedImage result = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		Graphics2D g = result.createGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), Palette.WHITE, null);
		g.dispose();

		return result;
	}

	public void copyTo(Graphics target) {
		Color c = target.getColor();
		target.setColor(Palette.WHITE);
		target.drawRect(0, 0, getWidth(), getHeight());
		target.drawImage(image, 0, 0, getWidth(), getHeight(), Palette.WHITE, null);
		target.setColor(c);
	}

	public void copyTo(CodeDrawGraphics target) {
		copyTo(target.g);
	}

	public void dispose() {
		g.dispose();
		image = null;
	}

	private static Line2D createLine(double startX, double startY, double endX, double endY) {
		return new Line2D.Double(
				startX, startY,
				endX, endY
		);
	}

	private static QuadCurve2D createCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		return new QuadCurve2D.Double(
				startX, startY,
				controlX, controlY,
				endX, endY
		);
	}

	private static CubicCurve2D createBezierCurve(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		return new CubicCurve2D.Double(
				startX, startY,
				control1X, control1Y,
				control2X, control2Y,
				endX, endY
		);
	}

	private Shape createFillRectangle(double x, double y, double width, double height) {
		if (corner == Corner.SHARP) {
			return createSharpRectangle(x, y, width, height);
		}
		else if (corner == Corner.ROUND) {
			return createRoundRectangle(x, y, width, height);
		}
		else {
			return createBevelRectangle(x, y, width, height);
		}
	}

	private Shape createDrawRectangle(double x, double y, double width, double height) {
		return createSharpRectangle(x, y, width, height);
	}

	private static Rectangle2D createSharpRectangle(double x, double y, double width, double height) {
		return new Rectangle2D.Double(
				x, y,
				width, height
		);
	}

	private RoundRectangle2D createRoundRectangle(double x, double y, double width, double height) {
		return new RoundRectangle2D.Double(
				x, y,
				width, height,
				lineWidth, lineWidth
		);
	}

	private Path2D createBevelRectangle(double x, double y, double width, double height) {
		double lw = lineWidth / 2;
		return createPolygon(
				x + lw, y,
				x + width - lw, y,
				x + width, y + lw,
				x + width, y + height - lw,
				x + width - lw, y + height,
				x + lw, y + height,
				x, y + height - lw,
				x, y + lw
		);
	}

	private static Ellipse2D createEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		return new Ellipse2D.Double(
				x - horizontalRadius, y - verticalRadius,
				2 * horizontalRadius, 2 * verticalRadius
		);
	}

	private static Arc2D createArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		return new Arc2D.Double(
				x - horizontalRadius,
				y - verticalRadius,
				2 * horizontalRadius,
				2 * verticalRadius,
				transformStart(startRadians),
				transformSweep(sweepRadians),
				Arc2D.OPEN
		);
	}

	private static Arc2D createPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		return new Arc2D.Double(
				x - horizontalRadius,
				y - verticalRadius,
				2 * horizontalRadius,
				2 * verticalRadius,
				transformStart(startRadians),
				transformSweep(sweepRadians),
				Arc2D.PIE
		);
	}

	private static Path2D createTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		return createPolygon(
				x1, y1,
				x2, y2,
				x3, y3
		);
	}

	private static Path2D createPolygon(double... doubles) {
		Path2D.Double result = new Path2D.Double();

		result.moveTo(doubles[0], doubles[1]);
		for (int i = 2; i < doubles.length; i += 2) {
			result.lineTo(doubles[i], doubles[i + 1]);
		}
		result.closePath();

		return result;
	}

	private static double transformStart(double startRadians) {
		return 90 - Math.toDegrees(startRadians);
	}

	private static double transformSweep(double sweepRadians) {
		return - Math.toDegrees(sweepRadians);
	}

	public static CodeDrawGraphics createDPIAwareCodeDrawGraphics(int width, int height) {
		Screen s = Screen.DEFAULT_SCREEN;
		return new CodeDrawGraphics(width, height, upscale(s.getXScale()), upscale(s.getYScale()));
	}

	private static int upscale(double scale) {
		return Math.max(1, (int)Math.ceil(scale));
	}
}
