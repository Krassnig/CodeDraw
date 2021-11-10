package codedraw.graphics;

import java.awt.*;

interface RenderingHintValue {
	RenderingHints.Key key();

	Object value();

	static void applyHint(Graphics2D graphics, RenderingHintValue hint) {
		graphics.setRenderingHint(hint.key(), hint.value());
	}
}
