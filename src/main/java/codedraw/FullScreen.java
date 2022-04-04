package codedraw;

import codedraw.drawing.Canvas;
import codedraw.events.EventScanner;

import javax.swing.*;
import java.awt.*;

/**
 * Press Alt + F4 to close.
 */
public class FullScreen extends Canvas implements AutoCloseable {
	public static String[] getAllScreenIds() {
		GraphicsDevice[] ge = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		String[] result = new String[ge.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = ge[i].getIDstring();
		}
		return result;
	}

	public FullScreen() {
		this(getByScreenId(getDefaultScreenId()));
	}

	public FullScreen(String screenId) {
		this(getByScreenId(screenId));
	}

	private FullScreen(GraphicsDevice graphicsDevice) {
		super(Canvas.fromDPIAwareSize(
			graphicsDevice.getDisplayMode().getWidth(),
			graphicsDevice.getDisplayMode().getHeight()
		));
		this.graphicsDevice = graphicsDevice;

		if (graphicsDevice.getFullScreenWindow() != null) throw new RuntimeException("Already attached");
		frame = new Frame(
			graphicsDevice.getDisplayMode().getWidth(),
			graphicsDevice.getDisplayMode().getHeight()
		);

		frame.setLayout(null);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.toFront();

		graphicsDevice.setFullScreenWindow(frame);

		setTitle("CodeDraw");
		show();
	}

	private final Frame frame;
	private final GraphicsDevice graphicsDevice;
	private boolean isInstantDraw = false;

	public boolean isInstantDraw() {
		return isInstantDraw;
	}

	public void setInstantDraw(boolean isInstantDraw) {
		this.isInstantDraw = isInstantDraw;
	}

	public boolean isAlwaysOnTop() {
		return frame.isAlwaysOnTop();
	}

	public void setIsAlwaysOnTop(boolean isAlwaysOnTop) {
		frame.setAlwaysOnTop(isAlwaysOnTop);
	}

	public int getWindowPositionX() {
		return frame.getEventHandler().getWindowPosition().x;
	}

	public int getWindowPositionY() {
		return frame.getEventHandler().getWindowPosition().y;
	}

	public void setWindowPositionX(int x) {
		frame.getEventHandler().setWindowPosition(new Point(x, getWindowPositionY()));
	}

	public void setWindowPositionY(int y) {
		frame.getEventHandler().setWindowPosition(new Point(getWindowPositionX(), y));
	}

	public int getCanvasPositionX() {
		return frame.getEventHandler().getCanvasPosition().x;
	}

	public int getCanvasPositionY() {
		return frame.getEventHandler().getCanvasPosition().y;
	}

	public void setCanvasPositionX(int x) {
		frame.getEventHandler().setCanvasPosition(new Point(x, getCanvasPositionY()));
	}

	public void setCanvasPositionY(int y) {
		frame.getEventHandler().setCanvasPosition(new Point(getCanvasPositionX(), y));
	}


	public String getTitle() {
		return frame.getTitle();
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public CursorStyle getCursorStyle() {
		return frame.getCursorStyle();
	}

	public void setCursorStyle(CursorStyle cursorStyle) {
		frame.setCursorStyle(cursorStyle);
	}

	public EventScanner getEventScanner() {
		return frame.getEventHandler().getEventScanner();
	}

	public void show() {
		show(0);
	}

	public void show(long waitMilliseconds) {
		frame.getPanel().render(this, waitMilliseconds, isInstantDraw);
	}

	@Override
	public void close() {
		close(false);
	}

	public void close(boolean terminateProcess) {
		frame.dispose(terminateProcess);
	}

	@Override
	public String toString() {
		return "FullScreen " + getWidth() + "x" + getHeight();
	}

	private static GraphicsDevice getByScreenId(String screenId) {
		GraphicsDevice[] ge = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

		for (int i = 0; i < ge.length; i++) {
			if (ge[i].getIDstring().equals(screenId)) {
				return ge[i];
			}
		}

		throw new RuntimeException("Unknown screen");
	}

	private static String getDefaultScreenId() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getIDstring();
	}
}
