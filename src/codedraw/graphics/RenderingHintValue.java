package codedraw.graphics;

import java.awt.*;

interface RenderingHintValue {
	RenderingHints.Key key();

	Object value();

	default void apply(Graphics2D graphics) {
		graphics.setRenderingHint(key(), value());
		//g.addRenderingHints(new RenderingHints(hint.key(), hint.value()));
	}
}
