package codedraw;

import codedraw.drawing.Canvas;
import codedraw.events.*;
import codedraw.drawing.*;

import java.awt.*;

/**
 * CodeDraw is an easy-to-use drawing library where you use code to create pictures and animations.
 * It is made for beginners that understand little about programming
 * and makes it very simple to draw and animate various shapes and images to a canvas.
 * <br>
 * <br>
 * The source code can be found in the <a href="https://github.com/Krassnig/CodeDraw">CodeDraw repository</a>.
 * <br>
 * If you are unfamiliar with graphical output and/or want more details you can read the
 * <a href="https://github.com/Krassnig/CodeDraw/blob/master/INTRODUCTION.md">Introduction to CodeDraw</a>.
 * <br>
 * For the JavaDoc visit <a href="https://krassnig.github.io/CodeDrawJavaDoc/">CodeDrawJavaDoc</a>.
 * <br>
 * Here is an example to get you started:<br>
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
 *         // Finally, the method cd.show() must be called
 *         // to display the drawn shapes in the CodeDraw window.
 *         cd.show();
 *     }
 * }
 * }</pre>
 * <b>Fun Fact</b>: You can copy the currently displayed canvas to your clipboard by pressing <b>Ctrl + C</b>
 * @author Niklas Krassnig, Nikolaus Kasyan
 */
public class CodeDraw extends Canvas implements AutoCloseable {
	/**
	 * Creates a canvas with size 600x600 pixels. The frame surrounding the canvas will be slightly bigger.
	 * The size remains fixed after calling this constructor.
	 */
	public CodeDraw() {
		this(600, 600);
	}

	/**
	 * Creates a canvas with the specified size. The frame surrounding the canvas will be slightly bigger.
	 * Once the size is set via this constructor it remains fixed.
	 * @param canvasWidth must be at least 1 pixel
	 * @param canvasHeight must be at least 1 pixel
	 */
	public CodeDraw(int canvasWidth, int canvasHeight) {
		super(Canvas.fromDPIAwareSize(canvasWidth, canvasHeight));
		if (canvasWidth < 1) throw new IllegalArgumentException("The width of the canvas has to be a positive number.");
		if (canvasHeight < 1) throw new IllegalArgumentException("The height of the canvas has to be a positive number.");

		window = new WindowFrame(canvasWidth, canvasHeight);

		setTitle("CodeDraw");
		show();
	}

	private WindowFrame window;
	private boolean isInstantDraw;

	/**
	 * When InstantDraw is disabled CodeDraw will only draw shapes to the window once show is called.
	 * When InstantDraw is enabled CodeDraw will immediately draw all shapes to the canvas.
	 * InstantDraw is disabled per default.
	 * @return whether InstantDraw is enabled.
	 */
	public boolean isInstantDraw() {
		return isInstantDraw;
	}

	/**
	 * When InstantDraw is disabled CodeDraw will only draw shapes to the window once show is called.
	 * When InstantDraw is enabled CodeDraw will immediately draw all shapes to the canvas.
	 * InstantDraw is disabled per default.
	 * @param isInstantDraw defines whether InstantDraw is enabled.
	 */
	public void setInstantDraw(boolean isInstantDraw) {
		this.isInstantDraw = isInstantDraw;
	}

	/**
	 * @return whether the CodeDraw window is always displayed on top of other windows.
	 */
	public boolean isAlwaysOnTop() {
		return window.isAlwaysOnTop();
	}

