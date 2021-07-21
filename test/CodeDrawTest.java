import codedraw.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CodeDrawTest {
	public static void main(String[] args) {
		//autoCloseTest();
		//framePositionTest();
		//disposeCloseTest();
		//smallWindowTest();
		//imageSaveTest();
		//imageTestScale();
		//imageTest();
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

	private static void disposeCloseTest() {
		CodeDraw l1 = new CodeDraw();
		CodeDraw l2 = new CodeDraw();
		l1.dispose();
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
