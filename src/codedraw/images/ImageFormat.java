package codedraw.images;

/**
 * Defines possible image formats.
 */
public enum ImageFormat {
	PNG,
	/**
	 * Does not support transparency.
	 */
	JPG,
	/**
	 * Does not support transparency.
	 */
	JPEG,
	GIF,
	/**
	 * Does not support transparency.
	 */
	BMP;

	boolean supportsTransparency() {
		return this != JPG && this != JPEG && this != BMP;
	}

	String getFormatName() {
		return toString().toLowerCase();
	}
}
