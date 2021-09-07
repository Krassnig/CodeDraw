package codedraw;

/**
 * Defines how the corners of drawn shapes should look.
 * Filled Shapes will not be affected.
 */
public enum Corner {
	/**
	 * All corners of drawn shapes (e.g. rectangles) will be pointy.
	 * Corners of lines will also be pointy and will be drawn <b>around</b> the starting and endpoint.
	 * Filled Shapes will not be affected.
	 */
	Sharp,
	/**
	 * All corners of drawn shapes (e.g. rectangles) will be round.
	 * Corners of lines will also be round and will be drawn in a radius around the starting and endpoint.
	 * Filled Shapes will not be affected.
	 */
	Round,
	/**
	 * All corners of drawn shapes (e.g. rectangles) will be 'cut off' or bevel.
	 * Corners of lines will be cut of at exactly the starting and endpoint.
	 * Filled Shapes will not be affected.
	 */
	Bevel
}
