package codedraw;

import java.awt.*;

enum RHFractionalMetrics implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT),
	OFF(RenderingHints.VALUE_FRACTIONALMETRICS_OFF),
	ON(RenderingHints.VALUE_FRACTIONALMETRICS_ON);

	RHFractionalMetrics(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_FRACTIONALMETRICS;
	}
}
