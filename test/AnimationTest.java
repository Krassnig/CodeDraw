import CodeDraw.*;

public class AnimationTest {
	public static void main(String[] args) {
		//clockTest();
		//sinCosTest();
		//granularAngleTest();
		//arcOriginTest();
		//textAnimationTest();
		//animationTest();
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
		CodeDraw c = new CodeDraw(600, 600);
		int radius = 100;

		for (double i = 0; true; i += Math.PI / 64) {
			c.clear();

			c.setColor(Palette.BLACK);
			c.drawCircle(300, 300, radius);

			c.setColor(Palette.BLUE);
			int newx = 300 + (int) (radius * Math.sin(-i));
			int newy = 300 + (int) (radius * Math.cos(-i));
			c.drawLine(300, 300, newx, 300);
			c.drawLine(newx, 300, newx, newy);

			c.setColor(Palette.RED);
			c.drawLine(300, 300, newx, newy);

			c.show(16);
		}
	}

	private static void granularAngleTest() {
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

	private static void arcOriginTest() {
		double tau = Math.PI * 2;

		CodeDraw c = new CodeDraw();

		double inc = tau / 16;

		for (double i = 0; i < tau; i += inc) {
			c.fillArc(300, 300, 100, 100, i, inc);
			c.drawArc(300, 300, 150, 150, i, inc);

			c.show(200);
		}

		c.setColor(Palette.RED);
		c.fillArc(300, 300, 50, 50, -tau / 8, tau / 8);

		c.show();
	}

	private static void textAnimationTest() {
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

	private static void animationTest() {
		CodeDraw draw = new CodeDraw();

		for (int i = 0; i < 30; i++) {
			draw.clear();

			draw.setColor(Palette.BLACK);
			draw.drawPoint(99, 399);
			draw.drawText(100, 400, "Hello World!");
			draw.fillRectangle(100 + i * 10, 100 + i, 100, 100);
			draw.setColor(Palette.ORANGE);
			draw.fillEllipse(20, 40, 20, 40);
			draw.show(30);
		}
	}

}
