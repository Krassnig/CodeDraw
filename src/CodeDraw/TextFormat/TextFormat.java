package CodeDraw.TextFormat;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

public class TextFormat {

	public TextFormat() {
	}

	private HorizontalAlign horizontalAlign = HorizontalAlign.LEFT;
	private VerticalAlign verticalAlign = VerticalAlign.TOP;

	//Font Properties
	private int fontSize = 16;
	private String fontName = "Arial";
	private float weight = 1f;
	private float posture = 0;
	private int underline = -1;
	private int kerning = 0;
	private boolean strikethrough = false;
	private static final Set<String> availableFonts = new HashSet<>(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

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

	private boolean isFontAvailable(String fontName) {
		return availableFonts.contains(fontName);
	}

	public void setItalic(boolean isItalic) {
		if (isItalic) setPosture(0.2f);
		else setPosture(0);
	}

	public void setPosture(float posture) {
		this.posture = posture;
	}

	public void setWeight(FontWeight fontWeight) {
		if (fontWeight == null) throw createArgumentNull("fontWeight");
		setWeight(fontWeight.getWeight());
	}

	public void setWeight(float weight) {
		if (weight <= 0) throw createArgumentNotNegative("weight");
		this.weight = weight;
	}

	public void setUnderlined(UnderlineType underlineType) {
		if (underlineType == null) throw createArgumentNull("underline");
		this.underline = underlineType.getUnderline();
	}

	public void setKerning(boolean kerning) {
		if (kerning) this.kerning = TextAttribute.KERNING_ON;
		else this.kerning = 0;
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
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>(){
			{
				put(TextAttribute.POSTURE, posture);
				put(TextAttribute.UNDERLINE, underline);
				put(TextAttribute.WEIGHT, weight);
				put(TextAttribute.KERNING, kerning);
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
