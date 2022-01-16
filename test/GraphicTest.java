import codedraw.*;
import codedraw.textformat.*;

public class GraphicTest {
	public static void main(String[] args) {
		//patternTest();
		//angleArcLineTest();
		//pieTest();
		//arcTest();
		//bezierTest();
		//polygonTest();
		//transparencyTest();
		//triangleTest();
		//antiAliasingTest();
		//lineWidthTest();
		//cornerTest();
	}

	private static void patternTest() {
		int size = 800;
		CodeDraw cd = new CodeDraw(size, size);

		double r = (size / 2D) * 0.95;
		double c = size / 2D;

		cd.clear(Palette.BLACK);
		cd.setColor(Palette.WHITE);

		cd.drawCircle(c, c, r);

		for (double i = 0, j = Math.PI / 2; true; i += 0.05538459 * 2, j += 0.02598203 * 2) {

			double x1 = c + Math.cos(i) * r;
			double y1 = c + Math.sin(i) * r;

			double x2 = c + Math.cos(j) * r;
			double y2 = c + Math.sin(j) * r;

			cd.drawLine(x1, y1, x2, y2);

			cd.show(20);
		}
	}

	private static void angleArcLineTest() {
		CodeDraw cd = new CodeDraw();

		for (double i = 0; true; i += Math.PI / 128) {
			cd.clear();
			cd.drawLine(300, 300, 300 + Math.cos(i) * 100, 300 + Math.sin(i) * 100);
			cd.drawArc(300, 300, 100, i, -Math.PI / 4);
			cd.drawArc(300, 300, 110, 0, i % (Math.PI * 2));
			cd.show(20);
		}
	}

	private static void pieTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.BLUE_VIOLET);

		cd.drawPie(100, 100, 50, 0, Math.PI / 4);
		cd.drawPie(100, 100, 50, Math.PI / 2, Math.PI);
		cd.fillPie(100, 200, 50, Math.PI / 2, Math.PI);
		cd.fillPie(100, 200, 50, 0, Math.PI / 4);

		cd.show();
	}

	private static void arcTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.BLUE_VIOLET);

		cd.fillPie(200, 200, 50, 50, -Math.PI / 2, Math.PI / 2);
		cd.fillPie(200, 400, 50, 50, -Math.PI / 2, Math.PI * 3 / 2);

		cd.fillPie(400, 200, 50, 50, -Math.PI / 2, Math.PI / 2);
		cd.fillPie(400, 400, 50, 50, -Math.PI / 2, Math.PI * 3 / 2);

		cd.setColor(Palette.ORANGE);
		cd.drawRectangle(150, 150, 100, 100);

		cd.setColor(Palette.RED);
		cd.fillCircle(200, 200, 10);

		cd.show();
	}

	private static void bezierTest() {
		CodeDraw cd = new CodeDraw();

		cd.drawPoint(300, 200);
		cd.drawPoint(200, 300);
		cd.drawBezier(100, 100, 300, 200, 200, 300, 400, 400);

		cd.show();
	}

	private static void polygonTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.BLACK);
		cd.fillPolygon(
				50, 100,
				240, 200,
				140, 36,
				200, 400,
				55, 60
		);

		cd.setColor(Palette.RED);
		cd.drawCircle(50, 100, 5);
		cd.drawCircle(240, 200, 5);
		cd.drawCircle(140, 36, 5);
		cd.drawCircle(200, 400, 5);
		cd.drawCircle(55, 60, 5);


		cd.show();
	}

	private static void transparencyTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.BLUE);
		cd.fillSquare(100, 100, 100);

		cd.setColor(Palette.fromBaseColor(Palette.RED, 77));
		cd.fillSquare(150, 150, 100);

		cd.show();
	}

	private static void triangleTest() {
		CodeDraw cd = new CodeDraw();

		cd.drawTriangle(100, 100, 200, 300, 50, 220);

		cd.setColor(Palette.RED);
		cd.drawCircle(100, 100, 5);
		cd.drawCircle(200, 300, 5);
		cd.drawCircle(50, 220, 5);

		cd.show();
	}

	private static void antiAliasingTest() {
		CodeDraw cd = new CodeDraw();
		TextFormat format = cd.getTextFormat();

		format.setVerticalAlign(VerticalAlign.BOTTOM);
		format.setHorizontalAlign(HorizontalAlign.CENTER);

		cd.setAntiAliased(true);
		cd.drawLine(200, 100, 300, 200);
		cd.drawText(200, 100, "aa");
		cd.drawLine(300, 300, 300, 400);
		cd.drawText(300, 300, "aa");

		cd.setAntiAliased(false);
		cd.drawLine(100, 100, 200, 200);
		cd.drawText(100, 100, "No aa");
		cd.drawLine(200, 300, 200, 400);
		cd.drawText(200, 300, "No aa");

		cd.setAntiAliased(true);

		cd.drawLine(400.00, 100, 400.00, 200);
		cd.drawLine(402.50, 100, 402.50, 200);
		cd.drawLine(405.00, 100, 405.00, 200);
		cd.drawLine(407.50, 100, 407.50, 200);
		cd.drawLine(410.00, 100, 410.00, 200);

		cd.drawRectangle(400.00, 300, 1, 100);
		cd.drawRectangle(402.50, 300, 1, 100);
		cd.drawRectangle(405.00, 300, 1, 100);
		cd.drawRectangle(407.50, 300, 1, 100);
		cd.drawRectangle(410.00, 300, 1, 100);

		cd.drawSquare(10, 10, 10.5);
		cd.drawSquare(30.5, 10.5, 10);
		cd.drawSquare(50.5, 10.5, 10.5);

		cd.show();
	}

	private static void lineWidthTest() {
		CodeDraw cd = new CodeDraw(900, 900);

		for (int i = 0; i < 8; i++) {
			cd.setLineWidth(i * 0.5 + 0.5);

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

	private static void cornerTest() {
		CodeDraw cd = new CodeDraw(750, 750);
		TextFormat format = cd.getTextFormat();

		format.setVerticalAlign(VerticalAlign.BOTTOM);

		capAndJoin(cd, Corner.SHARP, 100, 100);
		capAndJoin(cd, Corner.ROUND, 100, 300);
		capAndJoin(cd, Corner.BEVEL, 100, 500);

		cd.setAntiAliased(false);

		capAndJoin(cd, Corner.SHARP, 400, 100);
		capAndJoin(cd, Corner.ROUND, 400, 300);
		capAndJoin(cd, Corner.BEVEL, 400, 500);

		cd.show();
	}

	private static void capAndJoin(CodeDraw cd, Corner corner, int x, int y) {
		cd.setCorner(corner);

		cd.setLineWidth(8);
		cd.setColor(Palette.BLACK);
		cd.drawText(x, y - 10, corner.toString());
		cd.drawLine(x, y, x + 50, y + 50);
		cd.drawTriangle(x, y + 150, x + 25, y + 100, x + 50, y + 150);
		cd.fillSquare(x + 100, y, 50);
		cd.drawSquare(x + 100, y + 100, 50);

		cd.setColor(Palette.RED);
		cd.setLineWidth(2);
		cd.drawRectangle(x - 5, y - 5, 240, 160);
		cd.drawPoint(x, y);
		cd.drawPoint(x + 50, y + 50);
		cd.drawPoint(x, y + 150);
		cd.drawPoint(x + 25, y + 100);
		cd.drawPoint(x + 50, y + 150);
	}
}
