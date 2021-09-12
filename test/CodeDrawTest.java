import codedraw.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class CodeDrawTest {
	public static void main(String[] args) {
		//clearTest();
		//autoCloseTest();
		//windowPositionTest();
		//disposeCloseTest();
		//smallWindowTest();
		//imageSaveTest();
		//imageTestScale();
		//imageTest();
		//twoWindowTest();
		//cornerTest();
		//proofOfConcept();
	}

	private static void clearTest() {
		CodeDraw cd = new CodeDraw();

		cd.setCorner(Corner.Bevel);
		cd.clear(Palette.BLACK);
		cd.show();
	}

	private static void autoCloseTest() {
		CodeDraw cd1 = new CodeDraw();
		CodeDraw cd2 = new CodeDraw();

		cd1.dispose();
		cd2.dispose(false);

		try {
			for (int i = 0; i < 5; i++) {
				Thread.sleep(1000);
				System.out.println("sleeping");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Process should exit now.");
	}

	private static void windowPositionTest() {
		CodeDraw cd = new CodeDraw();
		cd.setWindowPositionX(0);
		cd.setWindowPositionY(0);

		for (int i = 0; i < 60; i++) {
			int pos = i * 10;

			cd.setWindowPositionX(pos);
			cd.setWindowPositionY(pos);

			cd.clear();
			cd.drawSquare(500 - pos, 500 - pos, 100);
			cd.show(100);
		}
	}

	private static void disposeCloseTest() {
		CodeDraw cd1 = new CodeDraw();
		cd1.drawText(300, 300, "I should close.");
		cd1.show();

		CodeDraw cd2 = new CodeDraw();
		cd2.drawText(300, 300, "I should stay open.");
		cd2.show();

		cd1.dispose();
	}

	private static void smallWindowTest() {
		CodeDraw cd = new CodeDraw(150, 1);

		for (int i = 0; i < 150; i++) {
			cd.drawPixel(i, 0);
			cd.show(50);
		}
	}

	private static void imageSaveTest() {
		CodeDraw cd = new CodeDraw();

		cd.drawText(100, 100, "There should be a out.png in your test folder.");
		cd.setColor(Palette.BLUE_VIOLET);

		cd.drawArc(200, 200, 50, 50, 0, Math.PI / 2);
		cd.fillPie(200, 400, 50, 50, 0, Math.PI * 3 / 2);

		cd.drawArc(400, 200, 50, 50, 0, Math.PI / 2);
		cd.fillPie(400, 400, 50, 50, 0, Math.PI * 3 / 2);

		cd.setColor(Palette.ORANGE);
		cd.drawRectangle(150, 150, 100, 100);

		cd.setColor(Palette.RED);
		cd.fillCircle(200, 200, 10);

		try {
			ImageIO.write(cd.saveCanvas(), "png", new File("test/out.png"));
		} catch (IOException e) {
			System.out.println("could not save image");
		}

		cd.show();
	}

	private static void imageTestScale() {
		CodeDraw cd = new CodeDraw();

		cd.drawImage(100, 100, 200, 200, "test/test.jpg");
		cd.show();
	}

	private static void imageTest() {
		CodeDraw cd = new CodeDraw(820, 620);

		cd.drawImage(10, 10, "test/test.jpg");
		cd.show();
	}

	private static void twoWindowTest() {
		CodeDraw cd1 = new CodeDraw();
		CodeDraw cd2 = new CodeDraw();

		cd1.drawCircle(100, 100, 50);
		cd2.drawCircle(400, 200, 100);

		cd1.show();
		cd2.show();
	}

	private static void cornerTest() {
		CodeDraw cd = new CodeDraw();

		int size = 1;

		cd.setColor(Palette.RED);
		cd.fillRectangle(0, 0, size, size);
		cd.fillRectangle(0, cd.getHeight() - size, size, size);
		cd.fillRectangle(cd.getWidth() - size, 0, size, size);
		cd.fillRectangle(cd.getWidth() - size, cd.getHeight() - size, size, size);
		cd.show();
	}

	private static void proofOfConcept() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);
		cd.fillRectangle(20, 20, 100, 100);

		cd.setTitle("Hello World");

		cd.setColor(Palette.BLUE);
		cd.fillCircle(50, 50, 50);

		cd.setColor(Palette.LIGHT_BLUE);
		cd.setLineWidth(5);
		cd.drawRectangle(30, 30, 200, 200);

		cd.show();
	}
}
