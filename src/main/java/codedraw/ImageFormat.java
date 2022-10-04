package codedraw;

/**
 * Defines possible image formats.
 * This enum is used to specify the image format in which images are saved.
 * See {@link Image#save(Image, String, ImageFormat)} for details.
 */
public enum ImageFormat {
	PNG,
	GIF,
	/**
	 * Does not support transparency.
	 */
	JPG,
	/**
	 * Does not support transparency.
	 */
	JPEG,
	/**
	 * Does not support transparency.
	 */
	BMP;

	boolean supportsTransparency() {
		return this == PNG || this == GIF;
	}

	String getFormatName() {
		return toString().toLowerCase();
	}
}
