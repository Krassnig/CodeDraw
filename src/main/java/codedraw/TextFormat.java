package codedraw;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

/**
 * TextFormat is used to specify how CodeDraw formats, places and styles its drawn text.
 * See also {@link codedraw.CodeDraw#drawText(double, double, String)}
 * and {@link Image#drawText(double, double, String)}.
 */
public final class TextFormat {
	public TextFormat() { }

	private TextOrigin textOrigin = TextOrigin.TOP_LEFT;

	/**
	 * Defines the origin of the drawn text relative to the position
	 * specified in the {@link Image#drawText(double, double, String)} method.
	 * The default is {@link TextOrigin#TOP_LEFT}.
	 * See {@link TextOrigin} for a more detailed explanation.
	 * @return the text origin
	 */
	public TextOrigin getTextOrigin() {
		return textOrigin;
	}

	/**
	 * Defines the origin of the drawn text relative to the position
	 * specified in the {@link Image#drawText(double, double, String)} method.
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

	private String fontName = Font.DIALOG;

	/**
	 * Defines the font of the drawn text.
	 * The default font is {@link Font#DIALOG}.
	 * @return the font name.
	 */
	public String getFontName() { return fontName; }

	/**
	 * Defines the font of the drawn text.
	 * If a given font is not available the next fallbackFontName will be used.
	 * If none of the fallbackFontNames are available the {@link Font#DIALOG} will be used.
	 * The default font is {@link Font#DIALOG}.
	 * To check which fonts are available you can call the static methods
	 * {@link #getAllAvailableFontNames()} and {@link #isFontNameAvailable(String)}.
	 * The font chosen by this method can be found by calling {@link #getFontName()} after setting
	 * one or several fonts.
	 * @param fontName Sets the font name.
	 * @param fallbackFontNames Defines alternative font names which are used in case fontName is not available.
	 */
	public TextFormat setFontName(String fontName, String... fallbackFontNames) {
		if (fontName == null) throw createParameterNullException("fontName");
		if (fallbackFontNames == null) throw createParameterNullException("fallbackFontNames");

		this.fontName = getFirstAvailableFontName(fontName, fallbackFontNames);
		return this;
	}

	private static String getFirstAvailableFontName(String font, String... fontNames) {
		if (isFontNameAvailable(font)) return font;

		for (String fontName : fontNames) {
			if (isFontNameAvailable(fontName)) {
				return fontName;
			}
		}

		return Font.DIALOG;
	}

	/**
	 * Sets the font-name property to its default value {@link Font#DIALOG}.
	 */
	public TextFormat setFontNameToDefault() {
		setFontName(Font.DIALOG);
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
	 * The default is {@link Underline#NONE}.
	 * @return the underline styling.
	 */
	public Underline getUnderline() { return underline; }

	/**
	 * Defines the underline styling of the drawn text.
	 * The default is {@link Underline#NONE}.
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

	private Font toFont() {
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

	/**
	 * Creates a list of available fonts on the current system.
	 * @return an array of all available fonts.
	 */
	public static String[] getAllAvailableFontNames() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	}

	/**
	 * Tests whether the font is available on the current system.
	 * If this method returns for a given font, {@link #setFontName(String, String...)} will not use the fallback fonts.
	 * @param fontName any font name.
	 * @return whether the font is available.
	 */
	public static boolean isFontNameAvailable(String fontName) {
		if (fontName == null) throw createParameterNullException("fontName");

		for (String font : getAllAvailableFontNames()) {
			if (font.equals(fontName)) {
				return true;
			}
		}

		return false;
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

	static void drawText(Graphics2D graphics, double x, double y, String text, TextFormat textFormat) {
		graphics.setFont(textFormat.toFont());
		FontMetrics fontMetrics = graphics.getFontMetrics();
		String[] lines = text.split("(\r\n)|\r|\n", -1);

		y += calculateVerticalOffset(textFormat.getTextOrigin(), fontMetrics, lines.length);

		for (int i = 0; i < lines.length; i++) {
			double xi = x + calculateHorizontalOffset(textFormat.getTextOrigin(), fontMetrics, lines[i]);
			graphics.drawString(lines[i], (float) xi, (float)(y + i * fontMetrics.getHeight()));
		}
	}

	private static double calculateVerticalOffset(TextOrigin verticalAlign, FontMetrics fontMetrics, int lineCount) {
		double capHeight = fontMetrics.getAscent() - fontMetrics.getDescent();
		double leading = fontMetrics.getHeight() * (lineCount - 1);

		switch (verticalAlign) {
			case TOP_LEFT:
			case TOP_MIDDLE:
			case TOP_RIGHT:
				return capHeight;
			case CENTER_LEFT:
			case CENTER:
			case CENTER_RIGHT:
				return (capHeight - leading) / 2;
			case BOTTOM_LEFT:
			case BOTTOM_MIDDLE:
			case BOTTOM_RIGHT:
				return -leading;
			default:
				throw new RuntimeException("Unknown vertical alignment option.");
		}
	}

	private static double calculateHorizontalOffset(TextOrigin horizontalAlign, FontMetrics fontMetrics, String text) {
		switch (horizontalAlign) {
			case TOP_LEFT:
			case CENTER_LEFT:
			case BOTTOM_LEFT:
				return 0;
			case TOP_MIDDLE:
			case CENTER:
			case BOTTOM_MIDDLE:
				return -fontMetrics.stringWidth(text) / 2D;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				return -fontMetrics.stringWidth(text);
			default:
				throw new RuntimeException("Unknown horizontal alignment option.");
		}
	}

	private static IllegalArgumentException createParameterNullException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " cannot be null.");
	}

	private static IllegalArgumentException createParameterMustBeGreaterThanZeroException(String parameterName) {
		return new IllegalArgumentException("The parameter " + parameterName + " must be greater than zero.");
	}
}
