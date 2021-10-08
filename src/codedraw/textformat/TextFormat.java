package codedraw.textformat;

import java.awt.*;
import java.util.*;

/**
 * TextFormat is used to specify how CodeDraw formats, places and styles its drawn text.
 * See also {@link codedraw.CodeDraw#drawText(double, double, String)}
 */
public final class TextFormat {
	private static final Set<String> availableFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

	public TextFormat() { }

	private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;

	/**
	 * Defines the horizontal alignment of the drawn text.
	 * The default is HorizontalAlign.LEFT.
	 * @return Returns the horizontal alignment.
	 */
	public HorizontalAlign getHorizontalAlign() { return horizontalAlign; }

	/**
	 * Defines the horizontal alignment of the drawn text.
	 * The default is HorizontalAlign.LEFT.
	 * @param horizontalAlign Sets the horizontal alignment.
	 */
	public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
		if (horizontalAlign == null) throw createArgumentNull("horizontalAlign");
		this.horizontalAlign = horizontalAlign;
	}

	private VerticalAlign verticalAlign = VerticalAlign.TOP;

	/**
	 * Defines the vertical alignment of the drawn text.
	 * The default is VerticalAlign.TOP
	 * @return Returns the vertical alignment.
	 */
	public VerticalAlign getVerticalAlign() { return verticalAlign; }

	/**
	 * Defines the vertical alignment of the drawn text.
	 * The default is VerticalAlign.TOP
	 * @param verticalAlign Sets the vertical alignment.
	 */
	public void setVerticalAlign(VerticalAlign verticalAlign) {
		if (horizontalAlign == null) throw createArgumentNull("verticalAlign");
		this.verticalAlign = verticalAlign;
	}

	private int fontSize = 16;

	/**
	 * Defines the font size of the drawn text.
	 * The default is 16.
	 * @return Returns the font size.
	 */
	public int getFontSize() { return fontSize; }

	/**
	 * Defines the font size of the drawn text.
	 * The default is 16.
	 * @param fontSize Sets the font size.
	 */
	public void setFontSize(int fontSize) {
		if (fontSize < 1) throw createArgumentNotNegative("fontSize");
		this.fontSize = fontSize;
	}

	private String fontName = "Arial";

	/**
	 * Defines the font of the drawn text.
	 * The default font is Arial.
	 * @return Returns the font name.
	 */
	public String getFontName() { return fontName; }

	/**
	 * Defines the font of the drawn text.
	 * The default font is Arial.
	 * @param fontName Sets the font name. Only accepts valid fonts installed on the system running this application.
	 */
	public void setFontName(String fontName) {
		if (fontName == null) throw createArgumentNull("fontName");
		if (!availableFonts.contains(fontName))
			throw new IllegalArgumentException("Font with the name " + fontName + " is not available");
		this.fontName = fontName;
	}

	private boolean isBold = false;

	/**
	 * Defines whether the drawn text is bold.
	 * The default is false (not bold).
	 * @return Returns whether the drawn text is bold.
	 */
	public boolean isBold() { return isBold; }

	/**
	 * Defines whether the drawn text is bold.
	 * The default is false (not bold).
	 * @param isBold Sets whether the drawn text is bold.
	 */
	public void isBold(boolean isBold) {
		this.isBold = isBold;
	}

	private boolean isItalic = false;

	/**
	 * Defines whether the drawn text is italic.
	 * The default is false (not italic).
	 * @return Returns whether the drawn text is italic.
	 */
	public boolean isItalic() { return isItalic; }

	/**
	 * Defines whether the drawn text is italic.
	 * The default is false (not italic).
	 * @param isItalic Sets whether the drawn text is italic.
	 */
	public void isItalic(boolean isItalic) { this.isItalic = isItalic; }

	private Underline underline = Underline.NONE;

	/**
	 * Defines the underline styling of the drawn text.
	 * The default is Underline.NONE.
	 * @return Returns the underline styling.
	 */
	public Underline getUnderline() { return underline; }

	/**
	 * Defines the underline styling of the drawn text.
	 * The default is Underline.NONE.
	 * @param underline Sets the underline styling.
	 */
	public void setUnderlined(Underline underline) {
		if (underline == null) throw createArgumentNull("underline");
		this.underline = underline;
	}

	private boolean isStrikethrough = false;

	/**
	 * Defines whether the drawn text is strikethrough.
	 * The default is false (no strikethrough).
	 * @return Returns whether the drawn text is strikethrough.
	 */
	public boolean isStrikethrough() { return isStrikethrough; }

	/**
	 * Defines whether the drawn text is strikethrough.
	 * The default is false (no strikethrough).
	 * @param strikethrough Sets whether the drawn text is strikethrough.
	 */
	public void isStrikethrough(boolean strikethrough) {
		this.isStrikethrough = strikethrough;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TextFormat that = (TextFormat) o;
		return fontSize == that.fontSize && isBold == that.isBold && isItalic == that.isItalic && isStrikethrough == that.isStrikethrough && horizontalAlign == that.horizontalAlign && verticalAlign == that.verticalAlign && Objects.equals(fontName, that.fontName) && underline == that.underline;
	}

	@Override
	public int hashCode() {
		return Objects.hash(horizontalAlign, verticalAlign, fontSize, fontName, isBold, isItalic, underline, isStrikethrough);
	}

	@Override
	public String toString() {
		return "TextFormat{" + fontName + ", " + fontSize
				+ ", h:" + horizontalAlign
				+ ", v:" + verticalAlign
				+ ", u: " + underline
				+ ", isBold: " + isBold
				+ ", isItalic: " + isItalic
				+ ", isStrikethrough: " + isStrikethrough +
				"}";
	}

	private static IllegalArgumentException createArgumentNull(String argumentName) {
		return new IllegalArgumentException("The parameter " + argumentName + " cannot be null.");
	}

	private static IllegalArgumentException createArgumentNotNegative(String argumentName) {
		return new IllegalArgumentException("Argument " + argumentName + " cannot be negative.");
	}
}
