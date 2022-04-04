package codedraw.drawing;

/**
 * Draws a line below the drawn text.
 * The style of that line can be defined with this Underline enum.
 */
public enum Underline {
	/**
	 * No underline will be drawn below the text.
	 */
	NONE,
	/**
	 * A solid line will be drawn below the text.
	 */
	SOLID,
	/**
	 * A dashed line will be drawn below the text.
	 */
	DASHED,
	/**
	 * A dotted line will be drawn below the text.
	 */
	DOTTED,
	/**
	 * A wavy line will be drawn below the text.
	 */
	WAVY
}
