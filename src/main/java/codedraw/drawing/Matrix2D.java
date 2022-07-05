package codedraw.drawing;

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import codedraw.CodeDraw;

/**
 * This class is used to transform the input coordinates given to {@link CodeDraw} and {@link Canvas}.
 * You can pass matrices with different transformations to {@link CodeDraw#setTransformation(Matrix2D)} and
 * {@link Canvas#setTransformation(Matrix2D)}.
 * All methods except for the {@link #multiply(Matrix2D)} method apply their operations left-to-right.
 * {@link #multiply(Matrix2D)} works like normal matrix-matrix multiplication right-to-left.
 */
public class Matrix2D {
	/**
	 * The zero matrix where all values are 0.
	 */
	public static final Matrix2D ZERO = new Matrix2D(
		0, 0, 0,
		0, 0, 0,
		0, 0, 0
	);

	/**
	 * The identity matrix. This matrix would not modify a coordinate system.
	 */
	public static final Matrix2D IDENTITY = new Matrix2D(
		1, 0, 0,
		0, 1, 0,
		0, 0, 1
	);

	/**
	 * Creates a new matrix from a 3x3 double array.
	 * @param matrix A 3x3 double array.
	 */
	public Matrix2D(double[][] matrix) {
		if (matrix == null) throw new NullPointerException();
		if (matrix.length != 3) throw new IllegalArgumentException();

		this.matrix = new double[3][3];

		for (int r = 0; r < 3; r++) {
			double[] row = matrix[r];
			if (row == null) throw new NullPointerException();
			if (row.length != 3) throw new IllegalArgumentException();

			for (int c = 0; c < 3; c++) {
				this.matrix[r][c] = row[c];
			}
		}
	}

	/**
	 * Creates a new matrix from the values provided.
	 * @param v00 The value at position (0, 0).
	 * @param v01 The value at position (0, 1).
	 * @param v02 The value at position (0, 2).
	 * @param v10 The value at position (1, 0).
	 * @param v11 The value at position (1, 1).
	 * @param v12 The value at position (1, 2).
	 * @param v20 The value at position (2, 0).
	 * @param v21 The value at position (2, 1).
	 * @param v22 The value at position (2, 2).
	 */
	public Matrix2D(double v00, double v01, double v02, double v10, double v11, double v12, double v20, double v21, double v22) {
		this.matrix = new double[][] {
			{ v00, v01, v02 },
			{ v10, v11, v12 },
			{ v20, v21, v22 }
		};
	}

	private final double[][] matrix;

	/**
	 * Gets a value from this matrix.
	 * @param row Can be either 0, 1 or 2.
	 * @param column Can be either 0, 1 or 2.
	 * @return The value.
	 */
	public double get(int row, int column) {
		if (row < 0 || 3 <= row) throw new IllegalArgumentException();
		if (column < 0 || 3 <= column) throw new IllegalArgumentException();

		return matrix[row][column];
	}

	/**
	 * Creates a new matrix and changes the value in the matrix at the specified location.
	 * @param row Can be either 0, 1 or 2.
	 * @param column Can be either 0, 1 or 2.
	 * @param value The value to be set at the specified location.
	 * @return The new matrix with the changed value.
	 */
	public Matrix2D set(int row, int column, double value) {
		if (row < 0 || 3 <= row) throw new IllegalArgumentException();
		if (column < 0 || 3 <= column) throw new IllegalArgumentException();

		Matrix2D result = new Matrix2D(matrix);
		result.matrix[row][column] = value;
		return result;
	}

	/**
	 * Calculates the inverse of this matrix.
	 * A matrix m times its inverse is the {@link #IDENTITY} matrix.
	 * @return The inverse of this matrix.
	 */
	public Matrix2D inverse() {
		return adjunct().divide(determinant());
	}

	/**
	 * Moves the coordinate system in the x and y direction.
	 * @param tx The change in the x direction.
	 * @param ty The change in the y direction.
	 * @return A new matrix with the translated coordinate system.
	 */
	public Matrix2D translate(double tx, double ty) {
		return new Matrix2D(
			1, 0, tx,
			0, 1, ty,
			0, 0, 1
		).multiply(this);
	}

	/**
	 * Rotates the matrix clockwise at the (0, 0) coordinate.
	 * @param angleRadians Angle in radians. The angle goes counter-clockwise.
	 * @return The rotated matrix.
	 */
	public Matrix2D rotate(double angleRadians) {
		return new Matrix2D(
			Math.cos(angleRadians), -Math.sin(angleRadians), 0,
			Math.sin(angleRadians), Math.cos(angleRadians), 0,
			0, 0, 1
		).multiply(this);
	}

