package codedraw;

import codedraw.events.*;
import codedraw.graphics.CodeDrawGraphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

class CanvasWindow {
	private static BufferedImage codeDrawIcon = getCodeDrawIcon();
	private static Semaphore windowCountLock = new Semaphore(1);
	private static int windowCount = 0;
	private boolean terminateOnLastClose = true;

	public CanvasWindow(EventCollection events, int canvasWidth, int canvasHeight) {
		windowCountLock.acquire();
		windowCount++;
		windowCountLock.release();

		canvas = new CanvasPanel(canvasWidth, canvasHeight);

		frame = new JFrame();
		frame.setContentPane(canvas);
		frame.pack();
		targetSize = frame.getPreferredSize().getSize();
		updateJFrameSize();
		frame.setResizable(false);
		frame.setIconImage(codeDrawIcon);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.toFront();

		windowPosition = frame.getLocationOnScreen();
		canvasPosition = canvas.getLocationOnScreen();

		bindEvents(events);
	}

	private final Dimension targetSize;
	private JFrame frame;
	private CanvasPanel canvas;
	private Point windowPosition;
	private Point canvasPosition;
	private CursorStyle cursorStyle;

	private void updateJFrameSize() {
		Dimension d = correctFrameSize(frame.getGraphicsConfiguration(), targetSize, canvas.getPreferredSize());
		frame.setMaximumSize(d);
		frame.setMinimumSize(d);
	}

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
		canvas.addMouseWheelListener(createMouseWheelListener(events));

		frame.addKeyListener(createKeyListener(events, keyDownMap));
		frame.addComponentListener(createComponentListener(events));
		frame.addWindowListener(createWindowListener(events));
	}

	private MouseWheelListener createMouseWheelListener(EventCollection events) {
		return e -> events.mouseWheel.invoke(new MouseWheelEventArgs(e));
	}

	private ComponentListener createComponentListener(EventCollection events) {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				canvasPosition = canvas.getLocationOnScreen();
				windowPosition = frame.getLocationOnScreen();
				events.windowMove.invoke(new WindowMoveEventArgs(canvasPosition, windowPosition));
			}

			@Override
			public void componentResized(ComponentEvent e) {
				updateJFrameSize();
			}
		};
	}

	private static MouseListener createMouseListener(EventCollection events) {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				events.mouseClick.invoke(new MouseClickEventArgs(e));
			}

			@Override
			public void mousePressed(MouseEvent e) {
				events.mouseDown.invoke(new MouseClickEventArgs(e));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				events.mouseUp.invoke(new MouseClickEventArgs(e));
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				events.mouseEnter.invoke(new MouseMoveEventArgs(e));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				events.mouseLeave.invoke(new MouseMoveEventArgs(e));
			}
		};
	}

	private KeyListener createKeyListener(EventCollection events, KeyDownMap keyDownMap) {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				KeyEventArgs a = new KeyEventArgs(e);
				keyDownMap.keyPress(a);
				events.keyPress.invoke(a);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				KeyEventArgs a = new KeyEventArgs(e);
				keyDownMap.keyRelease(a);
				events.keyUp.invoke(a);
			}
		};
	}

	private MouseMotionListener createMouseMotionListener(EventCollection events) {
		return new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				events.mouseMove.invoke(new MouseMoveEventArgs(e));
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				events.mouseMove.invoke(new MouseMoveEventArgs(e));
			}
		};
	}

	private WindowListener createWindowListener(EventCollection events) {
		return new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				events.windowClose.invoke(null);
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

	private static Dimension correctFrameSize(GraphicsConfiguration jFrameGraphicsConfiguration, Dimension jFrameTargetSize, Dimension canvasSize) {
		Rectangle totalBounds = getBoundsOfAllScreens();
		AffineTransform currentScreenTransformation = jFrameGraphicsConfiguration.getDefaultTransform();
		double xScale = currentScreenTransformation.getScaleX();
		double yScale = currentScreenTransformation.getScaleY();

		double xFactorCorrection = canvasSize.getWidth()  * xScale > totalBounds.width  || xScale < 1 ? xScale : 1;
		double yFactorCorrection = canvasSize.getHeight() * yScale > totalBounds.height || yScale < 1 ? yScale : 1;

		return new Dimension(
				(int)(jFrameTargetSize.getWidth() * xFactorCorrection),
				(int)(jFrameTargetSize.getHeight() * yFactorCorrection)
		);
	}

	private static Rectangle getBoundsOfAllScreens() {
		Rectangle totalArea = new Rectangle(0, 0, 0, 0);

		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			for (GraphicsConfiguration configuration : device.getConfigurations()) {
				totalArea = totalArea.union(configuration.getBounds());
			}
		}

		return totalArea;
	}
}
