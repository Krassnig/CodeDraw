package codedraw;

import java.awt.*;
import java.util.Objects;

/**
 * Represents the monitors attached to the computer.
 * This class is used to select which monitor a {@link FullScreen} instance of CodeDraw will be attached to.
 */
public class Screen {
	/**
	 * Creates a list of all attached screens to this computer.
	 * @return the list as an array of screens.
	 */
	public static Screen[] getAllScreens() {
		GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		Screen[] screens = new Screen[gd.length];
		for (int i = 0; i < screens.length; i++) {
			screens[i] = new Screen(gd[i]);
		}
		return screens;
	}

	/**
	 * Returns the default screen of this computer.
	 * @return the default screen.
	 */
	public static Screen getDefaultScreen() {
		return new Screen(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
	}

	private Screen(GraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
		this.name = graphicsDevice.getIDstring();
		this.width = graphicsDevice.getDisplayMode().getWidth();
		this.height = graphicsDevice.getDisplayMode().getHeight();
	}

	private GraphicsDevice graphicsDevice;
	private String name;
	private int width;
	private int height;

	/**
	 * The name of this monitor.
	 * @return the name of the monitor.
	 */
	public String getName() {
		return name;
	}

	/**
	 * The width in pixels of this monitor.
	 * @return the width of the monitor.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * The height in pixels of this monitor.
	 * @return the height of the monitor.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Tells whether this monitor is the default monitor of the computer.
	 * @return true if this is the default monitor.
	 */
	public boolean isDefaultScreen() {
		return this.equals(getDefaultScreen());
	}

	boolean canAttachGUI() {
		return graphicsDevice.getFullScreenWindow() == null;
	}

	void attachGUI(Window window) {
		graphicsDevice.setFullScreenWindow(window);
	}

	void detachGUI(Window window) {
		if (window == graphicsDevice.getFullScreenWindow()) {
			graphicsDevice.setFullScreenWindow(null);
		}
	}

	static IllegalArgumentException createWindowAlreadyAttachedException(Screen screen) {
		return new IllegalArgumentException(
			"There is already a fullscreen window attached on the '" + screen.getName() + "' screen. " +
			"Only one fullscreen window can be attached to a screen. " +
			"Close the other fullscreen window before creating another fullscreen window on this screen.");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Screen screen = (Screen) o;
		return Objects.equals(name, screen.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return name + " " + width + "x" + height;
	}
}
