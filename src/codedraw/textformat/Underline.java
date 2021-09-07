package codedraw.textformat;

import java.awt.font.TextAttribute;

/**
 * Draws a line below the drawn text.
 * The style of that line can be defined with this Underline enum.
 */
public enum Underline {
	NONE(-1),
	SOLID(TextAttribute.UNDERLINE_ON),
	DASHED(TextAttribute.UNDERLINE_LOW_DASHED),
	DOTTED(TextAttribute.UNDERLINE_LOW_DOTTED),
	WAVY(TextAttribute.UNDERLINE_LOW_GRAY);

	private final int underline;

	Underline(int underline) {
		this.underline = underline;
	}

	public int getUnderline() {
		return underline;
	}
}
