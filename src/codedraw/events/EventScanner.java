package codedraw.events;

import codedraw.CodeDraw;

import java.util.ArrayList;
import java.util.List;

public class EventScanner {

	private final EventQueue eventQueue = new EventQueue();

	private final List<Subscription> subscriptions = new ArrayList<>();

	public EventScanner(CodeDraw cd) {
		bindEvents(cd);
	}


	public boolean hasEvent() {
		return eventQueue.peekType() != EventType.WINDOW_CLOSE;
	}

	public boolean hasMouseMoveEvent() {
		return eventQueue.peekType() == EventType.MOUSE_MOVE;
	}

	public boolean hasMouseUpEvent() {
		return eventQueue.peekType() == EventType.MOUSE_UP;
	}

	public boolean hasMouseDownEvent() {
		return eventQueue.peekType() == EventType.MOUSE_DOWN;
	}

	public boolean hasMouseEnterEvent() {
		return eventQueue.peekType() == EventType.MOUSE_ENTER;
	}

	public boolean hasMouseLeaveEvent() {
		return eventQueue.peekType() == EventType.MOUSE_LEAVE;
	}

	public boolean hasMouseWheelEvent() {
		return eventQueue.peekType() == EventType.MOUSE_WHEEL;
	}

	public boolean hasKeyDownEvent() {
		return eventQueue.peekType() == EventType.KEY_DOWN;
	}

	public boolean hasKeyPressEvent() {
		return eventQueue.peekType() == EventType.KEY_PRESS;
	}

	public boolean hasKeyUpEvent() {
		return eventQueue.peekType() == EventType.KEY_UP;
	}

	public boolean hasWindowMoveEvent() {
		return eventQueue.peekType() == EventType.WINDOW_MOVE;
	}


	public void skipEvent() {
		eventQueue.popEventArg();
	}

	public MouseMoveEventArgs nextMouseMoveEvent() {
		return popEventArg(EventType.MOUSE_MOVE);
	}

	public MouseClickEventArgs nextMouseUpEvent() {
		return popEventArg(EventType.MOUSE_UP);
	}

	public MouseClickEventArgs nextMouseDownEvent() {
		return popEventArg(EventType.MOUSE_DOWN);
	}

	public MouseMoveEventArgs nextMouseEnterEvent() {
		return popEventArg(EventType.MOUSE_ENTER);
	}

	public MouseMoveEventArgs nextMouseLeaveEvent() {
		return popEventArg(EventType.MOUSE_LEAVE);
	}

	public MouseWheelEventArgs nextMouseWheelEvent() {
		return popEventArg(EventType.MOUSE_WHEEL);
	}

	public KeyEventArgs nextKeyDownEvent() {
		return popEventArg(EventType.KEY_DOWN);
	}

	public KeyEventArgs nextKeyPressEvent() {
		return popEventArg(EventType.KEY_PRESS);
	}

	public KeyEventArgs nextKeyUpEvent() {
		return popEventArg(EventType.KEY_UP);
	}

	public WindowMoveEventArgs nextWindowMoveEvent() {
		return popEventArg(EventType.WINDOW_MOVE);
	}

	public void close() {
		subscriptions.forEach(Subscription::unsubscribe);
	}

	private <T> T popEventArg(EventType expectedEventType) {
		EventType nextEventType = eventQueue.peekType();
		if (nextEventType != expectedEventType)
			throw new EventMismatchException(expectedEventType, nextEventType);
		return (T) eventQueue.popEventArg();
	}


	private void onMouseClick(CodeDraw codeDraw, MouseClickEventArgs mouseEvent) {
		eventQueue.pushEvent(EventType.MOUSE_CLICK, mouseEvent);
	}

	private void onMouseMove(CodeDraw codeDraw, MouseMoveEventArgs mouseEvent) {
		eventQueue.pushEvent(EventType.MOUSE_MOVE, mouseEvent);
	}

	private void onMouseUp(CodeDraw codeDraw, MouseClickEventArgs mouseEvent) {
		eventQueue.pushEvent(EventType.MOUSE_UP, mouseEvent);
	}

	private void onMouseDown(CodeDraw codeDraw, MouseClickEventArgs mouseEvent) {
		eventQueue.pushEvent(EventType.MOUSE_DOWN, mouseEvent);

	}

	private void onMouseEnter(CodeDraw codeDraw, MouseMoveEventArgs mouseEvent) {
		eventQueue.pushEvent(EventType.MOUSE_ENTER, mouseEvent);

	}

	private void onMouseLeave(CodeDraw codeDraw, MouseMoveEventArgs mouseEvent) {
		eventQueue.pushEvent(EventType.MOUSE_LEAVE, mouseEvent);
	}

	private void onMouseWheel(CodeDraw codeDraw, MouseWheelEventArgs mouseWheelEvent) {
		eventQueue.pushEvent(EventType.MOUSE_WHEEL, mouseWheelEvent);
	}


	private void onKeyDown(CodeDraw codeDraw, KeyEventArgs keyEvent) {
		eventQueue.pushEvent(EventType.KEY_DOWN, keyEvent);
	}

	private void onKeyPress(CodeDraw codeDraw, KeyEventArgs keyEvent) {
		eventQueue.pushEvent(EventType.KEY_PRESS, keyEvent);
	}

	private void onKeyUp(CodeDraw codeDraw, KeyEventArgs keyEvent) {
		eventQueue.pushEvent(EventType.KEY_UP, keyEvent);
	}

	private void onWindowMove(CodeDraw codeDraw, WindowMoveEventArgs componentEvent) {
		eventQueue.pushEvent(EventType.WINDOW_MOVE, componentEvent);
	}

	private void onWindowClose(CodeDraw codeDraw, Void unused) {
		eventQueue.pushEvent(EventType.WINDOW_CLOSE, null);
		close();
	}

	private void bindEvents(CodeDraw cd) {
		subscriptions.add(cd.onMouseClick(this::onMouseClick));
		subscriptions.add(cd.onMouseMove(this::onMouseMove));
		subscriptions.add(cd.onMouseUp(this::onMouseUp));
		subscriptions.add(cd.onMouseDown(this::onMouseDown));
		subscriptions.add(cd.onMouseEnter(this::onMouseEnter));
		subscriptions.add(cd.onMouseLeave(this::onMouseLeave));
		subscriptions.add(cd.onMouseWheel(this::onMouseWheel));
		subscriptions.add(cd.onKeyDown(this::onKeyDown));
		subscriptions.add(cd.onKeyPress(this::onKeyPress));
		subscriptions.add(cd.onKeyUp(this::onKeyUp));
		subscriptions.add(cd.onWindowMove(this::onWindowMove));
		subscriptions.add(cd.onWindowClose(this::onWindowClose));
	}


}
