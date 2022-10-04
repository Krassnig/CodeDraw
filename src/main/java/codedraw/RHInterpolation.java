package codedraw;

import java.awt.*;

enum RHInterpolation implements RenderingHintValue {
	NEAREST_NEIGHBOR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR),
	BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
	BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC);

	RHInterpolation(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_INTERPOLATION;
	}

	public static RHInterpolation fromInterpolation(Interpolation interpolation) {
		switch (interpolation) {
			case NEAREST_NEIGHBOR: return NEAREST_NEIGHBOR;
			case BILINEAR: return BILINEAR;
			case BICUBIC: return BICUBIC;
			default: throw new RuntimeException("Invalid or unknown interpolation");
		}
	}
}
