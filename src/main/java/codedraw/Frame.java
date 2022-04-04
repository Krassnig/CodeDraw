package codedraw;

import codedraw.drawing.Canvas;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Frame extends JFrame {
	private static final BufferedImage codeDrawIcon = getCodeDrawIcon();

	public Frame(int canvasWidth, int canvasHeight) {
		panel = new CanvasPanel(canvasWidth, canvasHeight);

		setContentPane(panel);
		setIconImage(codeDrawIcon);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		eventHandler = new EventHandler(this);
	}

	private final CanvasPanel panel;
	private final EventHandler eventHandler;
	private CursorStyle cursorStyle;

	public CanvasPanel getPanel() {
		return panel;
	}

	public EventHandler getEventHandler() {
		return eventHandler;
	}

	public CursorStyle getCursorStyle() {
		return cursorStyle;
	}


	public void setCursorStyle(CursorStyle cursorStyle) {
		this.cursorStyle = cursorStyle;
		setCursor(cursorStyle.getCursor());
	}

	public void dispose(boolean terminateOnLastClose) {
		super.dispose();
		eventHandler.dispose(terminateOnLastClose);
	}

	@Override
	public void dispose() {
		dispose(false);
	}

	private static BufferedImage getCodeDrawIcon() {
		return Canvas.fromBase64String(
			"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
				"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
				"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
				"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
		).toBufferedImage();
	}
}
