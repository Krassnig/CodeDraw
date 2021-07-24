package codedraw;

import java.util.ArrayDeque;
import java.util.Queue;

class EventLoop {
	public EventLoop() {
		new Thread(this::eventLoop).start();
	}

	private Semaphore queueLock = new Semaphore(1);
	private Queue<Runnable> queue = new ArrayDeque<>();
	private Semaphore queueCanTake = new Semaphore(0);

	public void queue(Runnable runnable) {
		queueLock.acquire();
		queue.add(runnable);
		queueLock.release();

		queueCanTake.release();
	}

	private void eventLoop() {
		while (true) {
			queueCanTake.acquire();

			queueLock.acquire();
			Runnable runnable = queue.remove();
			queueLock.release();

			try {
				runnable.run();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
