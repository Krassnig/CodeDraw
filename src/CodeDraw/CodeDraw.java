package CodeDraw;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

/**
 * CodeDraw is an easy-to-use drawing library.<br>
 * <br>
 * How to use:<br>
 * <pre>{@code
 * CodeDraw cd = new CodeDraw(); // creates a canvas of size 600x600 pixel
 *
 * // All following drawn objects will be red,
 * // until the color is set to a different color.
 * cd.setColor(Color.RED);
 *
 * // draws a red circle at the center of the canvas with a radius of 50 pixel.
 * // The circle is not yet displayed!
 * cd.drawCircle(300, 300, 50);
 *
 * // Must be called to display everything that has been drawn until now!
 * cd.show();
 * }</pre>
 * There are a few key ideas described by certain keywords used in this library:<br>
 * <br>
 * <b>canvas</b> - Is the rectangle on the screen that is used for drawing. It's origin<br>
 * point (0, 0) is at the top left. Everything is drawn front the top left to the bottom right.<br>
 * Once the size is set via the constructor the size of the canvas remains fixed.<br>
 * <br>
 * <b>frame</b> - Is the frame surrounding the canvas. It is larger than the size given<br>
 * to the constructor of CodeDraw. It contains the closing and minimize button.<br>
 * <br>
 * <b>event</b> - An event is something that occurs based on user input like the user<br>
 * pressing a button or moving the mouse. You can subscribe to an Event<br>
 * by passing a method reference or lambda to CodeDraw. All events are<br>
 * marked by starting with the 'on' keyword. Like: onMouseMove or onKeyPress.<br>
 * <br>
 * <b>Fun Fact</b>: You can copy the currently displayed canvas to your clipboard by pressing <b>Ctrl + C</b><br>
 * <br>
 * @author Niklas Krassnig
 */
public class CodeDraw {
	/**
	 * Creates a canvas with size 600x600 pixel
	 */
	public CodeDraw() {
		this(600, 600);
	}

	/**
	 * Creates a canvas with the specified size. The frame surrounding the canvas will be slightly bigger.
	 * @param canvasWidth must be at least 150 pixel
	 * @param canvasHeight must be at least 1 pixel
	 */
	public CodeDraw(int canvasWidth, int canvasHeight) {
		if (canvasWidth < 150) throw new IllegalArgumentException("The width of the canvas has to be at least 150px.");
		if (canvasHeight < 1) throw new IllegalArgumentException("The height of the canvas has to be positive.");

		this.width = canvasWidth;
		this.height = canvasHeight;

		frame = new CanvasFrame(canvasWidth, canvasHeight);
		frame.setTitle("CodeDraw");
		buffer = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
		g = buffer.createGraphics();

		bindEvents();
		setFont(new Font("Arial", Font.PLAIN, 16));
		setColor(Color.BLACK);
		setLineSize(1);
		clear();
		show();

		onKeyDown((sender, args) -> {
			if (args.isControlDown() && args.getKeyCode() == KeyEvent.VK_C) {
				sender.frame.copyCanvasToClipboard();
			}
		});
	}

	private int width;
	private int height;
	private CanvasFrame frame;
	private BufferedImage buffer;
	private Graphics2D g;
	private int lineSize = 1;

	/**
	 * @return width of the canvas
	 */
	public int getWidth() { return width; }

	/**
	 * @return height of the canvas
	 */
	public int getHeight() { return height; }

	public int getFramePositionX() { return frame.getFramePositionY(); }
	public int getFramePositionY() { return frame.getFramePositionX(); }

	public void setFramePositionX(int x) { frame.setFramePositionX(x); }
	public void setFramePositionY(int y) { frame.setFramePositionY(y); }

	public String getTitle() { return frame.getTitle(); }
	public void setTitle(String title)  {
		if (title == null) throw createArgumentNull("title");

		frame.setTitle(title);
	}

	public Font getFont() { return g.getFont(); }
	public void setFont(Font font) {
		if (font == null) throw createArgumentNull("font");

		g.setFont(font);
	}

	public Color getColor() { return g.getColor(); }
	public void setColor(Color color) {
		if (color == null) throw createArgumentNull("color");

		g.setColor(color);
	}

	public int getLineSize() { return lineSize; }
	public void setLineSize(int lineSize) {
		if (lineSize < 1) throw new IllegalArgumentException("Argument lineSize cannot be smaller than 1");

		this.lineSize = lineSize;
		updateBrushes();
	}

	private void updateBrushes() {
		g.setStroke(new BasicStroke((float) lineSize));
	}

