package CodeDraw.TextFormat;

public enum HorizontalAlign {
	/**
	 * The text will be drawn to the right of the x coordinate.
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
	 * The text will be drawn to the left of the x coordinate.
	 * <pre>{@code
	 * -------------- + --------------
	 * Text goes here |
	 * }</pre>
	 */
	RIGHT
}