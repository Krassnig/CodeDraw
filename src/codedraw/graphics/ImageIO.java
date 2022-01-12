package codedraw.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

class ImageIO {
	private ImageIO() { }

	public static BufferedImage read(String pathToImage) {
		try {
			return javax.imageio.ImageIO.read(new File(pathToImage));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
