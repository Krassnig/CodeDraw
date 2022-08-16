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

	private final Semaphore copyToClipboardLock = new Semaphore(1);
	private final Semaphore renderLock = new Semaphore(1);
	private final Semaphore waitUntilPreviousRenderFinished = new Semaphore(1);
	private final Semaphore waitUntilRenderFinished = new Semaphore(0);


	public void render(Image image, long waitMilliseconds, boolean waitForDisplay) {
		long start = System.currentTimeMillis();

		render(image, waitForDisplay);

		long executionTime = System.currentTimeMillis() - start;
		long remainingMilliseconds = Math.max(waitMilliseconds - executionTime, 0);

		if (remainingMilliseconds > 0) {
			sleep(remainingMilliseconds);
		}
	}

	private void render(Image image, boolean waitForDisplay) {
		waitUntilPreviousRenderFinished.acquire();
		waitUntilRenderFinished.emptySemaphore();

		copyToClipboardLock.acquire();
		renderLock.acquire();

		buffer.drawImage(0, 0, image);

		renderLock.release();
		copyToClipboardLock.release();

		repaint(10);

		if (waitForDisplay) {
			waitUntilRenderFinished.acquire();
		}
	}

	public void copyCanvasToClipboard() {
		copyToClipboardLock.acquire();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new TransferableImage(buffer), null);
		copyToClipboardLock.release();
	}

	@Override
	protected void paintComponent(Graphics componentGraphics) {
		super.paintComponent(componentGraphics);;

		renderLock.acquire();
		buffer.copyTo(componentGraphics, Interpolation.BICUBIC);
		renderLock.release();

		waitUntilRenderFinished.release();
		waitUntilPreviousRenderFinished.emptySemaphore();
		waitUntilPreviousRenderFinished.release();
	}

	private static void sleep(long waitMilliseconds) {
		try {
			Thread.sleep(waitMilliseconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}