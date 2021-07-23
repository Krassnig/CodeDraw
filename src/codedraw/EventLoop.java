package codedraw;

import java.util.ArrayDeque;
import java.util.Queue;

class EventLoop {
	private EventLoop() { }

	static {
		new Thread(EventLoop::eventLoop).start();
	}

	private static Semaphore queueLock = new Semaphore(1);
	private static Queue<Runnable> queue = new ArrayDeque<>();
	private static Semaphore queueCanTake = new Semaphore(0);

	public static void queue(Runnable runnable) {
		queueLock.acquire();
		queue.add(runnable);
		queueLock.release();

		queueCanTake.release();
	}

	private static void eventLoop() {
		while (true) {
			queueCanTake.acquire();

			queueLock.acquire();
			Runnable runnable = queue.remove();
			queueLock.release();

			try {
				runnable.run();
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