	/**
	 * Rotates the coordinate system at the specified coordinate.
	 * @param x Coordinate to rotate around.
	 * @param y Coordinate to rotate around.
	 * @param radians Angle in radians.
	 * @return The rotated matrix.
	 */
	public Matrix2D rotateAt(double x, double y, double radians) {
		return translate(-x, -y).rotate(radians).translate(x, y);
	}

	/**
	 * Scales the coordinate system at the (0, 0) coordinate.
	 * @param xScale The scale in the x direction.
	 * @param yScale The scale in the y direction.
	 * @return A new matrix with the scaled coordinate system.
	 */
	public Matrix2D scale(double xScale, double yScale) {
		return new Matrix2D(
			xScale, 0, 0,
			0, yScale, 0,
			0, 0, 1
		).multiply(this);
	}

	/**
	 * Scales the coordinate system from the specified coordinates.
	 * @param x The coordinate to scale from.
	 * @param y The coordinate to scale from.
	 * @param scaleX The scale in the x direction.
	 * @param scaleY The scale in the y direction.
	 * @return A new matrix with the scaled coordinate system.
	 */
	public Matrix2D scaleAt(double x, double y, double scaleX, double scaleY) {
		return translate(-x, -y).scale(scaleX, scaleY).translate(x, y);
	}

	/**
	 * Shears the coordinate system from the (0, 0) coordinate.
	 * @param shearX The shear in the x direction.
	 * @param shearY The shear in the y direction.
	 * @return A new matrix with the sheared coordinate system.
	 */
	public Matrix2D shear(double shearX, double shearY) {
		return new Matrix2D(
			1, shearX, 0,
			shearY, 1, 0,
			0, 0, 1
		).multiply(this);
	}

	/**
	 * Shears the coordinate system at the specified coordinate.
	 * @param x The coordinate to shear from.
	 * @param y The coordinate to shear from.
	 * @param shearX The shear in the x direction.
	 * @param shearY The shear in the y direction.
	 * @return A new matrix with the sheared coordinate system.
	 */
	public Matrix2D shearAt(double x, double y, double shearX, double shearY) {
		return translate(-x, -y).shear(shearX, shearY).translate(x, y);
	}

	/**
	 * Mirrors the coordinate system at the (0, 0) coordinate.
	 * An angle with the value 0 would mirror the coordinate system along the x-axis.
	 * An angle with the value Math.PI / 2 would mirror the coordinate system along the y-axis.
	 * @param angleRadians The angle which represents an infinite line that passes through the (0, 0) coordinate.
	 * @return A new matrix with the mirrored coordinate system.
	 */
	public Matrix2D mirror(double angleRadians) {
		return rotate(-angleRadians).scale(1, -1).rotate(angleRadians);
	}

	/**
	 * Mirrors the coordinate system at the (0, 0) coordinate.
	 * An angle with the value 0 would mirror the coordinate system along the x-axis.
	 * An angle with the value Math.PI / 2 would mirror the coordinate system along the y-axis.
	 * @param x The x coordinate where the mirror line goes through.
	 * @param y The y coordinate where the mirror line goes through.
	 * @param angleRadians The angle which represents an infinite line that passes through the specified coordinate.
	 * @return A new matrix with the mirrored coordinate system.
	 */
	public Matrix2D mirrorAt(double x, double y, double angleRadians) {
		return translate(-x, -y).mirror(angleRadians).translate(x, y);
	}

