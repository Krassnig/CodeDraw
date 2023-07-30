package codedraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class EventHandler implements AutoCloseable {
	public EventHandler(JFrame frame, CanvasPanel panel, Runnable closeCodeDrawGUI) {
		this.frame = frame;
		this.panel = panel;
		this.position = new PositionExtension(frame, panel);
		eventScanner = new EventScanner();

		this.closeCodeDrawGUI = closeCodeDrawGUI;

		createEvents();
		bindEvents();
	}

	private final JFrame frame;
	private final CanvasPanel panel;
	private final PositionExtension position;
	private final EventScanner eventScanner;

	private final Runnable closeCodeDrawGUI;

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

	private void createEvents() {
		MouseClickMap clickMap = new MouseClickMap(eventScanner);

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
				eventScanner.push(new MouseDownEvent(e));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				clickMap.mouseReleased(e);
				eventScanner.push(new MouseUpEvent(e));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				eventScanner.push(new MouseEnterEvent(e));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				eventScanner.push(new MouseLeaveEvent(e));
			}
		};
	}

	private MouseMotionListener createMouseMotionListener(MouseClickMap clickMap) {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				clickMap.mouseMoved(e);
				eventScanner.push(new MouseMoveEvent(e));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				clickMap.mouseMoved(e);
				eventScanner.push(new MouseMoveEvent(e));
			}
		};
	}

	private MouseWheelListener createMouseWheelListener() {
		return a -> eventScanner.push(new MouseWheelEvent(a));
	}

	private KeyListener createKeyListener() {
		return new KeyAdapter() {
			private final KeyDownMap keyDownMap = new KeyDownMap(eventScanner, panel);

			@Override
			public void keyPressed(KeyEvent e) {
				keyDownMap.keyPress(e);
				eventScanner.push(new KeyPressEvent(e));
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownMap.keyRelease(e);
				eventScanner.push(new KeyUpEvent(e));
			}
		};
	}

	private ComponentListener createComponentListener() {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				position.updateWindowAndCanvasPosition();
				eventScanner.push(new WindowMoveEvent(position.getCanvasPosition(), position.getWindowPosition()));
			}
		};
	}

	private WindowListener createWindowListener() {
		return new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				eventScanner.push(new WindowCloseEvent());
				closeCodeDrawGUI.run();
			}
		};
	}

	@Override
	public void close() {
		unbindEvents();
	}
}
