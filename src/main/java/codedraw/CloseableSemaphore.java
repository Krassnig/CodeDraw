package codedraw;

class CloseableSemaphore implements AutoCloseable {
	public CloseableSemaphore(int initialCount) {
		s = new java.util.concurrent.Semaphore(initialCount);
	}

	private final java.util.concurrent.Semaphore s;
	private Semaphore thisLock = new Semaphore(1);
	private Thread currentThread;
	private boolean isClosed = true;

	public void acquire() {
		thisLock.acquire();
		currentThread = Thread.currentThread();
		if (isClosed) {
			thisLock.release();
			return;
		}
		thisLock.release();


		try {
			s.acquire();
		}
		catch (InterruptedException ignored) { }

		thisLock.acquire();
		currentThread = null;
		thisLock.release();
	}

	public void release() {
		s.release();
	}

	public void emptySemaphore() {
		s.drainPermits();
	}

	@Override
	public void close() {
		if (!isClosed) {
			thisLock.acquire();
			isClosed = true;
			if (currentThread != null) {
				currentThread.interrupt();
			}
			thisLock.release();
		}
	}
}
