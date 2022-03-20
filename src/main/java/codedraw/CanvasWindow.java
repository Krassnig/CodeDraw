package codedraw;

import codedraw.drawing.Canvas;
import codedraw.events.EventScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class CanvasWindow {
	private static final BufferedImage codeDrawIcon = getCodeDrawIcon();

	public CanvasWindow(int canvasWidth, int canvasHeight) {
		panel = new CanvasPanel(canvasWidth, canvasHeight);

		frame = new JFrame();
		frame.setLayout(null);
		frame.setContentPane(panel);
		frame.pack();

		jFrameCorrector = new JFrameCorrector(frame, frame.getPreferredSize());

		frame.setResizable(false);
		frame.setIconImage(codeDrawIcon);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();

		position = new GuiExtension(frame, panel);
		eventHandler = new EventHandler(frame, panel, position);
	}

	private final JFrameCorrector jFrameCorrector;
	private final JFrame frame;
	private final CanvasPanel panel;
	private final EventHandler eventHandler;
	private final GuiExtension position;
	private CursorStyle cursorStyle;

	public boolean isAlwaysOnTop() {
		return frame.isAlwaysOnTop();
	}

	public void setIsAlwaysOnTop(boolean isAlwaysOnTop) {
		frame.setAlwaysOnTop(isAlwaysOnTop);
	}

	public Point getWindowPosition() {
		return position.getWindowPosition();
	}

	public void setWindowPosition(Point newWindowPosition) {
		position.setWindowPosition(newWindowPosition);
	}

	public Point getCanvasPosition() {
		return position.getCanvasPosition();
	}
	public void setCanvasPosition(Point newCanvasPosition) {
		position.setCanvasPosition(newCanvasPosition);
	}

	public String getTitle() {
		return frame.getTitle();
	}

	public void setTitle(String title) {
		frame.setTitle(title);
	}

	public CursorStyle getCursorStyle() {
		return cursorStyle;
	}

	public void setCursorStyle(CursorStyle cursorStyle) {
		this.cursorStyle = cursorStyle;
		this.panel.setCursor(cursorStyle.getCursor());
	}

	public EventScanner getEventScanner() {
		return eventHandler.getEventScanner();
	}

	public void render(Canvas buffer, boolean waitForDisplay) {
		panel.render(buffer, waitForDisplay);
	}

	public void dispose(boolean terminateOnLastClose) {
		jFrameCorrector.stop();
		frame.dispose();
		eventHandler.dispose(terminateOnLastClose);
	}

	private static BufferedImage getCodeDrawIcon() {
		return Canvas.fromBase64String(
				"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
				"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
				"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
				"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
		).convertToBufferedImage();
	}
}
