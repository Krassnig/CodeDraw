import CodeDraw.*;

import java.awt.event.*;
import java.util.function.BiFunction;

public class EventTest {
	public static void main(String[] args) {
		curveTest();
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
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);
		c.drawSquare(200, 200, 100);
		c.show();

		x = c.getCanvasPositionX();
		y = c.getCanvasPositionY();

		c.onFrameMove((s, a) -> {
			int dx = x - s.getCanvasPositionX();
			int dy = y - s.getCanvasPositionY();

			s.clear();
			s.drawSquare(dx + 200, dy + 200, 100);
			s.show();
		});
	}

	private static String s = "";
	private static void keyEventTest(BiFunction<CodeDraw, EventHandler<CodeDraw, KeyEvent>, Subscription> mapToEvent) {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		mapToEvent.apply(c, (w, a) -> {
			s += a.getKeyChar();
			w.drawText(100, 100, s);
			w.show();
		});
	}

	private static int l = 0;
	private static void mouseWheelTest() {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		c.onMouseWheel((s, a) -> {
			s.clear();
			int h = l + a.getWheelRotation();
			s.drawTriangle(200, 300, 400, 300, 300, 300 + 20 * h);
			s.show();
		});
	}

	private static void mouseTest(BiFunction<CodeDraw, EventHandler<CodeDraw, MouseEvent>, Subscription> mapToEvent) {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		mapToEvent.apply(c, (w, a) -> {
			w.fillRectangle(a.getX() - 5, a.getY() - 5, 10, 10);
			w.show();
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
			c.drawTriangle(200, 200, 400, 200, 300, 400);
			c.fillRectangle(10, 10, 40, unsubscribeProgress++ * 5);
			c.show();
			subscription.unsubscribe();
			subscription = c.onKeyPress(key);
		};
		key = (c, a) -> {
			c.clear();
			c.setColor(Palette.RED);
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
