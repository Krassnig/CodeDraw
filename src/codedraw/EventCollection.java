package codedraw;

import codedraw.events.*;

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
		windowClose = new Event<>(sender);
	}

	public final Event<CodeDraw, MouseClickEventArgs> mouseClick;
	public final Event<CodeDraw, MouseMoveEventArgs> mouseMove;
	public final Event<CodeDraw, MouseDownEventArgs> mouseDown;
	public final Event<CodeDraw, MouseUpEventArgs> mouseUp;
	public final Event<CodeDraw, MouseEnterEventArgs> mouseEnter;
	public final Event<CodeDraw, MouseLeaveEventArgs> mouseLeave;
	public final Event<CodeDraw, MouseWheelEventArgs> mouseWheel;
	public final Event<CodeDraw, KeyDownEventArgs> keyDown;
	public final Event<CodeDraw, KeyUpEventArgs> keyUp;
	public final Event<CodeDraw, KeyPressEventArgs> keyPress;
	public final Event<CodeDraw, WindowMoveEventArgs> windowMove;
	public final Event<CodeDraw, Void> windowClose;
}
