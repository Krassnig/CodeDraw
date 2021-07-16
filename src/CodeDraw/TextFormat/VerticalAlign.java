package CodeDraw.TextFormat;

public enum VerticalAlign {
	/**
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
	 * The text will be drawn above the given y coordinate.
	 * <pre>{@code
	 *  | Text goes here
	 *  + --------------------
	 *  |
	 * }</pre>
	 */
	BOTTOM
}