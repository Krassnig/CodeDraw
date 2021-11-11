package codedraw;

import java.awt.*;
import java.awt.geom.AffineTransform;

class Screen {
	public static final Screen DEFAULT_SCREEN = new Screen();

	private Screen() {
		try {
			GraphicsConfiguration conf = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
			AffineTransform at = conf.getDefaultTransform();
			xScale = at.getScaleX();
			yScale = at.getScaleY();
		}
		catch (Exception e) {
			xScale = 1;
			yScale = 1;
		}
	}

	private double xScale;
	private double yScale;

	public double getXScale() {
		return xScale;
	}

	public double getYScale() {
		return yScale;
	}
}
