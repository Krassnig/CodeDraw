package codedraw;

class Semaphore {
	public Semaphore(int initialCount) {
		s = new java.util.concurrent.Semaphore(initialCount);
	}

	private final java.util.concurrent.Semaphore s;

	public void acquire() {
		try {
			s.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void release() {
		s.release();
	}

	public void acquireAll() {
		while (s.tryAcquire());
	}
}
