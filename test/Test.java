import CodeDraw.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Test {
	public static void main(String[] args) {
		curveTest();
		//autoCloseTest();
		//triangleTest();
		//framePositionTest();
		//lineSizeTest();
		//disposeCloseTest();
		//transparencyTest();
		//smallWindowTest();
		//sinCosTest();
		//arcAngleTest();
		//arcAnimationTest();
		//fontTest();
		//imageSaveTest();
		//imageTestScale();
		//imageTest();
		//polygonTest();
		//bezierTest();
		//arcTest();
		//animationTest2();
		//twoWindowTest();
		//cornerTest();
		//animationTest();
		//proofOfConcept();
	}

	private static void curveTest() {
		CodeDraw cd = new CodeDraw();

		cd.onMouseMove((c, a) -> {
			c.clear();
			c.drawCurve(200, 200, a.getX(), a.getY(), 400, 400);
			c.show();
		});
	}

	private static void autoCloseTest() {
		CodeDraw cd1 = new CodeDraw();
		CodeDraw cd2 = new CodeDraw();

		cd1.dispose();
		cd2.dispose(false);

		try {
			for (int i = 0; i < 10; i++) {
				Thread.sleep(1000);
				System.out.println("sleeping");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void triangleTest() {
		CodeDraw l = new CodeDraw();

		l.drawTriangle(100, 100, 200, 300, 50, 220);
		l.show();
	}

	private static void framePositionTest() {
		CodeDraw l = new CodeDraw();
		l.setFramePositionX(0);
		l.setFramePositionY(0);

		for (int i = 0; i < 80; i++) {
			int pos = i * 10;

			l.setFramePositionX(pos);
			l.setFramePositionY(pos);

			l.clear();
			l.drawSquare(500 - pos, 500 - pos, 100);
			l.show(100);
		}
	}

	private static void lineSizeTest() {
		CodeDraw l = new CodeDraw();
		l.setLineSize(5);

		l.drawSquare(20, 20, 100);
		l.show();
	}

	private static void disposeCloseTest() {
		CodeDraw l1 = new CodeDraw();
		CodeDraw l2 = new CodeDraw();
		l1.dispose();
	}

	private static void transparencyTest() {
		CodeDraw l = new CodeDraw();

		l.setColor(Palette.BLUE);
		l.fillSquare(100, 100, 100);

		l.setColor(changeAlpha(Palette.RED, 0.3));
		l.fillSquare(150, 150, 100);

		l.show();
	}

	private static Color changeAlpha(Color color, double newAlpha) {
		return new Color(
				color.getRed(),
				color.getGreen(),
				color.getBlue(),
				(int)(newAlpha * 0xFF)
		);
	}

	private static void smallWindowTest() {
		CodeDraw l = new CodeDraw(150, 1);
		l.show();
	}

	private static void sinCosTest() {
		CodeDraw c = new CodeDraw(600, 600);
		int radius = 100;

		for (double i = 0; true; i += Math.PI / 64) {
			c.clear();

			c.setColor(Palette.BLACK);
			c.drawCircle(300, 300, radius);

			c.setColor(Palette.BLUE);
			int newx = 300 + (int)(radius * Math.sin(-i));
			int newy = 300 + (int)(radius * Math.cos(-i));
			c.drawLine(300, 300, newx, 300);
			c.drawLine(newx, 300, newx, newy);

			c.setColor(Palette.RED);
			c.drawLine(300, 300, newx, newy);

			c.show(16);
		}
	}

	private static void arcAngleTest() {
		CodeDraw c = new CodeDraw(1000, 1000);

		double tau = Math.PI * 2;
		double steps = tau / (1 << 14);

		for (double i = 0; i < Math.PI / 2; i += steps) {
			c.clear();

			c.fillArc(100, 100, 800, 800, Math.PI / 2, i);
			c.drawArc(100, 100, 850, 850, Math.PI / 2, i);

			c.show();
		}

		c.show();
	}

	private static void arcAnimationTest() {
		double tau = Math.PI * 2;

		CodeDraw c = new CodeDraw();

		double inc = tau / 16;

		for (double i = 0; i < tau; i += inc)
		{
			c.fillArc(300, 300, 100, 100, i, inc);
			c.drawArc(300, 300, 150, 150, i, inc);

			c.show(200);
		}

		c.setColor(Color.RED);
		c.fillArc(300, 300, 50, 50, -tau / 8, tau / 8);

		c.show();
	}

	private static void fontTest() {
		CodeDraw cd = new CodeDraw();
		cd.setFont(new Font("Arial", Font.BOLD, 20));
		cd.drawText(200, 200, "MY BOLD TEXT!");
		cd.show();
	}

	private static void imageSaveTest() {
		CodeDraw w = new CodeDraw();

		w.setColor(Palette.BLUE_VIOLET);

		w.drawArc(200, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(200, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.drawArc(400, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(400, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.setColor(Color.ORANGE);
		w.drawRectangle(150, 150, 100, 100);

		w.setColor(Color.RED);
		w.fillCircle(200, 200, 10);

		try {
			ImageIO.write(w.asImage(), "png", new File("test/out.png"));
		} catch (IOException e) {
			System.out.println("could not save image");
		}

		w.show();
	}

	private static void imageTestScale() {
		CodeDraw c = new CodeDraw();

		try {
			c.drawImage(100, 100, 200, 200, ImageIO.read(new File("test/test.jpg")));
		} catch (IOException e) {
			System.out.println("Could not load file");
		}

		c.show();
	}

	private static void imageTest() {
		CodeDraw c = new CodeDraw(820, 620);

		try {
			c.drawImage(10, 10, ImageIO.read(new File("test/test.jpg")));
		} catch (IOException e) {
			System.out.println("Could not load file");
		}

		c.show();
	}

	private static void polygonTest() {
		CodeDraw c = new CodeDraw();

		c.fillPolygon(
				new Point(10, 40),
				new Point(200, 200),
				new Point(100, 36)
		);

		c.show();
	}

	private static void bezierTest() {
		CodeDraw w = new CodeDraw();
		w.drawBezier(new Point(100, 100), new Point(300, 200), new Point(200, 300), new Point(400, 400));
		w.show();
	}

	private static void arcTest() {
		CodeDraw w = new CodeDraw();

		w.setColor(Palette.BLUE_VIOLET);

		w.drawArc(200, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(200, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.drawArc(400, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(400, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.setColor(Color.ORANGE);
		w.drawRectangle(150, 150, 100, 100);

		w.setColor(Color.red);
		w.fillCircle(200, 200, 10);

		w.show();
	}

	private static void animationTest2() {
		CodeDraw w = new CodeDraw();

		int steps = 5;
		int end = 80;
		int offset = 100;
		int pause = 10;

		while (true) {
			for (int i = 0; i < end; i++) {
				w.clear();
				w.drawText(offset + i * steps, offset, "I'm animated!");
				w.show(pause);
			}

			for (int i = 0; i < end; i++) {
				w.clear();
				w.drawText(offset + steps * end - i * steps, offset, "I'm animated!");
				w.show(pause);
			}
		}
	}

	private static void twoWindowTest() {
		CodeDraw w1 = new CodeDraw();
		CodeDraw w2 = new CodeDraw();

		w1.drawCircle(100, 100, 50);
		w2.drawCircle(400, 200, 100);

		w1.show();
		w2.show();
	}

	private static void cornerTest() {
		CodeDraw draw = new CodeDraw();

		int size = 1;

		draw.setColor(Color.RED);
		draw.fillRectangle(0, 0, size, size);
		draw.fillRectangle(0, draw.getHeight() - size, size, size);
		draw.fillRectangle(draw.getWidth() - size, 0, size, size);
		draw.fillRectangle(draw.getWidth() - size, draw.getHeight() - size, size, size);
		draw.show();
	}

	private static void animationTest() {
		CodeDraw draw = new CodeDraw();

		for (int i = 0; i < 30; i++)
		{
			draw.clear();

			draw.setColor(Color.BLACK);
			draw.drawPoint(99, 399);
			draw.drawText(100, 400, "Hello World!");
			draw.fillRectangle(100 + i * 10, 100 + i, 100, 100);
			draw.setColor(Color.ORANGE);
			draw.fillEllipse(20, 40, 20, 40);
			draw.show(30);
		}
	}

	private static void proofOfConcept() {
		CodeDraw d = new CodeDraw();

		d.setColor(Color.RED);
		d.fillRectangle(20, 20, 100, 100);

		d.setTitle("Hello World");

		d.setColor(Color.BLUE);
		d.fillCircle(50, 50, 50);

		d.setColor(Palette.LIGHT_BLUE);
		d.setLineSize(5);
		d.drawRectangle(30, 30, 200, 200);

		d.show();
	}
}
