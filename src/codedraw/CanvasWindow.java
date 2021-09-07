package codedraw;

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

	public CanvasWindow(int canvasWidth, int canvasHeight) {
		windowCountLock.acquire();
		windowCount++;
		windowCountLock.release();

		canvas = new CanvasPanel(canvasWidth, canvasHeight);

		frame = new JFrame();
		frame.setContentPane(canvas);
		frame.pack();
		frame.setResizable(false);
		frame.setIconImage(getIcon());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);

		windowPosition = frame.getLocationOnScreen();
		canvasPosition = canvas.getLocationOnScreen();

		bindEvents();
	}

	private JFrame frame;
	private CanvasPanel canvas;
	private Point windowPosition;
	private Point canvasPosition;

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

	private static Point minus(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	private static Point plus(Point a, Point b) {
		return new Point(a.x + b.x, a.y + b.y);
	}

	public String getTitle() { return frame.getTitle(); }
	public void setTitle(String title) { frame.setTitle(title); }

	public void render(BufferedImage buffer) {
		canvas.render(buffer);
	}

	public void copyCanvasToClipboard() {
		canvas.copyImageToClipboard();
	}

	private KeyDownMap<CanvasWindow> keyDownMap;
	private void bindEvents() {
		keyDownMap = new KeyDownMap<CanvasWindow>(keyDownEvent);

		canvas.addMouseListener(createMouseListener());
		canvas.addMouseMotionListener(createMouseMotionListener());
		canvas.addMouseWheelListener(args -> mouseWheelEvent.invoke(args));

		frame.addKeyListener(createKeyListener());
		frame.addComponentListener(createComponentListener());
		frame.addWindowListener(createWindowListener());
	}

	private ComponentListener createComponentListener() {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				windowPosition = frame.getLocationOnScreen();
				canvasPosition = canvas.getLocationOnScreen();
				windowMoveEvent.invoke(e);
			}
		};
	}

	private MouseListener createMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseClickEvent.invoke(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseDownEvent.invoke(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseUpEvent.invoke(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseEnterEvent.invoke(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseLeaveEvent.invoke(e);
			}
		};
	}

	private KeyListener createKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyDownMap.keyPress(e);
				keyPressEvent.invoke(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownMap.keyRelease(e);
				keyUpEvent.invoke(e);
			}
		};
	}

	private MouseMotionListener createMouseMotionListener() {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseMoveEvent.invoke(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseMoveEvent.invoke(e);
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

	private Event<CanvasWindow, MouseEvent> mouseClickEvent = new Event<CanvasWindow, MouseEvent>(this);
	public Subscription onMouseClick(EventHandler<CanvasWindow, MouseEvent> handler) { return mouseClickEvent.onInvoke(handler); }

	private Event<CanvasWindow, MouseEvent> mouseMoveEvent = new Event<CanvasWindow, MouseEvent>(this);
	public Subscription onMouseMove(EventHandler<CanvasWindow, MouseEvent> handler) { return mouseMoveEvent.onInvoke(handler); }

	private Event<CanvasWindow, MouseEvent> mouseDownEvent = new Event<CanvasWindow, MouseEvent>(this);
	public Subscription onMouseDown(EventHandler<CanvasWindow, MouseEvent> handler) { return mouseDownEvent.onInvoke(handler); }

	private Event<CanvasWindow, MouseEvent> mouseUpEvent = new Event<CanvasWindow, MouseEvent>(this);
	public Subscription onMouseUp(EventHandler<CanvasWindow, MouseEvent> handler) { return mouseUpEvent.onInvoke(handler); }

	private Event<CanvasWindow, MouseWheelEvent> mouseWheelEvent = new Event<CanvasWindow, MouseWheelEvent>(this);
	public Subscription onMouseWheel(EventHandler<CanvasWindow, MouseWheelEvent> handler) { return mouseWheelEvent.onInvoke(handler); }

	private Event<CanvasWindow, MouseEvent> mouseEnterEvent = new Event<CanvasWindow, MouseEvent>(this);
	public Subscription onMouseEnter(EventHandler<CanvasWindow, MouseEvent> handler) { return mouseEnterEvent.onInvoke(handler); }

	private Event<CanvasWindow, MouseEvent> mouseLeaveEvent = new Event<CanvasWindow, MouseEvent>(this);
	public Subscription onMouseLeave(EventHandler<CanvasWindow, MouseEvent> handler) { return mouseLeaveEvent.onInvoke(handler); }

	private Event<CanvasWindow, KeyEvent> keyDownEvent = new Event<CanvasWindow, KeyEvent>(this);
	public Subscription onKeyDown(EventHandler<CanvasWindow, KeyEvent> handler) { return keyDownEvent.onInvoke(handler); }

	private Event<CanvasWindow, KeyEvent> keyUpEvent = new Event<CanvasWindow, KeyEvent>(this);
	public Subscription onKeyUp(EventHandler<CanvasWindow, KeyEvent> handler) { return keyUpEvent.onInvoke(handler); }

	private Event<CanvasWindow, KeyEvent> keyPressEvent = new Event<CanvasWindow, KeyEvent>(this);
	public Subscription onKeyPress(EventHandler<CanvasWindow, KeyEvent> handler) { return keyPressEvent.onInvoke(handler); }

	private Event<CanvasWindow, ComponentEvent> windowMoveEvent = new Event<CanvasWindow, ComponentEvent>(this);
	public Subscription onWindowMove(EventHandler<CanvasWindow, ComponentEvent> handler) { return windowMoveEvent.onInvoke(handler); }

	public void dispose(boolean exitOnLastClose) {
		windowCountLock.acquire();
		this.exitOnLastClose = exitOnLastClose;
		windowCountLock.release();

		frame.dispose();
	}

	private static BufferedImage getIcon() {
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
}
