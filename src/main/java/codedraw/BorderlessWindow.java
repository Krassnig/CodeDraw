package codedraw;

import codedraw.drawing.Image;
import codedraw.events.EventScanner;

import java.awt.*;

/**
 * The BorderlessWindow class lets you create a borderless instance of a CodeDraw window and draw on it.
 * It works very similarly to the {@link CodeDraw} class.
 * The borderless window can be closed by pressing Alt + F4;
 */
public class BorderlessWindow extends Image implements AutoCloseable {
	/**
	 * Shows the specified image in a borderless window fitting the size of the image.
	 * @param image any image that should be displayed.
	 * @return the BorderlessWindow object that displays the image.
	 */
	public static BorderlessWindow show(Image image) {
		BorderlessWindow bw = new BorderlessWindow(image.getWidth(), image.getHeight());
		bw.drawImage(0, 0, image);
		bw.show();
		return bw;
	}

	/**
	 * Runs the {@link Animation} interface using a borderless window.
	 * This function returns when the borderless window is closed by the user.
	 * The borderless animation will appear with a size of 600 by 600 pixel
	 * running at 60 frames per second and 60 simulation per second.
	 * @param animation any class implementing the animation interface.
	 */
	public static void run(Animation animation) {
		run(animation, 600, 600, 60, 60);
	}

	/**
	 * Runs the {@link Animation} interface using a borderless window.
	 * This function returns when the borderless window is closed by the user.
	 * The borderless animation will run at 60 frames per second and 60 simulation per second.
	 * @param animation any class implementing the animation interface.
	 * @param width the width of the borderless window.
	 * @param height the height of the borderless window.
	 */
	public static void run(Animation animation, int width, int height) {
		run(animation, width, height, 60, 60);
	}

	/**
	 * Runs the {@link Animation} interface using a borderless window.
	 * This function returns when the borderless window is closed by the user.
	 * The borderless animation will run at 60 simulation per second.
	 * @param animation any class implementing the animation interface.
	 * @param width the width of the borderless window.
	 * @param height the height of the borderless window.
	 * @param framesPerSecond the rate at which the {@link Animation#draw(Image)} method should be called.
	 */
	public static void run(Animation animation, int width, int height, int framesPerSecond) {
		run(animation, width, height, framesPerSecond, framesPerSecond);
	}

	/**
	 * Runs the {@link Animation} interface using a borderless window.
	 * This function returns when the borderless window is closed by the user.
	 * @param animation any class implementing the animation interface.
	 * @param width the width of the borderless window.
	 * @param height the height of the borderless window.
	 * @param framesPerSecond the rate at which the {@link Animation#draw(Image)} method should be called.
	 * @param simulationsPerSecond the rate at which the {@link Animation#simulate()} method should be called.
	 */
	public static void run(Animation animation, int width, int height, int framesPerSecond, int simulationsPerSecond) {
		if (animation == null) throw createParameterNullException("animation");
		if (width < 1) throw createParameterMustBeGreaterThanZeroException("width");
		if (height < 1) throw createParameterMustBeGreaterThanZeroException("height");
		if (framesPerSecond < 1) throw createParameterMustBeGreaterThanZeroException("framesPerSecond");
		if (simulationsPerSecond < 1) throw createParameterMustBeGreaterThanZeroException("simulationsPerSecond");

		BorderlessWindow bw = new BorderlessWindow(width, height);
		CodeDrawGUI.run(animation, bw.gui, bw, framesPerSecond, simulationsPerSecond);
		bw.close(false);
	}


	/**
	 * Creates a borderless window of size 600x600.
	 * The borderless window can be closed by pressing Alt + F4;
	 */
	public BorderlessWindow() {
		this(600, 600);
	}

	/**
	 * Creates a borderless window of the specified size.
	 * The borderless window can be closed by pressing Alt + F4;
	 * @param width must be at least 1 pixel
	 * @param height must be at least 1 pixel
	 */
	public BorderlessWindow(int width, int height) {
		super(Image.fromDPIAwareSize(width, height));
		gui = CodeDrawGUI.createBorderlessWindow(width, height);
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
	public void setIsAlwaysOnTop(boolean isAlwaysOnTop) {
		gui.setAlwaysOnTop(isAlwaysOnTop);
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the borderless window.
	 * The window position is the same as the canvas position on a borderless window.
	 * @return The distance in pixel from the left side of the default screen to the left of the CodeDraw window.
	 */
	public int getWindowPositionX() {
		return gui.getWindowPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the borderless window.
	 * The window position is the same as the canvas position on a borderless window.
	 * @return The distance in pixel from the top side of the default screen to the top of the CodeDraw window.
	 */
	public int getWindowPositionY() {
		return gui.getWindowPosition().y;
	}

	/**
	 * Sets the distance in pixel from the top left corner of the default screen to the top left corner of the borderless window.
	 * The window position is the same as the canvas position on a borderless window.
	 * @param x The distance in pixel from the left side of the default screen to the left of the CodeDraw window.
	 */
	public void setWindowPositionX(int x) {
		gui.setWindowPosition(new Point(x, getWindowPositionY()));
	}

	/**
	 * Sets the distance in pixel from the top left corner of the default screen to the top left corner of the borderless window.
	 * The window position is the same as the canvas position on a borderless window.
	 * @param y The distance in pixel from the top side of the default screen to the top of the CodeDraw window.
	 */
	public void setWindowPositionY(int y) {
		gui.setWindowPosition(new Point(getWindowPositionX(), y));
	}

	/**
	 * The title is the description displayed in many places on your operating system.
	 * @return the text of the title.
	 */
	public String getTitle() {
		return gui.getTitle();
	}

	/**
	 * The title is the description displayed in many places on your operating system.
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
		gui.show(this);
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
		return "BorderlessWindow " + getWidth() + "x" + getHeight();
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

	private static IllegalArgumentException createParameterMustBeGreaterThanZeroException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " must be greater than zero.");
	}
}
