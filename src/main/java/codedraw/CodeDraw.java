package codedraw;

import codedraw.drawing.Image;
import codedraw.events.*;

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
public class CodeDraw extends Image implements AutoCloseable {
	public static CodeDraw show(Image image) {
		CodeDraw cd = new CodeDraw(image.getWidth(), image.getHeight());
		cd.drawImage(0, 0, image);
		cd.show();
		return cd;
	}

	public static void run(Animation animation) {
		run(animation, 600, 600, 60, 60);
	}

	public static void run(Animation animation, int width, int height) {
		run(animation, width, height, 60, 60);
	}

	public static void run(Animation animation, int width, int height, int framesPerSecond) {
		run(animation, width, height, framesPerSecond, framesPerSecond);
	}

	public static void run(Animation animation, int width, int height, int framesPerSecond, int simulationsPerSecond) {
		CodeDraw cd = new CodeDraw(width, height);
		CodeDrawGUI.run(animation, cd.gui, cd, framesPerSecond, simulationsPerSecond);
		cd.close();
	}

	/**
	 * Creates a canvas with size 600x600 pixels. The window surrounding the canvas will be slightly bigger.
	 * The size remains fixed after calling this constructor.
	 */
	public CodeDraw() {
		this(600, 600);
	}

	/**
	 * Creates a canvas with the specified size. The window surrounding the canvas will be slightly bigger.
	 * Once the size is set via this constructor it remains fixed.
	 * @param canvasWidth must be at least 1 pixel
	 * @param canvasHeight must be at least 1 pixel
	 */
	public CodeDraw(int canvasWidth, int canvasHeight) {
		super(Image.fromDPIAwareSize(canvasWidth, canvasHeight));
		gui = CodeDrawGUI.createWindow(canvasWidth, canvasHeight);
		show();
	}

	private CodeDrawGUI gui;

	/**
	 * Gets the EventScanner of this CodeDraw window.
	 * See the {@link EventScanner} for more details on how to use it.
	 * @return an EventScanner.
	 */
	public EventScanner getEventScanner() {
		return gui.getEventScanner();
	}

	/**
	 * When InstantDraw is disabled CodeDraw will only draw shapes to the window once show is called.
	 * When InstantDraw is enabled CodeDraw will immediately draw all shapes to the canvas.
	 * InstantDraw is disabled per default.
	 * @return whether InstantDraw is enabled.
	 */
	public boolean isInstantDraw() {
		return gui.isInstantDraw();
	}

	/**
	 * When InstantDraw is disabled CodeDraw will only draw shapes to the window once show is called.
	 * When InstantDraw is enabled CodeDraw will immediately draw all shapes to the canvas.
	 * InstantDraw is disabled per default.
	 * @param isInstantDraw defines whether InstantDraw is enabled.
	 */
	public void setInstantDraw(boolean isInstantDraw) {
		gui.setInstantDraw(isInstantDraw);
	}

	/**
	 * @return whether the CodeDraw window is always displayed on top of other windows.
	 */
	public boolean isAlwaysOnTop() {
		return gui.isAlwaysOnTop();
	}

