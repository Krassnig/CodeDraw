package codedraw;

import java.awt.*;

enum RHTextAntiAliasing implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT),
	OFF(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF),
	ON(RenderingHints.VALUE_TEXT_ANTIALIAS_ON),
	GAPS(RenderingHints.VALUE_TEXT_ANTIALIAS_GASP),
	LCD_HBGR(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR),
	LCD_HRGB(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB),
	LCD_VBGR(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR),
	LCD_VRGB(RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB);

	RHTextAntiAliasing(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_TEXT_ANTIALIASING;
	}
}
