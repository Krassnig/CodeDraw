
package codedraw;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;

class CanvasPanel extends JPanel implements AutoCloseable {
	public CanvasPanel(int width, int height) {
		buffer = Image.fromDPIAwareSize(width, height);

		setLayout(null);
		setPreferredSize(new Dimension(Math.max(width, 150), height));
	}

	private final Image buffer;

	private final Semaphore copyToClipboardLock = new Semaphore(1);
	private final Semaphore renderLock = new Semaphore(1);
	private final CloseableSemaphore waitForDisplay = new CloseableSemaphore(1);

	public void show(Image image) {
		waitForDisplay.acquire();
		waitForDisplay.emptySemaphore();

		copyToClipboardLock.acquire();
		renderLock.acquire();

		buffer.clear();
		buffer.drawImage(0, 0, image);

		renderLock.release();
		copyToClipboardLock.release();

		repaint(10);
	}

	public void waitForDisplay() {
		waitForDisplay.acquire();
		waitForDisplay.release();
	}

	public void copyCanvasToClipboard() {
		copyToClipboardLock.acquire();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new TransferableImage(buffer), null);
		copyToClipboardLock.release();
	}

	@Override
	public void close() {
		waitForDisplay.close();
	}

	@Override
	protected void paintComponent(Graphics componentGraphics) {
		super.paintComponent(componentGraphics);

		renderLock.acquire();
		buffer.copyTo(componentGraphics, Interpolation.BICUBIC);
		renderLock.release();

		waitForDisplay.release();
	}
}