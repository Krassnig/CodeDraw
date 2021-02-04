package CodeDrawTest;

import CodeDraw.*;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.function.Function;

public class EventTests {
	public static void main(String[] args) {
		mouseTest(l -> l.mouseClick());
		mouseTest(l -> l.mouseMove());
		mouseTest(l -> l.mouseDown());
		mouseTest(l -> l.mouseUp());
		mouseTest(l -> l.mouseLeave());
		mouseTest(l -> l.mouseEnter());
		mouseWheelTest();
		keyEventTest(l -> l.keyUp());
		keyEventTest(l -> l.keyPress());
		keyEventTest(l -> l.keyDown());
		windowMoveTest();
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

		c.frameMove().subscribe((s, a) -> {
			int dx = x - s.getFramePositionX();
			int dy = y - s.getFramePositionY();

			s.reset();
			s.drawSquare(dx, dy, 100);
			s.show();
		});
	}

	private static String s = "";
	private static void keyEventTest(Function<CodeDraw, Event<CodeDraw, KeyEvent>> mapToEvent) {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		mapToEvent.apply(c).subscribe((w, a) -> {
			s += a.getKeyChar();
			w.drawText(100, 100, s);
			w.show();
		});
	}

	private static int l = 0;
	private static void mouseWheelTest() {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		c.mouseWheel().subscribe((s, a) -> {
			s.reset();
			int h = l + a.getWheelRotation();
			s.drawTriangle(200, 300, 400, 300, 300, 300 + 20 * h);
			s.show();
		});
	}

	private static void mouseTest(Function<CodeDraw, Event<CodeDraw, MouseEvent>> mapToEvent) {
		CodeDraw c = new CodeDraw();

		c.setColor(Palette.RED);

		mapToEvent.apply(c).subscribe((w, a) -> {
			w.fillRectangle(a.getX() - 5, a.getY() - 5, 10, 10);
			w.show();
		});
	}
}
