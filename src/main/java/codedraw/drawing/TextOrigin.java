package codedraw.drawing;

/**
 * Horizontally and vertically aligns text.
 * The origin describes where the text is relative to the specified (x, y) coordinates.
 */
public enum TextOrigin {
	/**
	 * The origin will be to the top left of the text.
	 * The text will be drawn to the right below the (x, y) coordinate.
	 * <pre>{@code
	 *                |
	 * -------------- + --------------
	 *                | Text goes here
	 * }</pre>
	 */
	TOP_LEFT,
	/**
	 * The origin will be to the top center of the text.
	 * The text will be drawn centrally below the (x, y) coordinate.
	 * <pre>{@code
	 *                |
	 * -------------- + --------------
	 *          Text goes here
	 * }</pre>
	 */
	TOP_MIDDLE,
	/**
	 * The origin will be to the top right of the text.
	 * The text will be drawn to the left below the (x, y) coordinate.
	 * <pre>{@code
	 *                |
	 * -------------- + --------------
	 * Text goes here |
	 * }</pre>
	 */
	TOP_RIGHT,
	/**
	 * The origin will be to the center left of the text.
	 * The text will be drawn to the centered right of the (x, y) coordinate.
	 * <pre>{@code
	 *                |
	 * -------------- + Text goes here
	 *                |
	 * }</pre>
	 */
	CENTER_LEFT,
	/**
	 * The origin will be exactly at the center of the text.
	 * The text will be drawn around the (x, y) coordinate.
	 * <pre>{@code
	 *                |
	 * ------- Text go+es here -------
	 *                |
	 * }</pre>
	 */
	CENTER,
	/**
	 * The origin will be to the center right of the text.
	 * The text will be drawn to the centered left of the (x, y) coordinate.
	 * <pre>{@code
	 *                |
	 * Text goes here + --------------
	 *                |
	 * }</pre>
	 */
	CENTER_RIGHT,
	/**
	 * The origin will be to the bottom left of the text.
	 * The text will be drawn to the right above the (x, y) coordinate.
	 * <pre>{@code
	 *                | Text goes here
	 * -------------- + --------------
	 *                |
	 * }</pre>
	 */
	BOTTOM_LEFT,
	/**
	 * The origin will be to the bottom center of the text.
	 * The text will be drawn centrally above the (x, y) coordinate.
	 * <pre>{@code
	 *         Text goes here
	 * -------------- + --------------
	 *                |
	 * }</pre>
	 */
	BOTTOM_MIDDLE,
	/**
	 * The origin will be to the bottom right of the text.
	 * The text will be drawn to the left above the (x, y) coordinate.
	 * <pre>{@code
	 * Text goes here |
	 * -------------- + --------------
	 *                |
	 * }</pre>
	 */
	BOTTOM_RIGHT,
}
