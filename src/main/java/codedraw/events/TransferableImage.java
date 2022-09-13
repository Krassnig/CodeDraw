package codedraw.events;

import codedraw.drawing.BufferedImageType;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

class TransferableImage implements Transferable {
	private static final DataFlavor flavor = DataFlavor.imageFlavor;

	public TransferableImage(codedraw.drawing.Image image) {
		this.image = image.toBufferedImage(BufferedImageType.INT_RGB);
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
