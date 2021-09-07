package codedraw.textformat;

/**
 * Horizontally aligns the text. This property is also described as text alignment.
 */
public enum HorizontalAlign {
	/**
	 * The origin point will be to the left of the text.
	 * The text will be drawn to the right of the (x, y) coordinate.
	 * <pre>{@code
	 * -------------- + --------------
	 *                | Text goes here
	 * }</pre>
	 */
	LEFT,
	/**
	 * The text will be drawn at so that the x coordinate will be in the center of the text.
	 * <pre>{@code
	 * -------------- + --------------
	 *          Text goes here
	 * }</pre>
	 */
	CENTER,
	/**
	 * The origin point will be to the right of the text.
	 * The text will be drawn to the left of the x coordinate.
	 * <pre>{@code
	 * -------------- + --------------
	 * Text goes here |
	 * }</pre>
	 */
	RIGHT
}