import CodeDraw.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.BiFunction;

public class EventTest {
	public static void main(String[] args) {
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
		unsubscribeTest();
	}

	private static int x = 500;
	private static int y = 500;

	private static void windowMoveTest() {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);
		c.drawSquare(500, 500, 100);
		c.show();

		x = c.getFramePositionX();
		y = c.getFramePositionY();

		c.onFrameMove((s, a) -> {
			int dx = x - s.getFramePositionX();
			int dy = y - s.getFramePositionY();

			s.clear();
			s.drawSquare(dx, dy, 100);
			s.show();
		});
	}

	private static String s = "";
	private static void keyEventTest(BiFunction<CodeDraw, EventHandler<CodeDraw, KeyEvent>, Unsubscribe> mapToEvent) {
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

	private static void mouseTest(BiFunction<CodeDraw, EventHandler<CodeDraw, MouseEvent>, Unsubscribe> mapToEvent) {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		mapToEvent.apply(c, (w, a) -> {
			w.fillRectangle(a.getX() - 5, a.getY() - 5, 10, 10);
			w.show();
		});
	}

	private static Unsubscribe unsubscribe;
	private static EventHandler<CodeDraw, KeyEvent> key;
	private static EventHandler<CodeDraw, MouseEvent> mouse;

	private static void unsubscribeTest() {
		CodeDraw cd = new CodeDraw();

		mouse = (c, a) -> {
			c.clear();
			c.setColor(Palette.BLUE);
			c.drawTriangle(200, 200, 400, 200, 300, 400);
			c.show();
			unsubscribe.unsubscribe();
			unsubscribe = c.onKeyPress(key);
		};
		key = (c, a) -> {
			c.clear();
			c.setColor(Palette.RED);
			c.drawSquare(200, 200, 200);
			c.show();
			unsubscribe.unsubscribe();
			unsubscribe = c.onMouseClick(mouse);
		};

		unsubscribe = cd.onMouseClick(mouse);
		mouse.handle(cd, null);
	}
}
