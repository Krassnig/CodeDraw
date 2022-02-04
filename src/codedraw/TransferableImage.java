package codedraw;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class TransferableImage implements Transferable {
	private static final DataFlavor flavor = DataFlavor.imageFlavor;

	public TransferableImage(Image image) {
		this.image = image;
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
