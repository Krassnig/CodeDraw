package codedraw.events;

class EventLoop {
	public EventLoop() {
		thread = new Thread(this::eventLoop);
		thread.setDaemon(true);
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	private Thread thread;
	private ConcurrentQueue<Runnable> queue = new ConcurrentQueue<>();

	public void queue(Runnable runnable) {
		queue.push(runnable);
	}

	private void eventLoop() {
		while (true) {
			try {
				queue.pop().run();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
