package codedraw;

import codedraw.events.*;
import codedraw.events.MouseWheelEvent;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

class EventHandler {
	private static final Semaphore windowCloseLock = new Semaphore(1);
	private static int windowCount = 0;

	public EventHandler(Frame frame) {
		windowCloseLock.acquire();
		windowCount++;
		windowCloseLock.release();

		this.frame = frame;
		this.position = new PositionExtension(frame);
		eventScanner = new EventScanner(s -> queue = s);

		createEvents();
		bindEvents();
	}

	private final Frame frame;
	private final PositionExtension position;
	private final EventScanner eventScanner;

	private boolean terminateOnLastClose = true;
	private Consumer<Object> queue;

	private MouseListener mouseListener;
	private MouseMotionListener mouseMotionListener;
	private MouseWheelListener mouseWheelListener;
	private KeyListener keyListener;
	private ComponentListener componentListener;
	private WindowListener windowListener;

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

	public EventScanner getEventScanner() {
		return eventScanner;
	}

	public void dispose(boolean terminateOnLastClose) {
		windowCloseLock.acquire();
		this.terminateOnLastClose = terminateOnLastClose;
		windowCloseLock.release();

		unbindEvents();
	}

	private void createEvents() {
		MouseClickMap clickMap = new MouseClickMap(queue);

		mouseListener = createMouseListener(clickMap);
		mouseMotionListener = createMouseMotionListener(clickMap);
		mouseWheelListener = createMouseWheelListener();
		keyListener = createKeyListener();
		componentListener = createComponentListener();
		windowListener = createWindowListener();
	}

	private void bindEvents() {
		frame.getPanel().addMouseListener(mouseListener);
		frame.getPanel().addMouseMotionListener(mouseMotionListener);
		frame.getPanel().addMouseWheelListener(mouseWheelListener);
		frame.addKeyListener(keyListener);
		frame.addComponentListener(componentListener);
		frame.addWindowListener(windowListener);
	}

	private void unbindEvents() {
		frame.getPanel().removeMouseListener(mouseListener);
		frame.getPanel().removeMouseMotionListener(mouseMotionListener);
		frame.getPanel().removeMouseWheelListener(mouseWheelListener);
		frame.removeKeyListener(keyListener);
		frame.removeComponentListener(componentListener);
		//frame.removeWindowListener(windowListener);
	}

	private MouseListener createMouseListener(MouseClickMap clickMap) {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				clickMap.mousePressed(e);
				queue.accept(new MouseDownEvent(e));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clickMap.mouseReleased(e);
				queue.accept(new MouseUpEvent(e));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				queue.accept(new MouseEnterEvent(e));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				queue.accept(new MouseLeaveEvent(e));
			}
		};
	}

	private MouseMotionListener createMouseMotionListener(MouseClickMap clickMap) {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				clickMap.mouseMoved(e);
				queue.accept(new MouseMoveEvent(e));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				clickMap.mouseMoved(e);
				queue.accept(new MouseMoveEvent(e));
			}
		};
	}

	private MouseWheelListener createMouseWheelListener() {
		return a -> queue.accept(new MouseWheelEvent(a));
	}

	private KeyListener createKeyListener() {
		return new KeyAdapter() {
			private final KeyDownMap keyDownMap = new KeyDownMap(queue, frame.getPanel());

			@Override
			public void keyPressed(KeyEvent e) {
				keyDownMap.keyPress(e);
				queue.accept(new KeyPressEvent(e));
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownMap.keyRelease(e);
				queue.accept(new KeyUpEvent(e));
			}
		};
	}

	private ComponentListener createComponentListener() {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				position.updateWindowAndCanvasPosition();
				queue.accept(new WindowMoveEvent(position.getCanvasPosition(), position.getWindowPosition()));
			}
		};
	}

	private WindowListener createWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				queue.accept(new WindowCloseEvent());
				windowCloseLock.acquire();
				windowCount--;
				if (windowCount == 0 && terminateOnLastClose) {
					System.exit(0);
				}
				windowCloseLock.release();
			}
		};
	}
}
