package codedraw;

import codedraw.events.*;
import codedraw.events.MouseWheelEvent;
import codedraw.images.CodeDrawImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class CanvasWindow {
	private static final BufferedImage codeDrawIcon = getCodeDrawIcon();
	private static final Semaphore windowCountLock = new Semaphore(1);
	private static int windowCount = 0;
	private boolean terminateOnLastClose = true;

	public CanvasWindow(EventCollection events, int canvasWidth, int canvasHeight) {
		windowCountLock.acquire();
		windowCount++;
		windowCountLock.release();

		canvas = new CanvasPanel(canvasWidth, canvasHeight);

		frame = new JFrame();
		frame.setLayout(null);
		frame.setContentPane(canvas);
		frame.pack();
		jFrameCorrector = new JFrameCorrector(frame, frame.getPreferredSize());
		frame.setResizable(false);
		frame.setIconImage(codeDrawIcon);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();

		updateWindowAndCanvasPosition();

		bindEvents(events);
	}

	private final JFrameCorrector jFrameCorrector;
	private final JFrame frame;
	private final CanvasPanel canvas;
	private Point windowPosition;
	private Point distanceFromWindowToCanvas;
	private CursorStyle cursorStyle;

	public boolean isAlwaysOnTop() {
		return frame.isAlwaysOnTop();
	}

	public void setIsAlwaysOnTop(boolean isAlwaysOnTop) {
		frame.setAlwaysOnTop(isAlwaysOnTop);
	}

	public Point getWindowPosition() { return windowPosition; }
	public void setWindowPosition(Point newWindowPosition) {
		windowPosition = newWindowPosition;
		frame.setLocation(windowPosition);
	}

	public Point getCanvasPosition() {
		return plus(windowPosition, distanceFromWindowToCanvas);
	}
	public void setCanvasPosition(Point newCanvasPosition) {
		setWindowPosition(minus(newCanvasPosition, distanceFromWindowToCanvas));
	}

	private void updateWindowAndCanvasPosition() {
		this.windowPosition = frame.getLocationOnScreen().getLocation();
		distanceFromWindowToCanvas = minus(canvas.getLocationOnScreen().getLocation(), windowPosition);
	}

	public String getTitle() { return frame.getTitle(); }
	public void setTitle(String title) { frame.setTitle(title); }

	public CursorStyle getCursorStyle() { return cursorStyle; }

	public void setCursorStyle(CursorStyle cursorStyle) {
		this.cursorStyle = cursorStyle;
		this.canvas.setCursor(cursorStyle.getCursor());
	}

	public void render(CodeDrawImage buffer, boolean waitForDisplay) {
		canvas.render(buffer, waitForDisplay);
	}

	public void copyCanvasToClipboard() {
		canvas.copyImageToClipboard();
	}

	private void bindEvents(EventCollection events) {
		MouseClickMap clickMap = new MouseClickMap(events.mouseClick);
		canvas.addMouseListener(createMouseListener(events, clickMap));
		canvas.addMouseMotionListener(createMouseMotionListener(events, clickMap));
		canvas.addMouseWheelListener(createMouseWheelListener(events));

		frame.addKeyListener(createKeyListener(events));
		frame.addComponentListener(createComponentListener(events));
		frame.addWindowListener(createWindowListener(events));
	}

	private MouseWheelListener createMouseWheelListener(EventCollection events) {
		return e -> events.mouseWheel.invoke(new MouseWheelEvent(e));
	}

	private ComponentListener createComponentListener(EventCollection events) {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				updateWindowAndCanvasPosition();
				events.windowMove.invoke(new WindowMoveEvent(getCanvasPosition(), getWindowPosition()));
			}
		};
	}

	private static MouseListener createMouseListener(EventCollection events, MouseClickMap clickMap) {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				clickMap.mousePressed(e);
				events.mouseDown.invoke(new MouseDownEvent(e));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clickMap.mouseReleased(e);
				events.mouseUp.invoke(new MouseUpEvent(e));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				events.mouseEnter.invoke(new MouseEnterEvent(e));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				events.mouseLeave.invoke(new MouseLeaveEvent(e));
			}
		};
	}

	private MouseMotionListener createMouseMotionListener(EventCollection events, MouseClickMap clickMap) {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				clickMap.mouseMoved(e);
				events.mouseMove.invoke(new MouseMoveEvent(e));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				clickMap.mouseMoved(e);
				events.mouseMove.invoke(new MouseMoveEvent(e));
			}
		};
	}

	private KeyListener createKeyListener(EventCollection events) {
		return new KeyAdapter() {
			private final KeyDownMap keyDownMap = new KeyDownMap(events.keyDown);

			@Override
			public void keyPressed(KeyEvent e) {
				keyDownMap.keyPress(e);
				events.keyPress.invoke(new KeyPressEvent(e));
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownMap.keyRelease(e);
				events.keyUp.invoke(new KeyUpEvent(e));
			}
		};
	}

	private WindowListener createWindowListener(EventCollection events) {
		return new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				events.windowClose.invoke(new WindowCloseEvent());
				windowCountLock.acquire();
				windowCount--;
				if (windowCount == 0 && terminateOnLastClose) {
					System.exit(0);
				}
				windowCountLock.release();
			}
		};
	}

	public void dispose(boolean terminateOnLastClose) {
		windowCountLock.acquire();
		this.terminateOnLastClose = terminateOnLastClose;
		windowCountLock.release();

		jFrameCorrector.stop();
		frame.dispose();
	}

	private static BufferedImage getCodeDrawIcon() {
		return CodeDrawImage.fromBase64String(
				"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
				"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
				"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
				"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
		).convertToBufferedImage();
	}

	private static Point minus(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	private static Point plus(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}
}
