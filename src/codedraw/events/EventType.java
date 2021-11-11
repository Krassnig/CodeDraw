package codedraw.events;

import java.util.Arrays;
import java.util.stream.Collectors;

enum EventType {
		MOUSE_CLICK,MOUSE_MOVE,MOUSE_UP,MOUSE_DOWN,MOUSE_ENTER,MOUSE_LEAVE,MOUSE_WHEEL,
		KEY_UP, KEY_DOWN, KEY_PRESS,
		WINDOW_MOVE, WINDOW_CLOSE;

		public String toUpperCamelCase() {
			String name = name();
			return Arrays.stream(name.toLowerCase().split("_")).map(str -> str.substring(0,1).toUpperCase() + str.substring(1)).collect(Collectors.joining());
		}
		@Override
		public String toString() {
			return toUpperCamelCase() + "Event";
		}

	}