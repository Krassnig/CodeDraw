package codedraw;

import codedraw.images.CodeDrawImage;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;

class CanvasPanel extends JPanel {
	public CanvasPanel(int width, int height) {
		displayBuffer = CodeDrawImage.fromDPIAwareSize(width, height);

		setLayout(null);
		setPreferredSize(new Dimension(width, height));
	}

	private CodeDrawImage displayBuffer;

	private Semaphore clipboardCopyLock = new Semaphore(1);
	private Semaphore renderCopyLock = new Semaphore(1);

	public void render(CodeDrawImage codeDrawBuffer) {
		clipboardCopyLock.acquire();
		renderCopyLock.acquire();

		displayBuffer.drawImage(codeDrawBuffer);

		renderCopyLock.release();
		clipboardCopyLock.release();

		repaint();
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
	}
}