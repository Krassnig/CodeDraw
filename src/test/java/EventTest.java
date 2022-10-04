import codedraw.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.function.Supplier;

public class EventTest {
	private CodeDrawConfirmation confirm;
	private CodeDraw cd;
	private EventScanner esc;

	@Before
	public void beforeEach() {
		confirm = new CodeDrawConfirmation();
		cd = new CodeDraw();
		esc = cd.getEventScanner();
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

		EventScanner esc = cd.getEventScanner();

		cd.clear();
		cd.drawText(50, 50, "Click here, then I will turn blue, then green!");
		cd.setColor(Palette.RED);
		cd.fillSquare(100, 100, 100);
		cd.show(100);

		while (!esc.hasMouseClickEvent()) {
			esc.nextEvent();
		}

		cd.clear();
		cd.setColor(Palette.BLUE);
		cd.drawText(50, 50, "I'm blue da ba dee!");
		cd.fillSquare(100, 100, 100);
		cd.show(3000);

		cd.clear();
		cd.setColor(Palette.GREEN);
		cd.drawText(50, 50, "Now I'm green.");
		cd.fillSquare(100, 100, 100);
		cd.show(1000);

		confirm.assertConfirmation();
	}

	@Test
	public void curveTest() {
		confirm.setConfirmationDialogue("The curve should follow your mouse.");

		while (!confirm.hasConfirmationBeenPressed()) {
			if (esc.hasEventNow()) {
				if (esc.hasMouseMoveEvent()) {
					MouseMoveEvent a = esc.nextMouseMoveEvent();
					cd.clear();
					cd.drawCurve(200, 200, a.getX(), a.getY(), 400, 400);
					cd.show();
				}
				else {
					esc.nextEvent();
				}
			}
		}
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

		int dx = 0;
		int dy = 0;

		while (!confirm.hasConfirmationBeenPressed()) {
			while (esc.hasEventNow()) {
				if (esc.hasWindowMoveEvent()) {
					WindowMoveEvent a = esc.nextWindowMoveEvent();
					dx = x - a.getCanvasPositionX();
					dy = y - a.getCanvasPositionY();
				}
				else {
					esc.nextEvent();
				}
			}

			cd.clear();
			cd.drawSquare(300 + dx, 300 + dy, 100);
			cd.show();
		}
	}

	@Test
	public void keyDownTest() {
		confirm.setConfirmationDialogue("New characters should appear exactly once when a key is pressed down.");

		String text = "";
		cd.setColor(Palette.RED);
		while (!confirm.hasConfirmationBeenPressed()) {
			while (esc.hasEventNow()) {
				if (esc.hasKeyDownEvent()) {
					text += esc.nextKeyDownEvent().getChar();
				}
				else {
					esc.nextEvent();
				}
			}

			cd.clear();
			cd.drawText(100, 100, text);
			cd.show();
		}
	}

	@Test
	public void keyUpTest() {
		confirm.setConfirmationDialogue("New characters should appear exactly once when a key is released.");

		String text = "";
		cd.setColor(Palette.RED);
		while (!confirm.hasConfirmationBeenPressed()) {
			while (esc.hasEventNow()) {
				if (esc.hasKeyUpEvent()) {
					text += esc.nextKeyUpEvent().getChar();
				}
				else {
					esc.nextEvent();
				}
			}

			cd.clear();
			cd.drawText(100, 100, text);
			cd.show();
		}
	}

	@Test
	public void keyPressTest() {
		confirm.setConfirmationDialogue("New characters should appear when a key is pressed,\nmultiple when the key is held down.");

		String text = "";
		cd.setColor(Palette.RED);
		while (!confirm.hasConfirmationBeenPressed()) {
			while (esc.hasEventNow()) {
				if (esc.hasKeyPressEvent()) {
					text += esc.nextKeyPressEvent().getChar();
				}
				else {
					esc.nextEvent();
				}
			}

			cd.clear();
			cd.drawText(100, 100, text);
			cd.show();
		}
	}

	@Test
	public void mouseWheelTest() {
		confirm.setConfirmationDialogue("The triangle should point up or down depending on the scroll direction.");

		double l = 0;
		cd.setColor(Palette.RED);
		while (!confirm.hasConfirmationBeenPressed()) {
			while (esc.hasEventNow()) {
				if (esc.hasMouseWheelEvent()) {
					l += esc.nextMouseWheelEvent().getWheelRotation();
				}
				else {
					esc.nextEvent();
				}
			}

			cd.clear();
			cd.drawTriangle(200, 300, 400, 300, 300, 300 + 20 * l);
			cd.show();
		}
	}

