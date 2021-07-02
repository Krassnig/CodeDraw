package CodeDraw.TextFormat;

import java.awt.font.TextAttribute;

public enum UnderlineType {
	NONE(-1),
	NORMAL(TextAttribute.UNDERLINE_ON),
	DASHED(TextAttribute.UNDERLINE_LOW_DASHED),
	DOTTED(TextAttribute.UNDERLINE_LOW_DOTTED),
	WIGGLE(TextAttribute.UNDERLINE_LOW_GRAY),
	THICK(TextAttribute.UNDERLINE_LOW_TWO_PIXEL);

	private final int underline;

	UnderlineType(int underline) {
		this.underline = underline;
	}

	public int getUnderline() {
		return underline;
	}
}
