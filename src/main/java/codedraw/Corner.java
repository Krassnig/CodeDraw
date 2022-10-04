package codedraw;

/**
 * Defines how the corners of drawn shapes should look like.
 * The size of the corners scales with the lineWidth.
 * Round shapes such as circles are not affected.
 */
public enum Corner {
	/**
	 * All corners of drawn and filled shapes such as rectangles will be pointy.
	 * Corners of lines will also be pointy and will be drawn <b>around</b> the starting and endpoint.
	 * Round shapes such as circles are not affected.
	 */
	SHARP,
	/**
	 * All corners of drawn and filled shapes such as rectangles will be round.
	 * Corners of lines will also be round and will be drawn in a radius around the starting and endpoint.
	 * Round shapes such as circles are not affected.
	 */
	ROUND,
	/**
	 * All corners of drawn and filled shapes such as rectangles will be 'cut off' or bevel.
	 * Corners of lines will be cut of at exactly the starting and endpoint.
	 * Round shapes such as circles are not affected.
	 */
	BEVEL
}
