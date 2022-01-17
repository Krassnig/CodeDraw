import codedraw.*;
import codedraw.events.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.function.Consumer;

public class EventTest {
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
	public void eventSleepTest() {
		confirm.setConfirmationDialogue("Upon clicking, the square should turn blue, then green.");
		cd.drawText(50, 50, "Click here, then I will turn blue, then green!");
		cd.setColor(Palette.RED);
		cd.fillSquare(100, 100, 100);
		cd.show();

		cd.onMouseDown(a -> {
			cd.clear();
			cd.setColor(Palette.BLUE);
			cd.drawText(50, 50, "I'm blue da ba dee!");
			cd.fillSquare(100, 100, 100);
			cd.show(3000);

			cd.clear();
			cd.setColor(Palette.GREEN);
			cd.drawText(50, 50, "Now I'm green.");
			cd.fillSquare(100, 100, 100);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	@Test
	public void curveTest() {
		confirm.setConfirmationDialogue("The curve should follow your mouse.");
		cd.onMouseMove(a -> {
			cd.clear();
			cd.drawCurve(200, 200, a.getX(), a.getY(), 400, 400);
			cd.show();
		});
		confirm.assertConfirmation();
	}

	@Test
	public void windowMoveTest() {
		confirm.setConfirmationDialogue("The square in the center should not move while moving the window.");

		int x = cd.getCanvasPositionX();
		int y = cd.getCanvasPositionY();
		System.out.println(x + " " + y);

		cd.setColor(Palette.RED);
		cd.drawSquare(300, 300, 100);
		cd.show();

		cd.onWindowMove(a -> {
			int dx = x - cd.getCanvasPositionX();
			int dy = y - cd.getCanvasPositionY();

			cd.clear();
			cd.drawSquare(300 + dx, 300 + dy, 100);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	private String text = "";
	@Test
	public void keyDownTest() {
		confirm.setConfirmationDialogue("New characters should appear exactly once when a key is pressed down.");

		cd.setColor(Palette.RED);
		cd.onKeyDown(a -> {
			cd.clear();
			text += a.getChar();
			cd.drawText(100, 100, text);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	@Test
	public void keyUpTest() {
		confirm.setConfirmationDialogue("New characters should appear exactly once when a key is released.");

		cd.setColor(Palette.RED);
		cd.onKeyUp(a -> {
			cd.clear();
			text += a.getChar();
			cd.drawText(100, 100, text);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	@Test
	public void keyPressTest() {
		confirm.setConfirmationDialogue("New characters should appear when a key is pressed,\nmultiple when the key is held down.");

		cd.setColor(Palette.RED);
		cd.onKeyPress(a -> {
			cd.clear();
			text += a.getChar();
			cd.drawText(100, 100, text);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	private int l = 0;
	@Test
	public void mouseWheelTest() {
		confirm.setConfirmationDialogue("The triangle should point up or down depending on the scroll direction.");

		cd.setColor(Palette.RED);

		cd.onMouseWheel(a -> {
			cd.clear();
			double h = l + a.getWheelRotation();
			cd.drawTriangle(200, 300, 400, 300, 300, 300 + 20 * h);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	@Test
	public void mouseClickTest() {
		mouseTests(
				"A red square should appear once where the mouse is clicked.",
				handler -> cd.onMouseClick(a -> handler.accept(new Point(a.getX(), a.getY())))
		);
	}

	@Test
	public void mouseMoveTest() {
		mouseTests(
				"A red square should continuously where the mouse is moved.",
				handler -> cd.onMouseMove(a -> handler.accept(new Point(a.getX(), a.getY())))
		);
	}

	@Test
	public void mouseDownTest() {
		mouseTests(
				"A red square should appear once exactly as you click down.",
				handler -> cd.onMouseDown(a -> handler.accept(new Point(a.getX(), a.getY())))
		);
	}

	@Test
	public void mouseUpTest() {
		mouseTests(
				"A red square should appear once exactly as you release a mouse button.",
				handler -> cd.onMouseUp(a -> handler.accept(new Point(a.getX(), a.getY())))
		);
	}

	@Test
	public void mouseEnterTest() {
		mouseTests(
				"A red square should appear once exactly where the window is entered.",
				handler -> cd.onMouseEnter(a -> handler.accept(new Point(a.getX(), a.getY())))
		);
	}

	@Test
	public void mouseLeaveTest() {
		mouseTests(
				"A red square should appear once exactly where the window is left.",
				handler -> cd.onMouseLeave(a -> handler.accept(new Point(a.getX(), a.getY())))
		);
	}

	private void mouseTests(String confirmationDialogue, Consumer<Consumer<Point>> event) {
		confirm.setConfirmationDialogue(confirmationDialogue);

		cd.setColor(Palette.RED);
		event.accept(a -> {
			cd.fillRectangle(a.getX() - 5, a.getY() - 5, 10, 10);
			cd.show();
		});

		confirm.assertConfirmation();
	}

	private Subscription subscription;
	private EventHandler<KeyPressEventArgs> key;
	private EventHandler<MouseClickEventArgs> mouse;
	private int unsubscribeProgress = 0;

	@Test
	public void unsubscribeTest() {
		confirm.setConfirmationDialogue("The bar should only go up by alternating between pressing a key or a mouse button.");

		mouse = a -> {
			cd.clear();
			cd.setColor(Palette.BLUE);
			cd.drawText(200, 100, "Press a key on your keyboard.");
			cd.drawTriangle(200, 200, 400, 200, 300, 400);
			cd.fillRectangle(10, 10, 40, unsubscribeProgress++ * 5);
			cd.show();
			subscription.unsubscribe();
			subscription = cd.onKeyPress(key);
		};
		key = a -> {
			cd.clear();
			cd.setColor(Palette.RED);
			cd.drawText(200, 100, "Press a button on your mouse.");
			cd.drawSquare(200, 200, 200);
			cd.fillRectangle(10, 10, 40, unsubscribeProgress++ * 5);
			cd.show();
			subscription.unsubscribe();
			subscription = cd.onMouseClick(mouse);
		};

		subscription = cd.onMouseClick(mouse);
		mouse.handle(null);

		confirm.assertConfirmation();
	}
}
