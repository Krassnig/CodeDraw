package codedraw;

import codedraw.textformat.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
 * It is made for beginners that understand little about programming
 * and makes it very simple to draw and animate various shapes and images to a canvas.
 * <br>
 * <br>
 * You can view the CodeDraw repository <a href="https://github.com/Krassnig/CodeDraw">here</a>.
 * <br>
 * To get a brief overview and a bunch of examples visit the
 * <a href="https://github.com/Krassnig/CodeDraw/blob/master/README.md">README of the CodeDraw repository</a>.
 * <br>
 * Read the <a href="https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md">introduction to CodeDraw</a>
 * to learn how to use this library.
 * <br>
 * An example to get you started:<br>
 * <pre>{@code
 * import codedraw.*;
 *
 * public class MyProgram {
 *     public static void main(String[] args) {
 *         // Creates a new CodeDraw window with the size of 600x600 pixel
 *         CodeDraw cd = new CodeDraw();
 *
 *         // Sets the drawing color to red
 *         cd.setColor(Palette.RED);
 *         // Draws the outline of a rectangle
 *         cd.drawRectangle(100, 100, 200, 100);
 *         // Draws a filled Square
 *         cd.fillSquare(180, 150, 80);
 *
 *         // Changes the color to light blue
 *         cd.setColor(Palette.LIGHT_BLUE);
 *         cd.fillCircle(300, 200, 50);
 *
 *         // Finally, the method show must be called
 *         // to display the drawn shapes in the CodeDraw window.
 *         cd.show();
 *     }
 * }
 * }</pre>
 * <b>Fun Fact</b>: You can copy the currently displayed canvas to your clipboard by pressing <b>Ctrl + C</b>
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

		events = new EventCollection(this);
		window = new CanvasWindow(events, canvasWidth, canvasHeight);
		g = new CodeDrawGraphics(canvasWidth, canvasHeight);

		setTitle("CodeDraw");
		show();

		onKeyDown((sender, args) -> {
			if (args.isControlDown() && args.getKeyCode() == KeyEvent.VK_C) {
				sender.window.copyCanvasToClipboard();
			}
		});
	}

	private CanvasWindow window;
	private CodeDrawGraphics g;
	private EventCollection events;
	private TextFormat textFormat = new TextFormat();

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 */
	public int getWindowPositionX() { return window.getWindowPosition().x; }
	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 */
	public int getWindowPositionY() { return window.getWindowPosition().y; }

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param x can be negative
	 */
	public void setWindowPositionX(int x) { window.setWindowPosition(new Point(x, getWindowPositionY())); }
	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param y can be negative
	 */
	public void setWindowPositionY(int y) { window.setWindowPosition(new Point(getWindowPositionX(), y)); }

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 */
	public int getCanvasPositionX() { return window.getCanvasPosition().x; }
	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 */
	public int getCanvasPositionY() { return window.getCanvasPosition().y; }

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 */
	public void setCanvasPositionX(int x) { window.setCanvasPosition(new Point(x, getCanvasPositionY())); }
	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 */
	public void setCanvasPositionY(int y) { window.setCanvasPosition(new Point(getCanvasPositionX(), y)); }

	/**
	 * @return width of the canvas
	 */
	public int getWidth() { return g.getWidth(); }

	/**
	 * @return height of the canvas
	 */
	public int getHeight() { return g.getHeight(); }

	/**
	 * Defines the width or thickness of drawn shapes and lines.
	 */
	public double getLineWidth() { return g.getLineWidth(); }
	/**
	 * Defines the width or thickness of drawn shapes and lines.
	 */
	public void setLineWidth(double lineWidth) {
		if (lineWidth <= 0) throw new IllegalArgumentException("Argument lineWidth cannot be smaller or equal to 0");

		g.setLineWidth(lineWidth);
	}

	/**
	 * Defines the styling of drawn text.
	 * See also {@link #drawText(double, double, String)}
	 */
	public TextFormat getTextFormat() { return textFormat; }
	/**
	 * Defines the styling of drawn text.
	 * See also {@link #drawText(double, double, String)}
	 */
	public void setTextFormat(TextFormat textFormat){
		if(textFormat == null) throw createArgumentNull("textFormat");

		this.textFormat = textFormat;
	}

	/**
	 * Defines the cursor style for canvas when hovering over it.
	 * See also {@link CursorStyle}
	 */
	public CursorStyle getCursorStyle() {
		return this.window.getCursorStyle();
	}

	/**
	 * Defines the cursor style for canvas when hovering over it.
	 * See also {@link CursorStyle}
	 */
	public void setCursorStyle(CursorStyle cursorStyle) {
		window.setCursorStyle(cursorStyle);
	}

	/**
	 * Defines whether draw text, drawn shapes and filled shapes are anti-aliased.
	 * See <a href="https://en.wikipedia.org/wiki/Spatial_anti-aliasing">Wikipedia Spatial Anti-aliasing</a>
	 */
	public boolean isAntiAliased() { return g.isAntiAliased(); }
	/**
	 * Defines whether drawn text, drawn shapes and filled shapes are anti-aliased.
	 * See <a href="https://en.wikipedia.org/wiki/Spatial_anti-aliasing">Wikipedia Spatial Anti-aliasing</a>
	 */
	public void isAntiAliased(boolean isAntiAliased) {
		g.isAntiAliased(isAntiAliased);
		g.isTextAntiAliased(isAntiAliased);
	}

	public Corner getCorner() { return g.getCorner(); }
	public void setCorner(Corner corner) {
		if (corner == null) throw createArgumentNull("corner");

		g.setCorner(corner);
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

	/**
	 * Triggers once when a mouse button is pressed down and quickly released again.
	 */
	public Subscription onMouseClick(EventHandler<CodeDraw, MouseEvent> handler) { return events.mouseClick.onInvoke(handler); }

	/**
	 * Triggers continuously while the mouse is being moved.
	 */
	public Subscription onMouseMove(EventHandler<CodeDraw, MouseEvent> handler) { return events.mouseMove.onInvoke(handler); }

	/**
	 * Triggers exactly once when a mouse button is pressed down.
	 */
	public Subscription onMouseDown(EventHandler<CodeDraw, MouseEvent> handler) { return events.mouseDown.onInvoke(handler); }

	/**
	 * Triggers when a mouse button is released.
	 */
	public Subscription onMouseUp(EventHandler<CodeDraw, MouseEvent> handler) { return events.mouseUp.onInvoke(handler); }

	/**
	 * Triggers when the mouse enters the canvas.
	 */
	public Subscription onMouseEnter(EventHandler<CodeDraw, MouseEvent> handler) { return events.mouseEnter.onInvoke(handler); }

	/**
	 * Triggers when the mouse leaves the canvas.
	 */
	public Subscription onMouseLeave(EventHandler<CodeDraw, MouseEvent> handler) { return events.mouseLeave.onInvoke(handler); }

	/**
	 * Triggers each time the mouse wheel is turned
	 */
	public Subscription onMouseWheel(EventHandler<CodeDraw, MouseWheelEvent> handler) { return events.mouseWheel.onInvoke(handler); }

	/**
	 * Trigger exactly once when a key is pressed down.
	 */
	public Subscription onKeyDown(EventHandler<CodeDraw, KeyEvent> handler) { return events.keyDown.onInvoke(handler); }

	/**
	 * Trigger when a key is released.
	 */
	public Subscription onKeyUp(EventHandler<CodeDraw, KeyEvent> handler) { return events.keyUp.onInvoke(handler); }

	/**
	 * onKeyPress will continuously trigger while a key is being held down.
	 */
	public Subscription onKeyPress(EventHandler<CodeDraw, KeyEvent> handler) { return events.keyPress.onInvoke(handler); }

	/**
	 * Triggers every time the CodeDraw window is moved.
	 */
	public Subscription onWindowMove(EventHandler<CodeDraw, ComponentEvent> handler) { return events.windowMove.onInvoke(handler); }

	/**
	 * Draws the text at the specified (x, y) coordinate.
	 * Formatting options can be set via the TextFormat object.
	 * See {@link #getTextFormat()}, {@link #setTextFormat(TextFormat)} and the {@link codedraw.textformat.TextFormat} class.
	 * If not specified otherwise in the TextFormat object the (x, y) coordinates will be in the top left corner of the text.
	 */
	public void drawText(double x, double y, String text) {
		if (text == null) throw createArgumentNull("text");

		g.drawText(x, y, text, textFormat);
	}

	/**
	 * Draws a point which is exactly 1x1 pixel in size.
	 */
	public void drawPixel(double x, double y) {
		g.drawPixel(x, y);
	}

	/**
	 * Draws a point which changes size depending on the {@link #getLineWidth()}
	 */
	public void drawPoint(double x, double y) {
		g.drawPoint(x, y);
	}

	/**
	 * Draws a straight line between the start point and end point.
	 */
	public void drawLine(double startX, double startY, double endX, double endY) {
		g.drawLine(startX, startY, endX, endY);
	}

	/**
	 * Draws a quadratic bezier curve. See: <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The controlX/Y parameter specifies in what way the curve will be bent.
	 */
	public void drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		g.drawCurve(startX, startY, controlX, controlY, endX, endY);
	}

	/**
	 * Draws a cubic bezier curve. See <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The control1X/Y and control2X/Y parameter specify in what way the curve will be bent.
	 */
	public void drawBezier(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		g.drawBezier(startX, startY, control1X, control1Y, control2X, control2Y, endX, endY);
	}

	/**
	 * Draws the outline of a square.
	 * @param x The top left corner of the square
	 * @param y The top left corner of the square
	 */
	public void drawSquare(double x, double y, double sideLength) {
		if (sideLength < 0) throw createArgumentNotNegative("size");

		g.drawSquare(x, y, sideLength);
	}

	/**
	 * Draws a filled square.
	 * @param x The top left corner of the square
	 * @param y The top left corner of the square
	 */
	public void fillSquare(double x, double y, double sideLength) {
		if (sideLength < 0) throw createArgumentNotNegative("size");

		g.fillSquare(x, y, sideLength);
	}

	/**
	 * Draws the outline of a rectangle.
	 * @param x The top left corner of the rectangle
	 * @param y The top left corner of the rectangle
	 */
	public void drawRectangle(double x, double y, double width, double height) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");

		g.drawRectangle(x, y, width, height);
	}

	/**
	 * Draws a filled rectangle.
	 * @param x The top left corner of the rectangle
	 * @param y The top left corner of the rectangle
	 */
	public void fillRectangle(double x, double y, double width, double height) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");

		g.fillRectangle(x, y, width, height);
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

		g.drawCircle(x, y, radius);
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

		g.fillCircle(x, y, radius);
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

		g.drawEllipse(x, y, horizontalRadius, verticalRadius);
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

		g.fillEllipse(x, y, horizontalRadius, verticalRadius);
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

		g.drawArc(x, y, radius, startRadians, sweepRadians);
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

		g.drawArc(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
	}

	/**
	 * Draws the outline of a pie with the center being the (x, y) coordinates.
	 * The pie starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.
	 * @param x The center of the pie
	 * @param y The center of the pie
	 * @param radius The radius of the pie
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the pie in radians from the start angle in a clockwise direction.
	 */
	public void drawPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		g.drawPie(x, y, radius, startRadians, sweepRadians);
	}

	/**
	 * Draws the outline of a pie with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is the 2 * verticalRadius.
	 * The pie starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.
	 * @param x The center of the pie
	 * @param y The center of the pie
	 * @param horizontalRadius 2 * horizontalRadius is the width of the pie.
	 * @param verticalRadius 2 * verticalRadius is the height of the pie.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void drawPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createArgumentNotNegative("horizontalRadius");
		if (verticalRadius < 0) throw createArgumentNotNegative("verticalRadius");

		g.drawPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
	}

	/**
	 * Draws a filled pie with the center being the (x, y) coordinates.
	 * The pie starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.
	 * @param x The center of the pie
	 * @param y The center of the pie
	 * @param radius The radius of the pie
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the pie in radians from the start angle in a clockwise direction.
	 */
	public void fillPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		g.fillPie(x, y, radius, startRadians, sweepRadians);
	}

	/**
	 * Draws a filled pie with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is the 2 * verticalRadius.
	 * The pie starts at the 12 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.
	 * @param x The center of the pie
	 * @param y The center of the pie
	 * @param horizontalRadius 2 * horizontalRadius is the width of the pie.
	 * @param verticalRadius 2 * verticalRadius is the height of the pie.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void fillPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		if (horizontalRadius < 0) throw createArgumentNotNegative("horizontalRadius");
		if (verticalRadius < 0) throw createArgumentNotNegative("verticalRadius");

		g.fillPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
	}

	/**
	 * Draws the outline of a triangle.
	 */
	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		g.drawTriangle(x1, y1, x2, y2, x3, y3);
	}

	/**
	 * Draws a filled triangle.
	 */
	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		g.fillTriangle(x1, y1, x2, y2, x3, y3);
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

		g.drawPolygon(points);
	}

	/**
	 * Draws a filled polygon.
	 * This function must be called with an even number of parameters.
	 * Each parameter pair (x, y) represents a corner of the polygon.
	 * Must be called with at least two points as parameter.
	 * Each point passed to drawPolygon will be connected to the following points and
	 * the last point will be connected to the first point.
	 * <pre>{@code
	 * cd.fillPolygon(
	 *     200, 100,
	 *     100, 200,
	 *     300, 200
	 * );
	 * }</pre>
	 */
	public void fillPolygon(double... points) {
		if ((points.length & 1) == 1) throw new IllegalArgumentException("An even number of points must be passed to drawPolygon(double...)");
		if (points.length / 2 < 2) throw createMoreThanTwoPointsPolygon();

		g.fillPolygon(points);
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

		g.drawImage(x, y, image);
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
	 * The size of the example image will be 200x200 pixel.
	 * @param x the position of the top left corner of the image
	 * @param y the position of the top left corner of the image
	 */
	public void drawImage(double x, double y, double width, double height, Image image) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");
		if (image == null) throw createArgumentNull("image");

		g.drawImage(x, y, width, height, image);
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
	 * The size of the example image will be 200x200 pixel.
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
	 * The size of the example image will be 200x200 pixel.
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
	 *          cd.saveCanvas(),
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
		return g.copyAsImage();
	}

	/**
	 * Colors the whole canvas in white.
	 */
	public void clear() {
		g.clear();
	}

	/**
	 * Colors the whole canvas in the color given as a parameter.
	 */
	public void clear(Color color) {
		if (color == null) throw createArgumentNull("color");

		g.clear(color);
	}

	/**
	 * Displays the drawn shapes and images on the canvas.
	 *
	 * Calling show while executing an event is generally not recommended because rendering is slow and
	 * this will slow down you program. Instead, you should call show from the main thread.
	 */
	public void show() {
		window.render(g);
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
	 *
	 * Calling show or sleeping while executing an event will block all other events from executing.
	 * It is generally not recommended. Instead, you should call show from the main thread.
	 * @param waitMilliseconds Minimum time it takes this function to return.
	 */
	public void show(int waitMilliseconds) {
		if (waitMilliseconds < 0) throw createArgumentNotNegative("waitMilliseconds");

		long start = System.currentTimeMillis();
		show();
		int executionTime = (int)(System.currentTimeMillis() - start);
		waitMilliseconds = Math.max(waitMilliseconds - executionTime, 0);

		sleep(waitMilliseconds);
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
	 * @param exit When true terminates the process when all CodeDraw instances are closed.
	 *             When false lets the process continue even though all CodeDraw instances have been closed.
	 */
	public void dispose(boolean exit) {
		g.dispose();
		window.dispose(exit);
	}

	private static void sleep(int waitMilliseconds) {
		try {
			Thread.sleep(waitMilliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
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
