package codedraw.graphics;

import java.awt.*;

enum RHRendering implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_RENDER_DEFAULT),
	SPEED(RenderingHints.VALUE_RENDER_SPEED),
	QUALITY(RenderingHints.VALUE_RENDER_QUALITY);

	RHRendering(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_RENDERING;
	}
}
