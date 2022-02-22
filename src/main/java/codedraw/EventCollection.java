package codedraw;

import codedraw.events.*;

class EventCollection {
	public EventCollection() { }

	public final Event<MouseClickEvent> mouseClick = new Event<>();
	public final Event<MouseMoveEvent> mouseMove = new Event<>();
	public final Event<MouseDownEvent> mouseDown = new Event<>();
	public final Event<MouseUpEvent> mouseUp = new Event<>();
	public final Event<MouseEnterEvent> mouseEnter = new Event<>();
	public final Event<MouseLeaveEvent> mouseLeave = new Event<>();
	public final Event<MouseWheelEvent> mouseWheel = new Event<>();
	public final Event<KeyDownEvent> keyDown = new Event<>();
	public final Event<KeyUpEvent> keyUp = new Event<>();
	public final Event<KeyPressEvent> keyPress = new Event<>();
	public final Event<WindowMoveEvent> windowMove = new Event<>();
	public final Event<WindowCloseEvent> windowClose = new Event<>();
}
