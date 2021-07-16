package CodeDraw.TextFormat;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

public final class TextFormat {
	private static final Set<String> availableFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

	public TextFormat() { }

	private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
	public HorizontalAlign getHorizontalAlign() { return horizontalAlign; }
	public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
		if (horizontalAlign == null) throw createArgumentNull("horizontalAlign");
		this.horizontalAlign = horizontalAlign;
	}

	private VerticalAlign verticalAlign = VerticalAlign.TOP;
	public VerticalAlign getVerticalAlign() { return verticalAlign; }
	public void setVerticalAlign(VerticalAlign verticalAlign) {
		if (horizontalAlign == null) throw createArgumentNull("verticalAlign");
		this.verticalAlign = verticalAlign;
	}

	private int fontSize = 16;
	public int getFontSize() { return fontSize; }
	public void setFontSize(int fontSize) {
		if (fontSize < 1) throw createArgumentNotNegative("fontSize");
		this.fontSize = fontSize;
	}

	private String fontName = "Arial";
	public String getFontName() { return fontName; }
	public void setFontName(String fontName) {
		if (fontName == null) throw createArgumentNull("fontName");
		if (!availableFonts.contains(fontName))
			throw new IllegalArgumentException("Font with the name " + fontName + " is not available");
		this.fontName = fontName;
	}

	private boolean bold = false;
	public boolean getBold() { return bold; }
	public void setBold(boolean isBold) {
		this.bold = isBold;
	}

	private boolean italic = false;
	public boolean getItalic() { return italic; }
	public void setItalic(boolean isItalic) { this.italic = isItalic; }

	private Underline underline = Underline.NONE;
	public Underline getUnderline() { return underline; }
	public void setUnderlined(Underline underline) {
		if (underline == null) throw createArgumentNull("underline");
		this.underline = underline;
	}

	private boolean strikethrough = false;
	public boolean getStrikethrough() { return strikethrough; }
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
				put(TextAttribute.UNDERLINE, underline.getUnderline());
				put(TextAttribute.WEIGHT, bold ? 2.0f : 1.0f);
				put(TextAttribute.KERNING, TextAttribute.KERNING_ON); //Kerning is always on, 0 == KERNING_OFF
				put(TextAttribute.STRIKETHROUGH, strikethrough);
			}
		};
		return font.deriveFont(attributes);
	}

	private double getVerticalOffset(Font font) {
		switch (verticalAlign) {
			case TOP:
				return -font.getSize();
			case MIDDLE:
				return -font.getSize() / 2.0;
			case BOTTOM:
				return 0;
			default:
				throw new RuntimeException("Unknown vertical alignment option");
		}
	}

	private double getHorizontalOffset(Graphics2D graphics, String text) {
		FontMetrics fm = graphics.getFontMetrics(graphics.getFont());
		switch (horizontalAlign) {
			case LEFT:
				return 0;
			case CENTER:
				return fm.stringWidth(text) / 2.0;
			case RIGHT:
				return fm.stringWidth(text);
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
