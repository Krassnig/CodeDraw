package codedraw;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class JFrameCorrector {
	public JFrameCorrector(JFrame jFrame, Dimension pixelSize) {
		frame = jFrame;
		framePixelSize = pixelSize;
		Insets inset = jFrame.getInsets();
		frameInsidePixelSize = new Dimension(pixelSize.width - inset.left - inset.right, pixelSize.height - inset.top - inset.bottom);
		originalGraphics = jFrame.getGraphicsConfiguration();
		correctJFrameSize();
	}

	private final JFrame frame;
	private final Dimension framePixelSize;
	private final Dimension frameInsidePixelSize;
	private final GraphicsConfiguration originalGraphics;

	public void onResizeCorrectSize() {
		 correctJFrameSize();
	}

	private void correctJFrameSize() {
		Dimension target = calculateCorrectFrameSize();
		if (!target.equals(frame.getMinimumSize()) || !target.equals(frame.getMaximumSize())) {
			setJFrameSize(target);
		}
	}

	private void setJFrameSize(Dimension size) {
		Dimension extra = size.getSize();
		frame.setMinimumSize(extra);
		frame.setMaximumSize(extra);
		frame.repaint();
		System.out.println("Settings window size to " + size);
	}

	private Dimension calculateCorrectFrameSize() {
		Rectangle totalBounds = getBoundsOfAllScreens();
		if (originalGraphics.getDevice() == frame.getGraphicsConfiguration().getDevice()) {
			frame.getGraphicsConfiguration();
			AffineTransform currentScreenTransformation = originalGraphics.getDefaultTransform();
			double xScale = currentScreenTransformation.getScaleX();
			double yScale = currentScreenTransformation.getScaleY();

			double xFactorCorrection = frameInsidePixelSize.width  * xScale > totalBounds.width  || xScale < 1 ? xScale : 1;
			double yFactorCorrection = frameInsidePixelSize.height * yScale > totalBounds.height || yScale < 1 ? yScale : 1;

			return new Dimension(
					(int)(framePixelSize.width  * xFactorCorrection),
					(int)(framePixelSize.height * yFactorCorrection)
			);
		}
		else {
			return framePixelSize.getSize();
		}
	}

	private static Rectangle getBoundsOfAllScreens() {
		Rectangle totalArea = new Rectangle(0, 0, 0, 0);

		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			for (GraphicsConfiguration configuration : device.getConfigurations()) {
				totalArea = totalArea.union(configuration.getBounds());
			}
		}

		return totalArea;
	}

	private static Dimension scale(Dimension toBeScaled, AffineTransform transformation) {
		return new Dimension(
				(int)(toBeScaled.width * transformation.getScaleX()),
				(int)(toBeScaled.height * transformation.getScaleY())
		);
	}
}
