package codedraw.drawing;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Represents a Path that is used to create custom shapes.
 * Use {@link Image#drawPathStartingAt(double, double)} and {@link Image#fillPathStartingAt(double, double)}
 * to start creating a path.
 * Here is an example on how to draw a section of a dart board.
 * <pre>{@code
 *  double angle = Math.PI / 4; // 45°
 *  cd.fillPathStartingAt(200, 300)
 *      .arcTo(300, 300, angle)
 *      .lineTo(300 - Math.cos(angle) * 200, 300 - Math.sin(angle) * 200)
 *      .arcTo(300, 300, -angle)
 *      .complete();
 * }</pre>
 */
public class Path {
	Path(Image image, Graphics2D g, double startX, double startY, boolean fill) {
		if (g == null) throw new IllegalArgumentException("The parameter g cannot be null.");

		this.image = image;
		this.g = g;
		this.path = new Path2D.Double();
		this.path.moveTo(startX, startY);
		this.fill = fill;
	}

	private Image image;
	private Graphics2D g;
	private Path2D path;
	private boolean fill;

	/**
	 * Gets the current position of the path.
	 * This position changes with each section added to the path.
	 * @return the x position.
	 */
	public double getCurrentX() {
		return path.getCurrentPoint().getX();
	}

	/**
	 * Gets the current position of the path.
	 * This position changes with each section added to the path.
	 * @return the y position.
	 */
	public double getCurrentY() {
		return path.getCurrentPoint().getY();
	}

	/**
	 * Draws a straight line from the current position to the end position.
	 * The underlying image class can be used to change the {@link Image#setLineWidth(double)} and {@link Image#setCorner(Corner)}.
	 * @param endX The distance in pixel from the left side of the canvas to the end of the line.
	 * @param endY The distance in pixel from the top side of the canvas to the end of the line.
	 * @return An instance of this path which can be used to complete the shape.
	 */
	public Path lineTo(double endX, double endY) {
		checkNaNAndInfinity(endX, "endX");
		checkNaNAndInfinity(endY, "endY");

		path.lineTo(endX, endY);
		return this;
	}

	/**
	 * Draws a quadratic Bézier curve. See: <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>.
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The controlX/Y parameter specifies in what way the curve will be bent.
	 * The underlying image class can be used to change the {@link Image#setLineWidth(double)} and {@link Image#setCorner(Corner)}.
	 * @param controlX Defines the way the curve bends in the x direction.
	 * @param controlY Defines the way the curve bends in the y direction.
	 * @param endX The distance in pixel from the left side of the canvas to the end of the curve.
	 * @param endY The distance in pixel from the top side of the canvas to the end of the curve.
	 * @return An instance of this path which can be used to complete the shape.
	 */
	public Path curveTo(double controlX, double controlY, double endX, double endY) {
		checkNaNAndInfinity(controlX, "controlX");
		checkNaNAndInfinity(controlY, "controlY");
		checkNaNAndInfinity(endX, "endX");
		checkNaNAndInfinity(endY, "endY");

		path.quadTo(controlX, controlY, endX, endY);
		return this;
	}

	/**
	 * Draws a cubic Bézier curve. See <a href="https://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wikipedia Bezier Curve</a>
	 * The start and end of the curve will be precisely where startX/Y and endX/Y are specified.
	 * The control1X/Y and control2X/Y parameter specify in what way the curve will be bent.
	 * The underlying image class can be used to change the {@link Image#setLineWidth(double)} and {@link Image#setCorner(Corner)}.
	 * @param control1X Defines the way the curve bends in the x direction.
	 * @param control1Y Defines the way the curve bends in the y direction.
	 * @param control2X Defines the way the curve bends in the x direction.
	 * @param control2Y Defines the way the curve bends in the y direction.
	 * @param endX The distance in pixel from the left side of the canvas to the end of the curve.
	 * @param endY The distance in pixel from the top side of the canvas to the end of the curve.
	 * @return An instance of this path which can be used to complete the shape.
	 */
	public Path bezierTo(double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		checkNaNAndInfinity(control1X, "control1X");
		checkNaNAndInfinity(control1Y, "control1Y");
		checkNaNAndInfinity(control2X, "control2X");
		checkNaNAndInfinity(control2Y, "control2Y");
		checkNaNAndInfinity(endX, "endX");
		checkNaNAndInfinity(endY, "endY");

		path.curveTo(control1X, control1Y, control2X, control2Y, endX, endY);
		return this;
	}

	/**
	 * Draws the outline of an arc with the center being the (centerX, centerY) coordinates.
	 * The total length of the arc is defined by the sweepRadians parameter.
	 * The underlying image class can be used to change the {@link Image#setLineWidth(double)} and {@link Image#setCorner(Corner)}.
	 * @param centerX The distance in pixel from the left side of the canvas to the center of the arc.
	 * @param centerY The distance in pixel from the top side of the canvas to the center of the arc.
	 * @param sweepRadians The length of the arc in radians from the current Position in a clockwise direction.
	 * @return An instance of this path which can be used to complete the shape.
	 */
	public Path arcTo(double centerX, double centerY, double sweepRadians) {
		checkNaNAndInfinity(centerX, "centerX");
		checkNaNAndInfinity(centerY, "centerY");
		checkNaNAndInfinity(sweepRadians, "sweepRadians");

		double sign = Math.signum(sweepRadians);

		if (Math.abs(sweepRadians) > Math.PI / 2) {
			arcTo(centerX, centerY, sign * Math.PI / 2);
			arcTo(centerX, centerY, sweepRadians - sign * Math.PI / 2);
			return this;
		}

		java.awt.geom.Point2D start = path.getCurrentPoint();
		Point2D end = Matrix2D.IDENTITY.rotateAt(centerX, centerY, sweepRadians).multiply(start);
		drawBezierFromArc(start.getX(), start.getY(), centerX, centerY, end.getX(), end.getY());
		return this;
	}

	/*
	 * How to best approximate a geometrical arc with a Bézier curve?
	 * https://stackoverflow.com/a/44829356/7207255
	 */
	private void drawBezierFromArc(double x1, double y1, double xc, double yc, double x4, double y4) {
		double ax = x1 - xc;
		double ay = y1 - yc;
		double bx = x4 - xc;
		double by = y4 - yc;
		double q1 = ax * ax + ay * ay;
		double q2 = q1 + ax * bx + ay * by;
		double k1 = 3 * ax * by - 3 * ay * bx;
		double k2 = k1 == 0 ? 0 : (4 * Math.sqrt(2 * q1 * q2) - 4 * q2) / k1;

		double x2 = xc + ax - k2 * ay;
		double y2 = yc + ay + k2 * ax;
		double x3 = xc + bx + k2 * by;
		double y3 = yc + by - k2 * bx;

		bezierTo(x2, y2, x3, y3, x4, y4);
	}

	/**
	 * Completes the shapes by connecting the start point with the last position of this path.
	 * Complete has to be called to draw the path to the image.
	 */
	public void complete() {
		image.beforeDrawing();
		path.closePath();
		if (fill) {
			g.fill(path);
		}
		else {
			g.draw(path);
		}
		image.afterDrawing();
	}

	@Override
	public String toString() {
		return String.format("pos:[%.2f,%.2f]", getCurrentX(), getCurrentY());
	}

	private static void checkNaNAndInfinity(double parameter, String parameterName) {
		if (Double.isNaN(parameter)) {
			throw new IllegalArgumentException("The parameter '" + parameterName + "' is NaN (not a number).");
		}
		if (Double.isInfinite(parameter)) {
			throw new IllegalArgumentException("The parameter '" + parameterName + "' is infinite.");
		}
	}
}
