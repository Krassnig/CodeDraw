package codedraw.events;

class EventQueue {
	private final ConcurrentQueue<Event> eventQueue = new ConcurrentQueue<>(128);


	public void pushEvent(EventType type, Object eventArg) {
		eventQueue.push(new Event(type, eventArg));
	}

	public EventType peekType() {
		Event result = eventQueue.peek();
		return result.getType();
	}

	public Object popEventArg() {
		return eventQueue.pop().getEventArg();
	}

	private static class Event {
		private final EventType type;
		private final Object eventArg;

		Event(EventType type, Object event) {
			this.type = type;
			this.eventArg = event;
		}

		public EventType getType() {
			return type;
		}

		public Object getEventArg() {
			return eventArg;
		}

	}
}
