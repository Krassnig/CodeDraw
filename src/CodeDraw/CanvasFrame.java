package CodeDraw;

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

		bindEvents();
	}

	private JFrame frame;
	private CanvasPanel canvas;

	public Point getFramePosition() { return frame.getLocationOnScreen(); }
	public void setFramePosition(Point location) { frame.setLocation(location); }

	public String getTitle() { return frame.getTitle(); }
	public void setTitle(String title) { frame.setTitle(title); }

	public void render(BufferedImage buffer) {
		canvas.render(buffer);
	}

	public void copyCanvasToClipboard() {
		canvas.copyImageToClipboard();
	}

	private KeyDownDictionary<CanvasFrame> keyDownDictionary;
	private void bindEvents() {
		keyDownDictionary = new KeyDownDictionary<CanvasFrame>(keyDown);

		canvas.addMouseListener(createMouseListener());
		canvas.addMouseMotionListener(createMouseMotionListener());
		canvas.addMouseWheelListener(args -> mouseWheel.invoke(args));

		frame.addKeyListener(createKeyListener());
		frame.addComponentListener(createComponentListener());
		frame.addWindowListener(createWindowListener());
	}

	private ComponentListener createComponentListener() {
		return new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				windowMove.invoke(e);
			}
		};
	}

	private MouseListener createMouseListener() {
		return new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseClick.invoke(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseDown.invoke(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mouseUp.invoke(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				mouseEnter.invoke(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				mouseLeave.invoke(e);
			}
		};
	}

	private KeyListener createKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				keyDownDictionary.keyPress(e);
				keyPress.invoke(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				keyDownDictionary.keyRelease(e);
				keyUp.invoke(e);
			}
		};
	}

	private MouseMotionListener createMouseMotionListener() {
		return new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseMove.invoke(e);
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

	private InternalEvent<CanvasFrame, MouseEvent> mouseClick = new InternalEvent<CanvasFrame, MouseEvent>(this);
	public Event<CanvasFrame, MouseEvent> mouseClick() { return mouseClick; }

	private InternalEvent<CanvasFrame, MouseEvent> mouseMove = new InternalEvent<CanvasFrame, MouseEvent>(this);
	public Event<CanvasFrame, MouseEvent> mouseMove() { return mouseMove; }

	private InternalEvent<CanvasFrame, MouseEvent> mouseDown = new InternalEvent<CanvasFrame, MouseEvent>(this);
	public Event<CanvasFrame, MouseEvent> mouseDown() { return mouseDown; }

	private InternalEvent<CanvasFrame, MouseEvent> mouseUp = new InternalEvent<CanvasFrame, MouseEvent>(this);
	public Event<CanvasFrame, MouseEvent> mouseUp() { return mouseUp; }

	private InternalEvent<CanvasFrame, MouseWheelEvent> mouseWheel = new InternalEvent<CanvasFrame, MouseWheelEvent>(this);
	public Event<CanvasFrame, MouseWheelEvent> mouseWheel() { return mouseWheel; }

	private InternalEvent<CanvasFrame, MouseEvent> mouseEnter = new InternalEvent<CanvasFrame, MouseEvent>(this);
	public Event<CanvasFrame, MouseEvent> mouseEnter() { return mouseEnter; }

	private InternalEvent<CanvasFrame, MouseEvent> mouseLeave = new InternalEvent<CanvasFrame, MouseEvent>(this);
	public Event<CanvasFrame, MouseEvent> mouseLeave() { return mouseLeave; }

	private InternalEvent<CanvasFrame, KeyEvent> keyDown = new InternalEvent<CanvasFrame, KeyEvent>(this);
	public Event<CanvasFrame, KeyEvent> keyDown() { return keyDown; }

	private InternalEvent<CanvasFrame, KeyEvent> keyUp = new InternalEvent<CanvasFrame, KeyEvent>(this);
	public Event<CanvasFrame, KeyEvent> keyUp() { return keyUp; }

	private InternalEvent<CanvasFrame, KeyEvent> keyPress = new InternalEvent<CanvasFrame, KeyEvent>(this);
	public Event<CanvasFrame, KeyEvent> keyPress() { return keyPress; }

	private InternalEvent<CanvasFrame, ComponentEvent> windowMove = new InternalEvent<CanvasFrame, ComponentEvent>(this);
	public Event<CanvasFrame, ComponentEvent> windowMove() { return windowMove; }

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