	@Test
	public void mouseClickTest() {
		mouseTests(
				"A red square should appear once where the mouse is clicked.",
				() -> esc.hasMouseClickEvent(),
				() -> {
					MouseClickEvent a = esc.nextMouseClickEvent();
					return new Point(a.getX(), a.getY());
				}
		);
	}

	@Test
	public void mouseMoveTest() {
		mouseTests(
				"Red squares should be drawn everywhere the mouse is moved.",
				() -> esc.hasMouseMoveEvent(),
				() -> {
					MouseMoveEvent a = esc.nextMouseMoveEvent();
					return new Point(a.getX(), a.getY());
				}
		);
	}

	@Test
	public void mouseDownTest() {
		mouseTests(
				"A red square should appear once exactly as you click down.",
				() -> esc.hasMouseDownEvent(),
				() -> {
					MouseDownEvent a = esc.nextMouseDownEvent();
					return new Point(a.getX(), a.getY());
				}
		);
	}

	@Test
	public void mouseUpTest() {
		mouseTests(
				"A red square should appear once exactly as you release a mouse button.",
				() -> esc.hasMouseUpEvent(),
				() -> {
					MouseUpEvent a = esc.nextMouseUpEvent();
					return new Point(a.getX(), a.getY());
				}
		);
	}

	@Test
	public void mouseEnterTest() {
		mouseTests(
				"A red square should appear once exactly where the window is entered.",
				() -> esc.hasMouseEnterEvent(),
				() -> {
					MouseEnterEvent a = esc.nextMouseEnterEvent();
					return new Point(a.getX(), a.getY());
				}
		);
	}

	@Test
	public void mouseLeaveTest() {
		mouseTests(
				"A red square should appear once exactly where the window is left.",
				() -> esc.hasMouseLeaveEvent(),
				() -> {
					MouseLeaveEvent a = esc.nextMouseLeaveEvent();
					return new Point(a.getX(), a.getY());
				}
		);
	}

	private void mouseTests(String confirmationDialogue, Supplier<Boolean> hasEvent, Supplier<Point> nextEvent) {
		confirm.setConfirmationDialogue(confirmationDialogue);

		cd.setColor(Palette.RED);
		while (!confirm.hasConfirmationBeenPressed()) {
			while (esc.hasEventNow()) {
				if (hasEvent.get()) {
					Point p = nextEvent.get();
					cd.fillRectangle(p.getX() - 5, p.getY() - 5, 10, 10);
				}
				else {
					esc.nextEvent();
				}
			}

			cd.show(16);
		}
	}

	@Test
	public void windowCloseTest() {
		confirm.setConfirmationDialogue("Close the window");

		while (esc.hasEvent()) {
			if (esc.hasWindowCloseEvent()) {
				esc.nextWindowCloseEvent();
				break;
			}
			else {
				esc.nextEvent();
			}
		}
	}

	@Test
	public void unsubscribeTest() {
		confirm.setConfirmationDialogue("The bar should only go up by alternating between pressing a key or a mouse button.");

		EventScanner esc = cd.getEventScanner();
		int unsubscribeProgress = 0;

		while (!confirm.hasConfirmationBeenPressed()) {
			cd.clear();

			if (unsubscribeProgress % 2 == 0) {
				cd.setColor(Palette.RED);
				cd.drawText(200, 100, "Press a button on your mouse.");
				cd.drawSquare(200, 200, 200);
			}
			else {
				cd.setColor(Palette.BLUE);
				cd.drawText(200, 100, "Press a key on your keyboard.");
				cd.drawTriangle(200, 200, 400, 200, 300, 400);
			}

			cd.fillRectangle(10, 10, 40, unsubscribeProgress * 5);
			cd.show();

			while (esc.hasEventNow()) {
				if (unsubscribeProgress % 2 == 0 ? esc.hasMouseClickEvent() : esc.hasKeyPressEvent()) {
					unsubscribeProgress++;
					esc.nextEvent();
					break;
				}
				esc.nextEvent();
			}
		}

		confirm.assertConfirmation();
	}
}
