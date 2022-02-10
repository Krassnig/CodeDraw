package codedraw;

import codedraw.events.*;

class EventCollection {
	public EventCollection() { }

	public final Event<MouseClickEventArgs> mouseClick = new Event<>();
	public final Event<MouseMoveEventArgs> mouseMove = new Event<>();
	public final Event<MouseDownEventArgs> mouseDown = new Event<>();
	public final Event<MouseUpEventArgs> mouseUp = new Event<>();
	public final Event<MouseEnterEventArgs> mouseEnter = new Event<>();
	public final Event<MouseLeaveEventArgs> mouseLeave = new Event<>();
	public final Event<MouseWheelEventArgs> mouseWheel = new Event<>();
	public final Event<KeyDownEventArgs> keyDown = new Event<>();
	public final Event<KeyUpEventArgs> keyUp = new Event<>();
	public final Event<KeyPressEventArgs> keyPress = new Event<>();
	public final Event<WindowMoveEventArgs> windowMove = new Event<>();
	public final Event<WindowCloseEventArgs> windowClose = new Event<>();
}
