package codedraw.graphics;

import java.awt.*;

enum Interpolation implements RenderingHintValue {
	NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR),
	BILINIEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
	BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	Interpolation(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_INTERPOLATION;
	}
}
