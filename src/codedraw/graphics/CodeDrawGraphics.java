package codedraw.graphics;

import codedraw.Corner;
import codedraw.Interpolation;
import codedraw.Palette;
import codedraw.textformat.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class CodeDrawGraphics {
	public CodeDrawGraphics(int width, int height) {
		this(width, height, 1, 1);
	}

	private CodeDrawGraphics(int width, int height, int xScale, int yScale) {
		if (xScale < 1 || yScale < 1) throw new RuntimeException("scale must be greater than 0");
		this.width = width;
		this.height = height;
		image = new BufferedImage(width * xScale, height * yScale, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.scale(xScale, yScale);

		setRenderingHint(AlphaInterpolation.QUALITY);
		setRenderingHint(ColorRendering.QUALITY);
		setRenderingHint(Rendering.QUALITY);
		setRenderingHint(Dithering.ENABLE);

		setRenderingHint(FractionalMetrics.ON); // tested
		setRenderingHint(StrokeControl.PURE); // tested

		setRenderingHint(ResolutionVariant.DEFAULT); // unknown
		setRenderingHint(TextAntiAliasing.ON); // user settings
		setRenderingHint(AntiAliasing.ON); // user settings
		setRenderingHint(Interpolation.BICUBIC); // draw image specific

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
		setRenderingHint(isAntiAliased ? AntiAliasing.ON : AntiAliasing.OFF);
	}

	private boolean isTextAntiAliased = true;
	public boolean isTextAntiAliased() { return isTextAntiAliased; }
	public void isTextAntiAliased(boolean isTextAntiAliased) {
		this.isTextAntiAliased = isTextAntiAliased;
		setRenderingHint(isTextAntiAliased ? TextAntiAliasing.ON : TextAntiAliasing.OFF);
	}

	private void setRenderingHint(RenderingHintValue hint) {
		RenderingHintValue.applyHint(g, hint);
	}

	private void setRenderingHint(Interpolation interpolation) {
		RenderingHintValue.applyHint(g, interpolation);
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

	public void drawImage(double x, double y, double width, double height, Image image, Interpolation interpolation) {
		setRenderingHint(interpolation);
		g.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
	}

	public void drawText(double x, double y, String text, TextFormat textFormat) {
		g.setFont(CodeDrawGraphicsText.createFont(textFormat));
		CodeDrawGraphicsText.drawText(g, x, y, text, textFormat);
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
		copyTo(g);
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

	public void copyTo(Graphics2D target) {
		RenderingHintValue.applyHint(target, Interpolation.BICUBIC);
		copyTo((Graphics)target);
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
		return Math.toDegrees(-startRadians);
	}

	private static double transformSweep(double sweepRadians) {
		return Math.toDegrees(-sweepRadians);
	}

	public static CodeDrawGraphics createDPIAwareCodeDrawGraphics(int width, int height) {
		AffineTransform max = getMaximumDPIFromAllScreens();
		return new CodeDrawGraphics(width, height, upscale(max.getScaleX()), upscale(max.getScaleY()));
	}

	private static int upscale(double scale) {
		return Math.max(1, (int)Math.ceil(scale));
	}

	private static AffineTransform getMaximumDPIFromAllScreens() {
		AffineTransform max = new AffineTransform();

		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			for (GraphicsConfiguration configuration : device.getConfigurations()) {
				max = maxAffineTransformScale(max, configuration.getDefaultTransform());
			}
		}

		return max;
	}

	private static AffineTransform maxAffineTransformScale(AffineTransform a, AffineTransform b) {
		AffineTransform result = new AffineTransform();
		result.setToScale(
				Math.max(a.getScaleX(), b.getScaleX()),
				Math.max(a.getScaleY(), b.getScaleY())
		);
		return result;
	}
}
