package codedraw.drawing;

import java.awt.*;
import java.awt.geom.Path2D;

public class Path {
	Path(Graphics2D g, double startX, double startY, boolean fill) {
		if (g == null) throw new IllegalArgumentException("The parameter g cannot be null.");

		this.g = g;
		this.path = new Path2D.Double();
		this.path.moveTo(startX, startY);
		this.fill = fill;
	}

	private Graphics2D g;
	private Path2D path;
	private boolean fill;

	public Path lineTo(double x, double y) {
		path.lineTo(x, y);
		return this;
	}

	public Path bezierTo(double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		path.curveTo(control1X, control1Y, control2X, control2Y, endX, endY);
		return this;
	}

	public Path curveTo(double controlX, double controlY, double endX, double endY) {
		path.quadTo(controlX, controlY, endX, endY);
		return this;
	}

	public Path arcTo(double centerX, double centerY, double sweepRadians) {
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

	public void complete() {
		path.closePath();
		if (fill) {
			g.fill(path);
		}
		else {
			g.draw(path);
		}
	}

	/*
	 * How to best approximate a geometrical arc with a Bezier curve?
	 * https://stackoverflow.com/a/44829356/7207255
	 */
	private void drawBezierFromArc(double x1, double y1, double xc, double yc, double x4, double y4) {
		double ax = x1 - xc;
		double ay = y1 - yc;
		double bx = x4 - xc;
		double by = y4 - yc;
		double q1 = ax * ax + ay * ay;
		double q2 = q1 + ax * bx + ay * by;
		double k2 = (4D/3) * (Math.sqrt(2 * q1 * q2) - q2) / (ax * by - ay * bx);

		double x2 = xc + ax - k2 * ay;
		double y2 = yc + ay + k2 * ax;
		double x3 = xc + bx + k2 * by;
		double y3 = yc + by - k2 * bx;

		bezierTo(x2, y2, x3, y3, x4, y4);
	}
}
