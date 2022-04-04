package codedraw;

import codedraw.drawing.Canvas;
import codedraw.events.EventScanner;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class WindowFrame {
	public WindowFrame(int canvasWidth, int canvasHeight) {
		frame = new Frame(canvasWidth, canvasHeight);
		frame.setLayout(null);
		frame.pack();

		jFrameCorrector = new JFrameCorrector(frame, frame.getPreferredSize());

		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();
	}

	private final Frame frame;
	private final JFrameCorrector jFrameCorrector;

	public boolean isAlwaysOnTop() {
		return frame.isAlwaysOnTop();
	}

	public void setIsAlwaysOnTop(boolean isAlwaysOnTop) {
		frame.setAlwaysOnTop(isAlwaysOnTop);
	}

	public Point getWindowPosition() {
		return frame.getEventHandler().getWindowPosition();
	}

	public void setWindowPosition(Point newWindowPosition) {
		frame.getEventHandler().setWindowPosition(newWindowPosition);
	}

	public Point getCanvasPosition() {
		return frame.getEventHandler().getCanvasPosition();
	}

	public void setCanvasPosition(Point newCanvasPosition) {
		frame.getEventHandler().setCanvasPosition(newCanvasPosition);
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

	public void render(Canvas buffer, long waitMilliseconds, boolean waitForDisplay) {
		frame.getPanel().render(buffer, waitMilliseconds, waitForDisplay);
	}

	public void dispose(boolean terminateOnLastClose) {
		jFrameCorrector.stop();
		frame.dispose(terminateOnLastClose);
	}
}
