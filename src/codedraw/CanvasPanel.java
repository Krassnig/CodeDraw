package codedraw;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;

class CanvasPanel extends JPanel {
	public CanvasPanel(int width, int height) {
		this.width = width;
		this.height = height;
		this.displayBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		setPreferredSize(new Dimension(width, height));
		setIgnoreRepaint(true);
	}

	private int width;
	private int height;
	private BufferedImage displayBuffer;

	private Semaphore lockClipboardCopy = new Semaphore(1);
	private Semaphore lockRenderCopy = new Semaphore(1);

	public void render(BufferedImage buffer) {
		lockClipboardCopy.acquire();
		lockRenderCopy.acquire();

		Graphics2D g = displayBuffer.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1));
		g.drawImage(buffer, 0, 0, width, height, null);
		g.dispose();

		lockRenderCopy.release();
		lockClipboardCopy.release();

		repaint();
	}

	public void copyImageToClipboard() {
		lockClipboardCopy.acquire();
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(new TransferableImage(displayBuffer), null);
		lockClipboardCopy.release();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		lockRenderCopy.acquire();
		g.drawImage(displayBuffer, 0, 0, width, height, Color.WHITE, this);
		lockRenderCopy.release();
	}
}