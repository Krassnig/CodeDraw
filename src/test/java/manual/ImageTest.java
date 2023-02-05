package manual;

import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

public class ImageTest {
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
	public void imageSaveTest() {
		confirm.setConfirmationDialogue(
			"There should be an out.png in your test folder." +
			"Check to see if it matches the image on the screen."
		);

		cd.drawText(100, 100, Instant.now().toString());
		cd.setColor(Palette.BLUE_VIOLET);

		cd.drawArc(200, 200, 50, 50, -Math.PI / 2, Math.PI / 2);
		cd.fillPie(200, 400, 50, 50, -Math.PI / 2, Math.PI * 3 / 2);

		cd.drawArc(400, 200, 50, 50, -Math.PI / 2, Math.PI / 2);
		cd.fillPie(400, 400, 50, 50, -Math.PI / 2, Math.PI * 3 / 2);

		cd.setColor(Palette.ORANGE);
		cd.drawRectangle(150, 150, 100, 100);

		cd.setColor(Palette.RED);
		cd.fillCircle(200, 200, 10);

		Image.save(cd, "./src/test/java/out.png", ImageFormat.PNG);

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void imageScalingTest() {
		confirm.setConfirmationDialogue("The image should display the scaled down 200x200 image.");

		cd.drawImage(100, 100, 200, 200, Image.fromFile("./src/test/java/test.jpg"));
		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void imageDefaultSizeTest() {
		confirm.setConfirmationDialogue("The image should nicely fill out the canvas with a 10px padding.");

		cd.close();
		cd = new CodeDraw(820, 620);
		confirm.placeCodeDrawTestingInstance(cd);

		cd.drawImage(10, 10, Image.fromFile("./src/test/java/test.jpg"));
		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void imageDrawingTransparencyTest() {
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
	public void transparencyTest() {
		confirm.setConfirmationDialogue("Transparent rectangles should appear and overlap each other.");

		for (int i = 0; i < 56; i++) {
			int d = i * 10;
			cd.setColor(Palette.fromBaseColor(Palette.BLUE, 25));
			cd.fillSquare(d, d, 50);

			cd.setColor(Palette.fromBaseColor(Palette.RED, 25));
			cd.fillSquare(d, cd.getHeight() - d - 50, 50);

			cd.show(100);
		}

		cd.show();
		confirm.assertConfirmation();
	}
}
