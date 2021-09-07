import codedraw.*;
import codedraw.textformat.*;

import java.awt.geom.Point2D;

public class GraphicTest {
	public static void main(String[] args) {
		//arcTest();
		//bezierTest();
		//polygonTest();
		//transparencyTest();
		//triangleTest();
		//antiAliasingTest();
		//lineWidthTest();
		cornerTest();
	}

	private static void arcTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.BLUE_VIOLET);

		cd.drawArc(200, 200, 50, 50, 0, Math.PI / 2);
		cd.fillArc(200, 400, 50, 50, 0, Math.PI * 3 / 2);

		cd.drawArc(400, 200, 50, 50, 0, Math.PI / 2);
		cd.fillArc(400, 400, 50, 50, 0, Math.PI * 3 / 2);

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
				new Point2D.Double(50, 100),
				new Point2D.Double(240, 200),
				new Point2D.Double(140, 36)
		);

		cd.setColor(Palette.RED);
		cd.drawCircle(50, 100, 5);
		cd.drawCircle(240, 200, 5);
		cd.drawCircle(140, 36, 5);

		cd.show();
	}

	private static void transparencyTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.BLUE);
		cd.fillSquare(100, 100, 100);

		cd.setColor(Palette.fromRGBA(Palette.RED, 77));
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
		TextFormat format = cd.getFormat();

		format.setVerticalAlign(VerticalAlign.BOTTOM);
		format.setHorizontalAlign(HorizontalAlign.CENTER);

		cd.isAntiAliased(true);
		cd.drawLine(200, 100, 300, 200);
		cd.drawText(200, 100, "aa");
		cd.drawLine(300, 300, 300, 400);
		cd.drawText(300, 300, "aa");

		cd.isAntiAliased(false);
		cd.drawLine(100, 100, 200, 200);
		cd.drawText(100, 100, "No aa");
		cd.drawLine(200, 300, 200, 400);
		cd.drawText(200, 300, "No aa");

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
		TextFormat format = cd.getFormat();

		format.setVerticalAlign(VerticalAlign.BOTTOM);

		capAndJoin(cd, Corner.Sharp, 100, 100);
		capAndJoin(cd, Corner.Round, 100, 300);
		capAndJoin(cd, Corner.Bevel, 100, 500);

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
		cd.setLineWidth(1);
		cd.drawSquare(x - 5, y - 5, 160);
		cd.fillCircle(x, y, 2);
		cd.fillCircle(x + 50, y + 50, 2);
	}
}