	private void bindEvents() {
		frame.onMouseClick((s, a) -> mouseClickEvent.invoke(a));
		frame.onMouseMove ((s, a) -> mouseMoveEvent .invoke(a));
		frame.onMouseDown ((s, a) -> mouseDownEvent .invoke(a));
		frame.onMouseUp   ((s, a) -> mouseUpEvent   .invoke(a));
		frame.onMouseWheel((s, a) -> mouseWheelEvent.invoke(a));
		frame.onMouseEnter((s, a) -> mouseEnterEvent.invoke(a));
		frame.onMouseLeave((s, a) -> mouseLeaveEvent.invoke(a));
		frame.onKeyDown   ((s, a) -> keyDownEvent   .invoke(a));
		frame.onKeyUp     ((s, a) -> keyUpEvent     .invoke(a));
		frame.onKeyPress  ((s, a) -> keyPressEvent  .invoke(a));
		frame.onFrameMove ((s, a) -> frameMoveEvent .invoke(a));
	}

	private Event<CodeDraw, MouseEvent> mouseClickEvent = new Event<CodeDraw, MouseEvent>(this);
	public Subscription onMouseClick(EventHandler<CodeDraw, MouseEvent> handler) { return mouseClickEvent.onInvoke(handler); }

	private Event<CodeDraw, MouseEvent> mouseMoveEvent = new Event<CodeDraw, MouseEvent>(this);
	public Subscription onMouseMove(EventHandler<CodeDraw, MouseEvent> handler) { return mouseMoveEvent.onInvoke(handler); }

	private Event<CodeDraw, MouseEvent> mouseDownEvent = new Event<CodeDraw, MouseEvent>(this);
	public Subscription onMouseDown(EventHandler<CodeDraw, MouseEvent> handler) { return mouseDownEvent.onInvoke(handler); }

	private Event<CodeDraw, MouseEvent> mouseUpEvent = new Event<CodeDraw, MouseEvent>(this);
	public Subscription onMouseUp(EventHandler<CodeDraw, MouseEvent> handler) { return mouseUpEvent.onInvoke(handler); }

	private Event<CodeDraw, MouseEvent> mouseEnterEvent = new Event<CodeDraw, MouseEvent>(this);
	public Subscription onMouseEnter(EventHandler<CodeDraw, MouseEvent> handler) { return mouseEnterEvent.onInvoke(handler); }

	private Event<CodeDraw, MouseEvent> mouseLeaveEvent = new Event<CodeDraw, MouseEvent>(this);
	public Subscription onMouseLeave(EventHandler<CodeDraw, MouseEvent> handler) { return mouseLeaveEvent.onInvoke(handler); }

	private Event<CodeDraw, MouseWheelEvent> mouseWheelEvent = new Event<CodeDraw, MouseWheelEvent>(this);
	public Subscription onMouseWheel(EventHandler<CodeDraw, MouseWheelEvent> handler) { return mouseWheelEvent.onInvoke(handler); }

	private Event<CodeDraw, KeyEvent> keyDownEvent = new Event<CodeDraw, KeyEvent>(this);
	public Subscription onKeyDown(EventHandler<CodeDraw, KeyEvent> handler) { return keyDownEvent.onInvoke(handler); }

	private Event<CodeDraw, KeyEvent> keyUpEvent = new Event<CodeDraw, KeyEvent>(this);
	public Subscription onKeyUp(EventHandler<CodeDraw, KeyEvent> handler) { return keyUpEvent.onInvoke(handler); }

	private Event<CodeDraw, KeyEvent> keyPressEvent = new Event<CodeDraw, KeyEvent>(this);
	public Subscription onKeyPress(EventHandler<CodeDraw, KeyEvent> handler) { return keyPressEvent.onInvoke(handler); }

	private Event<CodeDraw, ComponentEvent> frameMoveEvent = new Event<CodeDraw, ComponentEvent>(this);
	public Subscription onFrameMove(EventHandler<CodeDraw, ComponentEvent> handler) { return frameMoveEvent.onInvoke(handler); }

	/**
	 * Draws text to the right and below the xy-coordinate. The text will be left aligned.
	 */
	public void drawText(double x, double y, String text) {
		if (text == null) throw createArgumentNull("text");

		TextLayout tl = new TextLayout(text, getFont(), g.getFontRenderContext());
		FontMetrics fm = g.getFontMetrics(getFont());
		g.fill(tl.getOutline(new AffineTransform(1, 0, 0, 1, x, y + fm.getAscent())));
	}

	public void drawPoint(double x, double y) {
		fillSquare(x, y, 1);
	}

	public void drawLine(double startX, double startY, double endX, double endY) {
		g.draw(new Line2D.Double(
				startX, startY,
				endX, endY
		));
	}

