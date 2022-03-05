package codedraw;

import codedraw.images.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;

class CanvasPanel extends JPanel {
	public CanvasPanel(int width, int height) {
		displayBuffer = CodeDrawImage.fromDPIAwareSize(width, height);

		setLayout(null);
		setPreferredSize(new Dimension(Math.max(width, 150), height));
	}

	private final CodeDrawImage displayBuffer;

	private final Semaphore clipboardCopyLock = new Semaphore(1);
	private final Semaphore renderCopyLock = new Semaphore(1);
	private final Semaphore waitRender = new Semaphore(0);

	public void render(CodeDrawImage codeDrawBuffer, boolean waitForDisplay) {
		clipboardCopyLock.acquire();
		renderCopyLock.acquire();

		displayBuffer.drawImage(0, 0, codeDrawBuffer);

		renderCopyLock.release();
		clipboardCopyLock.release();

		waitRender.acquireAll();
		repaint();
		if (waitForDisplay) waitRender.acquire();
	}

	public void copyImageToClipboard() {
		clipboardCopyLock.acquire();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new TransferableImage(displayBuffer.convertToBufferedImage()), null);
		clipboardCopyLock.release();
	}

	@Override
	protected void paintComponent(Graphics componentGraphics) {
		super.paintComponent(componentGraphics);

		renderCopyLock.acquire();
		displayBuffer.copyTo(componentGraphics, Interpolation.BICUBIC);
		renderCopyLock.release();

		waitRender.release();
	}
}