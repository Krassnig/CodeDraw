import CodeDraw.Palette;
import org.junit.Test;

import java.awt.*;

public class ColorTest {
	@Test
	public void test() {
		Color color = Palette.fromRGBA(10, 20, 30, 40);
		assertEquals(color, 10, 20, 30, 40);
	}

	@Test
	public void test2() {
		Color color = Palette.fromRGB(10, 20, 30);
		assertEquals(color, 10, 20, 30);
	}

	@Test
	public void test3() {
		Color color = Palette.fromRGB(0xAABBCC);
		assertEquals(color, 0xAA, 0xBB, 0xCC);
	}

	@Test
	public void test4() {
		Color color = Palette.fromRGBA(0xFFEEDDCC);
		assertEquals(color, 0xFF, 0xEE, 0xDD, 0xCC);
	}

	@Test
	public void test5() {
		Color color = Palette.fromRGBA(new Color(10, 20, 30), 40);
		assertEquals(color, 10, 20, 30, 40);
	}

	private static void assertEquals(Color color, int red, int green, int blue) {
		assertEquals(color, red, green, blue, 0xFF);
	}

	private static void assertEquals(Color color, int red, int green, int blue, int alpha) {
		assertEquals(color.getRed(), red);
		assertEquals(color.getGreen(), green);
		assertEquals(color.getBlue(), blue);
		assertEquals(color.getAlpha(), alpha);
	}

	private static void assertEquals(int expected, int actual) {
		org.junit.Assert.assertEquals(expected, actual);
	}
}
