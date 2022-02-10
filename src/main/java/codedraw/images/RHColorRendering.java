package codedraw.images;

import java.awt.*;

enum RHColorRendering implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_COLOR_RENDER_DEFAULT),
	SPEED(RenderingHints.VALUE_COLOR_RENDER_SPEED),
	QUALITY(RenderingHints.VALUE_COLOR_RENDER_QUALITY);

	RHColorRendering(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_COLOR_RENDERING;
	}
}
