import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnimationTest {
	private CodeDraw cd;
	private CodeDrawConfirmation confirm;

	@Before
	public void beforeEach() {
		cd = new CodeDraw();
		confirm = new CodeDrawConfirmation();
		confirm.placeCodeDrawTestingInstance(cd);
	}

	@After
	public void afterEach() {
		cd.close();
		confirm.close();
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
}
