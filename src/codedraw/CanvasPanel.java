package codedraw;

import codedraw.graphics.CodeDrawGraphics;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;

class CanvasPanel extends JPanel {
	public CanvasPanel(int width, int height) {
		displayBuffer = CodeDrawGraphics.createDPIAwareCodeDrawGraphics(width, height);

		setLayout(null);
		setPreferredSize(new Dimension(width, height));
	}

	private CodeDrawGraphics displayBuffer;

	private Semaphore clipboardCopyLock = new Semaphore(1);
	private Semaphore renderCopyLock = new Semaphore(1);

	public void render(CodeDrawGraphics codeDrawBuffer) {
		clipboardCopyLock.acquire();
		renderCopyLock.acquire();

		codeDrawBuffer.copyTo(displayBuffer);

		renderCopyLock.release();
		clipboardCopyLock.release();

		repaint();
	}

	public void copyImageToClipboard() {
		clipboardCopyLock.acquire();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new TransferableImage(displayBuffer.copyAsImage()), null);
		clipboardCopyLock.release();
	}

	@Override
	protected void paintComponent(Graphics componentGraphics) {
		super.paintComponent(componentGraphics);

		renderCopyLock.acquire();
		displayBuffer.copyTo(componentGraphics);
		renderCopyLock.release();
	}
}