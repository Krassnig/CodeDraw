package codedraw.images;

import codedraw.*;
import codedraw.textformat.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class CodeDrawImage {
	public static CodeDrawImage fromFile(String pathToImage) {
		if (pathToImage == null) throw createParameterNullException("pathToImage");

		try {
			return new CodeDrawImage(ImageIO.read(new File(pathToImage)));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static CodeDrawImage fromBase64String(String base64) {
		if (base64 == null) throw createParameterNullException("base64");

		try {
			return new CodeDrawImage(ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(base64))));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public static CodeDrawImage fromDPIAwareSize(int width, int height) {
		if (width < 1) throw createParameterMustBeGreaterThanZeroException("width");
		if (height < 1) throw createParameterMustBeGreaterThanZeroException("height");

		AffineTransform max = getMaximumDPIFromAllScreens();
		return new CodeDrawImage(width, height, upscale(max.getScaleX()), upscale(max.getScaleY()));
	}

	public static void saveAsPNG(CodeDrawImage image, String pathToImage) {
		if (image == null) throw createParameterNullException("image");
		if (pathToImage == null) throw createParameterNullException("pathToImage");

		try {
			ImageIO.write(image.image, "png", new File(pathToImage));
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public CodeDrawImage(CodeDrawImage image) {
		this(checkParameterNull(image, "image").image);
	}

	public CodeDrawImage(Image image) {
		this(
				checkParameterNull(image, "image").getWidth(null),
				checkParameterNull(image, "image").getHeight(null)
		);
		drawImageInternal(0, 0, image.getWidth(null), image.getHeight(null), image, Interpolation.NEAREST_NEIGHBOR);
	}

	public CodeDrawImage(int width, int height) {
		this(width, height, 1, 1);
	}

	private CodeDrawImage(int width, int height, int xScale, int yScale) {
		if (width < 1) throw createParameterMustBeGreaterThanZeroException("width");
		if (height < 1) throw createParameterMustBeGreaterThanZeroException("height");
		if (xScale < 1) throw createParameterMustBeGreaterThanZeroException("xScale");
		if (yScale < 1) throw createParameterMustBeGreaterThanZeroException("yScale");
		this.width = width;
		this.height = height;

		image = new BufferedImage(width * xScale, height * yScale, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		g.scale(xScale, yScale);

		setRenderingHint(RHAlphaInterpolation.QUALITY);
		setRenderingHint(RHColorRendering.QUALITY);
		setRenderingHint(RHRendering.QUALITY);
		setRenderingHint(RHDithering.ENABLE);
		setRenderingHint(RHFractionalMetrics.ON); // tested
		setRenderingHint(RHResolutionVariant.DEFAULT); // unknown

		setColor(Palette.BLACK);
		setLineWidth(1);
		setAntiAliased(true);
		setCorner(Corner.SHARP);

		clear();
	}

	private BufferedImage image;
	private Graphics2D g;
	private int width;
	private int height;

	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public Color getColor() {
		return g.getColor();
	}

	public void setColor(Color color) {
		if (color == null) throw createParameterNullException("color");

		g.setColor(color);
	}

	private double lineWidth = 1;
	public double getLineWidth() { return lineWidth; }
	public void setLineWidth(double lineWidth) {
		if (lineWidth <= 0) throw createParameterMustBeGreaterThanZeroException("lineWidth");

		this.lineWidth = lineWidth;
		updateBrush();
	}

	private Corner corner = Corner.SHARP;
	public Corner getCorner() { return corner; }
	public void setCorner(Corner corner) {
		if (corner == null) throw createParameterNullException("corner");

		this.corner = corner;
		updateBrush();
	}

	private boolean isAntiAliased = true;
	public boolean isAntiAliased() { return isAntiAliased; }
	public void setAntiAliased(boolean isAntiAliased) {
		this.isAntiAliased = isAntiAliased;
		if (isAntiAliased) {
			setRenderingHint(RHAntiAliasing.ON);
			setRenderingHint(RHTextAntiAliasing.ON);
			setRenderingHint(RHStrokeControl.PURE);
		}
		else {
			setRenderingHint(RHAntiAliasing.OFF);
			setRenderingHint(RHTextAntiAliasing.OFF);
			setRenderingHint(RHStrokeControl.NORMALIZE);
		}
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
		if (sideLength < 0) throw createParameterMustBeGreaterOrEqualToZeroException("sideLength");

		g.draw(createDrawRectangle(x, y, sideLength, sideLength));
	}

	public void fillSquare(double x, double y, double sideLength) {
		if (sideLength < 0) throw createParameterMustBeGreaterOrEqualToZeroException("sideLength");

		g.fill(createFillRectangle(x, y, sideLength, sideLength));
	}

	public void drawRectangle(double x, double y, double width, double height) {
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");

		g.draw(createDrawRectangle(x, y, width, height));
	}

	public void fillRectangle(double x, double y, double width, double height) {
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");

		g.fill(createFillRectangle(x, y, width, height));
	}

	public void drawCircle(double x, double y, double radius) {
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.draw(createEllipse(x, y, radius, radius));
	}

	public void fillCircle(double x, double y, double radius) {
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.fill(createEllipse(x, y, radius, radius));
	}

	public void drawEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.draw(createEllipse(x, y, horizontalRadius, verticalRadius));
	}

	public void fillEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.fill(createEllipse(x, y, horizontalRadius, verticalRadius));
	}

	public void drawArc(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.draw(createArc(x, y, radius, radius, startRadians, sweepRadians));
	}

	public void drawArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.draw(createArc(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians));
	}

	public void drawPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.draw(createPie(x, y, radius, radius, startRadians, sweepRadians));
	}

	public void drawPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.draw(createPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians));
	}

	public void fillPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.fill(createPie(x, y, radius, radius, startRadians, sweepRadians));
	}

	public void fillPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.fill(createPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians));
	}

	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		g.draw(createTriangle(x1, y1, x2, y2, x3, y3));
	}

	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		g.fill(createTriangle(x1, y1, x2, y2, x3, y3));
	}

	public void drawPolygon(double... points) {
		if (isInvalidPolygonCount(points)) throw createPolygonCountException(points, "drawPolygon");

		g.draw(createPolygon(points));
	}

	public void fillPolygon(double... points) {
		if (isInvalidPolygonCount(points)) throw createPolygonCountException(points, "fillPolygon");

		g.fill(createPolygon(points));
	}

	private void drawImageInternal(double x, double y, double width, double height, Image image, Interpolation interpolation) {
		setRenderingHint(interpolation);
		g.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
	}

	public void drawImage(CodeDrawImage image) {
		if (image == null) throw createParameterNullException("image");

		drawImageInternal(0, 0, image.getWidth(), image.getHeight(), image.image, Interpolation.NEAREST_NEIGHBOR);
	}

	public void drawImage(double x, double y, CodeDrawImage image) {
		if (image == null) throw createParameterNullException("image");

		drawImageInternal(x, y, image.getWidth(), image.getHeight(), image.image, Interpolation.NEAREST_NEIGHBOR);
	}

	public void drawImage(double x, double y, double width, double height, CodeDrawImage image) {
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");
		if (image == null) throw createParameterNullException("image");

		drawImageInternal(x, y, width, height, image.image, Interpolation.BICUBIC);
	}

	public void drawImage(double x, double y, double width, double height, CodeDrawImage image, Interpolation interpolation) {
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");
		if (image == null) throw createParameterNullException("image");

		drawImageInternal(x, y, width, height, image.image, interpolation);
	}

	public void drawText(double x, double y, String text, TextFormat textFormat) {
		if (text == null) throw createParameterNullException("text");
		if (textFormat == null) throw createParameterNullException("textFormat");

		g.setFont(TextRendering.createFont(textFormat));
		TextRendering.drawText(g, x, y, text, textFormat);
	}

	public void clear() {
		clear(Palette.WHITE);
	}

	public void clear(Color color) {
		if (color == null) throw createParameterNullException("color");

		Color c = getColor();
		setColor(color);
		g.fill(createSharpRectangle(0, 0, getWidth(), getHeight()));
		setColor(c);
	}

	public void copyTo(Graphics target, Interpolation interpolation) {
		if (target == null) throw createParameterNullException("target");
		if (interpolation == null) throw createParameterNullException("interpolation");

		if (target instanceof Graphics2D) {
			RenderingHintValue.applyHint((Graphics2D) target, interpolation);
		}

		Color c = target.getColor();
		target.setColor(Palette.WHITE);
		target.drawRect(0, 0, getWidth(), getHeight());
		target.drawImage(image, 0, 0, getWidth(), getHeight(), Palette.WHITE, null);
		target.setColor(c);
	}

	public BufferedImage convertToBufferedImage() {
		CodeDrawImage result = new CodeDrawImage(this);
		result.g.dispose();
		return result.image;
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

	private static IllegalArgumentException createParameterNullException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
	}

	private static IllegalArgumentException createParameterMustBeGreaterThanZeroException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " must be greater than zero.");
	}

	private static IllegalArgumentException createParameterMustBeGreaterOrEqualToZeroException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " must be equal or greater than zero.");
	}

	private static boolean isInvalidPolygonCount(double[] polygonParameter) {
		return polygonParameter.length < 4 || (polygonParameter.length & 1) == 1;
	}

	private static IllegalArgumentException createPolygonCountException(double[] polygonParameter, String methodName) {
		if (polygonParameter.length < 4) {
			return new IllegalArgumentException("You must pass at least 4 arguments to " + methodName + ". A polygon must have at least two points (2 arguments for each point).");
		}
		else if ((polygonParameter.length & 1) == 1) {
			return new IllegalArgumentException(methodName + " must be called with an even number of arguments. Each argument pair represents the x and y coordinate of on point of the polygon.");
		}
		else {
			throw new RuntimeException();
		}
	}

	private static <T> T checkParameterNull(T parameter, String parameterName) {
		if (parameter == null)
			throw new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
		else
			return parameter;
	}
}
