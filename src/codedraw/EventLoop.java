package codedraw;

class EventLoop {
	public EventLoop() {
		thread = new Thread(this::eventLoop);
		thread.start();
		thread.setPriority(Thread.MAX_PRIORITY);
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
