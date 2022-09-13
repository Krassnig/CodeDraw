package codedraw.events;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*INTERNAL*/ public class EventHandler {
	private static final Semaphore windowCloseLock = new Semaphore(1);
	private static int windowCount = 0;

	public EventHandler(JFrame frame, CanvasPanel panel) {
		windowCloseLock.acquire();
		windowCount++;
		windowCloseLock.release();

		this.frame = frame;
		this.panel = panel;
		this.position = new PositionExtension(frame, panel);
		eventScanner = new EventScanner(queue = new ConcurrentQueue<>(128));

		createEvents();
		bindEvents();
	}

	private final JFrame frame;
	private final CanvasPanel panel;
	private final PositionExtension position;
	private final EventScanner eventScanner;

	private boolean terminateOnLastClose = true;
	private ConcurrentQueue<Object> queue;

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
		panel.addMouseListener(mouseListener);
		panel.addMouseMotionListener(mouseMotionListener);
		panel.addMouseWheelListener(mouseWheelListener);
		frame.addKeyListener(keyListener);
		frame.addComponentListener(componentListener);
		frame.addWindowListener(windowListener);
	}

	private void unbindEvents() {
		panel.removeMouseListener(mouseListener);
		panel.removeMouseMotionListener(mouseMotionListener);
		panel.removeMouseWheelListener(mouseWheelListener);
		frame.removeKeyListener(keyListener);
		frame.removeComponentListener(componentListener);
		//frame.removeWindowListener(windowListener);
	}

	private MouseListener createMouseListener(MouseClickMap clickMap) {
		return new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				clickMap.mousePressed(e);
				queue.push(new MouseDownEvent(e));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clickMap.mouseReleased(e);
				queue.push(new MouseUpEvent(e));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				queue.push(new MouseEnterEvent(e));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				queue.push(new MouseLeaveEvent(e));
			}
		};
	}

	private MouseMotionListener createMouseMotionListener(MouseClickMap clickMap) {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				clickMap.mouseMoved(e);
				queue.push(new MouseMoveEvent(e));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				clickMap.mouseMoved(e);
				queue.push(new MouseMoveEvent(e));
			}
		};
	}

	private MouseWheelListener createMouseWheelListener() {
		return a -> queue.push(new MouseWheelEvent(a));
	}

	private KeyListener createKeyListener() {
		return new KeyAdapter() {
			private final KeyDownMap keyDownMap = new KeyDownMap(queue, panel);

			@Override
			public void keyPressed(KeyEvent e) {
				keyDownMap.keyPress(e);
				queue.push(new KeyPressEvent(e));
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownMap.keyRelease(e);
				queue.push(new KeyUpEvent(e));
			}
		};
	}

	private ComponentListener createComponentListener() {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				position.updateWindowAndCanvasPosition();
				queue.push(new WindowMoveEvent(position.getCanvasPosition(), position.getWindowPosition()));
			}
		};
	}

	private WindowListener createWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				queue.push(new WindowCloseEvent());
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