	/**
	 * When set to true this CodeDraw window will always be displayed on top of other windows.
	 * When set to false this CodeDraw window will disappear behind other windows when CodeDraw loses focus.
	 * @param isAlwaysOnTop defines whether this CodeDraw window is displayed on top of other windows.
	 */
	public void setAlwaysOnTop(boolean isAlwaysOnTop) {
		window.setIsAlwaysOnTop(isAlwaysOnTop);
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the left side of the main screen to the left of the CodeDraw window.
	 */
	public int getWindowPositionX() {
		return window.getWindowPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the top side of the main screen to the top of the CodeDraw window.
	 */
	public int getWindowPositionY() {
		return window.getWindowPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param x The distance in pixel from the left side of the main screen to the left of the CodeDraw window.
	 */
	public void setWindowPositionX(int x) {
		window.setWindowPosition(new Point(x, getWindowPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param y The distance in pixel from the top side of the main screen to the top of the CodeDraw window.
	 */
	public void setWindowPositionY(int y) {
		window.setWindowPosition(new Point(getWindowPositionX(), y));
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the left side of the main screen to the left of the CodeDraw canvas.
	 */
	public int getCanvasPositionX() {
		return window.getCanvasPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the top side of the main screen to the top of the CodeDraw canvas.
	 */
	public int getCanvasPositionY() {
		return window.getCanvasPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @param x The distance in pixel from the left side of the main screen to the left of the CodeDraw canvas.
	 */
	public void setCanvasPositionX(int x) {
		window.setCanvasPosition(new Point(x, getCanvasPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @param y The distance in pixel from the top side of the main screen to the top of the CodeDraw canvas.
	 */
	public void setCanvasPositionY(int y) {
		window.setCanvasPosition(new Point(getCanvasPositionX(), y));
	}


	/**
	 * Defines the style of the cursor while hovering of the CodeDraw canvas.
	 * See also {@link CursorStyle}.
	 * @return the cursor style of this CodeDraw canvas.
	 */
	public CursorStyle getCursorStyle() {
		return window.getCursorStyle();
	}

	/**
	 * Defines the style of the cursor while hovering of the CodeDraw canvas.
	 * See also {@link CursorStyle}.
	 * @param cursorStyle Sets the cursor style of this CodeDraw canvas.
	 */
	public void setCursorStyle(CursorStyle cursorStyle) {
		if (cursorStyle == null) throw createParameterNullException("cursorStyle");

		window.setCursorStyle(cursorStyle);
	}

	/**
	 * The title is the text displayed in the top left corner of the CodeDraw window.
	 * @return the text of the title.
	 */
	public String getTitle() {
		return window.getTitle();
	}

	/**
	 * The title is the text displayed in the top left corner of the CodeDraw window.
	 * @param title Sets the text of the title.
	 */
	public void setTitle(String title)  {
		if (title == null) throw createParameterNullException("title");

		window.setTitle(title);
	}

	public EventScanner getEventScanner() {
		return window.getEventScanner();
	}

	@Override
	public void drawText(double x, double y, String text) {
		super.drawText(x, y, text);
		afterDrawing();
	}

	@Override
	public void drawPixel(double x, double y) {
		super.drawPixel(x, y);
		afterDrawing();
	}

	@Override
	public void drawPoint(double x, double y) {
		super.drawPoint(x, y);
		afterDrawing();
	}

	@Override
	public void drawLine(double startX, double startY, double endX, double endY) {
		super.drawLine(startX, startY, endX, endY);
		afterDrawing();
	}

	@Override
	public void drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		super.drawCurve(startX, startY, controlX, controlY, endX, endY);
		afterDrawing();
	}

	@Override
	public void drawBezier(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		super.drawBezier(startX, startY, control1X, control1Y, control2X, control2Y, endX, endY);
		afterDrawing();
	}

	@Override
	public void drawSquare(double x, double y, double sideLength) {
		super.drawSquare(x, y, sideLength);
		afterDrawing();
	}

	@Override
	public void fillSquare(double x, double y, double sideLength) {
		super.fillSquare(x, y, sideLength);
		afterDrawing();
	}

	@Override
	public void drawRectangle(double x, double y, double width, double height) {
		super.drawRectangle(x, y, width, height);
		afterDrawing();
	}

	@Override
	public void fillRectangle(double x, double y, double width, double height) {
		super.fillRectangle(x, y, width, height);
		afterDrawing();
	}

	@Override
	public void drawCircle(double x, double y, double radius) {
		super.drawCircle(x, y, radius);
		afterDrawing();
	}

	@Override
	public void fillCircle(double x, double y, double radius) {
		super.fillCircle(x, y, radius);
		afterDrawing();
	}

	@Override
	public void drawEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		super.drawEllipse(x, y, horizontalRadius, verticalRadius);
		afterDrawing();
	}

	@Override
	public void fillEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		super.fillEllipse(x, y, horizontalRadius, verticalRadius);
		afterDrawing();
	}

	@Override
	public void drawArc(double x, double y, double radius, double startRadians, double sweepRadians) {
		super.drawArc(x, y, radius, startRadians, sweepRadians);
		afterDrawing();
	}

	@Override
	public void drawArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		super.drawArc(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
		afterDrawing();
	}

	@Override
	public void drawPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		super.drawPie(x, y, radius, startRadians, sweepRadians);
		afterDrawing();
	}

	@Override
	public void drawPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		super.drawPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
		afterDrawing();
	}

	@Override
	public void fillPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		super.fillPie(x, y, radius, startRadians, sweepRadians);
		afterDrawing();
	}

	@Override
	public void fillPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		super.fillPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
		afterDrawing();
	}

	@Override
	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		super.drawTriangle(x1, y1, x2, y2, x3, y3);
		afterDrawing();
	}

	@Override
	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		super.fillTriangle(x1, y1, x2, y2, x3, y3);
		afterDrawing();
	}

	@Override
	public void drawPolygon(double... points) {
		super.drawPolygon(points);
		afterDrawing();
	}

	@Override
	public void fillPolygon(double... points) {
		super.fillPolygon(points);
		afterDrawing();
	}

	@Override
	public void drawImage(double x, double y, Canvas image) {
		super.drawImage(x, y, image);
		afterDrawing();
	}

	@Override
	public void drawImage(double x, double y, double width, double height, Canvas image) {
		super.drawImage(x, y, width, height, image);
		afterDrawing();
	}

	@Override
	public void drawImage(double x, double y, double width, double height, Canvas image, Interpolation interpolation) {
		super.drawImage(x, y, width, height, image, interpolation);
		afterDrawing();
	}

	@Override
	public void clear() {
		super.clear();
		afterDrawing();
	}

	@Override
	public void clear(Color color) {
		super.clear(color);
		afterDrawing();
	}

	/**
	 * Displays all the drawn and filled shapes that have been drawn until now.
	 * Showing the drawn elements in the CodeDraw window is slow.
	 * Calling show frequently will slow down your program.
	 */
	public void show() {
		window.render(this, 0, isInstantDraw);
	}

	/**
	 * Displays all the drawn and filled shapes that have been drawn until now
	 * and then waits for the given amount of milliseconds.
	 * Since showing the drawn elements in the CodeDraw window is slow,
	 * CodeDraw will subtract the time it takes to show from waitMilliseconds.
	 * The amount of milliseconds this method must be called with to display a certain amount of frames per second:
	 * <br>
	 * 30 fps ~ 33ms<br>
	 * 60 fps ~ 16ms<br>
	 * 120 fps ~ 8ms<br>
	 * @param waitMilliseconds Minimum time it takes this function to return.
	 */
	public void show(long waitMilliseconds) {
		if (waitMilliseconds < 0) throw createParameterMustBeGreaterOrEqualToZeroException("waitMilliseconds");

		long start = System.currentTimeMillis();
		show();
		long executionTime = System.currentTimeMillis() - start;
		long remainingMilliseconds = Math.max(waitMilliseconds - executionTime, 0);

		sleep(remainingMilliseconds);
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 */
	@Override
	public void close() {
		close(false);
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 * @param terminateProcess When true terminates the process when all CodeDraw instances are closed.
	 *                         When false lets the process continue even though all CodeDraw instances have been closed.
	 */
	public void close(boolean terminateProcess) {
		window.dispose(terminateProcess);
	}

	@Override
	public String toString() {
		return "CodeDraw " + getWidth() + "x" + getHeight();
	}

	private void afterDrawing() {
		if (isInstantDraw) show();
	}

	private static void sleep(long waitMilliseconds) {
		try {
			Thread.sleep(waitMilliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private static IllegalArgumentException createParameterNullException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
	}

	private static IllegalArgumentException createParameterMustBeGreaterOrEqualToZeroException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " must be equal or greater than zero.");
	}
}
