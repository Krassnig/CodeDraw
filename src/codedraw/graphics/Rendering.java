package codedraw.graphics;

import java.awt.*;

public enum Rendering implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_RENDER_DEFAULT),
	SPEED(RenderingHints.VALUE_RENDER_SPEED),
	QUALITY(RenderingHints.VALUE_RENDER_QUALITY);

	Rendering(Object value) {
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
