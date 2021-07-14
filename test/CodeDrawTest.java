import CodeDraw.*;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;

public class CodeDrawTest {
	public static void main(String[] args) {
		//autoCloseTest();
		//triangleTest();
		//framePositionTest();
		lineWidthTest();
		//disposeCloseTest();
		//transparencyTest();
		//smallWindowTest();
		//imageSaveTest();
		//imageTestScale();
		//imageTest();
		//polygonTest();
		//bezierTest();
		//arcTest();
		//twoWindowTest();
		//cornerTest();
		//proofOfConcept();
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

	private static void lineWidthTest() {
		CodeDraw cd = new CodeDraw(900, 900);

		for (int i = 0; i < 8; i++) {
			cd.setLineWidth(i + 1);

			cd.drawText(10, 50 + 100 * i, "" + cd.getLineWidth());
			cd.drawLine(50, 50 + 100 * i, 100, 100 + 100 * i);
			cd.drawSquare(150, 50 + 100 * i, 50);
			cd.fillSquare(250, 50 + 100 * i, 50);
		}

		cd.setLineWidth(50);
		cd.drawSquare(500, 100, 100);
		cd.fillSquare(700, 100, 100);

		cd.setLineWidth(1);
		cd.drawSquare(500, 300, 100);
		cd.fillSquare(700, 300, 100);

		cd.show();
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

		l.setColor(Palette.fromRGBA(Palette.RED, 77));
		l.fillSquare(150, 150, 100);

		l.show();
	}

	private static void smallWindowTest() {
		CodeDraw l = new CodeDraw(150, 1);
		l.show();
	}

	private static void imageSaveTest() {
		CodeDraw w = new CodeDraw();

		w.setColor(Palette.BLUE_VIOLET);

		w.drawArc(200, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(200, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.drawArc(400, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(400, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.setColor(Palette.ORANGE);
		w.drawRectangle(150, 150, 100, 100);

		w.setColor(Palette.RED);
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
				new Point2D.Double(10, 40),
				new Point2D.Double(200, 200),
				new Point2D.Double(100, 36)
		);

		c.show();
	}

	private static void bezierTest() {
		CodeDraw w = new CodeDraw();
		w.drawBezier(100, 100, 300, 200, 200, 300, 400, 400);
		w.show();
	}

	private static void arcTest() {
		CodeDraw w = new CodeDraw();

		w.setColor(Palette.BLUE_VIOLET);

		w.drawArc(200, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(200, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.drawArc(400, 200, 50, 50, 0, Math.PI / 2);
		w.fillArc(400, 400, 50, 50, 0, Math.PI * 3 / 2);

		w.setColor(Palette.ORANGE);
		w.drawRectangle(150, 150, 100, 100);

		w.setColor(Palette.RED);
		w.fillCircle(200, 200, 10);

		w.show();
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

		draw.setColor(Palette.RED);
		draw.fillRectangle(0, 0, size, size);
		draw.fillRectangle(0, draw.getHeight() - size, size, size);
		draw.fillRectangle(draw.getWidth() - size, 0, size, size);
		draw.fillRectangle(draw.getWidth() - size, draw.getHeight() - size, size, size);
		draw.show();
	}

	private static void proofOfConcept() {
		CodeDraw d = new CodeDraw();

		d.setColor(Palette.RED);
		d.fillRectangle(20, 20, 100, 100);

		d.setTitle("Hello World");

		d.setColor(Palette.BLUE);
		d.fillCircle(50, 50, 50);

		d.setColor(Palette.LIGHT_BLUE);
		d.setLineWidth(5);
		d.drawRectangle(30, 30, 200, 200);

		d.show();
	}
}
