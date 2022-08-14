package codedraw.drawing;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

/**
 * TextFormat is used to specify how CodeDraw formats, places and styles its drawn text.
 * See also {@link codedraw.CodeDraw#drawText(double, double, String)}
 * and {@link Image#drawText(double, double, String)}.
 */
public final class TextFormat {
	private static final Set<String> availableFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

	public TextFormat() { }

	private TextOrigin textOrigin = TextOrigin.TOP_LEFT;

	/**
	 * Defines the origin of the drawn text.
	 * The default is {@link TextOrigin#TOP_LEFT}.
	 * See {@link TextOrigin} for a more detailed explanation.
	 * @return the text origin
	 */
	public TextOrigin getTextOrigin() {
		return textOrigin;
	}

	/**
	 * Defines the origin of the drawn text.
	 * The default is {@link TextOrigin#TOP_LEFT}.
	 * See {@link TextOrigin} for a more detailed explanation.
	 * @param textOrigin Sets the text origin.
	 */
	public TextFormat setTextOrigin(TextOrigin textOrigin) {
		this.textOrigin = textOrigin;
		return this;
	}

	private int fontSize = 16;

	/**
	 * Defines the font size of the drawn text.
	 * The default is 16.
	 * @return the font size.
	 */
	public int getFontSize() { return fontSize; }

	/**
	 * Defines the font size of the drawn text.
	 * The default is 16.
	 * @param fontSize Sets the font size.
	 */
	public TextFormat setFontSize(int fontSize) {
		if (fontSize < 1) throw createParameterMustBeGreaterThanZeroException("fontSize");
		this.fontSize = fontSize;
		return this;
	}

	private String fontName = "Arial";

	/**
	 * Defines the font of the drawn text.
	 * The default font is Arial.
	 * @return the font name.
	 */
	public String getFontName() { return fontName; }

	/**
	 * Defines the font of the drawn text.
	 * The default font is Arial.
	 * @param fontName Sets the font name. Only accepts valid fonts installed on the system running this application.
	 */
	public TextFormat setFontName(String fontName) {
		if (fontName == null) throw createParameterNullException("fontName");
		if (!availableFonts.contains(fontName))
			throw new IllegalArgumentException("The font " + fontName + " is not available on your device.");
		this.fontName = fontName;
		return this;
	}

	private boolean isBold = false;

	/**
	 * Defines whether the drawn text is bold.
	 * The default is false (not bold).
	 * @return whether the drawn text is bold.
	 */
	public boolean isBold() { return isBold; }

	/**
	 * Defines whether the drawn text is bold.
	 * The default is false (not bold).
	 * @param isBold Sets whether the drawn text is bold.
	 */
	public TextFormat setBold(boolean isBold) {
		this.isBold = isBold;
		return this;
	}

	private boolean isItalic = false;

	/**
	 * Defines whether the drawn text is italic.
	 * The default is false (not italic).
	 * @return whether the drawn text is italic.
	 */
	public boolean isItalic() { return isItalic; }

	/**
	 * Defines whether the drawn text is italic.
	 * The default is false (not italic).
	 * @param isItalic Sets whether the drawn text is italic.
	 */
	public TextFormat setItalic(boolean isItalic) {
		this.isItalic = isItalic;
		return this;
	}

	private Underline underline = Underline.NONE;

	/**
	 * Defines the underline styling of the drawn text.
	 * The default is Underline.NONE.
	 * @return the underline styling.
	 */
	public Underline getUnderline() { return underline; }

	/**
	 * Defines the underline styling of the drawn text.
	 * The default is Underline.NONE.
	 * @param underline Sets the underline styling.
	 */
	public TextFormat setUnderlined(Underline underline) {
		if (underline == null) throw createParameterNullException("underline");
		this.underline = underline;
		return this;
	}

	private boolean isStrikethrough = false;

	/**
	 * Defines whether the drawn text is strikethrough.
	 * The default is false (no strikethrough).
	 * @return whether the drawn text is strikethrough.
	 */
	public boolean isStrikethrough() { return isStrikethrough; }

	/**
	 * Defines whether the drawn text is strikethrough.
	 * The default is false (no strikethrough).
	 * @param isStrikethrough Sets whether the drawn text is strikethrough.
	 */
	public TextFormat setStrikethrough(boolean isStrikethrough) {
		this.isStrikethrough = isStrikethrough;
		return this;
	}

	Font toFont() {
		return Font.getFont(new HashMap<TextAttribute, Object>() {{
			put(TextAttribute.FAMILY, getFontName());
			put(TextAttribute.SIZE, getFontSize());
			put(TextAttribute.POSTURE, isItalic() ? 0.2f : 0);
			put(TextAttribute.UNDERLINE, underlineEnumToTextAttributeUnderlineNumber(getUnderline()));
			put(TextAttribute.WEIGHT, isBold() ? 2.0f : 1.0f);
			put(TextAttribute.KERNING, TextAttribute.KERNING_ON); //Kerning is always on, 0 == KERNING_OFF
			put(TextAttribute.STRIKETHROUGH, isStrikethrough());
		}});
	}

	private static int underlineEnumToTextAttributeUnderlineNumber(Underline underline) {
		switch (underline) {
			case NONE: return -1;
			case SOLID: return TextAttribute.UNDERLINE_ON;
			case DASHED: return TextAttribute.UNDERLINE_LOW_DASHED;
			case DOTTED: return TextAttribute.UNDERLINE_LOW_DOTTED;
			case WAVY: return TextAttribute.UNDERLINE_LOW_GRAY;
			default: throw new RuntimeException("Unknown underline type");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TextFormat that = (TextFormat) o;
		return fontSize == that.fontSize && isBold == that.isBold && isItalic == that.isItalic && isStrikethrough == that.isStrikethrough && textOrigin == that.textOrigin && Objects.equals(fontName, that.fontName) && underline == that.underline;
	}

	@Override
	public int hashCode() {
		return Objects.hash(textOrigin, fontSize, fontName, isBold, isItalic, underline, isStrikethrough);
	}

	@Override
	public String toString() {
		return "TextFormat{"
				+ "fontName: " + fontName
				+ ", fontSize: " + fontSize
				+ ", textOrigin: " + textOrigin
				+ (underline == Underline.NONE ? "" : ", underline: " + underline)
				+ (isBold ? ", bold" : "")
				+ (isItalic ? ", italic" : "")
				+ (isStrikethrough ? ", strikethrough" : "")
				+ "}";
	}


	private static IllegalArgumentException createParameterNullException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
	}

	private static IllegalArgumentException createParameterMustBeGreaterThanZeroException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " must be greater than zero.");
	}
}
