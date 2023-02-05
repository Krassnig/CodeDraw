package manual;

import codedraw.CodeDraw;
import codedraw.Palette;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EdgeCaseTest {
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
	public void terminateProcessOnlyWhenLastTest() {
		cd.close();
		confirm.setConfirmationDialogue("No other CodeDraw instance should be visible.");
		CodeDraw cd1 = new CodeDraw();
		CodeDraw cd2 = new CodeDraw();

		cd1.close(true);
		cd2.close();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		confirm.assertConfirmation();
	}

	@Test
	public void smallWindowTest() {
		confirm.setConfirmationDialogue("A 150x1 canvas should open and a red pixel bar should increase");
		cd.close();
		cd = new CodeDraw(150, 1);
		confirm.placeCodeDrawTestingInstance(cd);

		for (int i = 0; i < 150; i++) {
			cd.setPixel(i, 0, Palette.RED);
			cd.show(10);
		}

		confirm.assertConfirmation();
	}

	@Test
	public void disposeCloseTest() {
		confirm.setConfirmationDialogue("Only the 'I should stay open' window should stay open.");

		cd.drawText(300, 300, "I should close.");
		cd.show();

		CodeDraw cd2 = new CodeDraw();
		confirm.placeCodeDrawTestingInstance(cd2);
		cd2.drawText(300, 300, "I should stay open.");
		cd2.show();

		cd.close();

		confirm.assertConfirmation();
		cd2.close();
	}

	@Test
	public void cornerTest() {
		confirm.setConfirmationDialogue("In each corner there should be a red pixel. (hard to see)");

		int size = 1;

		cd.setColor(Palette.RED);
		cd.fillRectangle(0, 0, size, size);
		cd.fillRectangle(0, cd.getHeight() - size, size, size);
		cd.fillRectangle(cd.getWidth() - size, 0, size, size);
		cd.fillRectangle(cd.getWidth() - size, cd.getHeight() - size, size, size);
		cd.show();

		confirm.assertConfirmation();
	}
}
