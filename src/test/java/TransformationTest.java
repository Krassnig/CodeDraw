import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransformationTest {
	private CodeDrawConfirmation confirm;
	private CodeDraw cd;

	@Before
	public void beforeEach() {
		confirm = new CodeDrawConfirmation();
		cd = new CodeDraw();
		confirm.placeCodeDrawTestingInstance(cd);
	}

	@After
	public void afterEach() {
		cd.close();
		confirm.close();
	}

	@Test
	public void imageAndTextTransformationTest() {
		confirm.setConfirmationDialogue("The image should look like a diamond and be turned to look to the right.");
		cd.getTextFormat().setTextOrigin(TextOrigin.CENTER_LEFT);


		cd.drawImage(0, 0, 200, 200, Image.fromFile("./src/test/java/test.jpg"));
		cd.setColor(Palette.BLACK);
		cd.fillRectangle(0, 50, 100, 50);
		cd.setColor(Palette.WHITE);
		cd.drawText(0, 75, "Hello World!");

		cd.setTransformation(Matrix2D.IDENTITY.shearAt(200, 200, 0.5, 0.5).rotateAt(200, 200, -Math.PI / 4));

		cd.drawImage(200, 200, 200, 200, Image.fromFile("./src/test/java/test.jpg"));
		cd.setColor(Palette.BLACK);
		cd.fillRectangle(200, 250, 100, 50);
		cd.setColor(Palette.WHITE);
		cd.drawText(200, 275, "Hello World!");

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void mirrorTest() {
		confirm.setConfirmationDialogue(
				"The blue line should turn clockwise.\n" +
				"The red dot and the black rectangle should be mirrored along that blue line.\n" +
				"The mirror image should have an orange dot and a gray rectangle."
		);

		cd.setLineWidth(2);

		for (int i = 0; i <= 512; i++) {
			double angle = Math.PI * i / 512D;
			cd.clear();
			cd.setTransformationToIdentity();

			cd.setColor(Palette.RED);
			cd.fillCircle(400, 400, 100);
			cd.setColor(Palette.BLACK);
			cd.drawSquare(300, 300, 200);
			cd.drawLine(0, 300, 600, 300);

			cd.setColor(Palette.BLUE);
			cd.drawLine(300 + Math.cos(angle) * 600, 300 + Math.sin(angle) * 600, 300 - Math.cos(angle) * 600, 300 - Math.sin(angle) * 600);

			cd.setTransformation(Matrix2D.IDENTITY.mirrorAt(300, 300, angle));

			cd.setColor(Palette.ORANGE);
			cd.fillCircle(400, 400, 100);
			cd.setColor(Palette.DARK_GRAY);
			cd.drawSquare(300, 300, 200);
			cd.drawLine(0, 300, 600, 300);

			cd.show(16);
		}

		confirm.assertConfirmation();
	}

	@Test
	public void operationTest() {
		confirm.setConfirmationDialogue(
				"The face of the chicken should be moved to the right,\n" +
				" the lower part rotated clockwise.\n" +
				"The face of the kitten should be mirrored in place."
		);

		cd.getTextFormat().setTextOrigin(TextOrigin.BOTTOM_LEFT);
		cd.setColor(Palette.RED);

		Image image = Image.fromFile("./src/test/java/test.jpg");
		cd.drawImage(0, 0, image);

		cd.setColor(Palette.WHITE);
		cd.fillSquare(100, 100, 100);
		cd.setColor(Palette.RED);
		cd.drawText(100, 100, "moved to right by 100px");
		Image crop = Image.crop(image, 100, 100, 100, 100);
		cd.drawImage(200, 100, crop);

		cd.drawText(100, 200, "rotated clockwise");
		Image rotate = Image.rotateClockwise(Image.crop(image, 100, 200, 100, 100));
		cd.drawImage(100, 200, rotate);

		cd.drawText(300, 200, "mirrored vertically");
		Image mirror = Image.mirrorVertically(Image.crop(image, 300, 200, 100, 100));
		cd.drawImage(300, 200, mirror);

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void testPath() {
		confirm.setConfirmationDialogue(
			"Should fill out a custom shape with a movable curve.\n" +
			"Press a mouse button to complete the test."
		);

		EventScanner es = cd.getEventScanner();
		int mouseX = 300;
		int mouseY = 300;

		testComplete: while (!cd.isClosed()) {
			while (es.hasEventNow()) {
				if (es.hasMouseMoveEvent()) {
					MouseMoveEvent e = es.nextMouseMoveEvent();
					mouseX = e.getX();
					mouseY = e.getY();
				}
				else if (es.hasMouseUpEvent()) {
					es.nextEvent();
					break testComplete;
				}
				else {
					es.nextEvent();
				}
			}

			double control1X = mouseX;
			double control1Y = mouseY;
			double control2X = 100;
			double control2Y = 50;

			cd.clear();

			cd.fillPathStartingAt(300, 300).bezierTo(control1X, control1Y, control2X, control2Y, 0, 0).complete();
			cd.fillCircle(control1X, control1Y, 5);
			cd.fillCircle(control2X, control2Y, 5);

			cd.show(16);
		}

		confirm.assertConfirmation();
	}

	@Test
	public void testPath2() {
		confirm.setConfirmationDialogue(
			"Should draw a filled shape with an arc.\n" +
			"The length of the arc can be modified with the mouse wheel.\n" +
			"Click q mouse button to complete the test."
		);

		EventScanner es = cd.getEventScanner();
		int mouseX = 300;
		int mouseY = 300;
		double wheel = 5;

		testComplete: while (!cd.isClosed()) {
			while (es.hasEventNow()) {
				if (es.hasMouseMoveEvent()) {
					MouseMoveEvent e = es.nextMouseMoveEvent();
					mouseX = e.getX();
					mouseY = e.getY();
				}
				else if (es.hasMouseWheelEvent()) {
					MouseWheelEvent e = es.nextMouseWheelEvent();
					wheel += e.getWheelRotation();
				}
				else if (es.hasMouseUpEvent()) {
					es.nextEvent();
					break testComplete;
				}
				else {
					es.nextEvent();
				}
			}

			double sweep = Math.PI / 16 * wheel;

			double startX = 300;
			double startY = 300;
			double centerX = mouseX;
			double centerY = mouseY;
			Point2D end = Matrix2D.IDENTITY.rotateAt(centerX, centerY, sweep).multiply(startX, startY);
			double endX = end.getX();
			double endY = end.getY();

			cd.clear();

			cd.setColor(Palette.BLACK);
			cd.fillPathStartingAt(startX, startY).arcTo(centerX, centerY, sweep).lineTo(100, 100).lineTo(400, 100).complete();

			cd.setColor(Palette.GREEN);
			cd.fillCircle(startX, startY, 5);
			cd.fillCircle(centerX, centerY, 5);
			cd.fillCircle(endX, endY, 5);

			double xDif = centerX - endX;
			double yDif = centerY - endY;
			double radius = Math.sqrt(xDif * xDif + yDif * yDif);
			cd.setColor(Palette.RED);
			cd.drawCircle(centerX, centerY, radius + 2);

			cd.show(16);
		}

		confirm.assertConfirmation();
	}
}
