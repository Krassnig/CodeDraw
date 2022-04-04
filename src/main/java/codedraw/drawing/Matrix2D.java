package codedraw.drawing;

import java.awt.geom.AffineTransform;
import java.util.Arrays;

public class Matrix2D {
	public static final Matrix2D ZERO = new Matrix2D(
		0, 0, 0,
		0, 0, 0,
		0, 0, 0
	);

	public static final Matrix2D IDENTITY = new Matrix2D(
		1, 0, 0,
		0, 1, 0,
		0, 0, 1
	);

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

	public Matrix2D(double v00, double v01, double v02, double v10, double v11, double v12, double v20, double v21, double v22) {
		this.matrix = new double[][] {
			{ v00, v01, v02 },
			{ v10, v11, v12 },
			{ v20, v21, v22 }
		};
	}

	private Matrix2D(Matrix2D matrix) {
		this(matrix.matrix);
	}

	private final double[][] matrix;

	public double get(int row, int column) {
		if (column < 0 || 3 <= column) throw new IllegalArgumentException();
		if (row < 0 || 3 <= row) throw new IllegalArgumentException();

		return matrix[row][column];
	}

	public Matrix2D set(int row, int column, double value) {
		if (column < 0 || 3 <= column) throw new IllegalArgumentException();
		if (row < 0 || 3 <= row) throw new IllegalArgumentException();

		Matrix2D result = new Matrix2D(this);
		result.matrix[row][column] = value;
		return result;
	}

	public Matrix2D translate(double tx, double ty) {
		return new Matrix2D(
			1, 0, tx,
			0, 1, ty,
			0, 0, 1
		).multiply(this);
	}

	public Matrix2D rotate(double radians) {
		return new Matrix2D(
			Math.cos(radians), Math.sin(radians), 0,
			-Math.sin(radians), Math.cos(radians), 0,
			0, 0, 1
		).multiply(this);
	}

	public Matrix2D rotateAt(double x, double y, double radians) {
		return translate(-x, -y).rotate(radians).translate(x, y);
	}

	public Matrix2D scale(double xScale, double yScale) {
		return new Matrix2D(
			xScale, 0, 0,
			0, yScale, 0,
			0, 0, 1
		).multiply(this);
	}

	public Matrix2D scaleAt(double x, double y, double scaleX, double scaleY) {
		return translate(-x, -y).scale(scaleX, scaleY).translate(x, y);
	}

	public Matrix2D shear(double shearX, double shearY) {
		return new Matrix2D(
			1, shearX, 0,
			shearY, 1, 0,
			0, 0, 1
		).multiply(this);
	}

	public Matrix2D shearAt(double x, double y, double shearX, double shearY) {
		return translate(-x, -y).shear(shearX, shearY).translate(x, y);
	}

	public Matrix2D mirrorX() {
		return new Matrix2D(
			1, 0, 0,
			0, -1, 0,
			0, 0, 1
		).multiply(this);
	}

	public Matrix2D mirrorY() {
		return new Matrix2D(
			-1, 0, 0,
			0, 1, 0,
			0, 0, 1
		).multiply(this);
	}

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

	AffineTransform toAffineTransform() {
		AffineTransform result = new AffineTransform(
			matrix[0][0], matrix[1][0],
			matrix[0][1], matrix[1][1],
			matrix[0][2], matrix[1][2]
		);

		return result;
	}

	@Override
	public String toString() {
		return Arrays.deepToString(matrix);
	}
}
