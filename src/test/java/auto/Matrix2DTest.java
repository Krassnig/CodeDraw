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
		Matrix2D m = new Matrix2D(
				1, 2, 3,
				-2, 1.5, 0,
				0, 0, 1
		);

		Matrix2D inv = m.inverse();

		assertTrue(m.multiply(inv).equals(Matrix2D.IDENTITY, 0.001));
		assertTrue(inv.multiply(m).equals(Matrix2D.IDENTITY, 0.001));
	}
}
