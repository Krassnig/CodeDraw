package codedraw.drawing;

/**
 * Defines how the corners of drawn shapes should look like.
 * The size of the corners scales with the lineWidth.
 * Round shapes (e.g. circles) will not be affected.
 */
public enum Corner {
	/**
	 * All corners of drawn and filled shapes (e.g. rectangles) will be pointy.
	 * Corners of lines will also be pointy and will be drawn <b>around</b> the starting and endpoint.
	 * Round shapes (e.g. circles) will not be affected.
	 */
	SHARP,
	/**
	 * All corners of drawn and filled shapes (e.g. rectangles) will be round.
	 * Corners of lines will also be round and will be drawn in a radius around the starting and endpoint.
	 * Round shapes (e.g. circles) will not be affected.
	 */
	ROUND,
	/**
	 * All corners of drawn and filled shapes (e.g. rectangles) will be 'cut off' or bevel.
	 * Corners of lines will be cut of at exactly the starting and endpoint.
	 * Round shapes (e.g. circles) will not be affected.
	 */
	BEVEL
}
