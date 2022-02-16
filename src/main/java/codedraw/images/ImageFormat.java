package codedraw.images;

/**
 * Defines possible image formats.
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
