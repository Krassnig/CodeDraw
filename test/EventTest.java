import codedraw.*;
import codedraw.events.*;

import java.awt.event.*;
import java.util.function.BiFunction;

public class EventTest {
	public static void main(String[] args) {
		//eventSleepTest();
		//curveTest();
		//mouseTest((c, h) -> c.onMouseClick(h));
		//mouseTest((c, h) -> c.onMouseMove(h));
		//mouseTest((c, h) -> c.onMouseDown(h));
		//mouseTest((c, h) -> c.onMouseUp(h));
		//mouseTest((c, h) -> c.onMouseLeave(h));
		//mouseTest((c, h) -> c.onMouseEnter(h));
		//mouseWheelTest();
		//keyEventTest((c, h) -> c.onKeyDown(h));
		//keyEventTest((c, h) -> c.onKeyPress(h));
		//keyEventTest((c, h) -> c.onKeyUp(h));
		//windowMoveTest();
		//unsubscribeTest();
	}

	private static void eventSleepTest() {
		CodeDraw cd = new CodeDraw();

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
	}

	private static void curveTest() {
		CodeDraw cd = new CodeDraw();

		cd.onMouseMove(a -> {
			cd.clear();
			cd.drawCurve(200, 200, a.getX(), a.getY(), 400, 400);
			cd.show();
		});
	}

	private static int x = 500;
	private static int y = 500;

	private static void windowMoveTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);
		cd.drawSquare(200, 200, 100);
		cd.show();

		x = cd.getCanvasPositionX();
		y = cd.getCanvasPositionY();

		cd.onWindowMove(a -> {
			int dx = x - cd.getCanvasPositionX();
			int dy = y - cd.getCanvasPositionY();

			cd.clear();
			cd.drawSquare(dx + 200, dy + 200, 100);
			cd.show();
		});
	}

	private static String s = "";
	private static void keyEventTest(BiFunction<CodeDraw, EventHandler<KeyEvent>, Subscription> mapToEvent) {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);

		mapToEvent.apply(cd, a -> {
			cd.clear();
			s += a.getKeyChar();
			cd.drawText(100, 100, s);
			cd.show();
		});
	}

	private static int l = 0;
	private static void mouseWheelTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);

		cd.onMouseWheel(a -> {
			cd.clear();
			double h = l + a.getWheelRotation();
			cd.drawTriangle(200, 300, 400, 300, 300, 300 + 20 * h);
			cd.show();
		});
	}

	private static void mouseTest(BiFunction<CodeDraw, EventHandler<MouseEvent>, Subscription> mapToEvent) {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);

		mapToEvent.apply(cd, a -> {
			cd.fillRectangle(a.getX() - 5, a.getY() - 5, 10, 10);
			cd.show();
		});
	}

	private static Subscription subscription;
	private static EventHandler<KeyPressEventArgs> key;
	private static EventHandler<MouseClickEventArgs> mouse;
	private static int unsubscribeProgress = 0;

	private static void unsubscribeTest() {
		CodeDraw cd = new CodeDraw();

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
	}
}
