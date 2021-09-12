package codedraw;

import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

class EventCollection {
	public EventCollection(CodeDraw sender) {
		mouseClick = new Event<>(sender);
		mouseMove = new Event<>(sender);
		mouseDown = new Event<>(sender);
		mouseUp = new Event<>(sender);
		mouseEnter = new Event<>(sender);
		mouseLeave = new Event<>(sender);
		mouseWheel = new Event<>(sender);
		keyDown = new Event<>(sender);
		keyUp = new Event<>(sender);
		keyPress = new Event<>(sender);
		windowMove = new Event<>(sender);
	}

	public final Event<CodeDraw, MouseEvent> mouseClick;
	public final Event<CodeDraw, MouseEvent> mouseMove;
	public final Event<CodeDraw, MouseEvent> mouseDown;
	public final Event<CodeDraw, MouseEvent> mouseUp;
	public final Event<CodeDraw, MouseEvent> mouseEnter;
	public final Event<CodeDraw, MouseEvent> mouseLeave;
	public final Event<CodeDraw, MouseWheelEvent> mouseWheel;
	public final Event<CodeDraw, KeyEvent> keyDown;
	public final Event<CodeDraw, KeyEvent> keyUp;
	public final Event<CodeDraw, KeyEvent> keyPress;
	public final Event<CodeDraw, ComponentEvent> windowMove;
}
