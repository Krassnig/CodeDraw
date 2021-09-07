package codedraw;

import codedraw.textformat.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CodeDraw is an easy-to-use drawing library.<br>
 * <br>
 * How to use:<br>
 * <pre>{@code
 * CodeDraw cd = new CodeDraw(); // creates a canvas of size 600x600 pixel
 *
 * // All following drawn objects will be red,
 * // until the color is set to a different color.
 * cd.setColor(Palette.RED);
 *
 * // draws a red circle at the center of the canvas with a radius of 50 pixel.
 * // The circle is not yet displayed!
 * cd.drawCircle(300, 300, 50);
 *
 * // Must be called to display everything that has been drawn until now!
 * cd.show();
 * }</pre>
 * There are a few key ideas described by certain keywords used in this library:
 * <br>
 * <br>
 * <b>canvas</b> - Is the rectangle on the screen that is used for drawing. It's origin
 * point (0, 0) is at the top left. Everything is drawn front the top left to the bottom right.
 * Once the size is set via the constructor the size of the canvas remains fixed.
 * <br>
 * <br>
 * <b>window</b> - Is the frame surrounding the canvas. It is larger than the size given
 * to the constructor of CodeDraw. It contains the closing and minimize button, the title and the CodeDraw icon.
 * <br>
 * <br>
 * <b>event</b> - An event is something that occurs based on user input like the user
 * pressing a button or moving the mouse. You can subscribe to an Event
 * by passing a method reference or lambda to CodeDraw. All events start with the 'on' keyword.
 * Subscribing to an event method will return a Subscription which can be used to unsubscribe from the event.
 * Example events: {@link #onMouseMove} or {@link #onKeyPress(codedraw.EventHandler)}.
 * <br>
 * <br>
 * <b>Fun Fact</b>: You can copy the currently displayed canvas to your clipboard by pressing <b>Ctrl + C</b>
 * <br>
 * <br>
 * @author Niklas Krassnig, Nikolaus Kasyan
 */
public class CodeDraw {
	/**
	 * Creates a canvas with size 600x600 pixels.
	 * The size remains fixed after calling this constructor.
	 */
	public CodeDraw() {
		this(600, 600);
	}

	/**
	 * Creates a canvas with the specified size. The frame surrounding the canvas will be slightly bigger.
	 * Once the size is set via this constructor it remains fixed.
	 * @param canvasWidth must be at least 150 pixel
	 * @param canvasHeight must be at least 1 pixel
	 */
	public CodeDraw(int canvasWidth, int canvasHeight) {
		if (canvasWidth < 150) throw new IllegalArgumentException("The width of the canvas has to be at least 150px.");
		if (canvasHeight < 1) throw new IllegalArgumentException("The height of the canvas has to be positive.");

		this.width = canvasWidth;
		this.height = canvasHeight;

		window = new CanvasWindow(canvasWidth, canvasHeight);
		buffer = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
		g = buffer.createGraphics();
		g.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));

		bindEvents();

		setTitle("CodeDraw");
		setColor(Color.BLACK);
		setLineWidth(1);
		setFormat(new TextFormat());
		isAntiAliased(true);
		setCorner(Corner.Sharp);

		clear();
		show();

		onKeyDown((sender, args) -> {
			if (args.isControlDown() && args.getKeyCode() == KeyEvent.VK_C) {
				sender.window.copyCanvasToClipboard();
			}
		});
	}

	private CanvasWindow window;
	private BufferedImage buffer;
	private Graphics2D g;

	private int width;
	private int height;
	private double lineWidth;
	private TextFormat textFormat;
	private boolean isAntiAliased;
	private Corner corner = Corner.Sharp;

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 */
	public int getWindowPositionX() { return window.getWindowPosition().x; }
	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 */
	public int getWindowPositionY() { return window.getWindowPosition().y; }

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * @param x can be negative
	 */
	public void setWindowPositionX(int x) { window.setWindowPosition(new Point(x, getWindowPositionY())); }
	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * @param y can be negative
	 */
	public void setWindowPositionY(int y) { window.setWindowPosition(new Point(getWindowPositionX(), y)); }

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 */
	public int getCanvasPositionX() { return window.getCanvasPosition().x; }
	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 */
	public int getCanvasPositionY() { return window.getCanvasPosition().y; }

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 */
	public void setCanvasPositionX(int x) { window.setCanvasPosition(new Point(x, getCanvasPositionY())); }
	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 */
	public void setCanvasPositionY(int y) { window.setCanvasPosition(new Point(getCanvasPositionX(), y)); }

	/**
	 * @return width of the canvas
	 */
	public int getWidth() { return width; }

	/**
	 * @return height of the canvas
	 */
	public int getHeight() { return height; }

	/**
	 * Defines the width or thickness of drawn shapes and lines.
	 */
	public double getLineWidth() { return lineWidth; }
	/**
	 * Defines the width or thickness of drawn shapes and lines.
	 */
	public void setLineWidth(double lineWidth) {
		if (lineWidth <= 0) throw new IllegalArgumentException("Argument lineWidth cannot be smaller or equal to 0");

		this.lineWidth = lineWidth;
		updateBrushes();
	}

	/**
	 * Defines the styling of drawn text.
	 * See also {@link #drawText(double, double, String)}
	 */
	public TextFormat getFormat() { return textFormat; }
	/**
	 * Defines the styling of drawn text.
	 * See also {@link #drawText(double, double, String)}
	 */
	public void setFormat(TextFormat textFormat){
		if(textFormat == null) throw createArgumentNull("textFormat");

		this.textFormat = textFormat;
	}

	/**
	 * Defines whether draw text, drawn shapes and filled shapes are anti-aliased.
	 * See <a href="https://en.wikipedia.org/wiki/Spatial_anti-aliasing">Wikipedia Spatial Anti-aliasing</a>
	 */
	public boolean isAntiAliased() { return isAntiAliased; }
	/**
	 * Defines whether drawn text, drawn shapes and filled shapes are anti-aliased.
	 * See <a href="https://en.wikipedia.org/wiki/Spatial_anti-aliasing">Wikipedia Spatial Anti-aliasing</a>
	 */
	public void isAntiAliased(boolean isAntiAliased) {
		this.isAntiAliased = isAntiAliased;
		g.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				isAntiAliased ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF
		));
		g.addRenderingHints(new RenderingHints(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				isAntiAliased ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF
		));
	}

	public Corner getCorner() { return corner; }
	public void setCorner(Corner corner) {
		if (corner == null) throw createArgumentNull("corner");
		this.corner = corner;
		updateBrushes();
	}

	public String getTitle() { return window.getTitle(); }
	public void setTitle(String title)  {
		if (title == null) throw createArgumentNull("title");

		window.setTitle(title);
	}

	public Color getColor() { return g.getColor(); }
	public void setColor(Color color) {
		if (color == null) throw createArgumentNull("color");

		g.setColor(color);
	}

	private void updateBrushes() {
		g.setStroke(new BasicStroke((float)lineWidth, getCap(corner), getJoin(corner)));
	}

	private static int getCap(Corner corner) {
		switch (corner) {
			case Sharp: return BasicStroke.CAP_SQUARE;
			case Round: return BasicStroke.CAP_ROUND;
			case Bevel: return BasicStroke.CAP_BUTT;
			default: throw new RuntimeException("Unknown corner type.");
		}
	}

	private static int getJoin(Corner corner) {
		switch (corner) {
			case Sharp: return BasicStroke.JOIN_MITER;
			case Round: return BasicStroke.JOIN_ROUND;
			case Bevel: return BasicStroke.JOIN_BEVEL;
			default: throw new RuntimeException("Unknown corner type.");
		}
	}

	private void bindEvents() {
		window.onMouseClick((s, a) -> mouseClickEvent.invoke(a));
		window.onMouseMove ((s, a) -> mouseMoveEvent .invoke(a));
		window.onMouseDown ((s, a) -> mouseDownEvent .invoke(a));
		window.onMouseUp   ((s, a) -> mouseUpEvent   .invoke(a));
		window.onMouseWheel((s, a) -> mouseWheelEvent.invoke(a));
		window.onMouseEnter((s, a) -> mouseEnterEvent.invoke(a));
		window.onMouseLeave((s, a) -> mouseLeaveEvent.invoke(a));
		window.onKeyDown   ((s, a) -> keyDownEvent   .invoke(a));
		window.onKeyUp     ((s, a) -> keyUpEvent     .invoke(a));
		window.onKeyPress  ((s, a) -> keyPressEvent  .invoke(a));
		window.onWindowMove((s, a) -> windowMoveEvent.invoke(a));
	}

	/**
	 * Triggers once when a mouse button is pressed down and quickly released again.
	 */
	public Subscription onMouseClick(EventHandler<CodeDraw, MouseEvent> handler) { return mouseClickEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseEvent> mouseClickEvent = new Event<CodeDraw, MouseEvent>(this);

	/**
	 * Triggers continuously while the mouse is being moved.
	 */
	public Subscription onMouseMove(EventHandler<CodeDraw, MouseEvent> handler) { return mouseMoveEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseEvent> mouseMoveEvent = new Event<CodeDraw, MouseEvent>(this);

	/**
	 * Triggers exactly once when a mouse button is pressed down.
	 */
	public Subscription onMouseDown(EventHandler<CodeDraw, MouseEvent> handler) { return mouseDownEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseEvent> mouseDownEvent = new Event<CodeDraw, MouseEvent>(this);

	/**
	 * Triggers when a mouse button is released.
	 */
	public Subscription onMouseUp(EventHandler<CodeDraw, MouseEvent> handler) { return mouseUpEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseEvent> mouseUpEvent = new Event<CodeDraw, MouseEvent>(this);

	/**
	 * Triggers when the mouse enters the canvas.
	 */
	public Subscription onMouseEnter(EventHandler<CodeDraw, MouseEvent> handler) { return mouseEnterEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseEvent> mouseEnterEvent = new Event<CodeDraw, MouseEvent>(this);

	/**
	 * Triggers when the mouse leaves the canvas.
	 */
	public Subscription onMouseLeave(EventHandler<CodeDraw, MouseEvent> handler) { return mouseLeaveEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseEvent> mouseLeaveEvent = new Event<CodeDraw, MouseEvent>(this);

	/**
	 * Triggers each time the mouse wheel is turned
	 */
	public Subscription onMouseWheel(EventHandler<CodeDraw, MouseWheelEvent> handler) { return mouseWheelEvent.onInvoke(handler); }
	private Event<CodeDraw, MouseWheelEvent> mouseWheelEvent = new Event<CodeDraw, MouseWheelEvent>(this);

	/**
	 * Trigger exactly once when a key is pressed down.
	 */
	public Subscription onKeyDown(EventHandler<CodeDraw, KeyEvent> handler) { return keyDownEvent.onInvoke(handler); }
	private Event<CodeDraw, KeyEvent> keyDownEvent = new Event<CodeDraw, KeyEvent>(this);

	/**
	 * Trigger when a key is released.
	 */
	public Subscription onKeyUp(EventHandler<CodeDraw, KeyEvent> handler) { return keyUpEvent.onInvoke(handler); }
	private Event<CodeDraw, KeyEvent> keyUpEvent = new Event<CodeDraw, KeyEvent>(this);

	/**
	 * onKeyPress will continuously trigger while a key is being held down.
	 */
	public Subscription onKeyPress(EventHandler<CodeDraw, KeyEvent> handler) { return keyPressEvent.onInvoke(handler); }
	private Event<CodeDraw, KeyEvent> keyPressEvent = new Event<CodeDraw, KeyEvent>(this);

	/**
	 * Triggers every time the CodeDraw window is moved.
	 */
	public Subscription onWindowMove(EventHandler<CodeDraw, ComponentEvent> handler) { return windowMoveEvent.onInvoke(handler); }
	private Event<CodeDraw, ComponentEvent> windowMoveEvent = new Event<CodeDraw, ComponentEvent>(this);

	/**
	 * Draws the text at the specified (x, y) coordinate.
	 * Formatting options can be set via the TextFormat object.
	 * See {@link #getFormat()}, {@link #setFormat(TextFormat)} and the {@link codedraw.textformat.TextFormat} class.
	 * If not specified otherwise in the TextFormat object the (x, y) coordinates will be in the top left corner of the text.
	 */
	public void drawText(double x, double y, String text) {
		if (text == null) throw createArgumentNull("text");

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
				put(TextAttribute.UNDERLINE, format.getUnderline().getUnderline());
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

	/**
	 * Draws a point which is exactly 1x1 pixel in size.
	 */
	public void drawPixel(double x, double y) {
		fillSquare(x, y, 1);
	}

	/**
	 * Draws a point which changes size depending on the {@link #getLineWidth()}
	 */
	public void drawPoint(double x, double y) {
		fillCircle(x, y, getLineWidth());
	}

	/**
	 * Draws a straight line between the start point and end point.
	 */
	public void drawLine(double startX, double startY, double endX, double endY) {
		g.draw(new Line2D.Double(
				startX, startY,
				endX, endY
		));
	}

	/**
	 * Draws a quadratic bezier curve. See: <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The controlX/Y parameter specifies in what way the curve will be bent.
	 */
	public void drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		g.draw(new QuadCurve2D.Double(
				startX, startY,
				controlX, controlY,
				endX, endY
		));
	}

	/**
	 * Draws a cubic bezier curve. See <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The control1X/Y and control2X/Y parameter specify in what way the curve will be bent.
	 */
	public void drawBezier(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		g.draw(new CubicCurve2D.Double(
				startX, startY,
				control1X, control1Y,
				control2X, control2Y,
				endX, endY
		));
	}

	/**
	 * Draws the outline of a square.
	 * @param x The top left corner of the square
	 * @param y The top left corner of the square
	 */
	public void drawSquare(double x, double y, double sideLength) {
		if (sideLength < 0) throw createArgumentNotNegative("size");

		drawRectangle(x, y, sideLength, sideLength);
	}

	/**
	 * Draws a
	 * @param x The top left corner of the square
	 * @param y The top left corner of the square
	 */
	public void fillSquare(double x, double y, double sideLength) {
		if (sideLength < 0) throw createArgumentNotNegative("size");

		fillRectangle(x, y, sideLength, sideLength);
	}

	/**
	 * @param x The top left corner of the rectangle
	 * @param y The top left corner of the rectangle
	 */
	public void drawRectangle(double x, double y, double width, double height) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");

		g.draw(new Rectangle2D.Double(
				x, y,
				width, height
		));
	}

	/**
	 * Draws a filled rectangle.
	 * @param x The top left corner of the rectangle
	 * @param y The top left corner of the rectangle
	 */
	public void fillRectangle(double x, double y, double width, double height) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");

		g.fill(new Rectangle2D.Double(
				x, y,
				width, height
		));
	}

	/**
	 * Draws the outline of a circle.
	 * The center of the circle will be at the specified (x, y) coordinate.
	 * @param x The center of the circle
	 * @param y The center of the circle
	 * @param radius The radius of the circle.
	 */
	public void drawCircle(double x, double y, double radius) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		drawEllipse(x, y, radius, radius);
	}

	/**
	 * Draws a filled circle.
	 * The center of the circle will be at the specified (x, y) coordinate.
	 * @param x The center of the circle
	 * @param y The center of the circle
	 * @param radius The radius of the circle.
	 */
	public void fillCircle(double x, double y, double radius) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		fillEllipse(x, y, radius, radius);
	}

	/**
	 * Draws the outline of an ellipse.
	 * The center of the ellipse will be at the specified (x, y) coordinate.
	 * @param x The center of the ellipse
	 * @param y The center of the ellipse
	 * @param horizontalRadius 2 * horizontalRadius is the width of the ellipse
	 * @param verticalRadius 2 * verticalRadius is the height of the ellipse
	 */
	public void drawEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		if (horizontalRadius < 0) throw createArgumentNotNegative("horizontalRadius");
		if (verticalRadius < 0) throw createArgumentNotNegative("verticalRadius");

		g.draw(new Ellipse2D.Double(
				x - horizontalRadius, y - verticalRadius,
				2 * horizontalRadius, 2 * verticalRadius
		));
	}

	/**
	 * Draws a filled ellipse.
	 * The center of the ellipse will be at the specified (x, y) coordinate.
	 * @param x The center of the ellipse
	 * @param y The center of the ellipse
	 * @param horizontalRadius 2 * horizontalRadius is the width of the ellipse
	 * @param verticalRadius 2 * verticalRadius is the height of the ellipse
	 */
	public void fillEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		if (horizontalRadius < 0) throw createArgumentNotNegative("horizontalRadius");
		if (verticalRadius < 0) throw createArgumentNotNegative("verticalRadius");

		g.fill(new Ellipse2D.Double(
				x - horizontalRadius, y - verticalRadius,
				2 * horizontalRadius, 2 * verticalRadius
		));
	}

	/**
	 * Draws the outline of an arc with the center being the (x, y) coordinates.
	 * The arc starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * @param x The center of the arc
	 * @param y The center of the arc
	 * @param radius The radius of the arc
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void drawArc(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		drawArc(x, y, radius, radius, startRadians, sweepRadians);
	}

	/**
	 * Draws the outline of an arc with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is 2 * verticalRadius.
	 * The arc starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * @param x The center of the arc.
	 * @param y The center of the arc.
	 * @param horizontalRadius 2 * horizontalRadius is the width of the arc.
	 * @param verticalRadius 2 * verticalRadius is the height of the arc.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void drawArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createArgumentNotNegative("horizontalRadius");
		if (verticalRadius < 0) throw createArgumentNotNegative("verticalRadius");

		g.draw(new Arc2D.Double(
				x - horizontalRadius,
				y - verticalRadius,
				2 * horizontalRadius,
				2 * verticalRadius,
				transformStart(startRadians),
				transformSweep(sweepRadians),
				Arc2D.OPEN
		));
	}

	/**
	 * Draws a filled arc with the center being the (x, y) coordinates.
	 * The arc starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * @param x The center of the arc
	 * @param y The center of the arc
	 * @param radius The radius of the arc
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void fillArc(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		fillArc(x, y, radius, radius, startRadians, sweepRadians);
	}

	/**
	 * Draws a filled arc with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is the 2 * verticalRadius.
	 * The arc starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * @param x The center of the arc
	 * @param y The center of the arc
	 * @param horizontalRadius 2 * horizontalRadius is the width of the arc.
	 * @param verticalRadius 2 * verticalRadius is the height of the arc.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void fillArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createArgumentNotNegative("horizontalRadius");
		if (verticalRadius < 0) throw createArgumentNotNegative("verticalRadius");

		g.fill(new Arc2D.Double(
				x - horizontalRadius,
				y - verticalRadius,
				2 * horizontalRadius,
				2 * verticalRadius,
				transformStart(startRadians),
				transformSweep(sweepRadians),
				Arc2D.PIE
		));
	}

	/**
	 * Draws the outline of a triangle.
	 */
	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		drawPolygon(x1, y1, x2, y2, x3, y3);
	}

	/**
	 * Draws a filled triangle.
	 */
	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		fillPolygon(x1, y1, x2, y2, x3, y3);
	}

	/**
	 * Draws the outline of a polygon.
	 * This function must be called with an even number of parameters.
	 * Each parameter pair (x, y) represents a corner of the polygon.
	 * Must be called with at least two points as parameter.
	 * Each point passed to drawPolygon will be connected to the following points and
	 * the last point will be connected to the first point.
	 * <pre>{@code
	 * cd.drawPolygon(
	 *     200, 100,
	 *     100, 200,
	 *     300, 200
	 * );
	 * }</pre>
	 */
	public void drawPolygon(double... points) {
		if ((points.length & 1) == 1) throw new IllegalArgumentException("An even number of points must be passed to drawPolygon(double...)");
		if (points.length / 2 < 2) throw createMoreThanTwoPointsPolygon();

		g.draw(doubleToPath(points));
	}

	/**
	 * Draws a filled polygon.
	 * This function must be called with an even number of parameters.
	 * Each parameter pair (x, y) represents a corner of the polygon.
	 * Must be called with at least two points as parameter.
	 * Each point passed to drawPolygon will be connected to the following points and
	 * the last point will be connected to the first point.
	 * <pre>{@code
	 * cd.drawPolygon(
	 *     200, 100,
	 *     100, 200,
	 *     300, 200
	 * );
	 * }</pre>
	 */
	public void fillPolygon(double... points) {
		if ((points.length & 1) == 1) throw new IllegalArgumentException("An even number of points must be passed to drawPolygon(double...)");
		if (points.length / 2 < 2) throw createMoreThanTwoPointsPolygon();

		g.fill(doubleToPath(points));
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The width and height of the image will be used to draw the image.
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * BufferedImage img;
	 * try {
	 *     img = ImageIO.read(new File("C:\\pathToDirectory\\filename.png"));
	 * } catch (IOException e) {
	 *     throw new UncheckedIOException(e);
	 * }
	 *
	 * cd.drawImage(100, 100, img);
	 * cd.show();
	 * }</pre>
	 * @param x The position of the top left corner of the image
	 * @param y The position of the top left corner of the image
	 * @param image Any image
	 */
	public void drawImage(double x, double y, Image image) {
		if (image == null) throw createArgumentNull("image");

		g.drawImage(image, (int)x, (int)y, null);
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The width and height of the image will be used to draw the image.
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * cd.drawImage(100, 100, new File("C:\\pathToDirectory\\filename.png"));
	 * cd.show();
	 * }</pre>
	 * @param x The position of the top left corner of the image
	 * @param y The position of the top left corner of the image
	 * @param file A file that points to an image file. See {@link javax.imageio.ImageIO#read(File)}
	 */
	public void drawImage(double x, double y, File file) {
		if (file == null) throw createArgumentNull("file");

		try {
			drawImage(x, y, ImageIO.read(file));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The width and height of the image will be used to draw the image.
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * cd.drawImage(100, 100, "C:\\pathToDirectory\\filename.png");
	 * cd.show();
	 * }</pre>
	 * @param x The position of the top left corner of the image
	 * @param y The position of the top left corner of the image
	 * @param fileName A fileName that points to an image file. See {@link javax.imageio.ImageIO#read(File)}
	 */
	public void drawImage(double x, double y, String fileName) {
		if (fileName == null) throw createArgumentNull("fileName");

		drawImage(x, y, new File(fileName));
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The image will be rescaled to fit within width and height given as parameters.<br>
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * BufferedImage img;
	 * try {
	 *     img = ImageIO.read(new File("C:\\pathToDirectory\\filename.png"));
	 * } catch (IOException e) {
	 *     throw new UncheckedIOException(e);
	 * }
	 *
	 * cd.drawImage(100, 100, 200, 200, img);
	 * cd.show();
	 * }</pre>
	 * The size of the image will be 200x200 pixel.
	 * @param x the position of the top left corner of the image
	 * @param y the position of the top left corner of the image
	 */
	public void drawImage(double x, double y, double width, double height, Image image) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");
		if (image == null) throw createArgumentNull("image");

		g.drawImage(image, (int)x, (int)y, (int)width, (int)height, null);
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The image will be rescaled to fit within width and height given as parameters.<br>
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * cd.drawImage(100, 100, 200, 200, new File("C:\\pathToDirectory\\filename.png"));
	 * cd.show();
	 * }</pre>
	 * The size of the image will be 200x200 pixel.
	 * @param x the position of the top left corner of the image
	 * @param y the position of the top left corner of the image
	 */
	public void drawImage(double x, double y, double width, double height, File file) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");
		if (file == null) throw createArgumentNull("file");

		try {
			drawImage(x, y, width, height, ImageIO.read(file));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The image will be rescaled to fit within width and height given as parameters.<br>
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * cd.drawImage(100, 100, 200, 200, "C:\\pathToDirectory\\filename.png");
	 * cd.show();
	 * }</pre>
	 * The size of the image will be 200x200 pixel.
	 * @param x the position of the top left corner of the image
	 * @param y the position of the top left corner of the image
	 */
	public void drawImage(double x, double y, double width, double height, String fileName) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");
		if (fileName == null) throw createArgumentNull("fileName");

		drawImage(x, y, width, height, new File(fileName));
	}

	/**
	 * Creates a copy of the current buffer (not the displayed image) and returns
	 * it as a buffered image. A BufferedImage can be saved as a file with the following code:
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * // some drawing occurs here
	 *
	 * try {
	 *      ImageIO.write(
	 *          cd.asImage(),
	 *          "png",
	 *          new File("C:\\pathToDirectory\\filename.png")
	 *      );
	 * } catch (IOException e) {
	 *      throw new UncheckedIOException(e);
	 * }
	 * }</pre>
	 * <b>Fun Fact</b>: You can copy the currently displayed canvas to your clipboard by pressing <b>Ctrl + C</b><br>
	 */
	public BufferedImage saveCanvas() {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D g = result.createGraphics();
		g.drawImage(buffer, 0, 0, width, height, null);
		g.dispose();

		return result;
	}

	/**
	 * Colors the whole canvas in white.
	 */
	public void clear() {
		clear(Color.WHITE);
	}

	/**
	 * Colors the whole canvas in the color given as a parameter.
	 */
	public void clear(Color color) {
		if (color == null) throw createArgumentNull("color");

		Color c = getColor();
		setColor(color);
		fillRectangle(0, 0, getWidth(), getHeight());
		setColor(c);
	}

	/**
	 * Displays the drawn shapes and images on the canvas.
	 */
	public void show() {
		window.render(buffer);
	}

	/**
	 * Displays the drawn shapes and images on the canvas and then waits for the given amount of milliseconds.
	 * The copying of the buffer to the screen also takes a bit of time and that will be the minimum time it
	 * takes for {@link #show(int)} to return.<br>
	 * The amount of milliseconds {@link #show(int)} must be called to display a certain amount of frames per second:
	 * <br>
	 * 30 fps ~ 33ms<br>
	 * 60 fps ~ 16ms<br>
	 * 120 fps ~ 8ms<br>
	 * @param waitMilliseconds Minimum time it takes this function to return.
	 */
	public void show(int waitMilliseconds) {
		if (waitMilliseconds < 0) throw createArgumentNotNegative("waitMilliseconds");

		long start = System.currentTimeMillis();
		show();
		int executionTime = (int)(System.currentTimeMillis() - start);
		waitMilliseconds = Math.max(waitMilliseconds - executionTime, 0);

		try {
			Thread.sleep(waitMilliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 * Terminates the process when all CodeDraw instances are closed.
	 * To prevent termination pass false to this function. See {@link #dispose(boolean)}.
	 */
	public void dispose() {
		dispose(true);
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 * @param exit when true terminates the process when all CodeDraw instances are closed.
	 *             When false lets the process continue even though all CodeDraw instances have been closed.
	 */
	public void dispose(boolean exit) {
		g.dispose();
		window.dispose(exit);
	}

	private static Path2D.Double doubleToPath(double[] doubles) {
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

	private static IllegalArgumentException createArgumentNull(String argumentName) {
		return new IllegalArgumentException("The parameter " + argumentName + " cannot be null.");
	}

	private static IllegalArgumentException createArgumentNotNegative(String argumentName) {
		return new IllegalArgumentException("Argument " + argumentName + " cannot be negative.");
	}

	private static IllegalArgumentException createMoreThanTwoPointsPolygon() {
		return new IllegalArgumentException("There have to be at least two points to draw a polygon.");
	}
}