	/**
	 * When set to true this CodeDraw window will always be displayed on top of other windows.
	 * When set to false this CodeDraw window will disappear behind other windows when CodeDraw loses focus.
	 * @param isAlwaysOnTop defines whether this CodeDraw window is displayed on top of other windows.
	 */
	public void setAlwaysOnTop(boolean isAlwaysOnTop) {
		gui.setAlwaysOnTop(isAlwaysOnTop);
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the left side of the default screen to the left of the CodeDraw canvas.
	 */
	public int getCanvasPositionX() {
		return gui.getCanvasPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @return The distance in pixel from the top side of the default screen to the top of the CodeDraw canvas.
	 */
	public int getCanvasPositionY() {
		return gui.getCanvasPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @param x The distance in pixel from the left side of the default screen to the left of the CodeDraw canvas.
	 */
	public void setCanvasPositionX(int x) {
		gui.setCanvasPosition(new Point(x, getCanvasPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw canvas.
	 * The top left corner of the canvas is the origin point for all drawn objects.
	 * Changing the canvas position also changes the window position.
	 * @param y The distance in pixel from the top side of the default screen to the top of the CodeDraw canvas.
	 */
	public void setCanvasPositionY(int y) {
		gui.setCanvasPosition(new Point(getCanvasPositionX(), y));
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the left side of the default screen to the left of the CodeDraw window.
	 */
	public int getWindowPositionX() {
		return gui.getWindowPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @return The distance in pixel from the top side of the default screen to the top of the CodeDraw window.
	 */
	public int getWindowPositionY() {
		return gui.getWindowPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param x The distance in pixel from the left side of the default screen to the left of the CodeDraw window.
	 */
	public void setWindowPositionX(int x) {
		gui.setWindowPosition(new Point(x, getWindowPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the default screen to the top left corner of the CodeDraw window.
	 * Changing the window position also changes the canvas position.
	 * @param y The distance in pixel from the top side of the default screen to the top of the CodeDraw window.
	 */
	public void setWindowPositionY(int y) {
		gui.setWindowPosition(new Point(getWindowPositionX(), y));
	}

	/**
	 * The title is the text displayed in the top left corner of the CodeDraw window.
	 * It is also the description displayed in many places on your operating system.
	 * @return the text of the title.
	 */
	public String getTitle() {
		return gui.getTitle();
	}

	/**
	 * The title is the text displayed in the top left corner of the CodeDraw window.
	 * It is also the description displayed in many places on your operating system.
	 * @param title Sets the text of the title.
	 */
	public void setTitle(String title)  {
		if (title == null) throw createParameterNullException("title");

		gui.setTitle(title);
	}

	/**
	 * Defines the style of the cursor while hovering over the CodeDraw canvas.
	 * See also {@link CursorStyle}.
	 * @return the cursor style of this CodeDraw canvas.
	 */
	public CursorStyle getCursorStyle() {
		return gui.getCursorStyle();
	}

	/**
	 * Defines the style of the cursor while hovering over the CodeDraw canvas.
	 * See also {@link CursorStyle}.
	 * @param cursorStyle Sets the cursor style of this CodeDraw canvas.
	 */
	public void setCursorStyle(CursorStyle cursorStyle) {
		if (cursorStyle == null) throw createParameterNullException("cursorStyle");

		gui.setCursorStyle(cursorStyle);
	}

	/**
	 * Displays all the drawn and filled shapes that have been drawn until now.
	 * Showing the drawn elements in the CodeDraw window is slow.
	 * Calling show frequently will slow down your program.
	 */
	public void show() {
		show(0);
	}

	/**
	 * Displays all the drawn and filled shapes that have been drawn until now
	 * and then waits for the given amount of milliseconds.
	 * CodeDraw might take longer to return from show if you specify only a small amount of milliseconds.
	 * The amount of milliseconds this method must be called with to display a certain amount of frames per second:
	 * <br>
	 * 30 fps ~ 33ms<br>
	 * 60 fps ~ 16ms<br>
	 * 120 fps ~ 8ms<br>
	 * @param waitMilliseconds Minimum time it takes this function to return.
	 */
	public void show(long waitMilliseconds) {
		if (waitMilliseconds < 0) throw createParameterZeroOrGreaterException("waitMilliseconds");

		gui.show(this, waitMilliseconds);
	}

	/**
	 * Checks whether this CodeDraw window is already closed.
	 * The window can close if the user closes the window or when the {@link #close()} method is called.
	 * @return whether this CodeDraw window is closed.
	 */
	public boolean isClosed() {
		return gui.isClosed();
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 * Any methods associated with the graphical user interface can no longer be used then.
	 */
	@Override
	public void close() {
		gui.close();
	}

	/**
	 * Closes the frame and disposes all created resources associated with this CodeDraw instance.
	 * @param terminateProcess When true terminates the process when all CodeDraw instances are closed.
	 *                         When false lets the process continue even though all CodeDraw instances have been closed.
	 */
	public void close(boolean terminateProcess) {
		gui.close(terminateProcess);
	}

	@Override
	public String toString() {
		return "CodeDraw " + getWidth() + "x" + getHeight();
	}

	@Override
	protected void afterDrawing() {
		if (gui.isInstantDraw()) show();
	}

	private static IllegalArgumentException createParameterNullException(String parameterName) {
		return new IllegalArgumentException("The parameter '" + parameterName + "' cannot be null.");
	}

	private static IllegalArgumentException createParameterZeroOrGreaterException(String parameterName) {
		return new IllegalArgumentException("The parameter '" + parameterName + "' must be equal to or greater than zero.");
	}
}
