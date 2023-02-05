package manual;

import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ShapeTest {
	private CodeDraw cd;
	private CodeDrawConfirmation confirm;

	@Before
	public void beforeEach() {
		confirm = new CodeDrawConfirmation();
		cd = new CodeDraw();
		confirm.placeCodeDrawTestingInstance(cd);
	}

	@After
	public void afterEach() {
		confirm.close();
		cd.close();
	}

	@Test
	public void bezierTest() {
		confirm.setConfirmationDialogue("Curve should move according to the two control points.");

		cd.setLineWidth(4);

		for (int i = 0; i < 400; i++) {
			int c1x = 200;
			int c1y = 100 + i;
			int c2x = 200 + i;
			int c2y = 300;

			cd.clear();

			cd.drawPoint(c1x, c1y);
			cd.drawPoint(c2x, c2y);
			cd.drawBezier(100, 100, c1x, c1y, c2x, c2y, 400, 400);

			cd.show(10);
		}

		confirm.assertConfirmation();
	}

	@Test
	public void arcPieTest() {
		confirm.setConfirmationDialogue(
				"Should draw a pie, the outline of a pie, arc and just lines.\n" +
				"The left ones should look down to the left, the right should start at 12' and go to 9'."
		);
		cd.close();
		cd = new CodeDraw(600, 650);
		confirm.placeCodeDrawTestingInstance(cd);

		double startAngle = 1;
		double startAngle2 = -Math.PI / 2;
		double sweepAngle = 2;
		double sweepAngle2 = Math.PI * 3 / 2;

		cd.setColor(Palette.BLUE_VIOLET);
		cd.fillPie(200, 100, 50, 50, startAngle, sweepAngle);
		cd.fillPie(400, 100, 50, 50, startAngle2, sweepAngle2);

		cd.drawPie(200, 250, 50, 50, startAngle, sweepAngle);
		cd.drawPie(400, 250, 50, 50, startAngle2, sweepAngle2);

		cd.drawArc(200, 400, 50, 50, startAngle, sweepAngle);
		cd.drawArc(400, 400, 50, 50, startAngle2, sweepAngle2);

		cd.drawLine(200, 550, Math.cos(startAngle) * 50 + 200, Math.sin(startAngle) * 50 + 550);
		cd.drawLine(200, 550, Math.cos(startAngle + sweepAngle) * 50 + 200, Math.sin(startAngle + sweepAngle) * 50 + 550);

		cd.drawLine(400, 550, Math.cos(startAngle2) * 50 + 400, Math.sin(startAngle2) * 50 + 550);
		cd.drawLine(400, 550, Math.cos(startAngle2 + sweepAngle2) * 50 + 400, Math.sin(startAngle2 + sweepAngle2) * 50 + 550);

		cd.setColor(Palette.ORANGE);
		cd.drawSquare(150, 50, 100);
		cd.drawSquare(350, 50, 100);

		cd.drawSquare(150, 200, 100);
		cd.drawSquare(350, 200, 100);

		cd.drawSquare(150, 350, 100);
		cd.drawSquare(350, 350, 100);

		cd.drawSquare(150, 500, 100);
		cd.drawSquare(350, 500, 100);

		cd.setColor(Palette.RED);
		cd.setLineWidth(5);
		cd.drawPoint(200, 100);
		cd.drawPoint(400, 100);

		cd.drawPoint(200, 250);
		cd.drawPoint(400, 250);

		cd.drawPoint(200, 400);
		cd.drawPoint(400, 400);

		cd.drawPoint(200, 550);
		cd.drawPoint(400, 550);

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void granularAngleTest() {
		confirm.setConfirmationDialogue("The pie should increase in size smoothly.");
		cd.close();
		cd = new CodeDraw(1000, 1000);
		confirm.placeCodeDrawTestingInstance(cd);

		double tau = Math.PI * 2;
		double steps = tau / (1 << 12);

		for (double i = 0; i < Math.PI / 2; i += steps) {
			cd.clear();

			cd.fillPie(100, 100, 800, 800, 0, i);
			cd.drawArc(100, 100, 850, 850, 0, i);

			cd.show();
		}

		confirm.assertConfirmation();
	}

	@Test
	public void angleArcLineTest() {
		confirm.setConfirmationDialogue(
			"A hand should go around in a circle, follow behind by an 1/8 arc.\n" +
				"While going around in circles an outer arc should demarcate the wandered path."
		);

		for (double i = 0; i < Math.PI * 4; i += Math.PI / 128) {
			cd.clear();
			cd.drawLine(300, 300, 300 + Math.cos(i) * 100, 300 + Math.sin(i) * 100);
			cd.drawArc(300, 300, 100, i, -Math.PI / 4);
			cd.drawArc(300, 300, 110, 0, i % (Math.PI * 2));
			cd.show(20);
		}

		confirm.assertConfirmation();
	}

	@Test
	public void triangleTest() {
		confirm.setConfirmationDialogue("There should be two triangle and their corners should be surrounded by red circles.");

		cd.drawTriangle(100, 100, 200, 300, 50, 220);
		cd.fillTriangle(400, 100, 500, 300, 350, 220);

		cd.setColor(Palette.RED);
		cd.drawCircle(100, 100, 5);
		cd.drawCircle(200, 300, 5);
		cd.drawCircle(50, 220, 5);

		cd.drawCircle(400, 100, 5);
		cd.drawCircle(500, 300, 5);
		cd.drawCircle(350, 220, 5);

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void polygonTest() {
		confirm.setConfirmationDialogue(
				"There should be two polygons of the same shape, one with red circles\n" +
				"at the endpoints. For the two argument fill polygon almost nothing\n" +
				"should be displayed and a line for the draw polygon."
		);

		cd.setColor(Palette.BLACK);
		cd.fillPolygon(
				50, 100,
				240, 200,
				140, 36,
				150, 200,
				55, 60
		);

		cd.fillTriangle(400, 50, 400, 50, 500, 150);
		cd.fillTriangle(400, 100, 400, 101, 500, 200);

		cd.setColor(Palette.RED);
		cd.drawCircle(50, 100, 5);
		cd.drawCircle(240, 200, 5);
		cd.drawCircle(140, 36, 5);
		cd.drawCircle(150, 200, 5);
		cd.drawCircle(55, 60, 5);

		cd.setColor(Palette.BLACK);
		cd.drawPolygon(
				50, 300,
				240, 400,
				140, 236,
				150, 400,
				55, 260
		);

		cd.drawPolygon(400, 300, 500, 400);

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void customShapeTest() {
		confirm.setConfirmationDialogue("Should draw a section of a dartboard on screen.");

		double angle = Math.PI / 4; // 45°
		cd.fillPathStartingAt(200, 300)
			.arcTo(300, 300, angle)
			.lineTo(300 - Math.cos(angle) * 200, 300 - Math.sin(angle) * 200)
			.arcTo(300, 300, -angle)
			.complete();

		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void clearTest() {
		confirm.setConfirmationDialogue("The entire canvas should be black.");

		cd.setCorner(Corner.BEVEL);
		cd.clear(Palette.BLACK);
		cd.show();

		confirm.assertConfirmation();
	}
}
