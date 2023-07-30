package codedraw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;

/**
 * This class fixes a bug where the JFrame is too small when both the screen scaling (zoom) is enabled
 * and the JFrame is larger than the size of the screen.
 * It artificially specifies a larger size than desired to hit the actual
 * targetDimension specified in the constructor argument.
 */
class JFrameCorrector implements AutoCloseable {
	public JFrameCorrector(JFrame jFrame, Dimension jFrameTargetDimension) {
		if (jFrame.isUndecorated()) throw new RuntimeException("JFrame correction is not necessary for undecorated frames");
		frame = jFrame;
		framePixelSize = jFrameTargetDimension.getSize();
		Insets inset = jFrame.getInsets();
		innerPixelSize = new Dimension(framePixelSize.width - inset.left - inset.right, framePixelSize.height - inset.top - inset.bottom);
		AffineTransform startTransform = jFrame.getGraphicsConfiguration().getDefaultTransform();
		startXScale = startTransform.getScaleX();
		startYScale = startTransform.getScaleY();
		listener = createComponentListener();
		jFrame.addComponentListener(listener);
		correctJFrameSize();
	}

	private final JFrame frame;
	private final Dimension framePixelSize;
	private final Dimension innerPixelSize;
	private final double startXScale;
	private final double startYScale;
	private ComponentListener listener;
	private Dimension lastCorrection = null;

	@Override
	public void close() {
		if (listener != null) {
			frame.removeComponentListener(listener);
			listener = null;
		}
	}

	private ComponentListener createComponentListener() {
		return new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				correctJFrameSize();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				correctJFrameSize();
			}

			@Override
			public void componentShown(ComponentEvent e) {
				correctJFrameSize();
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				correctJFrameSize();
			}
		};
	}

	private void correctJFrameSize() {
		Dimension newCorrection = calculateCorrectFrameSize();
		if (!newCorrection.equals(lastCorrection)) {
			setJFrameSize(newCorrection);
			setJFrameSize(newCorrection);
		}
		lastCorrection = newCorrection;
	}

	private void setJFrameSize(Dimension size) {
		Dimension sizeCopy = size.getSize();
		frame.setSize(sizeCopy);
		frame.setMinimumSize(sizeCopy);
		frame.setMaximumSize(sizeCopy);
		frame.repaint();
	}

	private Dimension calculateCorrectFrameSize() {
		Rectangle allScreenSize = getUnscaledDimensionOfAllScreens();

		AffineTransform currentTransformation = frame.getGraphicsConfiguration().getDefaultTransform();
		double xScale = currentTransformation.getScaleX();
		double yScale = currentTransformation.getScaleY();

		boolean correctXScale = innerPixelSize.width  > Math.ceil(allScreenSize.width  / xScale) || xScale < 1;
		boolean correctYScale = innerPixelSize.height > Math.ceil(allScreenSize.height / yScale) || yScale < 1;

		double differenceX = startXScale - xScale;
		double differenceY = startYScale - yScale;

		return new Dimension(
				(int)(framePixelSize.width  * (correctXScale ? xScale : 1) + (differenceX * 4)),
				(int)(framePixelSize.height * (correctYScale ? yScale : 1) + (differenceY * 4))
		);
	}

	private static Rectangle getUnscaledDimensionOfAllScreens() {
		Rectangle sum = new Rectangle(0, 0, 0, 0);

		for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
			for (GraphicsConfiguration configuration : device.getConfigurations()) {
				sum = sum.union(getUnscaledDimensionOfScreen(configuration));
			}
		}

		return sum;
	}

	private static Rectangle getUnscaledDimensionOfScreen(GraphicsConfiguration configuration) {
		Rectangle r = configuration.getBounds();
		AffineTransform t = configuration.getDefaultTransform();

		return new Rectangle(
				r.x,
				r.y,
				(int)(r.width * t.getScaleX()),
				(int)(r.height * t.getScaleY())
		);
	}
}
