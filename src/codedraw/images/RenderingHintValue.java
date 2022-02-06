package codedraw.images;

import java.awt.*;

interface RenderingHintValue {
	RenderingHints.Key key();

	Object value();

	static void applyHint(Graphics2D graphics, RenderingHintValue hint) {
		graphics.setRenderingHint(hint.key(), hint.value());
	}

	static void applyHint(Graphics2D graphics, Interpolation interpolation) {
		applyHint(graphics, RHInterpolation.fromInterpolation(interpolation));
	}
}
