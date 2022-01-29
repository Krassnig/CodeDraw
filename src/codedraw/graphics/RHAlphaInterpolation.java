package codedraw.graphics;

import java.awt.*;

enum RHAlphaInterpolation implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT),
	SPEED(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED),
	QUALITY(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

	RHAlphaInterpolation(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_ALPHA_INTERPOLATION;
	}
}
