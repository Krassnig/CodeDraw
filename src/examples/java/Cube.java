import codedraw.*;

public class Cube {
	public static void main(String[] args) {
		CodeDraw cd = new CodeDraw();
		cd.setAlwaysOnTop(true);

		Vector3D[] cube = new Vector3D[8];
		for (int i = 0; i < cube.length; i++) {
			cube[i] = new Vector3D((i >> 2) & 0b1, (i >> 1) & 0b1, (i >> 0) & 0b1);
		}

		for (int i = 0; !cd.isClosed(); i++) {
			Matrix3D transformation = Matrix3D.IDENTITY
					.translate(-0.5, -0.5, -0.5) // center box around 0, 0, 0
					.rotateY((Math.PI * 2 / 512) * (i % 512)) // rotate around 0
					.rotateX((Math.PI * 2 / 512) * (i % 512)) // rotate around 0
					.translate(0, 0, -3) // move towards -z so the camera is able to see the box
					.perspective(Math.PI / 4, (double)cd.getWidth() / cd.getHeight())
					.scale(cd.getWidth() / 2, cd.getHeight() / 2, 1) // scale object to half the screen size -1 to 1
					.translate(cd.getWidth() / 2, cd.getHeight() / 2, 0); // translate object to center of screen

			Vector3D[] cubes = transformation.project(cube);

			cd.clear();
			for (int j : new int[] { 0, 3, 5, 6 }) {
				Vector3D start = cubes[j];

				Vector3D end1 = cubes[j ^ 0b001];
				cd.drawLine(start.getX(), start.getY(), end1.getX(), end1.getY());
				Vector3D end2 = cubes[j ^ 0b010];
				cd.drawLine(start.getX(), start.getY(), end2.getX(), end2.getY());
				Vector3D end3 = cubes[j ^ 0b100];
				cd.drawLine(start.getX(), start.getY(), end3.getX(), end3.getY());
			}

			cd.show(16);
		}
	}

	private static class Vector4D {
		public Vector4D(double x, double y, double z, double w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}

		private double x;
		private double y;
		private double z;
		private double w;

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}

		public double getW() {
			return w;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + ", " + z + ", " + w + "]";
		}
	}

	private static class Vector3D {
		public Vector3D(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		private double x;
		private double y;
		private double z;

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public double getZ() {
			return z;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + ", " + z + "]";
		}
	}

	private static class Matrix3D {
		public static final Matrix3D IDENTITY = fromRowMajor(
				1, 0, 0, 0,
				0, 1, 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1);

		private static Matrix3D fromRowMajor(
				double r0c0, double r0c1, double r0c2, double r0c3,
				double r1c0, double r1c1, double r1c2, double r1c3,
				double r2c0, double r2c1, double r2c2, double r2c3,
				double r3c0, double r3c1, double r3c2, double r3c3) {

			return new Matrix3D(new double[][] {
					{ r0c0, r0c1, r0c2, r0c3 },
					{ r1c0, r1c1, r1c2, r1c3 },
					{ r2c0, r2c1, r2c2, r2c3 },
					{ r3c0, r3c1, r3c2, r3c3 }
			});
		}

		private Matrix3D(double[][] matrix) {
			this.matrix = matrix;
		}

		private final double[][] matrix;

		public double get(int row, int column) {
			if (row < 0 || 4 <= row || column < 0 || 4 <= column) throw new IllegalArgumentException();
			return matrix[row][column];
		}

		private Matrix3D multiply(Matrix3D other) {
			double[][] a = this.matrix;
			double[][] b = other.matrix;

			double[][] result = new double[4][4];

			for (int r = 0; r < 4; r++) {
				for (int c = 0; c < 4; c++) {
					for (int i = 0; i < 4; i++) {
						result[r][c] += a[r][i] * b[i][c];
					}
				}
			}

			return new Matrix3D(result);
		}

		public Matrix3D rotateY(double angleRadians) {
			return fromRowMajor(
					Math.cos(angleRadians), 0, -Math.sin(angleRadians), 0,
					0, 1, 0, 0,
					Math.sin(angleRadians), 0, Math.cos(angleRadians), 0,
					0, 0, 0, 1
			).multiply(this);
		}

		public Matrix3D rotateX(double angleRadians) {
			return fromRowMajor(
					1, 0, 0, 0,
					0, Math.cos(angleRadians), -Math.sin(angleRadians), 0,
					0, Math.sin(angleRadians), Math.cos(angleRadians), 0,
					0, 0, 0, 1
			).multiply(this);
		}

		public Matrix3D translate(double x, double y, double z) {
			return fromRowMajor(
					1, 0, 0, x,
					0, 1, 0, y,
					0, 0, 1, z,
					0, 0, 0, 1
			).multiply(this);
		}

		public Matrix3D scale(double x, double y, double z) {
			return fromRowMajor(
					x, 0, 0, 0,
					0, y, 0, 0,
					0, 0, z, 0,
					0, 0, 0, 1
			).multiply(this);
		}

		public Matrix3D perspective(double fieldOfViewRadians, double aspectRatio) {
			double perspective = 1/(Math.tan(fieldOfViewRadians/2));
			return fromRowMajor(
					perspective/aspectRatio, 0, 0, 0,
					0, perspective, 0, 0,
					0, 0, 1, 0,
					0, 0, 1, 0
			).multiply(this);
		}

		public Vector4D transform(Vector3D vector) {
			double x = vector.getX();
			double y = vector.getY();
			double z = vector.getZ();

			return new Vector4D(
					get(0, 0) * x + get(0, 1) * y + get(0, 2) * z + get(0, 3),
					get(1, 0) * x + get(1, 1) * y + get(1, 2) * z + get(1, 3),
					get(2, 0) * x + get(2, 1) * y + get(2, 2) * z + get(2, 3),
					get(3, 0) * x + get(3, 1) * y + get(3, 2) * z + get(3, 3)
			);
		}

		public Vector3D project(Vector3D vector) {
			Vector4D vector2 = this.transform(vector);
			double w = vector2.getW();
			return new Vector3D(
					w == 0 ? 0 : vector2.getX() / w,
					w == 0 ? 0 : vector2.getY() / w,
					w == 0 ? 0 : vector2.getZ() / w
			);
		}

		public Vector3D[] project(Vector3D... vector) {
			Vector3D[] result = new Vector3D[vector.length];

			for (int i = 0; i < vector.length; i++) {
				result[i] = project(vector[i]);
			}

			return result;
		}
	}
}
