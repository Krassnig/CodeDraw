package codedraw;

import codedraw.events.*;
import codedraw.images.*;
import codedraw.textformat.*;

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
public class CodeDraw implements AutoCloseable {
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
	 * @param canvasWidth must be at least 150 pixel
	 * @param canvasHeight must be at least 1 pixel
	 */
	public CodeDraw(int canvasWidth, int canvasHeight) {
		checkEventInvocation();
		if (canvasWidth < 1) throw new IllegalArgumentException("The width of the canvas has to be a positive number.");
		if (canvasHeight < 1) throw new IllegalArgumentException("The height of the canvas has to be a positive number.");

		events = new EventCollection();
		window = new CanvasWindow(events, canvasWidth, canvasHeight);
		g = CodeDrawImage.fromDPIAwareSize(canvasWidth, canvasHeight);

		setTitle("CodeDraw");
		show();

		ctrlCSubscription = onKeyDown(args -> {
			if (args.isControlDown() && args.getKey() == Key.C) {
				window.copyCanvasToClipboard();
			}
		});
	}

	private CanvasWindow window;
	private CodeDrawImage g;
	private EventCollection events;
	private Subscription ctrlCSubscription;
	private boolean isInstantDraw = false;

	/**
	 * When InstantDraw is disabled CodeDraw will only draw shapes to the window once show is called.
	 * When InstantDraw is enabled CodeDraw will immediately draw all shapes to the canvas.
	 * ImmediateDraw is disabled per default.
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
		checkEventInvocation();
		return window.getWindowPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the top side of the main screen to the top of the CodeDraw window.
	 */
	public int getWindowPositionY() {
		checkEventInvocation();
		return window.getWindowPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param x The distance in pixel from the left side of the main screen to the left of the CodeDraw window.
	 */
	public void setWindowPositionX(int x) {
		checkEventInvocation();
		window.setWindowPosition(new Point(x, getWindowPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param y The distance in pixel from the top side of the main screen to the top of the CodeDraw window.
	 */
	public void setWindowPositionY(int y) {
		checkEventInvocation();
		window.setWindowPosition(new Point(getWindowPositionX(), y));
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the left side of the main screen to the left of the CodeDraw canvas.
	 */
	public int getCanvasPositionX() {
		checkEventInvocation();
		return window.getCanvasPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the top side of the main screen to the top of the CodeDraw canvas.
	 */
	public int getCanvasPositionY() {
		checkEventInvocation();
		return window.getCanvasPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @param x The distance in pixel from the left side of the main screen to the left of the CodeDraw canvas.
	 */
	public void setCanvasPositionX(int x) {
		checkEventInvocation();
		window.setCanvasPosition(new Point(x, getCanvasPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the screen to the top left corner of CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @param y The distance in pixel from the top side of the main screen to the top of the CodeDraw canvas.
	 */
	public void setCanvasPositionY(int y) {
		checkEventInvocation();
		window.setCanvasPosition(new Point(getCanvasPositionX(), y));
	}

	/**
	 * This value cannot be changed once set via the constructor.
	 * @return the width of the canvas.
	 */
	public int getWidth() {
		return g.getWidth();
	}

	/**
	 * This value cannot be changed once set via the constructor.
	 * @return the height of the canvas.
	 */
	public int getHeight() {
		return g.getHeight();
	}

	/**
	 * Defines the width or thickness of drawn shapes and lines.
	 * Must be greater than zero.
	 * @return the lineWidth of this CodeDraw window.
	 */
	public double getLineWidth() {
		checkEventInvocation();
		return g.getLineWidth();
	}

	/**
	 * Defines the width or thickness of drawn shapes and lines.
	 * Must be greater than zero.
	 * @param lineWidth Sets the lineWidth of this CodeDraw window.
	 */
	public void setLineWidth(double lineWidth) {
		checkEventInvocation();
		if (lineWidth <= 0) throw createParameterMustBeGreaterThanZeroException("lineWidth");

		g.setLineWidth(lineWidth);
	}

	/**
	 * Defines the styling of drawn text.
	 * See also {@link #drawText(double, double, String)} on how the styling is applied.
	 * @return the text formatting options of this CodeDraw window.
	 */
	public TextFormat getTextFormat() {
		checkEventInvocation();
		return g.getTextFormat();
	}

	/**
	 * Defines the styling of drawn text.
	 * See also {@link #drawText(double, double, String)} on how the styling is applied.
	 * @param textFormat Sets the text formatting options of this CodeDraw window.
	 */
	public void setTextFormat(TextFormat textFormat){
		checkEventInvocation();
		if (textFormat == null) throw createParameterNullException("textFormat");

		g.setTextFormat(textFormat);
	}

	/**
	 * Defines the style of the cursor while hovering of the CodeDraw canvas.
	 * See also {@link CursorStyle}.
	 * @return the cursor style of this CodeDraw canvas.
	 */
	public CursorStyle getCursorStyle() {
		checkEventInvocation();
		return window.getCursorStyle();
	}
	/**
	 * Defines the style of the cursor while hovering of the CodeDraw canvas.
	 * See also {@link CursorStyle}.
	 * @param cursorStyle Sets the cursor style of this CodeDraw canvas.
	 */
	public void setCursorStyle(CursorStyle cursorStyle) {
		checkEventInvocation();
		if (cursorStyle == null) throw createParameterNullException("cursorStyle");

		window.setCursorStyle(cursorStyle);
	}

	/**
	 * Defines whether draw text, drawn shapes and filled shapes are anti-aliased.
	 * See <a href="https://en.wikipedia.org/wiki/Spatial_anti-aliasing">Wikipedia Spatial Anti-aliasing</a>
	 * @return Whether this CodeDraw window anti aliases.
	 */
	public boolean isAntiAliased() {
		checkEventInvocation();
		return g.isAntiAliased();
	}

	/**
	 * Defines whether drawn text, drawn shapes and filled shapes are anti-aliased.
	 * See <a href="https://en.wikipedia.org/wiki/Spatial_anti-aliasing">Wikipedia Spatial Anti-aliasing</a>
	 * @param isAntiAliased Sets whether this CodeDraw window anti aliases.
	 */
	public void setAntiAliased(boolean isAntiAliased) {
		checkEventInvocation();
		g.setAntiAliased(isAntiAliased);
	}

	/**
	 * Defines how the corners of drawn shapes should look.
	 * See {@link Corner} for details.
	 * @return the corner style of this CodeDraw window.
	 */
	public Corner getCorner() {
		checkEventInvocation();
		return g.getCorner();
	}

	/**
	 * Defines how the corners of drawn shapes should look.
	 * See {@link Corner} for details.
	 * @param corner Sets the corner style of this CodeDraw window.
	 */
	public void setCorner(Corner corner) {
		checkEventInvocation();
		if (corner == null) throw createParameterNullException("corner");

		g.setCorner(corner);
	}

	/**
	 * The title is the text displayed in the top left corner of the CodeDraw window.
	 * @return the text of the title.
	 */
	public String getTitle() {
		checkEventInvocation();
		return window.getTitle();
	}

	/**
	 * The title is the text displayed in the top left corner of the CodeDraw window.
	 * @param title Sets the text of the title.
	 */
	public void setTitle(String title)  {
		checkEventInvocation();
		if (title == null) throw createParameterNullException("title");

		window.setTitle(title);
	}

	/**
	 * Defines the color that is used for drawing all shapes.
	 * @return the drawing color of this CodeDraw window.
	 */
	public Color getColor() {
		checkEventInvocation();
		return g.getColor();
	}

	/**
	 * Defines the color that is used for drawing all shapes.
	 * Use {@link Palette} for a large selection of colors.
	 * @param color Sets the drawing color of this CodeDraw window.
	 */
	public void setColor(Color color) {
		checkEventInvocation();
		if (color == null) throw createParameterNullException("color");

		g.setColor(color);
	}

	/**
	 * Triggers once when a mouse button is pressed down and quickly released again.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseClick(EventHandler<MouseClickEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseClick.onInvoke(handler);
	}

	/**
	 * Triggers continuously while the mouse is being moved.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseMove(EventHandler<MouseMoveEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseMove.onInvoke(handler);
	}

	/**
	 * Triggers exactly once when a mouse button is pressed down.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseDown(EventHandler<MouseDownEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseDown.onInvoke(handler);
	}

	/**
	 * Triggers when a mouse button is released.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseUp(EventHandler<MouseUpEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseUp.onInvoke(handler);
	}

	/**
	 * Triggers when the mouse enters the canvas.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseEnter(EventHandler<MouseEnterEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseEnter.onInvoke(handler);
	}

	/**
	 * Triggers when the mouse leaves the canvas.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseLeave(EventHandler<MouseLeaveEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseLeave.onInvoke(handler);
	}

	/**
	 * Triggers each time the mouse wheel is turned.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onMouseWheel(EventHandler<MouseWheelEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.mouseWheel.onInvoke(handler);
	}

	/**
	 * Trigger exactly once when a key is pressed down.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onKeyDown(EventHandler<KeyDownEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.keyDown.onInvoke(handler);
	}

	/**
	 * Trigger exactly once when a key is released.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onKeyUp(EventHandler<KeyUpEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.keyUp.onInvoke(handler);
	}

	/**
	 * Triggers continuously while a key is being held down.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onKeyPress(EventHandler<KeyPressEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.keyPress.onInvoke(handler);
	}

	/**
	 * Triggers every time the CodeDraw window is moved.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onWindowMove(EventHandler<WindowMoveEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.windowMove.onInvoke(handler);
	}

	/**
	 * Triggers exactly once when the user closes the window or {@link #close()} is called.
	 * If you have difficulty with this method try the {@link EventScanner}.
	 * @param handler A lambda or function reference.
	 */
	public Subscription onWindowClose(EventHandler<WindowCloseEventArgs> handler) {
		if (handler == null) throw createParameterNullException("handler");
		return events.windowClose.onInvoke(handler);
	}

	/**
	 * Draws the text at the specified (x, y) coordinate.
	 * Formatting options can be set via the TextFormat object.
	 * See {@link #getTextFormat()}, {@link #setTextFormat(TextFormat)} and the {@link TextFormat} class.
	 * If not specified otherwise in the TextFormat object the (x, y) coordinates will be in the top left corner of the text.
	 * @param x The distance in pixel from the left side of the canvas.
	 * @param y The distance in pixel from the top side of the canvas.
	 * @param text The text or string to be drawn.
	 */
	public void drawText(double x, double y, String text) {
		checkEventInvocation();
		if (text == null) throw createParameterNullException("text");

		g.drawText(x, y, text);
		afterDrawing();
	}

	/**
	 * Draws a point which is exactly 1x1 pixel in size.
	 * @param x The distance in pixel from the left side of the canvas.
	 * @param y The distance in pixel from the top side of the canvas.
	 */
	public void drawPixel(double x, double y) {
		checkEventInvocation();
		g.drawPixel(x, y);
		afterDrawing();
	}

	/**
	 * Draws a point which changes size depending on the {@link #getLineWidth()}
	 * @param x The distance in pixel from the left side of the canvas to the center of the point.
	 * @param y The distance in pixel from the top side of the canvas to the center of the point.
	 */
	public void drawPoint(double x, double y) {
		checkEventInvocation();
		g.drawPoint(x, y);
		afterDrawing();
	}

	/**
	 * Draws a straight line between the start point and end point.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param startX The distance in pixel from the left side of the canvas to the start of the line.
	 * @param startY The distance in pixel from the top side of the canvas to the start of the line.
	 * @param endX The distance in pixel from the left side of the canvas to the end of the line.
	 * @param endY The distance in pixel from the top side of the canvas to the end of the line.
	 */
	public void drawLine(double startX, double startY, double endX, double endY) {
		checkEventInvocation();
		g.drawLine(startX, startY, endX, endY);
		afterDrawing();
	}

	/**
	 * Draws a quadratic Bézier curve. See: <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The controlX/Y parameter specifies in what way the curve will be bent.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param startX The distance in pixel from the left side of the canvas to the start of the curve.
	 * @param startY The distance in pixel from the top side of the canvas to the start of the curve.
	 * @param controlX Defines the way the curve bends in the x direction.
	 * @param controlY Defines the way the curve bends in the y direction.
	 * @param endX The distance in pixel from the left side of the canvas to the end of the curve.
	 * @param endY The distance in pixel from the top side of the canvas to the end of the curve.
	 */
	public void drawCurve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		checkEventInvocation();
		g.drawCurve(startX, startY, controlX, controlY, endX, endY);
		afterDrawing();
	}

	/**
	 * Draws a cubic Bézier curve. See <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The control1X/Y and control2X/Y parameter specify in what way the curve will be bent.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param startX The distance in pixel from the left side of the canvas to the start of the curve.
	 * @param startY The distance in pixel from the top side of the canvas to the start of the curve.
	 * @param control1X Defines the way the curve bends in the x direction.
	 * @param control1Y Defines the way the curve bends in the y direction.
	 * @param control2X Defines the way the curve bends in the x direction.
	 * @param control2Y Defines the way the curve bends in the y direction.
	 * @param endX The distance in pixel from the left side of the canvas to the end of the curve.
	 * @param endY The distance in pixel from the top side of the canvas to the end of the curve.
	 */
	public void drawBezier(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		checkEventInvocation();
		g.drawBezier(startX, startY, control1X, control1Y, control2X, control2Y, endX, endY);
		afterDrawing();
	}

	/**
	 * Draws the outline of a square.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the left size of the square.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the square.
	 * @param sideLength The width and the height of the square in pixel.
	 */
	public void drawSquare(double x, double y, double sideLength) {
		checkEventInvocation();
		if (sideLength < 0) throw createParameterMustBeGreaterOrEqualToZeroException("sideLength");

		g.drawSquare(x, y, sideLength);
		afterDrawing();
	}

	/**
	 * Draws a filled square.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the left size of the square.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the square.
	 * @param sideLength The width and the height of the square in pixel.
	 */
	public void fillSquare(double x, double y, double sideLength) {
		checkEventInvocation();
		if (sideLength < 0) throw createParameterMustBeGreaterOrEqualToZeroException("sideLength");

		g.fillSquare(x, y, sideLength);
		afterDrawing();
	}

	/**
	 * Draws the outline of a rectangle.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the left size of the rectangle.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the rectangle.
	 * @param height The height of the rectangle in pixel.
	 * @param width The width of the rectangle in pixel.
	 */
	public void drawRectangle(double x, double y, double width, double height) {
		checkEventInvocation();
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");

		g.drawRectangle(x, y, width, height);
		afterDrawing();
	}

	/**
	 * Draws a filled rectangle.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the left size of the rectangle.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the rectangle.
	 * @param height The height of the rectangle in pixel.
	 * @param width The width of the rectangle in pixel.
	 */
	public void fillRectangle(double x, double y, double width, double height) {
		checkEventInvocation();
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");

		g.fillRectangle(x, y, width, height);
		afterDrawing();
	}

	/**
	 * Draws the outline of a circle.
	 * The center of the circle will be at the specified (x, y) coordinate.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * @param x The distance in pixel from the left side of the canvas to the center of the circle.
	 * @param y The distance in pixel from the top side of the canvas to the center of the circle.
	 * @param radius The radius of the circle in pixel.
	 */
	public void drawCircle(double x, double y, double radius) {
		checkEventInvocation();
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.drawCircle(x, y, radius);
		afterDrawing();
	}

	/**
	 * Draws a filled circle.
	 * The center of the circle will be at the specified (x, y) coordinate.
	 * @param x The distance in pixel from the left side of the canvas to the center of the circle.
	 * @param y The distance in pixel from the top side of the canvas to the center of the circle.
	 * @param radius The radius of the circle in pixel.
	 */
	public void fillCircle(double x, double y, double radius) {
		checkEventInvocation();
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.fillCircle(x, y, radius);
		afterDrawing();
	}

	/**
	 * Draws the outline of an ellipse.
	 * The center of the ellipse will be at the specified (x, y) coordinate.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * @param x The distance in pixel from the left side of the canvas to the center of the ellipse.
	 * @param y The distance in pixel from the top side of the canvas to the center of the ellipse.
	 * @param horizontalRadius The horizontal radius of the ellipse in pixel. The width of the ellipse is 2 * horizontalRadius.
	 * @param verticalRadius The vertical radius of the ellipse in pixel. The height of the ellipse is 2 * verticalRadius.
	 */
	public void drawEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		checkEventInvocation();
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.drawEllipse(x, y, horizontalRadius, verticalRadius);
		afterDrawing();
	}

	/**
	 * Draws a filled ellipse.
	 * The center of the ellipse will be at the specified (x, y) coordinate.
	 * @param x The distance in pixel from the left side of the canvas to the center of the ellipse.
	 * @param y The distance in pixel from the top side of the canvas to the center of the ellipse.
	 * @param horizontalRadius The horizontal radius of the ellipse in pixel. The width of the ellipse is 2 * horizontalRadius.
	 * @param verticalRadius The vertical radius of the ellipse in pixel. The height of the ellipse is 2 * verticalRadius.
	 */
	public void fillEllipse(double x, double y, double horizontalRadius, double verticalRadius) {
		checkEventInvocation();
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.fillEllipse(x, y, horizontalRadius, verticalRadius);
		afterDrawing();
	}

	/**
	 * Draws the outline of an arc with the center being the (x, y) coordinates.
	 * The arc starts at the 3 o'clock position offset by the startRadians parameter.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * The line width can be changed with {@link #setLineWidth(double)}.<br>
	 * You can use {@link Math#toRadians(double)} to specify the angle in degrees.
	 * <pre>{@code
	 * cd.drawArc(200, 200, 50, Math.toRadians(90), Math.toRadians(180));
	 * }</pre>
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the center of the arc.
	 * @param y The distance in pixel from the top side of the canvas to the center of the arc.
	 * @param radius The radius of the arc in pixel.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 3 o'clock going clockwise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void drawArc(double x, double y, double radius, double startRadians, double sweepRadians) {
		checkEventInvocation();
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.drawArc(x, y, radius, startRadians, sweepRadians);
		afterDrawing();
	}

	/**
	 * Draws the outline of an arc with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is 2 * verticalRadius.
	 * The arc starts at the 3 o'clock position offset by the startRadians parameter.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * The line width can be changed with {@link #setLineWidth(double)}.<br>
	 * You can use {@link Math#toRadians(double)} to specify the angle in degrees.
	 * <pre>{@code
	 * cd.drawArc(200, 200, 50, 50, Math.toRadians(90), Math.toRadians(180));
	 * }</pre>
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the center of the arc.
	 * @param y The distance in pixel from the top side of the canvas to the center of the arc.
	 * @param horizontalRadius The horizontal radius of the arc in pixel. The width of the arc is 2 * horizontalRadius.
	 * @param verticalRadius The vertical radius of the arc in pixel. The height of the arc is 2 * verticalRadius.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 3 o'clock going clockwise.
	 * @param sweepRadians The length of the arc in radians from the start angle in a clockwise direction.
	 */
	public void drawArc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		checkEventInvocation();
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.drawArc(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
		afterDrawing();
	}

	/**
	 * Draws the outline of a pie with the center being the (x, y) coordinates.
	 * The pie starts at the 3 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.
	 * The line width can be changed with {@link #setLineWidth(double)}.<br>
	 * You can use {@link Math#toRadians(double)} to specify the angle in degrees.
	 * <pre>{@code
	 * cd.drawPie(200, 200, 50, Math.toRadians(90), Math.toRadians(180));
	 * }</pre>
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the center of the pie.
	 * @param y The distance in pixel from the top side of the canvas to the center of the pie.
	 * @param radius The radius of the pie in pixel.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 3 o'clock going clockwise.
	 * @param sweepRadians The length of the pie in radians from the start angle in a clockwise direction.
	 */
	public void drawPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		checkEventInvocation();
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.drawPie(x, y, radius, startRadians, sweepRadians);
		afterDrawing();
	}

	/**
	 * Draws the outline of a pie with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is the 2 * verticalRadius.
	 * The pie starts at the 3 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.
	 * The line width can be changed with {@link #setLineWidth(double)}.<br>
	 * You can use {@link Math#toRadians(double)} to specify the angle in degrees.
	 * <pre>{@code
	 * cd.drawPie(200, 200, 50, 50, Math.toRadians(90), Math.toRadians(180));
	 * }</pre>
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x The distance in pixel from the left side of the canvas to the center of the pie.
	 * @param y The distance in pixel from the top side of the canvas to the center of the pie.
	 * @param horizontalRadius The horizontal radius of the pie in pixel. The width of the pie is 2 * horizontalRadius.
	 * @param verticalRadius The vertical radius of the pie in pixel. The height of the pie is 2 * verticalRadius.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 3 o'clock going clockwise.
	 * @param sweepRadians The length of the pie in radians from the start angle in a clockwise direction.
	 */
	public void drawPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		checkEventInvocation();
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.drawPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
		afterDrawing();
	}

	/**
	 * Draws a filled pie with the center being the (x, y) coordinates.
	 * The pie starts at the 3 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.<br>
	 * You can use {@link Math#toRadians(double)} to specify the angle in degrees.
	 * <pre>{@code
	 * cd.fillPie(200, 200, 50, Math.toRadians(90), Math.toRadians(180));
	 * }</pre>
	 * @param x The distance in pixel from the left side of the canvas to the center of the pie.
	 * @param y The distance in pixel from the top side of the canvas to the center of the pie.
	 * @param radius The radius of the pie in pixel.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 3 o'clock going clockwise.
	 * @param sweepRadians The length of the pie in radians from the start angle in a clockwise direction.
	 */
	public void fillPie(double x, double y, double radius, double startRadians, double sweepRadians) {
		checkEventInvocation();
		if (radius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("radius");

		g.fillPie(x, y, radius, startRadians, sweepRadians);
		afterDrawing();
	}

	/**
	 * Draws a filled pie with the center being the (x, y) coordinates.
	 * The width is 2 * horizontalRadius and the height is the 2 * verticalRadius.
	 * The pie starts at the 3 o'clock position offset by the startRadians parameter.
	 * The total length of the pie is defined by the sweepRadians parameter.<br>
	 * You can use {@link Math#toRadians(double)} to specify the angle in degrees.
	 * <pre>{@code
	 * cd.fillPie(200, 200, 50, 50, Math.toRadians(90), Math.toRadians(180));
	 * }</pre>
	 * @param x The distance in pixel from the left side of the canvas to the center of the pie.
	 * @param y The distance in pixel from the top side of the canvas to the center of the pie.
	 * @param horizontalRadius The horizontal radius of the pie in pixel. The width of the pie is 2 * horizontalRadius.
	 * @param verticalRadius The vertical radius of the pie in pixel. The height of the pie is 2 * verticalRadius.
	 * @param startRadians The starting angle in radians. A 0 radians angle would be interpreted as starting at 3 o'clock going clockwise.
	 * @param sweepRadians The length of the pie in radians from the start angle in a clockwise direction.
	 */
	public void fillPie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		checkEventInvocation();
		if (horizontalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("horizontalRadius");
		if (verticalRadius < 0) throw createParameterMustBeGreaterOrEqualToZeroException("verticalRadius");

		g.fillPie(x, y, horizontalRadius, verticalRadius, startRadians, sweepRadians);
		afterDrawing();
	}

	/**
	 * Draws the outline of a triangle.
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param x1 The distance in pixel from the left side of the canvas to the first corner of the triangle.
	 * @param y1 The distance in pixel from the top side of the canvas to the first corner of the triangle.
	 * @param x2 The distance in pixel from the left side of the canvas to the second corner of the triangle.
	 * @param y2 The distance in pixel from the top side of the canvas to the second corner of the triangle.
	 * @param x3 The distance in pixel from the left side of the canvas to the third corner of the triangle.
	 * @param y3 The distance in pixel from the top side of the canvas to the third corner of the triangle.
	 */
	public void drawTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		checkEventInvocation();
		g.drawTriangle(x1, y1, x2, y2, x3, y3);
		afterDrawing();
	}

	/**
	 * Draws a filled triangle.
	 * @param x1 The distance in pixel from the left side of the canvas to the first corner of the triangle.
	 * @param y1 The distance in pixel from the top side of the canvas to the first corner of the triangle.
	 * @param x2 The distance in pixel from the left side of the canvas to the second corner of the triangle.
	 * @param y2 The distance in pixel from the top side of the canvas to the second corner of the triangle.
	 * @param x3 The distance in pixel from the left side of the canvas to the third corner of the triangle.
	 * @param y3 The distance in pixel from the top side of the canvas to the third corner of the triangle.
	 */
	public void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		checkEventInvocation();
		g.fillTriangle(x1, y1, x2, y2, x3, y3);
		afterDrawing();
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
	 * The line width can be changed with {@link #setLineWidth(double)}.
	 * The corners can be changed with {@link #setCorner(Corner)}. For details see the {@link Corner} class.
	 * @param points An even number of doubles. Each pair represents one corner of the polygon.
	 */
	public void drawPolygon(double... points) {
		checkEventInvocation();
		if (isInvalidPolygonCount(points)) throw createPolygonCountException(points, "drawPolygon");

		g.drawPolygon(points);
		afterDrawing();
	}

	/**
	 * Draws a filled polygon.
	 * This function must be called with an even number of parameters.
	 * Each parameter pair (x, y) represents a corner of the polygon.
	 * Must be called with at least two points as parameter.
	 * Each point passed to drawPolygon will be connected to the following points and
	 * the last point will be connected to the first point.
	 * If you pass only two arguments to fillPolygon nothing will be drawn.
	 * <pre>{@code
	 * cd.fillPolygon(
	 *     200, 100,
	 *     100, 200,
	 *     300, 200
	 * );
	 * }</pre>
	 * @param points An even number of doubles. Each pair represents one corner of the polygon.
	 */
	public void fillPolygon(double... points) {
		checkEventInvocation();
		if (isInvalidPolygonCount(points)) throw createPolygonCountException(points, "fillPolygon");

		g.fillPolygon(points);
		afterDrawing();
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The width and height of the image will be used to draw the image.
	 * @param x The distance in pixel from the left side of the canvas to the left side of the image.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the image.
	 * @param image Any image.
	 */
	public void drawImage(double x, double y, CodeDrawImage image) {
		checkEventInvocation();
		if (image == null) throw createParameterNullException("image");

		g.drawImage(x, y, image);
		afterDrawing();
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The image will be rescaled to fit within the width and height given as parameters.
	 * @param x The distance in pixel from the left side of the canvas to the left side of the image.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the image.
	 * @param width The width of the image on the canvas.
	 * @param height The height of the image on the canvas.
	 * @param image Any image.
	 */
	public void drawImage(double x, double y, double width, double height, CodeDrawImage image) {
		checkEventInvocation();
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");
		if (image == null) throw createParameterNullException("image");

		g.drawImage(x, y, width, height, image);
		afterDrawing();
	}

	/**
	 * Draws an image at the specified (x, y) coordinate.
	 * The image will be rescaled to fit within the width and height given as parameters.
	 * @param x The distance in pixel from the left side of the canvas to the left side of the image.
	 * @param y The distance in pixel from the top side of the canvas to the top side of the image.
	 * @param width The width of the image on the canvas.
	 * @param height The height of the image on the canvas.
	 * @param image Any image.
	 * @param interpolation Defines the way the images is interpolated when scaled. See {@link Interpolation}.
	 */
	public void drawImage(double x, double y, double width, double height, CodeDrawImage image, Interpolation interpolation) {
		checkEventInvocation();
		if (width < 0) throw createParameterMustBeGreaterOrEqualToZeroException("width");
		if (height < 0) throw createParameterMustBeGreaterOrEqualToZeroException("height");
		if (image == null) throw createParameterNullException("image");
		if (interpolation == null) throw createParameterNullException("interpolation");

		g.drawImage(x, y, width, height, image, interpolation);
		afterDrawing();
	}

	/**
	 * Creates a copy of the current buffer (not the displayed image) and returns
	 * it as a buffered image. A BufferedImage can be saved to a file with the following code:
	 * <pre>{@code
	 * CodeDraw cd = new CodeDraw();
	 *
	 * // some drawing occurs here
	 *
	 * CodeDrawImage.saveAsPNG(cd.copyCanvas(), "/pathToDirectory/filename.png");
	 * }</pre>
	 * <b>Fun Fact</b>: You can copy the currently displayed canvas to your clipboard by pressing <b>Ctrl + C</b>.
	 * @return The current buffer as an image object.
	 */
	public CodeDrawImage copyCanvas() {
		checkEventInvocation();
		return new CodeDrawImage(g);
	}

	/**
	 * Colors the whole canvas in white.
	 */
	public void clear() {
		checkEventInvocation();
		g.clear();
		afterDrawing();
	}

	/**
	 * Colors the whole canvas in the color given as a parameter.
	 * @param color The color the canvas will be colored in.
	 */
	public void clear(Color color) {
		checkEventInvocation();
		if (color == null) throw createParameterNullException("color");

		g.clear(color);
		afterDrawing();
	}

	/**
	 * Displays all the drawn and filled shapes that have been drawn until now.
	 * Showing the drawn elements in the CodeDraw window is slow.
	 * Calling show frequently will slow down your program.
	 */
	public void show() {
		checkEventInvocation();
		window.render(g, isInstantDraw);
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
		checkEventInvocation();
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
		ctrlCSubscription.unsubscribe();
		window.dispose(terminateProcess);
	}

	private void afterDrawing() {
		if (isInstantDraw) show();
	}

	@Override
	public String toString() {
		return "CodeDraw " + getWidth() + "x" + getHeight();
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

	private static void checkEventInvocation() {
		if (Event.isCurrentThreadOnEventLoop()) {
			throw new CodeDrawEventInvocationException();
		}
	}
}
