import codedraw.*;
import codedraw.images.CodeDrawImage;
import codedraw.images.ImageFormat;
import codedraw.textformat.HorizontalAlign;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static codedraw.CursorStyle.*;

public class CodeDrawTest {
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
	public void cursorTest() {
		confirm.setConfirmationDialogue("Hover over the CodeDraw window, the cursor should change.");

		CursorStyle[] cursors = {
				DEFAULT,
				CROSS_HAIR,
				TEXT,
				WAIT,
				SOUTH_WEST_RESIZE,
				SOUTH_EAST_RESIZE,
				NORTH_WEST_RESIZE,
				NORTH_EAST_RESIZE,
				NORTH_RESIZE,
				SOUTH_RESIZE,
				WEST_RESIZE,
				EAST_RESIZE,
				HAND,
				MOVE,
				new CursorStyle(getCodeDrawIcon())
		};

		cd.getTextFormat().setHorizontalAlign(HorizontalAlign.CENTER);
		cd.drawText(300, 200, "Move mouse over here.");

		for (CursorStyle cursorStyle : cursors) {
			cd.setCursorStyle(cursorStyle);
			cd.show(500);
		}

		confirm.assertConfirmation();
	}

	private static CodeDrawImage getCodeDrawIcon() {
		return CodeDrawImage.fromBase64String(
				"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
				"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
				"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
				"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
		);
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
	public void windowPositionTest() {
		confirm.setConfirmationDialogue("The rectangle should not move while the canvas does move.");
		int startPosX = cd.getWindowPositionX();
		int startPosY = cd.getWindowPositionY();

		int posX = startPosX;
		int posY = startPosY;

		for (int i = 0; i < 60; i++) {
			cd.setWindowPositionX(posX += 2);
			cd.setWindowPositionY(posY += 2);

			cd.clear();
			cd.drawSquare(startPosX + 200 - posX, startPosY + 200 - posY, 100);
			cd.show(100);
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
	public void smallWindowTest() {
		confirm.setConfirmationDialogue("A 150x1 canvas should open and a red pixel bar should increase");
		cd.close();
		cd = new CodeDraw(150, 1);
		confirm.placeCodeDrawTestingInstance(cd);
		cd.setColor(Palette.RED);

		for (int i = 0; i < 150; i++) {
			cd.drawPixel(i, 0);
			cd.show(10);
		}

		confirm.assertConfirmation();
	}

	@Test
	public void imageSaveTest() {
		confirm.setConfirmationDialogue("There should be an out.png in your test folder.");

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

		CodeDrawImage.saveAs(cd.copyCanvas(), "./test/out.png", ImageFormat.PNG);

		cd.show();
		confirm.assertConfirmation();
	}

	@Test
	public void imageTestScale() {
		confirm.setConfirmationDialogue("The image should display the scaled down 200x200 image.");

		cd.drawImage(100, 100, 200, 200, CodeDrawImage.fromFile("test/test.jpg"));
		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void imageTest() {
		confirm.setConfirmationDialogue("The image should nicely fill out the canvas");

		cd.close();
		cd = new CodeDraw(820, 620);
		confirm.placeCodeDrawTestingInstance(cd);

		cd.drawImage(10, 10, CodeDrawImage.fromFile("test/test.jpg"));
		cd.show();

		confirm.assertConfirmation();
	}

	@Test
	public void cornerTest() {
		confirm.setConfirmationDialogue("In each corner there should be a red pixel.");

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
