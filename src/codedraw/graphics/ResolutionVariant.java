package codedraw.graphics;

import java.awt.*;

public enum ResolutionVariant implements RenderingHintValue {
	DEFAULT(RenderingHints.VALUE_RESOLUTION_VARIANT_DEFAULT),
	BASE(RenderingHints.VALUE_RESOLUTION_VARIANT_BASE),
	DPI_FIT(RenderingHints.VALUE_RESOLUTION_VARIANT_DPI_FIT),
	SIZE_FIT(RenderingHints.VALUE_RESOLUTION_VARIANT_SIZE_FIT);

	ResolutionVariant(Object value) {
		this.value = value;
	}

	private final Object value;

	public Object value() {
		return value;
	}

	public RenderingHints.Key key() {
		return RenderingHints.KEY_RESOLUTION_VARIANT;
	}
}
