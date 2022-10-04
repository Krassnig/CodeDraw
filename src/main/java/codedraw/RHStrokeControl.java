package codedraw;

import java.awt.*;

enum RHStrokeControl implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_STROKE_DEFAULT),
	PURE(RenderingHints.VALUE_STROKE_PURE),
	NORMALIZE(RenderingHints.VALUE_STROKE_NORMALIZE);

	RHStrokeControl(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_STROKE_CONTROL;
	}
}
