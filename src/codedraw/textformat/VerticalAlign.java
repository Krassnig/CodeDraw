package codedraw.textformat;

/**
 * Vertically aligns the text. This property is also described as the base line.
 */
public enum VerticalAlign {
	/**
	 * The origin point will be above the text.
	 * The text will be drawn below the given y coordinate.
	 * <pre>{@code
	 *  |
	 *  + --------------------
	 *  |  Text goes here
	 * }</pre>
	 */
	TOP,
	/**
	 * The text will be drawn so that the y coordinate will be in the middle of the text.
	 * <pre>{@code
	 *  |
	 *  + -Text goes here-----
	 *  |
	 * }</pre>
	 */
	MIDDLE,
	/**
	 * The origin point will be below the text.
	 * The text will be drawn above the given y coordinate.
	 * <pre>{@code
	 *  | Text goes here
	 *  + --------------------
	 *  |
	 * }</pre>
	 */
	BOTTOM
}