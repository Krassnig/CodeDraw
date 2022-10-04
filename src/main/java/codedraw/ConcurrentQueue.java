package codedraw;

class ConcurrentQueue<T> {
	public ConcurrentQueue(int initialCapacity) {
		if (initialCapacity <= 0) throw new RuntimeException("Initial capacity must be larger than zero.");

		queue = new Queue<>(initialCapacity);
	}

	private final Queue<T> queue;
	private final Semaphore queueLock = new Semaphore(1);
	private final Semaphore queueCount = new Semaphore(0);

	public void push(T element) {
		queueLock.acquire();
		queue.push(element);
		queueLock.release();

		queueCount.release();
	}

	public boolean isEmpty() {
		return !queueCount.canAcquire();
	}

	public T pop() {
		queueCount.acquire();

		queueLock.acquire();
		T result = queue.pop();
		queueLock.release();

		return result;
	}

	public T peek(){
		queueCount.acquire();

		queueLock.acquire();
		T result = queue.peek();
		queueLock.release();

		queueCount.release();
		return result;
	}
}
