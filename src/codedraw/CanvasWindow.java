package codedraw;

import codedraw.graphics.CodeDrawGraphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

class CanvasWindow {
	private static Semaphore windowCountLock = new Semaphore(1);
	private static int windowCount = 0;
	private boolean exitOnLastClose = true;

	public CanvasWindow(EventCollection events, int canvasWidth, int canvasHeight) {
		windowCountLock.acquire();
		windowCount++;
		windowCountLock.release();

		canvas = new CanvasPanel(canvasWidth, canvasHeight);

		frame = new JFrame();
		frame.setContentPane(canvas);
		frame.pack();
		frame.setResizable(false);
		frame.setIconImage(getCodeDrawIcon());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.toFront();
		frame.setVisible(true);

		windowPosition = frame.getLocationOnScreen();
		canvasPosition = canvas.getLocationOnScreen();

		bindEvents(events);
	}

	private JFrame frame;
	private CanvasPanel canvas;
	private Point windowPosition;
	private Point canvasPosition;
	private CursorStyle cursorStyle;

	public Point getWindowPosition() { return windowPosition; }
	public void setWindowPosition(Point newPosition) {
		canvasPosition = plus(canvasPosition, minus(windowPosition, newPosition));
		windowPosition = newPosition;

		frame.setLocation(windowPosition);
	}

	public Point getCanvasPosition() { return canvasPosition; }
	public void setCanvasPosition(Point newPosition) {
		setWindowPosition(minus(newPosition, minus(getCanvasPosition(), getWindowPosition())));
	}

	public String getTitle() { return frame.getTitle(); }
	public void setTitle(String title) { frame.setTitle(title); }

	public CursorStyle getCursorStyle() { return cursorStyle; }

	public void setCursorStyle(CursorStyle cursorStyle) {
		this.cursorStyle = cursorStyle;
		this.canvas.setCursor(cursorStyle.getCursor());
	}

	public void render(CodeDrawGraphics buffer) {
		canvas.render(buffer);
	}

	public void copyCanvasToClipboard() {
		canvas.copyImageToClipboard();
	}

	private void bindEvents(EventCollection events) {
		KeyDownMap keyDownMap = new KeyDownMap(events.keyDown);

		canvas.addMouseListener(createMouseListener(events));
		canvas.addMouseMotionListener(createMouseMotionListener(events));
		canvas.addMouseWheelListener(events.mouseWheel::invoke);

		frame.addKeyListener(createKeyListener(events, keyDownMap));
		frame.addComponentListener(createComponentListener(events));
		frame.addWindowListener(createWindowListener());
	}

	private ComponentListener createComponentListener(EventCollection events) {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				windowPosition = frame.getLocationOnScreen();
				canvasPosition = canvas.getLocationOnScreen();
				events.windowMove.invoke(e);
			}
		};
	}

	private static MouseListener createMouseListener(EventCollection events) {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				events.mouseClick.invoke(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				events.mouseDown.invoke(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				events.mouseUp.invoke(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				events.mouseEnter.invoke(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				events.mouseLeave.invoke(e);
			}
		};
	}

	private KeyListener createKeyListener(EventCollection events, KeyDownMap keyDownMap) {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyDownMap.keyPress(e);
				events.keyPress.invoke(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownMap.keyRelease(e);
				events.keyUp.invoke(e);
			}
		};
	}

	private MouseMotionListener createMouseMotionListener(EventCollection events) {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				events.mouseMove.invoke(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				events.mouseMove.invoke(e);
			}
		};
	}

	private WindowListener createWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				windowCountLock.acquire();
				windowCount--;
				if (windowCount == 0 && exitOnLastClose) {
					System.exit(0);
				}
				windowCountLock.release();
			}
		};
	}

	public void dispose(boolean exitOnLastClose) {
		windowCountLock.acquire();
		this.exitOnLastClose = exitOnLastClose;
		windowCountLock.release();

		frame.dispose();
	}

	private static BufferedImage getCodeDrawIcon() {
		try {
			return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(
					"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
					"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
					"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
					"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
			)));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static Point minus(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	private static Point plus(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
}
