package codedraw;

import codedraw.drawing.*;
import codedraw.drawing.Image;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;

class CanvasPanel extends JPanel {
	public CanvasPanel(int width, int height) {
		buffer = Image.fromDPIAwareSize(width, height);

		setLayout(null);
		setPreferredSize(new Dimension(Math.max(width, 150), height));
	}

	private final Image buffer;

	private final Semaphore clipboardCopyLock = new Semaphore(1);
	private final Semaphore renderCopyLock = new Semaphore(1);
	private final Semaphore waitRender = new Semaphore(0);

	public void render(Image image, long waitMilliseconds, boolean waitForDisplay) {
		long start = System.currentTimeMillis();

		render(image, waitForDisplay);

		long executionTime = System.currentTimeMillis() - start;
		long remainingMilliseconds = Math.max(waitMilliseconds - executionTime, 0);

		if (remainingMilliseconds != 0) {
			sleep(remainingMilliseconds);
		}
	}

	private void render(Image image, boolean waitForDisplay) {
		clipboardCopyLock.acquire();
		renderCopyLock.acquire();

		buffer.drawImage(0, 0, image);

		renderCopyLock.release();
		clipboardCopyLock.release();

		waitRender.acquireAll();

		repaint();

		if (waitForDisplay) waitRender.acquire();
	}

	public void copyCanvasToClipboard() {
		clipboardCopyLock.acquire();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new TransferableImage(buffer), null);
		clipboardCopyLock.release();
	}

	@Override
	protected void paintComponent(Graphics componentGraphics) {
		super.paintComponent(componentGraphics);

		renderCopyLock.acquire();
		buffer.copyTo(componentGraphics, Interpolation.BICUBIC);
		renderCopyLock.release();

		waitRender.release();
	}

	private static void sleep(long waitMilliseconds) {
		try {
			Thread.sleep(waitMilliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}