package codedraw;

import codedraw.drawing.Image;
import codedraw.events.EventScanner;

/**
 * The Fullscreen class lets you create fullscreen application and draw on it.
 * It works very similarly to the {@link CodeDraw} class.
 * The fullscreen window can be closed by pressing Alt + F4.
 */
public class FullScreen extends Image implements AutoCloseable {
	public static void run(Animation animation) {
		run(animation, Screen.getDefaultScreen(), 60, 60);
	}

	public static void run(Animation animation, Screen screen) {
		run(animation, screen, 60, 60);
	}

	public static void run(Animation animation, Screen screen, int framesPerSecond) {
		run(animation, screen, framesPerSecond, framesPerSecond);
	}

	public static void run(Animation animation, Screen screen, int framesPerSecond, int simulationsPerSecond) {
		FullScreen fs = new FullScreen(screen);
		CodeDrawGUI.run(animation, fs.gui, fs, framesPerSecond, simulationsPerSecond);
		fs.close();
	}

	/**
	 * Creates a full screen CodeDraw window on your default screen.
	 * The fullscreen window can be closed by pressing Alt + F4.
	 */
	public FullScreen() {
		this(Screen.getDefaultScreen());
	}

	/**
	 * Creates a full screen CodeDraw window on the specified screen.
	 * You can pick any screen from {@link Screen#getAllScreens()}.
	 * The fullscreen window can be closed by pressing Alt + F4.
	 * @param screen A screen on your computer.
	 */
	public FullScreen(Screen screen) {
		super(Image.fromDPIAwareSize(screen.getWidth(), screen.getHeight()));
		if (screen == null) throw createParameterNullException("screen");
		if (!screen.canAttachGUI()) throw Screen.createWindowAlreadyAttachedException(screen);

		gui = CodeDrawGUI.createFullscreen(screen);
		show();
	}

	private CodeDrawGUI gui;

	/**
	 * Gets the screen the full screen window is currently displayed on.
	 * @return A screen.
	 */
	public Screen getScreen() {
		return gui.getScreen();
	}

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
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the fullscreen window.
	 * The fullscreen window position cannot be changed.
	 * @return The distance in pixel from the left side of the default screen to the left of the fullscreen window.
	 */
	public int getWindowPositionX() {
		return gui.getWindowPosition().x;
	}

	/**
	 * Gets the distance in pixel from the top left corner of the default screen to the top left corner of the fullscreen window.
	 * The fullscreen window position cannot be changed.
	 * @return The distance in pixel from the top side of the default screen to the top of the fullscreen window.
	 */
	public int getWindowPositionY() {
		return gui.getWindowPosition().y;
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
	public void setTitle(String title) {
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
		close(false);
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
		return "FullScreen CodeDraw " + getWidth() + "x" + getHeight();
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
