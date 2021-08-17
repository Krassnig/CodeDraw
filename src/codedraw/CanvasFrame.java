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

class CanvasFrame {
	private static Semaphore lockWindowCount = new Semaphore(1);
	private static int windowCount = 0;
	private boolean exitOnLastClose = true;

	public CanvasFrame(int canvasWidth, int canvasHeight) {
		lockWindowCount.acquire();
		windowCount++;
		lockWindowCount.release();

		canvas = new CanvasPanel(canvasWidth, canvasHeight);

		frame = new JFrame();
		frame.setContentPane(canvas);
		frame.pack();
		frame.setResizable(false);
		frame.setIconImage(getIcon());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);

		framePosition = frame.getLocationOnScreen();
		canvasPosition = canvas.getLocationOnScreen();

		bindEvents();
	}

	private JFrame frame;
	private CanvasPanel canvas;
	private Point framePosition;
	private Point canvasPosition;

	public Point getFramePosition() { return framePosition; }
	public void setFramePosition(Point location) {
		canvasPosition = plus(canvasPosition, minus(framePosition, location));
		framePosition = location;

		frame.setLocation(framePosition);
	}

	public Point getCanvasPosition() { return canvasPosition; }
	public void setCanvasPosition(Point position) {
		setFramePosition(minus(position, minus(getCanvasPosition(), getFramePosition())));
	}

	private static Point minus(Point a, Point b) {
		return new Point(a.x - b.x, a.y - b.y);
	}

	private static Point plus(Point a, Point b) { return new Point(a.x + b.x, a.y + b.y); }

	public String getTitle() { return frame.getTitle(); }
	public void setTitle(String title) { frame.setTitle(title); }

	public void render(BufferedImage buffer) {
		canvas.render(buffer);
	}

	public void copyCanvasToClipboard() {
		canvas.copyImageToClipboard();
	}

	private KeyDownMap<CanvasFrame> keyDownMap;
	private void bindEvents() {
		keyDownMap = new KeyDownMap<CanvasFrame>(keyDownEvent);

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
				framePosition = frame.getLocationOnScreen();
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
				lockWindowCount.acquire();
				windowCount--;
				if (windowCount == 0 && exitOnLastClose) {
					System.exit(0);
				}
				lockWindowCount.release();
			}
		};
	}

	private Event<CanvasFrame, MouseEvent> mouseClickEvent = new Event<CanvasFrame, MouseEvent>(this);
	public Subscription onMouseClick(EventHandler<CanvasFrame, MouseEvent> handler) { return mouseClickEvent.onInvoke(handler); }

	private Event<CanvasFrame, MouseEvent> mouseMoveEvent = new Event<CanvasFrame, MouseEvent>(this);
	public Subscription onMouseMove(EventHandler<CanvasFrame, MouseEvent> handler) { return mouseMoveEvent.onInvoke(handler); }

	private Event<CanvasFrame, MouseEvent> mouseDownEvent = new Event<CanvasFrame, MouseEvent>(this);
	public Subscription onMouseDown(EventHandler<CanvasFrame, MouseEvent> handler) { return mouseDownEvent.onInvoke(handler); }

	private Event<CanvasFrame, MouseEvent> mouseUpEvent = new Event<CanvasFrame, MouseEvent>(this);
	public Subscription onMouseUp(EventHandler<CanvasFrame, MouseEvent> handler) { return mouseUpEvent.onInvoke(handler); }

	private Event<CanvasFrame, MouseWheelEvent> mouseWheelEvent = new Event<CanvasFrame, MouseWheelEvent>(this);
	public Subscription onMouseWheel(EventHandler<CanvasFrame, MouseWheelEvent> handler) { return mouseWheelEvent.onInvoke(handler); }

	private Event<CanvasFrame, MouseEvent> mouseEnterEvent = new Event<CanvasFrame, MouseEvent>(this);
	public Subscription onMouseEnter(EventHandler<CanvasFrame, MouseEvent> handler) { return mouseEnterEvent.onInvoke(handler); }

	private Event<CanvasFrame, MouseEvent> mouseLeaveEvent = new Event<CanvasFrame, MouseEvent>(this);
	public Subscription onMouseLeave(EventHandler<CanvasFrame, MouseEvent> handler) { return mouseLeaveEvent.onInvoke(handler); }

	private Event<CanvasFrame, KeyEvent> keyDownEvent = new Event<CanvasFrame, KeyEvent>(this);
	public Subscription onKeyDown(EventHandler<CanvasFrame, KeyEvent> handler) { return keyDownEvent.onInvoke(handler); }

	private Event<CanvasFrame, KeyEvent> keyUpEvent = new Event<CanvasFrame, KeyEvent>(this);
	public Subscription onKeyUp(EventHandler<CanvasFrame, KeyEvent> handler) { return keyUpEvent.onInvoke(handler); }

	private Event<CanvasFrame, KeyEvent> keyPressEvent = new Event<CanvasFrame, KeyEvent>(this);
	public Subscription onKeyPress(EventHandler<CanvasFrame, KeyEvent> handler) { return keyPressEvent.onInvoke(handler); }

	private Event<CanvasFrame, ComponentEvent> windowMoveEvent = new Event<CanvasFrame, ComponentEvent>(this);
	public Subscription onFrameMove(EventHandler<CanvasFrame, ComponentEvent> handler) { return windowMoveEvent.onInvoke(handler); }

	public void dispose(boolean exitOnLastClose) {
		lockWindowCount.acquire();
		this.exitOnLastClose = exitOnLastClose;
		lockWindowCount.release();

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
