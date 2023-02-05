package manual;

import codedraw.*;
import codedraw.Image;
import codedraw.TextOrigin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static codedraw.CursorStyle.*;

public class PropertyTest {
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
	public void drawOverTest() {
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

	@Test
	public void cursorTest() {
		confirm.setConfirmationDialogue(
			"Hover over the CodeDraw window, the cursor should change between several options.\n" +
			"The last cursor should be the CodeDraw icon."
		);

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

		cd.getTextFormat().setTextOrigin(TextOrigin.TOP_MIDDLE);
		cd.drawText(300, 200, "Move mouse over here.");

		for (CursorStyle cursorStyle : cursors) {
			cd.setCursorStyle(cursorStyle);
			cd.show(500);
		}

		confirm.assertConfirmation();
	}

	private static Image getCodeDrawIcon() {
		return Image.fromBase64String(
				"iVBORw0KGgoAAAANSUhEUgAAABAAAAAQBAMAAADt3eJSAAAABGdBTUEAALGPC/xh" +
				"BQAAAA9QTFRF/59o/0RHY2Np/8iZAAAAQFvSagAAAAlwSFlzAAAOwgAADsIBFShK" +
				"gAAAADtJREFUGNNtyMENACAMQlF0AruBMoKM4P47eaAeTPovkIe/9k4Mb4/w4TJ1" +
				"0kJyJrAGJUDbAMmAI1QBFz7NBPgXRK/qAAAAAElFTkSuQmCC"
		);
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
}