	public void drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		g.draw(new QuadCurve2D.Double(
				startX, startY,
				controlX, controlY,
				endX, endY
		));
	}

	/**
	 * Draws a cubic bezier curve. See: @see <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
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
	 * @param x The top left corner of the square
	 * @param y The top left corner of the square
	 */
	public void drawSquare(double x, double y, double sideLength) {
		if (sideLength < 0) throw createArgumentNotNegative("size");

		drawRectangle(x, y, sideLength, sideLength);
	}

	/**
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
	 * @param x The center of the circle
	 * @param y The center of the circle
	 */
	public void drawCircle(double x, double y, double radius) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		drawEllipse(x, y, radius, radius);
	}

	/**
	 * @param x The center of the circle
	 * @param y The center of the circle
	 */
	public void fillCircle(double x, double y, double radius) {
		if (radius < 0) throw createArgumentNotNegative("radius");

		fillEllipse(x, y, radius, radius);
	}

	/**
	 * @param x The center of the ellipse
	 * @param y The center of the ellipse
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
	 * @param x The center of the ellipse
	 * @param y The center of the ellipse
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
	 * @param x The center of the arc
	 * @param y The center of the arc
	 * @param startRadians The starting angle. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
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
	 * @param x the center of the filled arc
	 * @param y the center of the filled arc
	 * @param startRadians the starting angle. A 0 radians angle would be interpreted as starting at 12 o'clock going clock-wise.
	 * @param sweepRadians the length of the filled arc in radians from the start angle in a clockwise direction.
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

	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		drawPolygon(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3));
	}

	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		fillPolygon(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3));
	}

	public void drawPolygon(Point... points) {
		if (points.length < 2) throw new IllegalArgumentException("There have to be at least two points to draw a polygon.");

		g.draw(pointsToPath(points));
	}

	public void fillPolygon(Point... points) {
		if (points.length < 2) throw new IllegalArgumentException("There have to be at least two points to draw a polygon.");

		g.fill(pointsToPath(points));
	}

	/**
	 * The width and height of the image will be used to draw the image.<br>
	 * Example:
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
	 */
	public void drawImage(int x, int y, Image image) {
		if (image == null) throw createArgumentNull("image");

		g.drawImage(image, x, y, null);
	}

	/**
	 * The image will be rescaled to fit within the rectangle given by x, y, width and height.<br>
	 * Example:
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
	public void drawImage(int x, int y, int width, int height, Image image) {
		if (width < 0) throw createArgumentNotNegative("width");
		if (height < 0) throw createArgumentNotNegative("height");
		if (image == null) throw createArgumentNull("image");

		g.drawImage(image, x, y, width, height, null);
	}

	/**
	 * Creates a copy of the current buffer (not the displayed image) and returns
	 * it as a buffered image. A BufferedImage can be saved as a file with this code:
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
	public BufferedImage asImage() {
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
	 * Displays the drawn graphics on the canvas.
	 */
	public void show() {
		frame.render(buffer);
	}

	/**
	 * Displays the drawn graphics on the canvas and then waits for the given amount of milliseconds.
	 * The copying of the buffer to the screen also takes a bit of time so the wait time might be
	 * larger than the given amount of milliseconds.<br>
	 * How many milliseconds the program must pause in order to display a certain amount of frames per second:
	 * <br>
	 * 30 fps = 33ms<br>
	 * 60 fps = 16ms<br>
	 * 120 fps = 8ms<br>
	 * @param waitMilliseconds Time it takes this function to return.
	 */
	public void show(int waitMilliseconds) {
		if (waitMilliseconds < 0) throw createArgumentNotNegative("waitMilliseconds");

		show();

		try {
			Thread.sleep(waitMilliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 */
	public void dispose() {
		dispose(true);
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 * @param exit when true terminates the process when all CodeDraw instances are closed.
	 */
	public void dispose(boolean exit) {
		g.dispose();
		frame.dispose(exit);
	}

	private static Path2D.Double pointsToPath(Point[] points) {
		Path2D.Double result = new Path2D.Double();

		result.moveTo(points[0].getX(), points[1].getY());
		for (int i = 1; i < points.length; i++) {
			result.lineTo(points[i].getX(), points[i].getY());
		}
		result.lineTo(points[0].getX(), points[1].getY());

		return result;
	}

	private static double transformStart(double startRadians) {
		return 90 - Math.toDegrees(startRadians);
	}

	private static double transformSweep(double sweepRadians) {
		return - Math.toDegrees(sweepRadians);
	}

	private static NullPointerException createArgumentNull(String argumentName) {
		return new NullPointerException("The parameter " + argumentName + " cannot be null.");
	}

	private static IllegalArgumentException createArgumentNotNegative(String argumentName) {
		return new IllegalArgumentException("Argument " + argumentName + " cannot be negative.");
	}
}
