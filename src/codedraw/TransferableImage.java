package codedraw;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;

class TransferableImage implements Transferable {
	private static final DataFlavor flavor = DataFlavor.imageFlavor;

	public TransferableImage(BufferedImage image) {
		BufferedImage tmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = tmp.createGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), Palette.WHITE, null);
		g.dispose();
		this.image = tmp;
	}

	private final Image image;

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] {
				flavor
		};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return TransferableImage.flavor.equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
		return image;
	}
}
