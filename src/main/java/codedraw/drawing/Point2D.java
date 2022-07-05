package codedraw.drawing;

import java.util.Objects;

/**
 * This class represents a 2D point.
 * It is only used in {@link Matrix2D#multiply(Point2D)} and {@link Matrix2D#multiply(double, double)}.
 */
public class Point2D {
	/**
	 * Creates a Point from the two arguments.
	 * This class is only used in {@link Matrix2D#multiply(Point2D)} and {@link Matrix2D#multiply(double, double)}.
	 * @param x the x component of the point.
	 * @param y the y component of the point.
	 */
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	private double x;
	private double y;

	/**
	 * The x component of the point.
	 * @return the x component.
	 */
	public double getX() {
		return x;
	}

	/**
	 * The y component of the point.
	 * @return the y component.
	 */
	public double getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point2D point2D = (Point2D) o;
		return Double.compare(point2D.x, x) == 0 && Double.compare(point2D.y, y) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		return "[" + x + ", " + y + "]";
	}
}
