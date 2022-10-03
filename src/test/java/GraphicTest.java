import codedraw.*;
import codedraw.drawing.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphicTest {
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
	public void polygonTest() {
		confirm.setConfirmationDialogue(
				"There should be two polygons of the same shape, one with red circles\n" +
				"at the endpoints. For the two argument fill polygon nothing\n" +
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

		cd.fillPolygon(400, 100, 500, 200);

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
	public void antiAliasingLineWidthTest() {
		confirm.setConfirmationDialogue(
				"The lines and the outlined rectangles should increase in size.\n" +
				" The size of the filled square should stay constant."
		);
		cd.close();
		cd = new CodeDraw(900, 750);
		confirm.placeCodeDrawTestingInstance(cd);

		cd.setAntiAliased(true);
		cd.drawText(50, 10, "Anti Aliasing");
		for (int i = 0; i < 9; i++) {
			cd.setLineWidth(i * 0.5 + 0.5);
			int x = 0;
			int y = 80 * i + 30;

			cd.drawText(x + 10, y + 25, "" + cd.getLineWidth());

			cd.drawLine(x + 50, y, x + 100, y);
			cd.drawLine(x + 50, y, x + 50, y + 50);
			cd.drawLine(x + 50, y, x + 100, y + 50);

			cd.drawSquare(x + 150, y, 50);
			cd.fillSquare(x + 250, y, 50);
		}

		cd.drawText(450, 10, "No Anti Aliasing");
		cd.setAntiAliased(false);
		for (int i = 0; i < 9; i++) {
			cd.setLineWidth(i * 0.5 + 0.5);
			int x = 400;
			int y = 80 * i + 30;

			cd.drawText(x + 10, y + 25, "" + cd.getLineWidth());

			cd.drawLine(x + 50, y, x + 100, y);
			cd.drawLine(x + 50, y, x + 50, y + 50);
			cd.drawLine(x + 50, y, x + 100, y + 50);

			cd.drawSquare(x + 150, y, 50);
			cd.fillSquare(x + 250, y, 50);
		}

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void cornerTest() {
		confirm.setConfirmationDialogue(
				"Sharp corners should of the red dot should be rectangular.\n" +
				"Round corners should form a radius around the red dot.\n" +
				"Bevel corners should cut of at the red dot."
		);
		cd.close();
		cd = new CodeDraw(750, 750);
		confirm.placeCodeDrawTestingInstance(cd);
		TextFormat format = cd.getTextFormat();

		format.setTextOrigin(TextOrigin.BOTTOM_LEFT);

		cd.setAntiAliased(true);
		cd.setColor(Palette.BLACK);
		cd.drawText(100, 50, "Anti Aliased");

		capAndJoin(cd, Corner.SHARP, 100, 100);
		capAndJoin(cd, Corner.ROUND, 100, 300);
		capAndJoin(cd, Corner.BEVEL, 100, 500);

		cd.setAntiAliased(false);
		cd.setColor(Palette.BLACK);
		cd.drawText(400, 50, "Not Anti Aliased");

		capAndJoin(cd, Corner.SHARP, 400, 100);
		capAndJoin(cd, Corner.ROUND, 400, 300);
		capAndJoin(cd, Corner.BEVEL, 400, 500);

		cd.show();
		confirm.assertConfirmation();
	}

	private static void capAndJoin(CodeDraw cd, Corner corner, int x, int y) {
		cd.setCorner(corner);

		cd.setLineWidth(12);
		cd.setColor(Palette.BLACK);
		cd.drawText(x, y - 15, "Corner = " + corner.toString());

		cd.drawLine(x, y, x + 50, y + 50);
		cd.drawTriangle(x, y + 150, x + 25, y + 100, x + 50, y + 150);

		cd.fillSquare(x + 100, y, 50);
		cd.drawSquare(x + 100, y + 100, 50);

		cd.fillTriangle(x + 200, y, x + 200, y + 50, x + 250, y + 50);
		cd.drawTriangle(x + 200, y + 100, x + 200, y + 150, x + 250, y + 150);

		cd.setColor(Palette.RED);
		cd.setLineWidth(2);
		cd.drawRectangle(x - 12, y - 12, 290, 170);

		cd.drawPoint(x, y);
		cd.drawPoint(x + 50, y + 50);

		cd.drawPoint(x, y + 150);
		cd.drawPoint(x + 25, y + 100);
		cd.drawPoint(x + 50, y + 150);
	}

	@Test
	public void clearTest() {
		confirm.setConfirmationDialogue("The entire canvas should be black.");

		cd.setCorner(Corner.BEVEL);
		cd.clear(Palette.BLACK);
		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void transparencyTest() {
		confirm.setConfirmationDialogue(
			"A bear should appear with different colored backgrounds,\n" +
			"each time the bear should have a different interpolation"
		);

		Image bear = Image.fromFile("./src/test/java/player.png");

		cd.setColor(Palette.RED);
		cd.fillSquare(0, 0, 100);
		cd.drawImage(0, 0, 100, 100, bear, Interpolation.NEAREST_NEIGHBOR);
		cd.setColor(Palette.GREEN);
		cd.fillSquare(0, 100, 100);
		cd.drawImage(0, 100, 100, 100, bear, Interpolation.BILINEAR);
		cd.setColor(Palette.BLUE);
		cd.fillSquare(100, 0, 100);
		cd.drawImage(100, 0, 100, 100, bear, Interpolation.BICUBIC);
		cd.setColor(Palette.BLACK);
		cd.fillSquare(100, 100, 100);
		cd.drawImage(100, 100, 100, 100, bear);

		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void transparencyTest2() {
		confirm.setConfirmationDialogue(
			"A red square with a transparent and semi-transparent\n" +
			"section should be drawn over the image"
		);

		Image backgroundImage = Image.fromFile("./src/test/java/test.jpg");
		Image transparentImage = new Image(200, 200, Palette.RED);

		transparentImage.setDrawOver(false);
		transparentImage.setColor(Palette.TRANSPARENT);
		transparentImage.fillSquare(50, 50, 100);
		transparentImage.setColor(Palette.fromBaseColor(Palette.BLACK, 128));
		transparentImage.fillTriangle(50, 50, 50, 150, 150, 50);

		cd.drawImage(0, 0, backgroundImage);
		cd.drawImage(100, 100, transparentImage);

		cd.show();

		confirm.assertConfirmation();
	}
}
