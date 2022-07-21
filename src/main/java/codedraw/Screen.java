package codedraw;

import java.awt.*;
import java.util.Objects;

public class Screen {
	public static Screen[] getAllScreens() {
		GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		Screen[] screens = new Screen[gd.length];
		for (int i = 0; i < screens.length; i++) {
			screens[i] = new Screen(gd[i]);
		}
		return screens;
	}

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

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

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
