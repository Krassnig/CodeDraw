package CodeDraw.TextFormat;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

public final class TextFormat {

	public TextFormat() {
	}

	private static final Set<String> availableFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));


	private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
	private VerticalAlign verticalAlign = VerticalAlign.TOP;

	//Font Properties
	private int fontSize = 16;
	private String fontName = "Arial";
	private boolean bold = false;
	private boolean italic = false;
	private UnderlineType underlineType = UnderlineType.NONE;
	private boolean kerning = false;
	private boolean strikethrough = false;


	public HorizontalAlign getHorizontalAlign() {
		return horizontalAlign;
	}

	public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
		this.horizontalAlign = horizontalAlign;
	}

	public VerticalAlign getVerticalAlign() {
		return verticalAlign;
	}

	public void setVerticalAlign(VerticalAlign verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	public void setFontSize(int fontSize) {
		if (fontSize < 1) throw createArgumentNotNegative("fontSize");

		this.fontSize = fontSize;
	}

	public void setFontName(String fontName) {
		if (fontName == null) throw createArgumentNull("fontName");
		if (!isFontAvailable(fontName))
			throw new IllegalArgumentException("Font with the name " + fontName + " is not available");

		this.fontName = fontName;

	}

	private static boolean isFontAvailable(String fontName) {
		return availableFonts.contains(fontName);
	}

	public void setItalic(boolean isItalic) {
		this.italic = isItalic;
	}

	public void setBold(boolean isBold) {
		this.bold = isBold;
	}

	public void setUnderlined(UnderlineType underlineType) {
		if (underlineType == null) throw createArgumentNull("underline");
		this.underlineType = underlineType;
	}

	public void setKerning(boolean isKerning) {
		this.kerning = isKerning;
	}

	public void setStrikethrough(boolean strikethrough) {
		this.strikethrough = strikethrough;
	}

	public void renderText(Graphics2D graphics2D, double x, double y, String text) {
		Font font = createFont();
		graphics2D.setFont(font);

		x -= getHorizontalOffset(graphics2D, text);
		y -= getVerticalOffset(graphics2D.getFont());

		graphics2D.drawString(text, (float) x, (float) y);
	}

	private Font createFont() {
		Font font = new Font(fontName, Font.PLAIN, fontSize);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>() {
			{
				put(TextAttribute.POSTURE, italic ? 0.2f : 0);
				put(TextAttribute.UNDERLINE, underlineType.getUnderline());
				put(TextAttribute.WEIGHT, bold ? 2.0f : 1.0f);
				put(TextAttribute.KERNING, kerning ? TextAttribute.KERNING_ON : 0); // 0 == KERNING_OFF
				put(TextAttribute.STRIKETHROUGH, strikethrough);
			}
		};
		return font.deriveFont(attributes);
	}

	private double getVerticalOffset(Font font) {
		switch (verticalAlign) {
			case TOP:
				return 0;
			case BOTTOM:
				return font.getSize();
			case MIDDLE:
				return font.getSize() / 2.0;
			default:
				throw new RuntimeException("Unknown vertical alignment option");
		}
	}

	private double getHorizontalOffset(Graphics2D graphics, String text) {
		FontMetrics fm = graphics.getFontMetrics(graphics.getFont());
		switch (horizontalAlign) {
			case LEFT:
				return 0;
			case RIGHT:
				return fm.stringWidth(text);
			case CENTER:
				return fm.stringWidth(text) / 2.0;
			default:
				throw new RuntimeException("Unknown horizontal alignment option");
		}
	}

	private static IllegalArgumentException createArgumentNull(String argumentName) {
		return new IllegalArgumentException("The parameter " + argumentName + " cannot be null.");
	}

	private static IllegalArgumentException createArgumentNotNegative(String argumentName) {
		return new IllegalArgumentException("Argument " + argumentName + " cannot be negative.");
	}
}
