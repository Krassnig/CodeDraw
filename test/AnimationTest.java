import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AnimationTest {
	private CodeDrawConfirmation confirm;

	@Before
	public void beforeEach() {
		confirm = new CodeDrawConfirmation();
	}

	@After
	public void afterEach() {
		confirm.close();
	}

	@Test
	public void transparencyTest() {
		confirm.setConfirmationDialogue("Transparent rectangles should appear and overlap each other.");
		CodeDraw cd = new CodeDraw();
		confirm.placeCodeDrawTestingInstance(cd);

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
		cd.close();
	}

	@Test
	public void granularAngleTest() {
		confirm.setConfirmationDialogue("The pie should increase in size smoothly.");
		CodeDraw cd = new CodeDraw(1000, 1000);
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
		CodeDraw cd = new CodeDraw();
		confirm.placeCodeDrawTestingInstance(cd);

		for (double i = 0; i < Math.PI * 4; i += Math.PI / 128) {
			cd.clear();
			cd.drawLine(300, 300, 300 + Math.cos(i) * 100, 300 + Math.sin(i) * 100);
			cd.drawArc(300, 300, 100, i, -Math.PI / 4);
			cd.drawArc(300, 300, 110, 0, i % (Math.PI * 2));
			cd.show(20);
		}

		confirm.assertConfirmation();
	}
}
