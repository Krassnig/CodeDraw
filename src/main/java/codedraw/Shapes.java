package codedraw;

import java.awt.*;
import java.awt.geom.*;

class Shapes {
	private Shapes() { }

	public static Ellipse2D point(double x, double y, double radius) {
		return new Ellipse2D.Double(
			x - radius,
			y - radius,
			radius * 2,
			radius * 2
		);
	}

	public static Line2D line(double startX, double startY, double endX, double endY) {
		return new Line2D.Double(
			startX, startY,
			endX, endY
		);
	}

	public static QuadCurve2D curve(double startX, double startY, double controlX, double controlY, double endX, double endY) {
		return new QuadCurve2D.Double(
			startX, startY,
			controlX, controlY,
			endX, endY
		);
	}

	public static CubicCurve2D bezierCurve(double startX, double startY, double control1X, double control1Y, double control2X, double control2Y, double endX, double endY) {
		return new CubicCurve2D.Double(
			startX, startY,
			control1X, control1Y,
			control2X, control2Y,
			endX, endY
		);
	}

	public static Shape rectangle(double x, double y, double width, double height, Corner corner, double cornerRadius) {
		if (corner == Corner.SHARP) {
			return rectangleSharp(x, y, width, height);
		}
		else if (corner == Corner.ROUND) {
			return rectangleRound(x, y, width, height, cornerRadius);
		}
		else {
			return rectangleBevel(x, y, width, height, cornerRadius);
		}
	}

	private static Rectangle2D rectangleSharp(double x, double y, double width, double height) {
		return new Rectangle2D.Double(
			x, y,
			width, height
		);
	}

	private static RoundRectangle2D rectangleRound(double x, double y, double width, double height, double cornerRadius) {
		return new RoundRectangle2D.Double(
			x, y,
			width, height,
			cornerRadius, cornerRadius
		);
	}

	private static Path2D rectangleBevel(double x, double y, double width, double height, double cornerRadius) {
		double cr = cornerRadius;
		return polygon(
			x + cr, y,
			x + width - cr, y,
			x + width, y + cr,
			x + width, y + height - cr,
			x + width - cr, y + height,
			x + cr, y + height,
			x, y + height - cr,
			x, y + cr
		);
	}

	public static Ellipse2D ellipse(double x, double y, double horizontalRadius, double verticalRadius) {
		return new Ellipse2D.Double(
			x - horizontalRadius, y - verticalRadius,
			2 * horizontalRadius, 2 * verticalRadius
		);
	}

	public static Arc2D arc(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		return new Arc2D.Double(
			x - horizontalRadius,
			y - verticalRadius,
			2 * horizontalRadius,
			2 * verticalRadius,
			transformStart(startRadians),
			transformSweep(sweepRadians),
			Arc2D.OPEN
		);
	}

	public static Arc2D pie(double x, double y, double horizontalRadius, double verticalRadius, double startRadians, double sweepRadians) {
		return new Arc2D.Double(
			x - horizontalRadius,
			y - verticalRadius,
			2 * horizontalRadius,
			2 * verticalRadius,
			transformStart(startRadians),
			transformSweep(sweepRadians),
			Arc2D.PIE
		);
	}

	public static Path2D polygon(double... doubles) {
		Path2D.Double result = new Path2D.Double();

		result.moveTo(doubles[0], doubles[1]);
		for (int i = 2; i < doubles.length; i += 2) {
			result.lineTo(doubles[i], doubles[i + 1]);
		}
		result.closePath();

		return result;
	}

	private static double transformStart(double startRadians) {
		return Math.toDegrees(-startRadians);
	}

	private static double transformSweep(double sweepRadians) {
		return Math.toDegrees(-sweepRadians);
	}
}
