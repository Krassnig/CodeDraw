package codedraw.graphics;

import java.awt.*;

public enum AntiAliasing implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_ANTIALIAS_DEFAULT),
	OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
	ON(RenderingHints.VALUE_ANTIALIAS_ON);

	AntiAliasing(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_ANTIALIASING;
	}
}
