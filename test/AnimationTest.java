import codedraw.*;

public class AnimationTest {
	public static void main(String[] args) {
		//transparencyTest();
		//clockTest();
		//sinCosTest();
		//granularAngleTest();
		//arcOriginTest();
		textAnimationTest();
		//animationTest();
	}

	private static void transparencyTest() {
		CodeDraw cd = new CodeDraw();

		for (int i = 0; i < 56; i++) {
			int d = i * 10;
			cd.setColor(Palette.fromBaseColor(Palette.BLUE, 25));
			cd.fillSquare(d, d, 50);

			cd.setColor(Palette.fromBaseColor(Palette.RED, 25));
			cd.fillSquare(d, cd.getHeight() - d - 50, 50);

			cd.show(100);
		}

		cd.show();
	}

	private static void clockTest() {
		CodeDraw cd = new CodeDraw();

		for (double sec = -Math.PI / 2; true; sec += Math.PI / 30) {
			cd.clear();
			cd.drawLine(300, 300, Math.cos(sec) * 100 + 300, Math.sin(sec) * 100 + 300);

			double min = sec / 60 - Math.PI / 2;
			cd.drawLine(300, 300, Math.cos(min) * 70 + 300, Math.sin(min) * 70 + 300);

			for (double j = 0; j < Math.PI * 2; j += Math.PI / 6) {
				cd.fillCircle(Math.cos(j) * 100 + 300, Math.sin(j) * 100 + 300, 4);
			}

			cd.show(1000);
		}
	}


	private static void sinCosTest() {
		CodeDraw cd = new CodeDraw(600, 600);
		int radius = 100;

		for (double i = 0; true; i += Math.PI / 64) {
			cd.clear();

			cd.setColor(Palette.BLACK);
			cd.drawCircle(300, 300, radius);

			cd.setColor(Palette.BLUE);
			int newx = 300 + (int) (radius * Math.sin(-i));
			int newy = 300 + (int) (radius * Math.cos(-i));
			cd.drawLine(300, 300, newx, 300);
			cd.drawLine(newx, 300, newx, newy);

			cd.setColor(Palette.RED);
			cd.drawLine(300, 300, newx, newy);

			cd.show(16);
		}
	}

	private static void granularAngleTest() {
		CodeDraw cd = new CodeDraw(1000, 1000);

		double tau = Math.PI * 2;
		double steps = tau / (1 << 14);

		for (double i = 0; i < Math.PI / 2; i += steps) {
			cd.clear();

			cd.fillPie(100, 100, 800, 800, Math.PI / 2, i);
			cd.drawArc(100, 100, 850, 850, Math.PI / 2, i);

			cd.show();
		}
	}

	private static void arcOriginTest() {
		double tau = Math.PI * 2;

		CodeDraw cd = new CodeDraw();

		double inc = tau / 16;

		for (double i = 0; i < tau; i += inc) {
			cd.fillPie(300, 300, 100, 100, i, inc);
			cd.drawArc(300, 300, 150, 150, i, inc);

			cd.show(200);
		}

		cd.setColor(Palette.RED);
		cd.fillPie(300, 300, 50, 50, -tau / 8, tau / 8);

		cd.show();
	}

	private static void textAnimationTest() {
		CodeDraw cd = new CodeDraw();

		int steps = 5;
		int end = 80;
		int offset = 100;
		int pause = 10;

		while (true) {
			cd.setColor(Palette.randomColor());

			for (int i = 0; i < end; i++) {
				cd.clear();
				cd.drawText(offset + i * steps, offset, "I'm animated!");
				cd.show(pause);
			}
			cd.setColor(Palette.randomColor());

			for (int i = 0; i < end; i++) {
				cd.clear();
				cd.drawText(offset + steps * end - i * steps, offset, "I'm animated!");
				cd.show(pause);
			}
		}
	}

	private static void animationTest() {
		CodeDraw cd = new CodeDraw();

		for (int i = 0; i < 30; i++) {
			cd.clear();

			cd.setColor(Palette.BLACK);
			cd.drawPoint(99, 399);
			cd.drawText(100, 400, "Hello World!");
			cd.fillRectangle(100 + i * 10, 100 + i, 100, 100);
			cd.setColor(Palette.ORANGE);
			cd.fillEllipse(20, 40, 20, 40);
			cd.show(30);
		}
	}

}
