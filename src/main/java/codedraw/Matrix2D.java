package codedraw;

import java.awt.geom.AffineTransform;
import java.util.Arrays;

/**
 * This class is used to transform the input coordinates.
 * You can pass matrices with different transformations to {@link CodeDraw#setTransformation(Matrix2D)} and
 * {@link Image#setTransformation(Matrix2D)}.
 * All methods except for the {@link #multiply(Matrix2D)} method apply their operations left-to-right.
 * {@link #multiply(Matrix2D)} applies its operation right-to-left like in normal matrix multiplication.
 */
public class Matrix2D {
	/**
	 * The zero matrix where all values are 0.
	 */
	public static final Matrix2D ZERO = fromRowMajor(
		0, 0, 0,
		0, 0, 0,
		0, 0, 0
	);

	/**
	 * The identity matrix. This matrix would not modify a coordinate system.
	 */
	public static final Matrix2D IDENTITY = fromRowMajor(
		1, 0, 0,
		0, 1, 0,
		0, 0, 1
	);

	/**
	 * Creates a new matrix from a 3x3 row major matrix.
	 * If in doubt you probably want to use row-major as it visually aligns with most examples.
	 * @param matrix 3x3 matrix
	 * @return the matrix.
	 */
	public static Matrix2D fromRowMajor(double[][] matrix) {
		throwIfInvalidMatrix(matrix);
		return new Matrix2D(new double[][] {
				{ matrix[0][0], matrix[0][1], matrix[0][2] },
				{ matrix[1][0], matrix[1][1], matrix[1][2] },
				{ matrix[2][0], matrix[2][1], matrix[2][2] },
		});
	}

	/**
	 * Creates a new matrix from a 3x3 column major matrix.
	 * If in doubt you probably want to use row-major as it visually aligns with most examples.
	 * @param matrix 3x3 matrix
	 * @return the matrix.
	 */
	public static Matrix2D fromColumnMajor(double[][] matrix) {
		throwIfInvalidMatrix(matrix);
		return new Matrix2D(new double[][] {
				{ matrix[0][0], matrix[1][0], matrix[2][0] },
				{ matrix[0][1], matrix[1][1], matrix[2][1] },
				{ matrix[0][2], matrix[1][2], matrix[2][2] },
		});
	}

	/**
	 * Creates a new matrix from a 3x3 row major matrix.
	 * If in doubt you probably want to use row-major as it visually aligns with most examples.
	 * @return the matrix.
	 */
	public static Matrix2D fromRowMajor(double r0c0, double r0c1, double r0c2, double r1c0, double r1c1, double r1c2, double r2c0, double r2c1, double r2c2) {
		return new Matrix2D(new double[][] {
				{ r0c0, r0c1, r0c2 },
				{ r1c0, r1c1, r1c2 },
				{ r2c0, r2c1, r2c2 }
		});
	}

	/**
	 * Creates a new matrix from a 3x3 column major matrix.
	 * If in doubt you probably want to use row-major as it visually aligns with most examples.
	 * @return the matrix.
	 */
	public static Matrix2D fromColumnMajor(double r0c0, double r1c0, double r2c0, double r0c1, double r1c1, double r2c1, double r0c2, double r1c2, double r2c2) {
		return new Matrix2D(new double[][] {
				{ r0c0, r0c1, r0c2 },
				{ r1c0, r1c1, r1c2 },
				{ r2c0, r2c1, r2c2 }
		});
	}

	private static void throwIfInvalidMatrix(double[][] matrix) {
		if (matrix == null) throw new IllegalArgumentException("Argument matrix cannot be null.");
		if (matrix.length != 3) throw new IllegalArgumentException("Argument matrix must be length 3.");

		for (int i = 0; i < 3; i++) {
			if (matrix[i] == null) throw new IllegalArgumentException("Inner array inside matrix cannot be null.");
			if (matrix[i].length != 3) throw new IllegalArgumentException("Inner array inside matrix must be length 3.");
		}
	}

	/**
	 * Creates a new row major matrix from a 3x3 double array.
	 * @param matrix A 3x3 double array.
	 */
	private Matrix2D(double[][] matrix) {
		this.matrix = matrix;
	}

	private final double[][] matrix;

	/**
	 * Gets a value from this matrix.
	 * @param row Can be either 0, 1 or 2.
	 * @param column Can be either 0, 1 or 2.
	 * @return The value.
	 */
	public double get(int row, int column) {
		if (row < 0 || 3 <= row) throw new IllegalArgumentException("Row index can only be 0, 1 or 2.");
		if (column < 0 || 3 <= column) throw new IllegalArgumentException("Column index can only be 0, 1 or 2.");

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
		if (row < 0 || 3 <= row) throw new IllegalArgumentException("Row index can only be 0, 1 or 2.");
		if (column < 0 || 3 <= column) throw new IllegalArgumentException("Column index can only be 0, 1 or 2.");

		Matrix2D result = fromRowMajor(matrix);
		result.matrix[row][column] = value;
		return result;
	}

	/**
	 * Calculates the inverse of this matrix.
	 * A matrix M times its inverse is the {@link #IDENTITY} matrix.
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
		return fromRowMajor(
			1, 0, tx,
			0, 1, ty,
			0, 0, 1
		).multiply(this);
	}

	/**
	 * Rotates the coordinate system at the (0, 0) coordinate.
	 * @param angleRadians Angle in radians. The angle goes counter-clockwise.
	 * @return The rotated matrix.
	 */
	public Matrix2D rotate(double angleRadians) {
		return fromRowMajor(
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
		return fromRowMajor(
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
		return fromRowMajor(
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
	 * Mirrors the coordinate system at the specified coordinate.
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
	 * This multiply method works the same as normal matrix multiplication.
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

	Point2D multiply(java.awt.geom.Point2D point) {
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
	 * Compares this matrix to the matrix given as a parameter.
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
		return fromRowMajor(
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
		// this.matrix is in row major order
		return fromColumnMajor(this.matrix);
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

		return fromRowMajor(
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
		return fromRowMajor(
				+get(0, 0), -get(0, 1), +get(0, 2),
				-get(1, 0), +get(1, 1), -get(1, 2),
				+get(2, 0), -get(2, 1), +get(2, 2)
		);
	}

}
