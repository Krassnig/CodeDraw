import codedraw.*;

import java.awt.event.*;
import java.util.function.BiFunction;

public class EventTest {
	public static void main(String[] args) {
		//eventSleep();
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

	private static void eventSleep() {
		CodeDraw cd = new CodeDraw();

		cd.drawText(50, 50, "Click Here, then I will turn blue, then green!");
		cd.setColor(Palette.RED);
		cd.fillSquare(100, 100, 100);
		cd.show();

		cd.onMouseDown((c, a) -> {
			c.clear();
			c.setColor(Palette.BLUE);
			c.drawText(50, 50, "I'm blue da ba dee!");
			c.fillSquare(100, 100, 100);
			c.show(3000);

			c.clear();
			c.setColor(Palette.GREEN);
			c.drawText(50, 50, "Now I'm green.");
			c.fillSquare(100, 100, 100);
			c.show();
		});
	}

	private static void curveTest() {
		CodeDraw cd = new CodeDraw();

		cd.onMouseMove((c, a) -> {
			c.clear();
			c.drawCurve(200, 200, a.getX(), a.getY(), 400, 400);
			c.show();
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

		cd.onWindowMove((c, a) -> {
			int dx = x - c.getCanvasPositionX();
			int dy = y - c.getCanvasPositionY();

			c.clear();
			c.drawSquare(dx + 200, dy + 200, 100);
			c.show();
		});
	}

	private static String s = "";
	private static void keyEventTest(BiFunction<CodeDraw, EventHandler<CodeDraw, KeyEvent>, Subscription> mapToEvent) {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);

		mapToEvent.apply(cd, (c, a) -> {
			c.clear();
			s += a.getKeyChar();
			c.drawText(100, 100, s);
			c.show();
		});
	}

	private static int l = 0;
	private static void mouseWheelTest() {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);

		cd.onMouseWheel((c, a) -> {
			c.clear();
			int h = l + a.getWheelRotation();
			c.drawTriangle(200, 300, 400, 300, 300, 300 + 20 * h);
			c.show();
		});
	}

	private static void mouseTest(BiFunction<CodeDraw, EventHandler<CodeDraw, MouseEvent>, Subscription> mapToEvent) {
		CodeDraw cd = new CodeDraw();

		cd.setColor(Palette.RED);

		mapToEvent.apply(cd, (c, a) -> {
			c.fillRectangle(a.getX() - 5, a.getY() - 5, 10, 10);
			c.show();
		});
	}

	private static Subscription subscription;
	private static EventHandler<CodeDraw, KeyEvent> key;
	private static EventHandler<CodeDraw, MouseEvent> mouse;
	private static int unsubscribeProgress = 0;

	private static void unsubscribeTest() {
		CodeDraw cd = new CodeDraw();

		mouse = (c, a) -> {
			c.clear();
			c.setColor(Palette.BLUE);
			c.drawText(200, 100, "Press a key on your keyboard.");
			c.drawTriangle(200, 200, 400, 200, 300, 400);
			c.fillRectangle(10, 10, 40, unsubscribeProgress++ * 5);
			c.show();
			subscription.unsubscribe();
			subscription = c.onKeyPress(key);
		};
		key = (c, a) -> {
			c.clear();
			c.setColor(Palette.RED);
			c.drawText(200, 100, "Press a button on your mouse.");
			c.drawSquare(200, 200, 200);
			c.fillRectangle(10, 10, 40, unsubscribeProgress++ * 5);
			c.show();
			subscription.unsubscribe();
			subscription = c.onMouseClick(mouse);
		};

		subscription = cd.onMouseClick(mouse);
		mouse.handle(cd, null);
	}
}
