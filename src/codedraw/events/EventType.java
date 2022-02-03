package codedraw.events;

enum EventType {
	ANY,
	MOUSE_CLICK,
	MOUSE_MOVE,
	MOUSE_DOWN,
	MOUSE_UP,
	MOUSE_ENTER,
	MOUSE_LEAVE,
	MOUSE_WHEEL,
	KEY_DOWN,
	KEY_UP,
	KEY_PRESS,
	WINDOW_MOVE,
	WINDOW_CLOSE,
	END_OF_EVENT;

	public boolean isEqual(EventType other) {
		return this == other || this == ANY || other == ANY;
	}

	@Override
	public String toString() {
		return super.toString().toLowerCase().replace('_', ' ').concat(" event");
	}
}
