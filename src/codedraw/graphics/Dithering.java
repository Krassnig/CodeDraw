package codedraw.graphics;

import java.awt.*;

enum Dithering implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_DITHER_DEFAULT),
	DISABLE(RenderingHints.VALUE_DITHER_DISABLE),
	ENABLE(RenderingHints.VALUE_DITHER_ENABLE);

	Dithering(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_DITHERING;
	}
}
