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
	private final Semaphore waitForRender = new Semaphore(1);

	public void render(Image image, boolean waitForDisplay) {
		waitForRender.acquire();
		waitForRender.emptySemaphore();

		copyToClipboardLock.acquire();
		renderLock.acquire();

		buffer.drawImage(0, 0, image);

		renderLock.release();
		copyToClipboardLock.release();

		repaint(10);

		if (waitForDisplay) {
			waitForRender.acquire();
			waitForRender.release();
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

		waitForRender.release();
	}
}