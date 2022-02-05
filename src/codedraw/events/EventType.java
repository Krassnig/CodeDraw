package codedraw.events;

enum EventType {
	ANY(Object.class),
	MOUSE_CLICK(MouseClickEventArgs.class),
	MOUSE_MOVE(MouseMoveEventArgs.class),
	MOUSE_DOWN(MouseDownEventArgs.class),
	MOUSE_UP(MouseUpEventArgs.class),
	MOUSE_ENTER(MouseEnterEventArgs.class),
	MOUSE_LEAVE(MouseLeaveEventArgs.class),
	MOUSE_WHEEL(MouseWheelEventArgs.class),
	KEY_DOWN(KeyDownEventArgs.class),
	KEY_UP(KeyUpEventArgs.class),
	KEY_PRESS(KeyPressEventArgs.class),
	WINDOW_MOVE(WindowMoveEventArgs.class),
	WINDOW_CLOSE(WindowCloseEventArgs.class),
	END_OF_EVENT(Object.class);

	private Class<?> clazz;

	EventType(Class<?> clazz) {
		this.clazz = clazz;
	}

	public boolean isEqual(EventType other) {
		return this == other || this == ANY || other == ANY;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace('_', ' ').concat(" event");
	}
}
