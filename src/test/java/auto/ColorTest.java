package auto;

import codedraw.Palette;
import org.junit.Test;

import java.awt.*;

public class ColorTest {
	@Test
	public void testRGBA() {
		Color color = Palette.fromRGBA(10, 20, 30, 40);
		assertEquals(color, 10, 20, 30, 40);
	}

	@Test
	public void testRGB() {
		Color color = Palette.fromRGB(10, 20, 30);
		assertEquals(color, 10, 20, 30);
	}

	@Test
	public void testRGBBit() {
		Color color = Palette.fromRGB(0xAABBCC);
		assertEquals(color, 0xAA, 0xBB, 0xCC);
	}

	@Test
	public void testRGBABit() {
		Color color = Palette.fromRGBA(0xFFEEDDCC);
		assertEquals(color, 0xFF, 0xEE, 0xDD, 0xCC);
	}

	@Test
	public void testRGBABaseColor() {
		Color color = Palette.fromBaseColor(new Color(10, 20, 30), 40);
		assertEquals(color, 10, 20, 30, 40);
	}

	@Test
	public void testGrayscale() {
		Color color = Palette.fromGrayscale(100);
		assertEquals(color, 100, 100, 100, 255);
	}

	@Test
	public void testHSVMin() {
		Color color = Palette.fromHSV(0, 0, 0);
		assertEquals(color, 0, 0, 0);
	}

	@Test
	public void testHSVMax() {
		Color color = Palette.fromHSV(360, 100, 100);
		assertEquals(color, 255, 0, 0);
	}

	@Test
	public void testHSV() {
		Color color = Palette.fromHSV(262, 29, 51);
		assertEquals(color, 106, 92, 130);
	}

	@Test
	public void testHSV2() {
		Color color = Palette.fromHSV(147, 35, 51);
		assertEquals(color, 84, 130, 105);
	}

	@Test
	public void testHSV3() {
		Color color = Palette.fromHSV(250, 88, 4);
		assertEquals(color, 2, 1, 10);
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
