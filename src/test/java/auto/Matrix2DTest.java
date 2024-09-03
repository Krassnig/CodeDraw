package auto;

import codedraw.Matrix2D;
import org.junit.Test;

import static org.junit.Assert.*;

public class Matrix2DTest {
	@Test
	public void matrixTimesInverseIsIdentity() {
		Matrix2D m = Matrix2D.IDENTITY.scale(20, 5).rotateAt(100, 100, -Math.PI / 5).translate(5, -9);
		Matrix2D inv = m.inverse();

		assertTrue(m.multiply(inv).equals(Matrix2D.IDENTITY, 0.001));
		assertTrue(inv.multiply(m).equals(Matrix2D.IDENTITY, 0.001));
	}

	@Test
	public void matrixTimesInverseIsIdentity2() {
		Matrix2D m = Matrix2D.fromRowMajor(
				1, 2, 3,
				-2, 1.5, 0,
				0, 0, 1
		);

		Matrix2D inv = m.inverse();

		assertTrue(m.multiply(inv).equals(Matrix2D.IDENTITY, 0.001));
		assertTrue(inv.multiply(m).equals(Matrix2D.IDENTITY, 0.001));
	}

	@Test
	public void rowMajorIsCorrect() {
		Matrix2D m1 = Matrix2D.fromRowMajor(0, 1, 2, 3, 4, 5, 6, 7, 8);
		Matrix2D m2 = Matrix2D.fromRowMajor(new double[][] {
				{ 0, 1, 2 },
				{ 3, 4, 5 },
				{ 6, 7, 8 }
		});

		Matrix2D expected = Matrix2D.ZERO
				.set(0, 0, 0).set(0, 1, 1).set(0, 2, 2)
				.set(1, 0, 3).set(1, 1, 4).set(1, 2, 5)
				.set(2, 0, 6).set(2, 1, 7).set(2, 2, 8);

		assertEquals(expected, m1);
		assertEquals(expected, m2);
	}

	@Test
	public void columnMajorIsCorrect() {
		Matrix2D m1 = Matrix2D.fromColumnMajor(0, 1, 2, 3, 4, 5, 6, 7, 8);
		Matrix2D m2 = Matrix2D.fromColumnMajor(new double[][] {
				{ 0, 1, 2 },
				{ 3, 4, 5 },
				{ 6, 7, 8 }
		});

		Matrix2D expected = Matrix2D.ZERO
				.set(0, 0, 0).set(0, 1, 3).set(0, 2, 6)
				.set(1, 0, 1).set(1, 1, 4).set(1, 2, 7)
				.set(2, 0, 2).set(2, 1, 5).set(2, 2, 8);

		assertEquals(expected, m1);
		assertEquals(expected, m2);
	}
}