	/**
	 * Multiplies two matrices with each other.
	 * This multiply method works the same as normal matrix-matrix multiplication.
	 * This means that the operational order would be right-to-left.
	 * @param other The other matrix to multiply this matrix with.
	 * @return The multiplied matrix as the result.
	 */
	public Matrix2D multiply(Matrix2D other) {
		double[][] a = this.matrix;
		double[][] b = other.matrix;

		double[][] result = new double[3][3];

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				for (int i = 0; i < 3; i++) {
					result[r][c] += a[r][i] * b[i][c];
				}
			}
		}

		return new Matrix2D(result);
	}

	/**
	 * Transforms the point given as a coordinate according to this matrix.
	 * @param point A point.
	 * @return The transformed point.
	 */
	public Point2D multiply(Point2D point) {
		return multiply(point.getX(), point.getY());
	}

	/**
	 * Transforms the point given as a coordinate according to this matrix.
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 * @return The transformed point.
	 */
	public Point2D multiply(double x, double y) {
		return new Point2D(
			get(0, 0) * x + get(0, 1) * y + get(0, 2),
			get(1, 0) * x + get(1, 1) * y + get(1, 2)
		);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Matrix2D matrix2D = (Matrix2D) o;
		return Arrays.deepEquals(matrix, matrix2D.matrix);
	}

	/**
	 * Compares both this matrix and the other matrix.
	 * The error allows for some difference between their values.
	 * For example if the first value of this matrix is 1 and the first values of the other matrix is 0.999998
	 * and an error of 0.0001 is given then this function will return true.
	 * {@link #equals(Object)} would return false.
	 * However, if a value of this matrix is 1 and the same value is 2 in the other matrix and the error is 0.0001
	 * this function and {@link #equals(Object)} would both return false.
	 * @param other The matrix to compare with.
	 * @param error The maximum difference allowed by the values in the matrix.
	 * @return Whether these two matrices are the same within some error.
	 */
	public boolean equals(Matrix2D other, double error) {
		return this.minus(other).absolute().allLessThanOrEqual(error);
	}

	@Override
	public int hashCode() {
		return Arrays.deepHashCode(matrix);
	}

	@Override
	public String toString() {
		return Arrays.deepToString(matrix);
	}

	private Matrix2D minus(Matrix2D other) {
		double[][] a = this.matrix;
		double[][] b = other.matrix;

		double[][] result = new double[3][3];

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				result[r][c] = a[r][c] - b[r][c];
			}
		}

		return new Matrix2D(result);
	}

	AffineTransform toAffineTransform() {
		return new AffineTransform(
				get(0, 0), get(1, 0),
				get(0, 1), get(1, 1),
				get(0, 2), get(1, 2)
		);
	}

	private Matrix2D absolute() {
		double[][] a = this.matrix;
		double[][] result = new double[3][3];

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				result[r][c] = Math.abs(a[r][c]);
			}
		}

		return new Matrix2D(result);
	}

	private boolean allLessThanOrEqual(double value) {
		double[][] a = this.matrix;

		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (a[r][c] > value) {
					return false;
				}
			}
		}

		return true;
	}

	private Matrix2D divide(double divisor) {
		return new Matrix2D(
			get(0, 0) / divisor, get(0, 1) / divisor, get(0, 2) / divisor,
			get(1, 0) / divisor, get(1, 1) / divisor, get(1, 2) / divisor,
			get(2, 0) / divisor, get(2, 1) / divisor, get(2, 2) / divisor
		);
	}

	private double determinant() {
		double m00 = get(0, 0);
		double m01 = get(0, 1);
		double m02 = get(0, 2);
		double m10 = get(1, 0);
		double m11 = get(1, 1);
		double m12 = get(1, 2);
		double m20 = get(2, 0);
		double m21 = get(2, 1);
		double m22 = get(2, 2);

		return m00 * m11 * m22 + m01 * m12 * m20 + m02 * m10 * m21 - m00 * m12 * m21 - m01 * m10 * m22 - m02 * m11 * m20;
	}

	private Matrix2D adjunct() {
		return minor().cofactor().transpose();
	}

	private Matrix2D transpose() {
		return new Matrix2D(
				get(0, 0), get(1, 0), get(2, 0),
				get(0, 1), get(1, 1), get(2, 1),
				get(0, 2), get(1, 2), get(2, 2)
		);
	}

	private Matrix2D minor() {
		double m00 = get(0, 0);
		double m01 = get(0, 1);
		double m02 = get(0, 2);
		double m10 = get(1, 0);
		double m11 = get(1, 1);
		double m12 = get(1, 2);
		double m20 = get(2, 0);
		double m21 = get(2, 1);
		double m22 = get(2, 2);

		return new Matrix2D(
			m11 * m22 - m21 * m12,
			m10 * m22 - m20 * m12,
			m10 * m21 - m20 * m11,

			m01 * m22 - m21 * m02,
			m00 * m22 - m20 * m02,
			m00 * m21 - m20 * m01,

			m01 * m12 - m11 * m02,
			m00 * m12 - m10 * m02,
			m00 * m11 - m10 * m01
		);
	}

	private Matrix2D cofactor() {
		return new Matrix2D(
				+get(0, 0), -get(0, 1), +get(0, 2),
				-get(1, 0), +get(1, 1), -get(1, 2),
				+get(2, 0), -get(2, 1), +get(2, 2)
		);
	}

}
